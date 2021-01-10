package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import net.minecraft.server.v1_7_R4.MinecraftServer;

public class PingCommand extends ICommand {

	@Command(name = "ping", aliases = "ms")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&7&m----------------------------------------"));
			player.sendMessage(Utils.color("&e§ &6Votre ping est de&f: " + getPing(player) + "ms"));
			player.sendMessage(Utils.color("&e§ &6Performance du serveur (1m, 5m, 15m)&f: " + getTps()));
			player.sendMessage(Utils.color("&7&m----------------------------------------"));
		} else {
			Player target = Bukkit.getPlayer(args.getArgs(0));
			if (target == null) {
				player.sendMessage(Utils.color("&eCe joueur est introuvable."));
				return;
			}
			player.sendMessage(Utils.color("&7&m----------------------------------------"));
			player.sendMessage(Utils.color("&e§ &6Le ping de &e" + target.getName() + " &6est de&f: " + getPing(target) + "ms"));
			player.sendMessage(Utils.color("&e§ &6Performance du serveur (1m, 5m, 15m)&f: " + getTps()));
			player.sendMessage(Utils.color("&7&m----------------------------------------"));
		}
	}
	

	public static String getPing(Player player) {
		int ping = ((CraftPlayer) player).getHandle().ping;
		if (ping > 150 && ping < 200) {
			return ChatColor.YELLOW + "" + ping;
		}
		if (ping > 200 && ping < 300) {
			return ChatColor.RED + "" + ping;
		}
		if (ping > 300) {
			return ChatColor.DARK_RED + "" + ping;
		}
		return ChatColor.GREEN + "" + ping;
	}

	public static String getTps() {
		String result = "";

		double[] tps = {MinecraftServer.getServer().tps1.getAverage(), MinecraftServer.getServer().tps5.getAverage(), MinecraftServer.getServer().tps15.getAverage()};
		for (Double d : tps) {
			double value = Math.min(Math.round(d * 100.0) / 100.0, 20.0);
			if (d >= 15.00) {
				result += "" + ChatColor.GREEN + value + ", ";
			} else {
				result += "" + ChatColor.YELLOW + value + ", ";
			}
		}
		result = result.substring(0, result.length() - 2);

		return result;
	}
}
