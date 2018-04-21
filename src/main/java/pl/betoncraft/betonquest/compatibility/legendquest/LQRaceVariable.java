/**
 * BetonQuest - advanced quests for Bukkit Copyright (C) 2016  Jakub "Co0sh" Sapalski
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package pl.betoncraft.betonquest.compatibility.legendquest;

import me.sablednah.legendquest.Main;
import me.sablednah.legendquest.races.Race;
import org.bukkit.Bukkit;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.api.Variable;
import pl.betoncraft.betonquest.utils.PlayerConverter;

/**
 * Resolves to player's race.
 *
 * @author Jakub Sapalski
 */
public class LQRaceVariable extends Variable<Race> {

  private Main lq;

  public LQRaceVariable(Instruction instruction) throws InstructionParseException {
    super(instruction);
    lq = ((Main) Bukkit.getPluginManager().getPlugin("LegendQuest"));
  }

  /**
   * This method should return a resolved value of variable for given player.
   *
   * @param playerID ID of the player
   *
   * @return the value of this variable
   */
  @Override
  public Race getValue(String playerID) {
    return lq.getPlayers().getPC(PlayerConverter.getPlayer(playerID)).race;
  }

  @Override
  public String getValueAsString(String playerID) {
    return getValue(playerID).name;
  }
}
