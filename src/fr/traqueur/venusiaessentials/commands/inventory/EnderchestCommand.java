package fr.traqueur.venusiaessentials.commands.inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.ProfileInventory;

public class EnderchestCommand extends ICommand implements Listener {
	
	private Map<UUID, Profile> enderchestEditors = new HashMap<>();


	@Command(name = "enderchest", aliases = "ec", permission = "base.enderchest")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		ProfileModule profileManager = ProfileModule.getInstance();
		if (args.length() < 1) {
			player.openInventory(player.getEnderChest());
		} else {
			if (!player.hasPermission("base.enderchest.other")) {
				player.sendMessage(Utils.color("&cVous n'avez pas la permission d'executer cette commande."));
				return;
			}
			String target = args.getArgs(0);
			if (Bukkit.getPlayer(target) != null) {
				player.openInventory(Bukkit.getPlayer(target).getEnderChest());
				player.sendMessage(Utils.color("&eVous venez d'ouvrir l'enderchest du joueur &6" + target + "&e."));
				return;
			}
			Profile profile = profileManager.getProfile(target);
			if (profile == null) {
				player.sendMessage(Utils.color("&eCe joueur est introuvable."));
				return;
			}
			ProfileInventory inv = profile.getProfileInventory();
			Inventory fakeEc = Bukkit.createInventory(null, InventoryType.ENDER_CHEST);
			if(!inv.getEnderchest().isEmpty()) {
				fakeEc.setContents(inv.enderchestAsArray());				
			}
			
			enderchestEditors.put(player.getUniqueId(), profile);
			player.openInventory(fakeEc);
			player.sendMessage(Utils.color("&eVous venez d'ouvrir l'enderchest du joueur &6" + target + " &7(&cHORS-LIGNE&7)&e."));
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();
		
		if (inv != null && enderchestEditors.containsKey(player.getUniqueId())) {
			Profile profile = enderchestEditors.get(player.getUniqueId());
			profile.getProfileInventory().setEnderchest(Arrays.asList(inv.getContents()));
			profile.getProfileInventory().setShouldUpdateEnderchest(true);
		}
	}

	
}
