package fr.traqueur.venusiaessentials.modules.kit.commands;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.ItemBuilder;
import fr.traqueur.venusiaessentials.api.utils.JavaUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.kit.Kit;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;

public class KitCreateCommand extends ICommand{


	@Command(name = "kit.create", permission = "base.kit.create")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		KitModule manager = KitModule.getInstance();
		
		if (args.length() < 2){
			player.sendMessage(Utils.color("&e/kit create <nom> <délais> [Utilisation maximal]"));
			return;
		}
		String name = args.getArgs(0);
		if (manager.getKit(name) != null) {
			player.sendMessage(Utils.color("&eCe kit existe déjà."));
			return;
		}
		long delay = JavaUtils.parse(args.getArgs(1));
		if (delay == -1L) {
			player.sendMessage(Utils.color("&eCe délais est invalide."));
			return;
		}
		int maxUse = 0;
		if (args.length() > 2) {
			
			if (!JavaUtils.isInteger(args.getArgs(2))) {
				player.sendMessage(Utils.color("&eVeuillez spécifier une valeur valide."));
				return;
			}
			maxUse = Integer.parseInt(args.getArgs(2));
		}
		ItemStack icon = player.getItemInHand();
		ItemBuilder iconBuilder = new ItemBuilder(icon);
		if (icon == null || icon.getType() == Material.AIR) {
			player.sendMessage(Utils.color("&eVous devez avoir un item à la main, cette item sera l'icon du kit"));
			return;
		}
		
		UUID id = UUID.randomUUID();
		Kit kit = new Kit(id, name, player, delay, (maxUse > 0 ? maxUse : Integer.MAX_VALUE), iconBuilder);
		manager.getKits().put(id, kit);
		player.sendMessage(Utils.color("&eVous venez de créer le kit &c" + name + " &edisponible tous les &c" + args.getArgs(1) +"&e."));
	}

}
