package fr.traqueur.venusiaessentials.modules.warps.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.traqueur.venusiaessentials.api.utils.CooldownUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class WarpListener implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player =event.getPlayer();
		if (!CooldownUtils.isOnCooldown("WarpTP", player)) {
			return;
		}
		if (!Utils.compareLocations(event.getFrom(), event.getTo())) {
			CooldownUtils.removeCooldown("WarpTP", player);
			player.sendMessage(Utils.color("&cAnnulation de la téleportation."));
		}
	}

	
}