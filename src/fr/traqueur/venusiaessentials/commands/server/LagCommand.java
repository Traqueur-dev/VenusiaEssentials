package fr.traqueur.venusiaessentials.commands.server;

import java.lang.management.ManagementFactory;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.commands.essentials.PingCommand;
import net.minecraft.server.v1_7_R4.MinecraftServer;

public class LagCommand extends ICommand {

	@Command(name = "lag", permission = "base.memory")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();

		long memory = (Runtime.getRuntime().totalMemory() / 1024L) / 1024L;
		long memoryMax = (Runtime.getRuntime().maxMemory() / 1024L) / 1024L;
		long memoryFree = (Runtime.getRuntime().freeMemory() / 1024L) / 1024L;
		
		long upTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		int procesors = Runtime.getRuntime().availableProcessors();

		double percent = 100.0F;
		double[] tps = {MinecraftServer.getServer().tps1.getAverage(), MinecraftServer.getServer().tps5.getAverage(), MinecraftServer.getServer().tps15.getAverage()};
		double value = Math.min(Math.round(tps[0] * 100.0) / 100.0, 20.0);

		double lagData = percent - (value * 5);

		player.sendMessage(Utils.color(""));
		player.sendMessage(Utils.color(Utils.LINE));
		player.sendMessage(Utils.color("&e§ &6Durée de fonctionnement&f: &c"
				+ DurationFormatter.getDurationWords(System.currentTimeMillis() - upTime)));
		player.sendMessage(Utils.color("&e§ &eIl y a actuellement &6" + Utils.getOnlinePlayers().length
				+ "/"
				+ Bukkit.getMaxPlayers() + " &ejoueur(s) en ligne."));
		player.sendMessage(Utils.color("&e§ &6Performances &7(&e1m&8, &e5m&8, &e15m&7)&f: " + PingCommand.getTps()));
		player.sendMessage(Utils.color("&e§ &6Chance de lag(s) &7(&e%&7)&f: &e" + lagData + "&6%"));
		player.sendMessage(Utils.color("&e§ &6Memoire utilisée§&f: &e" + memory + " &6MO&7/&e" + memoryMax + " &6MO"));
		player.sendMessage(Utils.color("&e§ &6Memoire libre&f: &e" + memoryFree + " &6MO&7/&e" + memoryMax + " &6MO"));
		player.sendMessage(
				Utils.color("&e§ &6Processeurs &7(&ccoeurs&7)&f: &c" + procesors / 2 + " &een cours d'utilisation"));
		player.sendMessage(Utils.color("&e§ &6Mondes&7:"));

		for (World world : Bukkit.getWorlds()) {
			int entites = world.getEntities().size();
			int chunck = world.getLoadedChunks().length;
			int tileEntities = 0;

			for (Chunk chunk : world.getLoadedChunks()) {
				tileEntities += chunk.getTileEntities().length;
			}
			Environment envi = world.getEnvironment();

			player.sendMessage(Utils.color(" &c- &6" + world.getName() + "&7(" + envi + ") &e" + chunck
					+ " chunks&7, &e" + entites + " entitées&7, &e" + tileEntities + " tiles &7(&cblocks&7)&6."));
		}
		player.sendMessage(Utils.color(Utils.LINE));
	}

}
