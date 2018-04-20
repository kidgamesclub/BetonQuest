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
package pl.betoncraft.betonquest.compatibility.liquify;

import club.kidgames.liquid.plugin.LiquidRuntime;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.api.Variable;
import pl.betoncraft.betonquest.utils.PlayerConverter;

public class LiquidVariable extends Variable {

	private final String templateString;

	public LiquidVariable(Instruction instruction) throws InstructionParseException {
		super(instruction);

		final String templateString = instruction.next();
    this.templateString = templateString.startsWith("{") ? templateString : "{{" + templateString + "}}";
	}

	@Override
	public String getValue(String playerID) {
    final Player player = PlayerConverter.getPlayer(playerID);
    return LiquidRuntime.getEngine().render(templateString, player);
	}

}
