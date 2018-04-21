package pl.betoncraft.betonquest.api;

import java.math.BigDecimal;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;

public abstract class DoubleVariable extends NumberVariable<Double> {
  public DoubleVariable(Instruction instruction) throws InstructionParseException {
    super(instruction, BigDecimal::doubleValue);
  }
}
