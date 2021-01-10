package fr.traqueur.venusiaessentials.commands.essentials;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.util.com.google.common.primitives.Ints;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;

public class BottleCommand extends ICommand {

	@Command(name = "bottlexp")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		Integer amount = player.getLevel();
		;
		if (player.getLevel() <= 0) {
			player.sendMessage("§e" + "Vous n'avez aucun niveau !");
			return;
		}
		if (args.length() > 0) {
			amount = Ints.tryParse(args.getArgs(0));
			if (amount == null) {
				player.sendMessage("§e" + "\"" + args.getArgs(0) + "\" n'est pas un nombre !");
				return;
			}
			if (!(amount <= player.getLevel())) {
				player.sendMessage("§e" + "Vous n'avez pas assez de niveaux !");
				return;
			}

			if (amount <= 0) {
				player.sendMessage("§e" + amount + " n'est pas assez grand !");
				return;
			}
		}
		ItemStack is = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§8• §aNiveaux §8•");
		lore.add("§c" + amount);
		im.setLore(lore);
		im.setDisplayName("§8► §eRéserve d'XP §8◄");
		is.setItemMeta(im);
		player.setLevel(player.getLevel() - amount);
		player.getInventory().addItem(new ItemStack[] { is });
		player.sendMessage(String.valueOf("Votre XP vient d'\u00eatre mis en bouteille."));
	}
}