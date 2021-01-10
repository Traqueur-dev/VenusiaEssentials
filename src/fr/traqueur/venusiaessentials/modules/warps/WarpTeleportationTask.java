package fr.traqueur.venusiaessentials.modules.warps;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.traqueur.venusiaessentials.api.utils.CooldownUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class WarpTeleportationTask implements Runnable {

	private Player player;
	private Warp warp;
	
	public WarpTeleportationTask(Player player, Warp warp) {
		this.player = player;
		this.warp = warp;
	}
	
	@Override
	public void run() {
		if (CooldownUtils.isOnCooldown("WarpTP", player)) {
			CooldownUtils.removeCooldown("WarpTP", player);
			player.teleport(warp.getLocation(), TeleportCause.COMMAND);
			player.sendMessage( Utils.color("&aVous &evous êtes téléporté au warp §6" + warp.getName() + "&e."));
		}
		
	}

}