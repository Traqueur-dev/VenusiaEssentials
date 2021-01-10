package fr.traqueur.venusiaessentials.commands.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Factions;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class ClaimCommand extends ICommand {

	@Command(name ="claim", permission = "op")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();

		player.sendMessage(Utils.LINE);
		player.sendMessage("§c- §e/claim §6WarZone §7- §eClaim la §4WarZone §ea partir d'une séléction.");
		player.sendMessage("§c- §e/claim §6SafeZone §7- §eClaim la §6SafeZone §ea partir d'une séléction.");
		player.sendMessage(Utils.LINE);
	}

	@Command(name ="claim.warzone", permission = "op")
	public void onClaimWarZoneCommand(CommandArgs args) {
		Player player = args.getPlayer();

		Selection selection = ((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit")).getSelection(player);

		if (selection == null) {
			player.sendMessage(Utils.color((String) "&eVous n'avez §6établit §eaucune §cséléction§e."));
			return;
		}

		Bukkit.broadcastMessage(Utils.getPrefix("Claim") + "§eClaim de la §4WarZone §een cours...");

		long time = System.currentTimeMillis();

		for (Location location : getLocations(selection.getMaximumPoint(), selection.getMinimumPoint())) {
			Board.setFactionAt(Factions.i.getWarZone(), new FLocation(location));
		}

		time = System.currentTimeMillis() - time;
		Bukkit.broadcastMessage(
				Utils.getPrefix("Claim") + "§eClaim de la §4WarZone §aterminé §e! §8(§7" + time + "ms§8)");
	}

	@Command(name = "claim.safezone", permission = "op")
	public void onClaimSafeZoneCommand(CommandArgs args) {
		Player player = args.getPlayer();

		Selection selection = ((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit")).getSelection(player);

		if (selection == null) {
			player.sendMessage(Utils.color((String) "&eVous n'avez §6établit §eaucune §cséléction§e."));
			return;
		}

		Bukkit.broadcastMessage(Utils.getPrefix("Claim") + "§eClaim de la §6SafeZone §een cours...");

		long time = System.currentTimeMillis();

		for (Location location : getLocations(selection.getMaximumPoint(), selection.getMinimumPoint())) {
			Board.setFactionAt(Factions.i.getSafeZone(), new FLocation(location));
		}

		time = System.currentTimeMillis() - time;
		Bukkit.broadcastMessage(
				Utils.getPrefix("Claim") + "§eClaim de la §6SafeZone §aterminé §e! §8(§7" + time + "ms§8)");
	}

	public List<Location> getLocations(Location l1, Location l2) {
		List<Location> locations = new ArrayList<Location>();
		int topBlockX = (l1.getBlockX() < l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());
		int bottomBlockX = (l1.getBlockX() > l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());

		int topBlockY = (l1.getBlockY() < l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());
		int bottomBlockY = (l1.getBlockY() > l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());

		int topBlockZ = (l1.getBlockZ() < l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());
		int bottomBlockZ = (l1.getBlockZ() > l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());

		for (int x = bottomBlockX; x <= topBlockX; x++) {
			for (int z = bottomBlockZ; z <= topBlockZ; z++) {
				for (int y = bottomBlockY; y <= topBlockY; y++) {
					Location location = new Location(l1.getWorld(), x, y, z);
					locations.add(location);
				}
			}
		}
		return locations;
	}
}
