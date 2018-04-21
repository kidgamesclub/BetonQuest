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
package pl.betoncraft.betonquest.compatibility.racesandclasses;

import static pl.betoncraft.betonquest.api.ValueType.AMOUNT;
import static pl.betoncraft.betonquest.api.ValueType.LEFT;

import de.tobiyas.racesandclasses.APIs.LevelAPI;
import lombok.Getter;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.api.IntegerVariable;
import pl.betoncraft.betonquest.api.NumberVariable;
import pl.betoncraft.betonquest.api.ValueType;
import pl.betoncraft.betonquest.utils.PlayerConverter;

/**
 * Prints RaC experience points.
 *
 * @author Jakub Sapalski
 */
@Getter
public class RaCExpVariable extends IntegerVariable {

	private ValueType type;
	private int amount;

	public RaCExpVariable(Instruction instruction) throws InstructionParseException {
		super(instruction);
		if (instruction.next().equalsIgnoreCase("amount")) {
			type = AMOUNT;
		} else if (instruction.current().toLowerCase().startsWith("left:")) {
			type = LEFT;
			try {
				amount = Integer.parseInt(instruction.current().substring(5));
			} catch (NumberFormatException e) {
				throw new InstructionParseException("Could not parse experience amount");
			}
		}
	}

	@Override
  protected Integer getCurrentValue(String playerID) {
		return LevelAPI.getCurrentExpOfLevel(PlayerConverter.getPlayer(playerID));
	}

}
