package fr.traqueur.venusiaessentials.modules.warps;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.google.common.collect.Lists;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.jsons.DiscUtil;
import fr.traqueur.venusiaessentials.api.modules.Saveable;
import fr.traqueur.venusiaessentials.api.utils.CooldownUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.warps.commands.WarpCommand;
import fr.traqueur.venusiaessentials.modules.warps.listeners.WarpListener;
import net.minecraft.util.com.google.gson.reflect.TypeToken;

public class WarpModule extends Saveable {

	private static WarpModule instance;

	private List<Warp> warps;
	
	public WarpModule(Plugin plugin) {
		super(plugin, "Warp");
		instance = this;
		warps = Lists.newArrayList();
		this.registerCommand(new WarpCommand());
		this.registerListener(new WarpListener());
	}


	public void teleport(Player player, Warp warp) {
		if (player.hasPermission("base.warp.instant")) {
			player.teleport(warp.getLocation(), TeleportCause.COMMAND);
			player.sendMessage(Utils.color("&aTéléportation &eau warp &6" + warp.getName() + "&e."));
		} else {
			int delay = 5;
			CooldownUtils.addCooldown("WarpTP",player, delay + 1);
			Bukkit.getScheduler().runTaskLaterAsynchronously(Plugin.getInstance(),new WarpTeleportationTask(player, warp), 20*delay);
			player.sendMessage(Utils.color("&aTéléportation &eau warp &6" + warp.getName() + " &edans &c" + delay + " &esecondes."));
		}
	}
	
	public Warp getWarp(String name) {
		for (Warp warp : warps) {
			if (warp.getName().equals(name)) {
				return warp;
			}
		}
		return null;
	}
	
	public boolean exist(Warp warp) {
		return this.exist(warp.getName());
	}
	
	public boolean exist(String name) {
		for (Warp w : warps) {
			if (name.equals(w.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean createWarp(Warp warp) {
		if (!exist(warp)) {
			this.warps.add(warp);
			return true;
		}
		return false;
	}
	
	public boolean deleteWarp(Warp name) {
		if (!exist(name.getName())) {
			return false;
		}
		this.warps.remove(name);
		return true;
	}
	
	public boolean setVisible(Warp name) {
		if (!exist(name.getName())) {
			return false;
		}
		boolean isVisible = !name.isVisible();
		name.setVisible(isVisible);
		return true;
	}

	
	public List<Warp> getWarps() {
		return this.warps;
	}
	
	@Override
	public File getFile() {
		return new File(Plugin.getInstance().getDataFolder(), "/warps/warps.json");
	}

	@Override
	public void loadData() {
		String content = DiscUtil.readCatch(this.getFile());
		if(content != null) {
			Type type = new TypeToken<List<Warp>>() {}.getType();
			this.warps = this.getGson().fromJson(content, type);
		}
	}

	@Override
	public void saveData() {
		DiscUtil.writeCatch(this.getFile(), this.getGson().toJson(this.warps));
	}

	public static WarpModule getInstance() {
		return instance;
	}
	
}
