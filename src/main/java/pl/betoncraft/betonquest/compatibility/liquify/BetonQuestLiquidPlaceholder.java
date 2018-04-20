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

import club.kidgames.liquid.api.PlaceholderExtender;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.utils.PlayerConverter;

public class BetonQuestLiquidPlaceholder extends PlaceholderExtender {

  public BetonQuestLiquidPlaceholder(String pluginId, String identifier) {
    super(pluginId, identifier);
  }

  @Override
  public Object resolvePlaceholder(Player player) {
    String pack = null;
    String identifier = getName();
    if (identifier.contains(":")) {
      pack = identifier.substring(0, identifier.indexOf(':'));
      identifier = identifier.substring(identifier.indexOf(':') + 1);
    } else {
      pack = BetonQuest.getInstance().getConfig().getString("default_package", "default");
    }
    return BetonQuest.getInstance().getVariableValue(pack, '%' + identifier + '%', PlayerConverter.getID(player));
  }
}
