package fr.traqueur.venusiaessentials.modules.chat.listeners;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.traqueur.venusiaessentials.api.utils.CooldownUtils;
import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.chat.ChatModule;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onMessage(AsyncPlayerChatEvent event) {
		ChatModule manager = ChatModule.getInstance();
		if (event.isCancelled()) {return;}
		Player player = (Player) event.getPlayer();
		String message = event.getMessage();
		
		if (player.hasPermission("base.chat.bypass")) {return;}
		if (manager.getChat().getSlowTime() == 0) {return;}
		if (!manager.getChat().isEnabled()) {
			player.sendMessage("§aVous §ene pouvez pas parler lorsque le tchat est §cdésactivé§e.");
			event.setCancelled(true);
			return;
		}
		if (message.length() < 3) {
			event.setCancelled(true);
			player.sendMessage(Utils.color("&aVotre &emessage contient moins de &c3 &elettres."));
			return;
		}
		if (CooldownUtils.isOnCooldown("Chat", player)) {
			event.setCancelled(true);
			String time = DurationFormatter.getRemaining(CooldownUtils.getCooldownForPlayerLong("Chat", player), true);
			player.sendMessage(Utils.color("&eAttend &c" + time + " &esecondes avant d'écrire un message."));
			return;
		}
		CooldownUtils.addCooldown("Chat",player, (int) TimeUnit.MILLISECONDS.toSeconds(manager.getChat().getSlowTime()));
	}
	
	@EventHandler
	public void onStaffChat(AsyncPlayerChatEvent event) {
		Player player = (Player) event.getPlayer();
		if(!player.hasPermission("base.chat.staff") || !event.getMessage().startsWith("!") || event.getMessage().equals("!"))
			return;
		for(Player p : Utils.getOnlinePlayers()) {
			if(p.hasPermission("base.chat.staff"))
				p.sendMessage("§8[§e§lStaff§8] §e" + player.getName() + " §7» §e" + event.getMessage().substring(1));
		}
		event.setCancelled(true);
	}

	
}