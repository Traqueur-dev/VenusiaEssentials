package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class SunCommand extends ICommand {

	@Command(name = "weatherclear", aliases = {"sun", "wc"}, permission = "base.sun", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		
		for (World world : Bukkit.getWorlds()) {
			if (world.getEnvironment() != Environment.NORMAL) continue;
			world.setStorm(false);
		}
		
		sender.sendMessage(Utils.color("&eVous venez de &aprogrammé &ele beau §6temps §esur le serveur."));
	}

}
