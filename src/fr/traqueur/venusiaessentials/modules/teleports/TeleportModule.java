package fr.traqueur.venusiaessentials.modules.teleports;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.modules.Saveable;
import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;

public class TeleportModule extends Saveable {
	
	
	public static TeleportModule getInstance() {
		return instance;
	}

	public Map<UUID, TeleportRequest> getTeleportRequests() {
		return teleportRequests;
	}

	private static TeleportModule instance;
	
	
	private Map<UUID, TeleportRequest> teleportRequests;

	public TeleportModule(Plugin plugin) {
		super(plugin, "Teleports");

		instance = this;
		
		this.teleportRequests = new HashMap<>();

		this.registerCommand(new TeleportCommand());
		this.registerListener(new TeleportListener());
	}

	public void handleTeleport(Player player, Object obj) {
		if (obj == null)
			return;

		Player target = null;
		Location to = null;

		if (obj instanceof Player) {
			target = (Player) obj;
		} else if (obj instanceof Location) {
			to = (Location) obj;
		}

		if (player.hasPermission("base.teleport.bypass")) {
			player.teleport((to != null ? to : target.getLocation()));
			return;
		}
		if (this.getTeleportRequests().containsKey(player.getUniqueId())) {
			TeleportRequest request = this.getTeleportRequests().get(player.getUniqueId());
			player.sendMessage(Utils.color("&eVous êtes déjà en cours de téléportation, veuillez patienter &c"
					+ DurationFormatter.getRemaining(request.getRemaining(), true) + "&e."));
			return;
		}
		TeleportRequest request = new TeleportRequest(player, obj);
		request.setTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(VenusiaEssentials.getInstance(), request, 20L, 20L));
		this.getTeleportRequests().put(player.getUniqueId(), request);
		player.sendMessage(Utils.color("&eTéléportation dans &c5 secondes&e, ne bougez pas !"));
	}

	public TeleportCooldown getRequestFrom(Profile to, Player from) {
		for (int i = 0; i < to.getTeleportRequests().size(); i++) {
			TeleportCooldown request = to.getTeleportRequests().get(i);
			if (request.getReceiver().equalsIgnoreCase(from.getName())) {
				return request;
			}
		}
		return null;
	}

	public TeleportCooldown getFristRequestToAccept(Profile to) {
		for (int i = 0; i < to.getTeleportRequests().size(); i++) {
			TeleportCooldown request = to.getTeleportRequests().get(i);
			if (request.getReceiver().equalsIgnoreCase(to.getPlayerName())) {
				return request;
			}
		}
		return null;
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveData() {
		// TODO Auto-generated method stub
		
	}
}
