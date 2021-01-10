package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Enchantments;
import fr.traqueur.venusiaessentials.api.utils.JavaUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class EnchantCommand extends ICommand {
	
	@Command(name = "enchant", permission = "base.enchant")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		if (args.length() < 2) {
			player.sendMessage(Utils.color("&e/enchant <nom de l'enchantement> <niveau>"));
			return;
		}
		ItemStack item = player.getItemInHand();
		if (item == null || item.getType() == Material.AIR) {
			player.sendMessage(Utils.color("&eVous n'avez rien dans la main."));
			return;
		}
		if (Enchantments.getByName(args.getArgs(0)) == null) {
			player.sendMessage(Utils.color("&eLe type d'enchantement &c" + args.getArgs(0) + " &eest introuvable."));
			return;
		}
		if (!JavaUtils.isInteger(args.getArgs(1))) {
			player.sendMessage(Utils.color("&eVeuillez spécifier un niveau valide."));
			return;
		}
		Enchantment enchant = Enchantments.getByName(args.getArgs(0));
		int level = Integer.parseInt(args.getArgs(1));
		item.addUnsafeEnchantment(enchant, level);
		player.updateInventory();
		this.logCommand(args);
	}

}
