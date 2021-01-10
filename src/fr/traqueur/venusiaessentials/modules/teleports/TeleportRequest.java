package fr.traqueur.venusiaessentials.modules.teleports;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.utils.Utils;

public class TeleportRequest implements Runnable {

	private Player player;
	private long runtime;
	private Location initialLocation;
	private int taskID = -1;

	private @Nullable Location to;
	private @Nullable Player target;

	public TeleportRequest(Player player, Object obj) {
		this.player = player;
		this.runtime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5L);
		this.initialLocation = player.getLocation();
		if (obj instanceof Player) {
			this.target = (Player) obj;
		} else {
			this.to = (Location) obj;
		}
	}

	@Override
	public void run() {
		if (!TeleportModule.getInstance().getTeleportRequests().containsKey(player.getUniqueId())) {
			this.cancel(false);
			return;
		}

		if (this.player == null || !this.player.isOnline() || player.getLocation() == null) {
			this.cancel(false);
			return;
		}

		if (isOutsideRadius()) {
			this.cancel(true);
			return;
		}

		if (this.getRemaining() <= 0L) {
			this.cancel(false);

			player.sendMessage(Utils.color("&eVous venez d'être &6téléporté&e."));

			if (to != null) {
				player.teleport(to);
			} else {

				if (target == null || !target.isOnline()) {
					this.cancel(true);
					player.sendMessage(Utils.color(
							"&7&oLe joueur qui a accepter votre demande de téléportation vient de se deconnecter."));
					return;
				}
				player.teleport(target.getLocation());
			}
		}
	}

	public boolean isOutsideRadius() {
		return initialLocation.getWorld().getUID() != player.getWorld().getUID()
				|| player.getLocation().distanceSquared(initialLocation) >= 1;
	}

	public void cancel(boolean notify) {
		TeleportModule.getInstance().getTeleportRequests().remove(player.getUniqueId());
		Bukkit.getScheduler().cancelTask(taskID);
		if (!notify)
			return;
		player.sendMessage(Utils.color("&eVotre &6téléportation &evient d'être annulée."));
	}

	public long getRemaining() {
		return this.runtime - System.currentTimeMillis();
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public Player getPlayer() {
		return player;
	}

	public long getRuntime() {
		return runtime;
	}

	public Location getInitialLocation() {
		return initialLocation;
	}

	public Location getTo() {
		return to;
	}

	public Player getTarget() {
		return target;
	}
	
	
}
