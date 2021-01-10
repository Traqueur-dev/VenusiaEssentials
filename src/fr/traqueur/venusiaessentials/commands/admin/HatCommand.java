package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class HatCommand extends ICommand{

	@Command(name = "hat", permission = "base.hat")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
			player.sendMessage(Utils.color("&eVous n'avez rien dans la main."));
			return;
		}
		PlayerInventory inv = player.getInventory();
		ItemStack item = player.getItemInHand();
		ItemStack old = inv.getHelmet();
		
		inv.setHelmet(item);
		player.setItemInHand(old);
		
		player.sendMessage(Utils.color("&eVous avez maintenant un nouveau chapeau!"));
	}
	
}
