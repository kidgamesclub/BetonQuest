package pl.betoncraft.betonquest.api;

import static pl.betoncraft.betonquest.api.ValueType.AMOUNT;
import static pl.betoncraft.betonquest.api.ValueType.LEFT;

import java.math.BigDecimal;
import java.util.function.Function;
import lombok.Getter;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;

@Getter
public abstract class NumberVariable<N extends Number> extends Variable<N> {

  private final Function<BigDecimal, N> toConverter;
  private final ValueType type;
  private final BigDecimal amount;

  public NumberVariable(Instruction instruction,
                        Function<BigDecimal, N> toConverter) throws InstructionParseException {
    super(instruction);
    if (instruction.next().equalsIgnoreCase("amount")) {
      type = AMOUNT;
      amount = BigDecimal.ZERO;
    } else if (instruction.current().toLowerCase().startsWith("left:")) {
      type = LEFT;
      try {
        double amount = Double.parseDouble(instruction.current().substring(5));
        this.amount = BigDecimal.valueOf(amount);
      } catch (NumberFormatException e) {
        throw new InstructionParseException("Could not parse amount");
      }
    } else {
      type = AMOUNT;
      amount = BigDecimal.ZERO;
    }
    this.toConverter = toConverter;
  }

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

  protected abstract N getCurrentValue(String playerID);
}
