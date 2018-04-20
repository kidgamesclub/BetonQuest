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
package pl.betoncraft.betonquest.compatibility.liquify;

import club.kidgames.liquid.api.LiquidExtenderRegistry;
import club.kidgames.liquid.api.LiquidRenderEngine;
import club.kidgames.liquid.plugin.LiquidRuntime;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.compatibility.Integrator;

public class LiquifyIntegrator implements Integrator {
  public LiquifyIntegrator() {
  }

  @Override
  public void hook() {
    final LiquidRenderEngine engine = LiquidRuntime.getEngine();
    final LiquidExtenderRegistry registry = LiquidRuntime.getRegistry();

    BetonQuest.getInstance().registerVariable("liquify", LiquidVariable.class);
    registry.registerPlaceholder(new BetonQuestLiquidPlaceholder("BetonQuest", "beton"));
    BetonQuest.getInstance().addConversationFilter(new LiquidConversationFilter(engine));
  }

  @Override
  public void reload() {

  }

  @Override
  public void close() {

  }
}
