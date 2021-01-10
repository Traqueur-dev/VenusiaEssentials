package fr.traqueur.venusiaessentials.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.utils.CooldownUtils;

public class RandomTPListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();

		if (inv != null && inv.getName() != null) {
			if (inv.getName().contains("§dRandom TP")) {
				event.setCancelled(true);
				ItemStack item = event.getCurrentItem();

				if (item != null && item.getType() == Material.ENDER_PORTAL_FRAME) {

					player.closeInventory();
					CooldownUtils.addCooldown("RTP", player, (60 * 60) * 2);
					randomTP(player);

				}
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getCause() == DamageCause.FALL) {
				Player player = (Player) event.getEntity();
				if (CooldownUtils.isOnCooldown("Fall", player)) {
					event.setCancelled(true);
					CooldownUtils.removeCooldown("Fall", player);
				}
			}
		}
	}

	public void randomTP(Player player) {
		Random r = new Random();

		int coords = 2000;
		int x = r.nextInt(coords * 2) - coords;
		int y = r.nextInt(player.getWorld().getMaxHeight()) + 1;
		int z = r.nextInt(coords * 2) - coords;

		Location loc = new Location(player.getWorld(), x, y, z);

		while (!isCorrectLoc(loc)) {
			y = r.nextInt(player.getWorld().getMaxHeight()) + 1;
			x = r.nextInt(coords * 2) - coords;
			z = r.nextInt(coords * 2) - coords;
			loc.setX(x);
			loc.setY(y);
			loc.setZ(z);
		}
		CooldownUtils.addCooldown("Fall", player, 10);

		player.teleport(new Location(player.getWorld(), loc.getX(), loc.getY(), loc.getZ()));
	}

	private boolean isCorrectLoc(Location loc) {
		return loc.getBlock().getType() == Material.AIR
				&& Board.getFactionAt(new FLocation(loc)).isNone();
	}

	public static class LoreUpdater extends BukkitRunnable {

		ItemStack item;
		Player player;

		public LoreUpdater(Player player, ItemStack item) {
			this.item = item;
			this.player = player;
			this.runTaskTimer(VenusiaEssentials.getInstance(), 20L, 20L);
		}

		@Override
		public void run() {
			if (!player.isOnline() || !CooldownUtils.isOnCooldown("RTP", player)
					|| !player.getOpenInventory().getTopInventory().getName().contains("§dRandom TP")) {
				if (player.getOpenInventory().getTopInventory().getName().contains("Random TP")) {
					item.setType(Material.ENDER_PORTAL_FRAME);

					List<String> lore = new ArrayList<String>();

					lore.add(" ");
					lore.add("§4- §eCliquez-ici pour lancer la téléportation !");

					ItemMeta meta = item.getItemMeta();
					meta.getLore().clear();
					meta.setDisplayName("§a§lTéléportation aléatoire");
					meta.setLore(lore);
					item.setItemMeta(meta);
				}
				this.cancel();
			} else {

				ItemMeta meta = item.getItemMeta();
				String time = CooldownUtils.getCooldownRemaining(player, "RTP");

				List<String> lore = new ArrayList<String>();

				lore.add(" ");
				lore.add("§4- §eVous êtes encore en cooldown pour§7:");
				lore.add("§6Temps§7: §c" + time);

				meta.setLore(lore);
				item.setItemMeta(meta);
			}
		}
	}
}
