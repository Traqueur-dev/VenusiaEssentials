package fr.traqueur.venusiaessentials.modules.chat.listeners;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.ChatMode;

import fr.traqueur.venusiaessentials.api.utils.CooldownUtils;
import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.chat.ChatModule;
import fr.traqueur.venusiaessentials.modules.groups.Group;
import fr.traqueur.venusiaessentials.modules.groups.GroupModule;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class ChatListener implements Listener {

	private ProfileModule profileModule = ProfileModule.getInstance();
	private GroupModule groupManager = GroupModule.getInstance();
	
	@EventHandler
	public void onMessage(AsyncPlayerChatEvent event) {
		ChatModule manager = ChatModule.getInstance();
		if (event.isCancelled()) {
			return;
		}
		Player player = (Player) event.getPlayer();
		String message = event.getMessage();

		Profile userProfile = this.profileModule.getProfile(player.getName());
		GroupModule groupManager = this.groupManager;

		if (player.hasPermission("chat.colored")) {
			event.setMessage(event.getMessage().replace("&", "§"));
		}
		
		FPlayer fplayer = FPlayers.i.get(player);
		Group group = groupManager.getByName(userProfile.getGroup());
		if(fplayer.getChatMode() != ChatMode.PUBLIC) {
			return;
		}
		
		event.setCancelled(true);
		
		if (player.hasPermission("base.chat.staff") && event.getMessage().startsWith("!")
				&& !event.getMessage().equals("!")) {
			for (Player p : Utils.getOnlinePlayers()) {
				if (p.hasPermission("base.chat.staff"))
					p.sendMessage("§8[§e§lStaff§8] §e" + player.getName() + " §7» §e" + event.getMessage().substring(1));
			}
			return;
		}
		
		if (player.hasPermission("base.chat.bypass")) {

			for(Player p: Utils.getOnlinePlayers()) {
				FPlayer fp = FPlayers.i.get(p);
				String factionFormat = fplayer.hasFaction() ? "§7[§r" + fplayer.getChatTag(fp) + "§7] " : "";
				p.sendMessage(factionFormat + group.getPrefix() + " " + player.getName() + "§7: §f" + group.getColor() + event.getMessage()); 
			}
			return;
		}
		if (manager.getChat().getSlowTime() == 0) {

			for(Player p: Utils.getOnlinePlayers()) {
				FPlayer fp = FPlayers.i.get(p);
				String factionFormat = fplayer.hasFaction() ? "§7[§r" + fplayer.getChatTag(fp) + "§7] " : "";
				p.sendMessage(factionFormat + group.getPrefix() + " " + player.getName() + "§7: §f" + group.getColor() + event.getMessage()); 
			}
			return;
		}
		if (!manager.getChat().isEnabled()) {
			player.sendMessage("§aVous §ene pouvez pas parler lorsque le tchat est §cdésactivé§e.");
			return;
		}
		if (message.length() < 3) {
			player.sendMessage(Utils.color("&aVotre &emessage contient moins de &c3 &elettres."));
			return;
		}
		if (CooldownUtils.isOnCooldown("Chat", player)) {
			String time = DurationFormatter.getRemaining(CooldownUtils.getCooldownForPlayerLong("Chat", player), true);
			player.sendMessage(Utils.color("&eAttend &c" + time + " &esecondes avant d'écrire un message."));
			return;
		}


		for(Player p: Utils.getOnlinePlayers()) {
			FPlayer fp = FPlayers.i.get(p);
			String factionFormat = fplayer.hasFaction() ? "§7[§r" + fplayer.getChatTag(fp) + "§7] " : "";
			p.sendMessage(factionFormat + group.getPrefix() + " " + player.getName() + "§7: §f" + group.getColor() + event.getMessage()); 
		}
		CooldownUtils.addCooldown("Chat", player,
				(int) TimeUnit.MILLISECONDS.toSeconds(manager.getChat().getSlowTime()));
	}

}