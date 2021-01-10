package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class FeedCommand extends ICommand {

	@Command(name = "feed",aliases = {"fed", "eat"}, permission = "base.feed", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		
		if (args.length() < 1) {
			
			if (!args.isPlayer()) {
				sender.sendMessage(Utils.color("&e/feed <joueur>"));
				return;
			}
			
			feedPlayer(args.getPlayer(), args.getPlayer().hasPermission("base.cooldown"));
		} else {
			Player target = Bukkit.getPlayer(args.getArgs(0));
			if (target == null) {
				sender.sendMessage(Utils.color("&eCe joueur n'est plus en ligne."));
				return;
			}
			feedPlayer(target, true);
			sender.sendMessage(Utils.color("&eVous venez de &6rassasier &ele joueur &c" + target.getName() + "&e."));
		}
	}
	
	public void feedPlayer(Player player, boolean force) {
		if (!this.checkCooldown(player, 30) && !force) {
			return;
		}
 		player.sendMessage(Utils.color("&eVous avez été §6rassasiée."));
 		player.setFoodLevel(20);
 		player.setSaturation(20f);
 		player.setExhaustion(0.00f);
	}
	
}
