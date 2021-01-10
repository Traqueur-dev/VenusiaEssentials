package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class ListCommand extends ICommand{

	@Command(name = "list", inGameOnly =  false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		
		sender.sendMessage(Utils.LINE);
		sender.sendMessage(Utils.color("&7- &eIl y a actuellement &6" + Utils.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers() + " &ejoueur(s) en ligne."));
		sender.sendMessage(Utils.LINE);
	}

}
