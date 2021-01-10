package fr.traqueur.venusiaessentials.modules.teleports;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class TeleportListener implements Listener {

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled() || event.getCause() == TeleportCause.ENDER_PEARL)
			return;
		Profile profile = ProfileModule.getInstance().getProfile(event.getPlayer().getName());
		if(profile == null) {
			return;
		}
		profile.setLastTeleportLocation(event.getFrom().clone());
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Profile profile = ProfileModule.getInstance().getProfile(event.getEntity().getName());
		if(profile == null) {
			return;
		}
		profile.setLastTeleportLocation(event.getEntity().getLocation());
	}
}
