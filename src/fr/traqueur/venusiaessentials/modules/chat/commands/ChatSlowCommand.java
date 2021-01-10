package fr.traqueur.venusiaessentials.modules.chat.commands;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;
import fr.traqueur.venusiaessentials.modules.chat.ChatModule;
import net.minecraft.util.com.google.common.primitives.Longs;

public class ChatSlowCommand extends BaseCommand {

	@Command(name = "chat.slow", aliases = {"tchat.slow"})
	public void onCommand(CommandArgs args) {
		ChatModule module = ChatModule.getInstance();
		if(args.length() < 1) {
			args.getPlayer().sendMessage(Utils.color("&cUsage: /chat slow <long>"));
			return;
		}
		Long delay = Longs.tryParse(args.getArgs(0));
		
		if(delay == null) {
			args.getPlayer().sendMessage(Utils.color("&cUsage: /chat slow <long>"));
			return;
		}
		
		if (delay == 0) {
			Bukkit.broadcastMessage(Utils.getPrefix(VenusiaEssentials.PREFIX) + Utils.color("&eLe tchat public n'est maintenant plus ralentit."));
			module.getChat().setSlowTime(0);
			return;
		}
		
		Bukkit.broadcastMessage(Utils.getPrefix(VenusiaEssentials.PREFIX) + Utils.color("&eLe tchat public vient d'être ralentit à 1 message toute les &c" + delay + " secondes&e."));
		module.getChat().setSlowTime(TimeUnit.SECONDS.toMillis(delay));

	}

}
