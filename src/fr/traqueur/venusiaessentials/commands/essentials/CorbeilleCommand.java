package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import net.md_5.bungee.api.ChatColor;

public class CorbeilleCommand extends ICommand {

	@Command(name = "poubelle", aliases = {"corbeille", "trash" })
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		player.openInventory(Bukkit.createInventory(player, 54, ChatColor.GRAY + "Corbeille"));
	}
}
