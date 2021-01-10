package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class GodModeCommand extends ICommand{

	@Command(name ="god", aliases ="godmode", permission = "base.god", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		ProfileModule profileManager = ProfileModule.getInstance();
		Player target = null;
		if (args.length() < 1) {
			
			if (!args.isPlayer()) {
				sender.sendMessage(Utils.color("&e/god <joueur>"));
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
		Profile profile = profileManager.getProfile(target.getName());
		boolean newGodMode = !profile.isGodMode();
		profile.setGodMode(newGodMode);
		
	    sender.sendMessage(Utils.color("&eLe joueur &6" + target.getName() + " &e" + (newGodMode ? "&aest maintenant" : "&cn'est maintenant plus") 
	    		+ " &een mode &6God Mode&e."));
	}
	
}
