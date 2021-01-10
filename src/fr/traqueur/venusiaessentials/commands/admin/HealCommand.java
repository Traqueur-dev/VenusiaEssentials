package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class HealCommand extends ICommand{

	@Command(name = "heal", permission = "base.heal", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		
		Player target = null;
		if (args.length() < 1) {
			
			if (!args.isPlayer()) {
				sender.sendMessage(Utils.color("&e/heal <joueur>"));
				return;
			}
			target = args.getPlayer();
		} else {
			if (Bukkit.getPlayer(args.getArgs(0)) == null) {
				sender.sendMessage(Utils.color("&cCe joueur est introuvable."));
				return;
			}
			target = Bukkit.getPlayer(args.getArgs(0));
		}
		target.setHealth( ((Damageable)target).getMaxHealth());
		sender.sendMessage(Utils.color("&eVous venez de &cheal &ele joueur &6" + target.getName() + "&e."));
	} 
	
}
