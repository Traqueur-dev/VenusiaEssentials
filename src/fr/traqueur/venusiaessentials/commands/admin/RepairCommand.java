package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class RepairCommand extends ICommand {

	@Command(name = "repair", aliases = "fix", permission = "base.repair")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		ItemStack item = player.getItemInHand();
		if (item == null || item.getType() == Material.AIR) {
			player.sendMessage(Utils.color("&eVous n'avez aucun item dans la main."));
			return;
		}
		if (item.getType().isBlock() || item.getType().getMaxDurability() < 1) {
			player.sendMessage(Utils.color("&eVous ne pouvez pas réparé cette item."));
			return;
		}
		if (!this.checkCooldown(player, 60 * 30)) return;
		
		item.setDurability((short) 0);
		player.sendMessage(Utils.color("&eVous venez de &créparé &el'item en main."));
	}
	
	@Command(name = "repairall", aliases="fixall", permission = "base.fixall")
	public void repairAllCmd(CommandArgs args) {
		Player player = args.getPlayer();
		PlayerInventory inv = player.getInventory();
		
		if (!this.checkCooldown(player, 60 * 30)) return;
		
		for (ItemStack is : inv.getContents()) {
			if (is != null && is.getType() != Material.AIR && !is.getType().isBlock() && is.getType().getMaxDurability() > 1) {
				is.setDurability((short) 0);
			}
		}
		for (ItemStack is : inv.getArmorContents()) {
			if (is != null && is.getType() != Material.AIR && !is.getType().isBlock() && is.getType().getMaxDurability() > 1) {
				is.setDurability((short) 0);
			}
		}
		
		player.sendMessage(Utils.color("&eVotre inventaire et votre armure viennent d'être &créparé &eavec succés."));
	}
	

}
