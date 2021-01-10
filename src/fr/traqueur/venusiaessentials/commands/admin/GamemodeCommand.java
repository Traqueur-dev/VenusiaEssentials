package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class GamemodeCommand extends ICommand {


	@Command(name = "gamemode", aliases ="gm", permission = "base.gamemode", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		if (args.length() < 1) {
			sender.sendMessage(Utils.color("&e/gm <gameMode> [joueur]"));
			return;
		}
		GameMode gamemode = getGameMode(args.getArgs(0));
		if (gamemode == null) {
			sender.sendMessage(Utils.color("&eType de gamemode introuvable."));
			return;
		}
		Player target = null;
		if (args.length() > 1) {
			Player pls = Bukkit.getPlayer(args.getArgs(1));
			if (pls != null) {
				target = pls;
			}
		}
		if (target == null) {
			if (!args.isPlayer()) {
				sender.sendMessage(Utils.color("&cCe joueur est introuvable."));
				return;
			}
			target = args.getPlayer();
		}
		if (target.getGameMode() == gamemode) {
			sender.sendMessage(Utils.color("&eLe joueur &6" + target.getName() + " &eest déjà en &c" + gamemode.name() + "&e."));
			return;
		}
		target.setGameMode(gamemode);
		sender.sendMessage(Utils.color("&eVous venez de mettre à jour le gamemode de &6" + target.getName() + " &een &c" + gamemode.name() + "&e."));
	}


	private GameMode getGameMode(String args) {
	    String name = args.toLowerCase();
	    
	    if (name.equalsIgnoreCase("gmc") || name.contains("creat") || name.equalsIgnoreCase("1") || name.equalsIgnoreCase("c")) {
	    	return GameMode.CREATIVE;
	    } else if (name.equalsIgnoreCase("gms") || name.contains("survi") || name.equalsIgnoreCase("0") || name.equalsIgnoreCase("s")) {
	    	return GameMode.SURVIVAL;
	    } else if (name.equalsIgnoreCase("gma") || name.contains("adven") || name.equalsIgnoreCase("2") || name.equalsIgnoreCase("a")) {
	    	return GameMode.ADVENTURE;
	    }
	    return null;
	}

}
