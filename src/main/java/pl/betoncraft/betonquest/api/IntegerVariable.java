package pl.betoncraft.betonquest.api;

import java.math.BigDecimal;
import java.util.function.Function;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;

public abstract class IntegerVariable extends NumberVariable<Integer> {
  public IntegerVariable(Instruction instruction) throws InstructionParseException {
    super(instruction, BigDecimal::intValueExact);
  }
}
