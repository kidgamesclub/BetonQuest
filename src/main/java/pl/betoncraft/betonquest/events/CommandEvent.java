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
package pl.betoncraft.betonquest.events;

import java.util.List;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.api.QuestEvent;
import pl.betoncraft.betonquest.utils.PlayerConverter;

/**
 * Fires a list of commands for the player.
 *
 * @author Jakub Sapalski
 */
public class CommandEvent extends QuestEvent {

	private final Command[] commands;

	public CommandEvent(Instruction instruction) throws InstructionParseException {
		super(instruction);
		staticness = true;
		persistent = true;
		try {
			String string = instruction.getInstruction();
			String[] rawCommands = string.trim().substring(string.indexOf(" ") + 1).split("\\|");
			commands = new Command[rawCommands.length];
			for (int i = 0; i < rawCommands.length; i++) {
				commands[i] = new Command(rawCommands[i]);
			}
		} catch (Exception e) {
			throw new InstructionParseException("Could not parse commands");
		}
	}

	@Override
	public void run(String playerID) {
		for (Command command : commands) {
			if (command.variables.isEmpty()) {
				// if there are no variables, this is a global command
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.command);
			} else {
				if (playerID == null) {
					// this is a static command, run for each player
					for (Player player : Bukkit.getOnlinePlayers()) {
						String com = command.command;
						for (String var : command.variables) {
							com = com.replace(var, BetonQuest.getInstance().getVariableValue(
									instruction.getPackage().getName(), var, PlayerConverter.getID(player)));
						}
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), com);
					}
				} else {
					Player player = PlayerConverter.getPlayer(playerID);
					if (player == null) {
						// the player is offline, cannot resolve variables, at least replace %player%
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.command
								.replaceAll("%player%", PlayerConverter.getName(playerID)));
					} else {
						// run the command for the single player
						String com = command.command;
						for (String var : command.variables) {
							com = com.replace(var, BetonQuest.getInstance().getVariableValue(
									instruction.getPackage().getName(), var, playerID));
						}
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), com);
					}
				}
			}
		}
	}

	private class Command {

		String command;
		Set<String> variables;

		public Command(String command) {
			this.command = command;
			variables = BetonQuest.resolveVariables(command);
		}

	}
}
