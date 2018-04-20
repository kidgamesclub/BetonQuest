package pl.betoncraft.betonquest.api;

import org.bukkit.entity.Player;

public interface ConversationFilter {
  String handleMessage(Player player, String message);
}
