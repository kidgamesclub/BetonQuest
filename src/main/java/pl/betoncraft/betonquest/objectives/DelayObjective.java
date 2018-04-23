/**
 * BetonQuest - advanced quests for Bukkit
 * Copyright (C) 2016  Jakub "Co0sh" Sapalski
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.betoncraft.betonquest.objectives;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;

import java.util.Optional;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.config.Config;

/**
 * Player has to wait specified amount of time. He may logout, the objective
 * will be completed as soon as the time is up and he logs in again.
 *
 * @author Jakub Sapalski
 */
public class DelayObjective extends Objective<Duration> {

	private final double delay;
	private BukkitTask runnable;
	private int interval;

	public DelayObjective(Instruction instruction) throws InstructionParseException {
		super(instruction);
		template = DelayData.class;
		if (instruction.hasArgument("ticks")) {
		    delay = instruction.getDouble() * 50;
		} else if (instruction.hasArgument("seconds")) {
		    delay = instruction.getDouble() * 1000;
		} else {
		    delay = instruction.getDouble() * 1000 * 60;
		}
		if (delay < 0) {
			throw new InstructionParseException("Delay cannot be less than 0");
		}
		interval = instruction.getInt(instruction.getOptional("interval"), 20 * 10);
		if (interval < 1) {
			throw new InstructionParseException("Interval cannot be less than 1 tick");
		}
	}

	@Override
	public void start() {
		runnable = new BukkitRunnable() {
			@Override
			public void run() {
				LinkedList<String> players = new LinkedList<>();
				long time = new Date().getTime();
        dataMap.forEach((playerID, value) -> {
          DelayData playerData = (DelayData) value;
          if (time >= playerData.getTime() && checkConditions(playerID)) {
            // don't complete the objective, it will throw CME/
            // store the player instead, complete later
            players.add(playerID);
          }
        });
				for (String playerID : players) {
					completeObjective(playerID);
				}
			}
		}.runTaskTimer(BetonQuest.getInstance(), 0, interval);
	}

	@Override
	public void stop() {
		if (runnable != null)
			runnable.cancel();
	}

	@Override
	public String getDefaultDataInstruction() {
		return Long.toString(new Date().getTime() + (long) delay);
	}

  /**
   * This method should return various properties of the objective, formatted as readable Strings. An example would be
   * "5h 5min" for "time_left" keyword in "delay" objective or "12" for keyword "mobs_killed" in "mobkill" objective.
   * The method is not abstract since not all objectives need to have properties, i.e. "die" objective. By default it
   * returns an empty string.
   *
   * @param name     the name of the property you need to return; you can parse it to extract additional information
   * @param playerID ID of the player for whom the property is to be returned
   *
   * @return the property with given name
   */
  @Override
  public Optional<Duration> getProperty(String name, String playerID) {
    return Optional.ofNullable((DelayData) dataMap.get(playerID))
          .map(DelayData::getTime)
          .map(Instant::ofEpochMilli)
          .map(start -> Duration.between(start, new Date().toInstant()));
  }

  @Override
	public String getReadableProperty(String name, String playerID) {
		if (name.equalsIgnoreCase("left")) {
			String lang = BetonQuest.getInstance().getPlayerData(playerID).getLanguage();
			String daysWord = Config.getMessage(lang, "days");
			String hoursWord = Config.getMessage(lang, "hours");
			String minutesWord = Config.getMessage(lang, "minutes");
			String secondsWord = Config.getMessage(lang, "seconds");
      final Duration duration = getProperty(name, playerID).orElse(Duration.ZERO);
      final long timeLeft = duration.toMillis();
      long s = (timeLeft / (1000)) % 60;
			long m = (timeLeft / (1000 * 60)) % 60;
			long h = (timeLeft / (1000 * 60 * 60)) % 24;
			long d = (timeLeft / (1000 * 60 * 60 * 24));
			StringBuilder time = new StringBuilder();
			String[] words = new String[3];
			if (d > 0)
				words[0] = d + " " + daysWord;
			if (h > 0)
				words[1] = h + " " + hoursWord;
			if (m > 0)
				words[2] = m + " " + minutesWord;
			int count = 0;
			for (String word : words) {
				if (word != null)
					count++;
			}
			if (count == 0) {
				time.append(s + " " + secondsWord);
			} else if (count == 1) {
				for (String word : words) {
					if (word == null)
						continue;
					time.append(word);
				}
			} else if (count == 2) {
				boolean second = false;
				for (String word : words) {
					if (word == null)
						continue;
					if (second) {
						time.append(" " + word);
					} else {
						time.append(word + " " + Config.getMessage(lang, "and"));
						second = true;
					}
				}
			} else {
				time.append(words[0] + ", " + words[1] + " " + Config.getMessage(lang, "and ") + words[2]);
			}
			return time.toString();
		} else if (name.equalsIgnoreCase("date")) {
			return new SimpleDateFormat(Config.getString("config.date_format"))
					.format(new Date(((DelayData) dataMap.get(playerID)).getTime()));
		}
		return "";
	}

	public static class DelayData extends ObjectiveData {

		private final long timestamp;

		public DelayData(String instruction, String playerID, String objID) {
			super(instruction, playerID, objID);
			timestamp = Long.parseLong(instruction);
		}

		private long getTime() {
			return timestamp;
		}

	}
}
