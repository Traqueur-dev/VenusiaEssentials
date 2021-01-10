package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class KillCommand extends ICommand {
	
	@Command(name = "kill", permission = "base.kill", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		
		if (args.length() < 1) {
			sender.sendMessage(Utils.color("&e/kill <joueur>"));
			return;
		}
		Player target = Bukkit.getPlayer(args.getArgs(0));
		if (target == null) {
			sender.sendMessage(Utils.color("&cCe joueur est introuvable."));
			return;
		}
		target.setHealth(0.00);
		target.sendMessage(Utils.color("&eVous venez d'être &ckill &epar un membre du &cStaff&e."));
		sender.sendMessage(Utils.color("&eVous venez de &ckill &ele joueur &c" + target.getName() + "&e."));
		this.logCommand(args);
	}

}
