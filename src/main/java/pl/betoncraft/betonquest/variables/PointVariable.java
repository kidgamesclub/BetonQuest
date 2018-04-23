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
package pl.betoncraft.betonquest.variables;

import java.util.List;
import lombok.Getter;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.Point;
import pl.betoncraft.betonquest.api.IntegerVariable;
import pl.betoncraft.betonquest.api.ValueType;
import pl.betoncraft.betonquest.api.Variable;

/**
 * Allows you to display total amount of points or amount of points remaining to
 * some other amount.
 *
 * @author Jakub Sapalski
 */
@Getter
public class PointVariable extends IntegerVariable {

  protected String category;

  public PointVariable(Instruction instruction) throws InstructionParseException {
    super(instruction);
    category = instruction.next();
    if (category.contains("*")) {
      category = category.replace('*', '.');
    } else {
      category = instruction.getPackage().getName() + "." + category;
    }
  }

  protected List<Point> getPoints(String playerID) {
    return BetonQuest.getInstance().getPlayerData(playerID).getPoints();
  }

  @Override
  protected Integer getCurrentValue(String playerID) {
    final List<Point> points = getPoints(playerID);
    Point point = null;
    for (Point p : points) {
      if (p.getCategory().equalsIgnoreCase(category)) {
        point = p;
        break;
      }
    }
    int count = 0;
    if (point != null) {
      count = point.getCount();
    }
    return count;
  }
}
