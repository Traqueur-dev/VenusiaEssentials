package fr.traqueur.venusiaessentials.modules.chat.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class PrivateMessageCommand extends ICommand {

	@Command(name = "message", aliases = { "tell", "w", "m", "msg" })
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		if (args.length() < 2) {
			player.sendMessage(Utils.color("&cUsage: /msg <joueur> <message>"));
			return;
		}
		Player target = Bukkit.getPlayer(args.getArgs(0));
		if (target == null) {
			player.sendMessage(Utils.color("&cLe joueur est introuvable"));
			return;
		}

		ProfileModule module = ProfileModule.getInstance();
		Profile sender = module.getProfile(player.getName());
		Profile receiver = module.getProfile(target.getName());

		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < args.length(); i++) {
			builder.append(args.getArgs(i));
		}

		receiver.setLastWhisper(sender.getPlayerName());
		sender.setLastWhisper(receiver.getPlayerName());
		
		receiver.msg("&d" + sender.getPlayerName() + "⇒ Toi:&b " + builder.toString());
		sender.msg("&dToi ⇒ " + receiver.getPlayerName() + ":&b " + builder.toString());

		for(Player p: Utils.getOnlinePlayers()) {
			Profile profile = module.getProfile(p.getName());
			if((!(profile.getPlayerName().equals(sender.getPlayerName())) ||!(profile.getPlayerName().equals(receiver.getPlayerName())) ) && profile.isSpyMode() && p.hasPermission("base.perm.spy")) {
				player.sendMessage(Utils.color("&d"+sender.getPlayerName()+ " ⇒ " + receiver.getPlayerName() + ":&b " + builder.toString()));
			}
		}
		
	}

	@Command(name = "spy", permission = "spy.toggle")
	public void onSpyCommand(CommandArgs args) {
		Player player = args.getPlayer();
		ProfileModule module = ProfileModule.getInstance();
		Profile sender = module.getProfile(player.getName());

		boolean newMode = sender.toggleSpy();
		sender.msg("&eMode &6espion&f: " + Utils.toStringOnOff(newMode));
	}
	
	@Command(name = "reply", aliases = { "r" })
	public void onReplyCommand(CommandArgs args) {
		Player player = args.getPlayer();
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&cUsage: /reply <message>"));
			return;
		}

		ProfileModule module = ProfileModule.getInstance();
		Profile sender = module.getProfile(player.getName());

		if (sender.getLastWhisper() == null) {
			sender.msg("&cVous n'avez personne à qui répondre.");
			return;
		}

		Profile receiver = module.getProfile(sender.getLastWhisper());

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < args.length(); i++) {
			builder.append(args.getArgs(i));
		}
		receiver.setLastWhisper(sender.getPlayerName());
		sender.setLastWhisper(receiver.getPlayerName());

		receiver.msg("&d" + sender.getPlayerName() + "⇒ Toi:&b " + builder.toString());
		sender.msg("&dToi ⇒ " + sender.getPlayerName() + ":&b " + builder.toString());
	}

}
