package pl.betoncraft.betonquest.api;

import java.math.BigDecimal;
import java.util.function.Function;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;

public abstract class NumberVariable<N extends Number> extends Variable<N> {

  private final Function<BigDecimal, N> toConverter;

  public NumberVariable(Instruction instruction, Function<BigDecimal, N> toConverter) throws InstructionParseException {
    super(instruction);
    this.toConverter = toConverter;
  }

  /**
   * This method should return a resolved value of variable for given player.
   *
   * @param playerID ID of the player
   *
   * @return the value of this variable
   */
  @Override
  public final N getValue(String playerID) {
    final N currentAmount = getCurrentValue(playerID);
    final BigDecimal currBD = BigDecimal.valueOf(currentAmount.doubleValue());
    if (getType() == ValueType.LEFT) {
      final BigDecimal amountBD = BigDecimal.valueOf(getAmount().doubleValue());
      return toConverter.apply(amountBD.subtract(currBD));
    } else if (getType() == ValueType.AMOUNT) {
      return currentAmount;
    } else {
      return toConverter.apply(BigDecimal.ZERO);
    }
  }

  protected abstract ValueType getType();

  protected abstract N getAmount();

  protected abstract N getCurrentValue(String playerID);
}
