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

public class InventorySeeCommand extends ICommand implements Listener {

	private Map<UUID, Profile> inventoryEditors = new HashMap<>();
	
	@Command(name = "invsee", permission = "base.invsee")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(args.getPlayer().getName());
		if (args.length() < 1) {
			profile.msg("&e/invsee <joueur>");
			return;
		}
		String target = args.getArgs(0);
		if (Bukkit.getPlayer(target) != null) {
			profile.getPlayer().openInventory(Bukkit.getPlayer(target).getInventory());
			profile.msg(Utils.color("&eOuverture de l'inventaire du joueur &a" + target + "&e..."));
			return;
		}
		Profile targetProfile = profileManager.getProfile(target);
		if (targetProfile == null) {
			profile.msg(Utils.color("&eCe joueur est introuvable."));
			return;
		}
		ProfileInventory inv = targetProfile.getProfileInventory();
		Inventory fakeInv = Bukkit.createInventory(null, InventoryType.PLAYER);
		if(!inv.getInventory().isEmpty()) {
			fakeInv.setContents(inv.inventoryAsArray());			
		}
		
		inventoryEditors.put(player.getUniqueId(), targetProfile);
		player.openInventory(fakeInv);
		player.sendMessage(Utils.color("&eOuverture de l'inventaire du joueur &a" + target + "&e... &7(&cHORS-LIGNE&7)"));
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();
		
		if (inv != null && inventoryEditors.containsKey(player.getUniqueId())) {
			Profile profile = inventoryEditors.get(player.getUniqueId());
			profile.getProfileInventory().setInventory(Arrays.asList(inv.getContents()));
			profile.getProfileInventory().setShouldUpdateInventory(true);
		}
	}
}
