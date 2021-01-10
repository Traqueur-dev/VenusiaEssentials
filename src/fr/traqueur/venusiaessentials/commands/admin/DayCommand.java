package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class DayCommand extends ICommand {

	@Command(name ="day", permission = "base.day", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		
		for (World world : Bukkit.getWorlds()) {
			if (world.getEnvironment() != Environment.NORMAL) continue;
			world.setTime(6000);
		}
		
		sender.sendMessage(Utils.color("&eVous venez de &aprogrammé &ele §6jour §esur le serveur."));

	} 
	
}
