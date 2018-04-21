package pl.betoncraft.betonquest.api;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Filters a single message based on the provided models.
 */
public interface MessageFilter {
  <X> X processMessage(String message, List<Pair<String, Object>> model);
}
