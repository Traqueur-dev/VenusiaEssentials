package fr.traqueur.venusiaessentials.modules.kit.commands;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;
import fr.traqueur.venusiaessentials.modules.kit.gui.KitListInventory;

public class KitListCommand extends ICommand {
	
	@Command(name = "kit.list", aliases = {"kits", "kit.menu"})
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		KitModule manager = KitModule.getInstance();

		if (manager.getKits().isEmpty()) {
			player.sendMessage(Utils.color("&eAucun kit disponible."));
			return;
		}
		
		KitListInventory.INVENTORY.open(player);
	}
}
