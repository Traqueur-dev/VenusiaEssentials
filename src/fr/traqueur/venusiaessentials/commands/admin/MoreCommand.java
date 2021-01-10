package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class MoreCommand extends ICommand {

	@Command(name = "more", permission = "base.more")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		ItemStack item = player.getItemInHand();
		if (item == null || item.getType() == Material.AIR) {
			player.sendMessage("&cVous n'avez rien dans la main !");
			return;
		}
		item.setAmount(64);
		player.sendMessage(Utils.color("&eCette item est maintenant à sa quantité maximal !"));
	}

}
