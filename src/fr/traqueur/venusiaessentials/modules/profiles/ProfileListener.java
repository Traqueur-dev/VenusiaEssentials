package fr.traqueur.venusiaessentials.modules.profiles;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

import fr.traqueur.venusiaessentials.api.utils.CooldownUtils;
import fr.traqueur.venusiaessentials.api.utils.FactionUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class ProfileListener implements Listener {

	private ImmutableSet<Material> BLOCK_BLOCKS = ImmutableSet.of(Material.IRON_DOOR, Material.IRON_DOOR_BLOCK,
			Material.WOODEN_DOOR, Material.WOOD_DOOR);
	private ImmutableSet<Material> PROTECTED_BLOCKS = ImmutableSet.of(Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK,
			Material.CACTUS, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM);

	private List<Material> materials = Lists.newArrayList(new Material[] {});

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		ProfileModule profileManager = ProfileModule.getInstance();
		Player player = e.getPlayer();

		profileManager.createProfile(player);

		if (!player.hasPlayedBefore()) {
			Bukkit.broadcastMessage(
					"§eBienvenue §d" + player.getName() + " §esur §6§lVenusia§e! " + "§7(§b/bienvenue§7)");
			player.teleport(profileManager.getServerProfile().getSpawn());
			profileManager.setNewPlayer(player);
		}

		player.sendMessage("");
		player.sendMessage("§c» §eBonjour §6" + player.getName());
		player.sendMessage("§c» §eActuellement §6" + fr.traqueur.venusiaessentials.api.utils.Utils.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers()
				+ " §ejoueur(s) connecté(s) !");
		player.sendMessage("§c» §eNous te souhaitons un bon jeu sur §6Venusia.");
		player.sendMessage("");

		Profile profile = profileManager.getProfile(player.getName());
		
		profile.getProfileInventory().applyUpdate(player);
		profile.setIpAddress(e.getPlayer().getAddress().getAddress().getHostAddress());

		if (profile.isFlyMode()) { // on set le fly mode du joueur
			profile.msg("&eVous êtes toujours en &6Fly&e.");
			player.setAllowFlight(true);
			player.setFlying(true);
		}

		if (profile.isVanish()) { // on cache le joueur si il est en vanish
			profile.msg("&eVous êtes toujours en &6Vanish&e.");
			for (Player p : Utils.getOnlinePlayers()) {
				if (p.hasPermission("base.vanish"))
					continue;
				p.hidePlayer(player);
			}
		} else if (!e.getPlayer().hasPermission("base.vanish")) { // on cache tous les joueurs qui sont en vanish
			for (Player p : Utils.getOnlinePlayers()) {
				Profile targetProfile = profileManager.getProfile(p.getName());
				if (targetProfile.isVanish()) {
					player.hidePlayer(p);
				}
			}
		}
		e.setJoinMessage(Utils.color("&7[&a+&7] &7" + player.getName()));

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(Utils.color("&7[&c-&7] &7" + p.getName()));

		ProfileModule profileManager = ProfileModule.getInstance();
		
		Profile profile = profileManager.getProfile(p.getName());

		if (profile == null) {
			return;
		}

		profile.setLogoutLocation(p.getLocation());
		profile.setLastConnectionMillis(System.currentTimeMillis());
		profile.getProfileInventory().updateContents(p);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled()) {
			return;
		}

		ProfileModule profileManager = ProfileModule.getInstance();

		Player player = event.getPlayer();
		Profile profile = profileManager.getProfile(player.getName());

		if (profile == null) {
			return;
		}

		if (profile.isJail()) {
			event.setCancelled(true);
			player.sendMessage(Utils.color("&cVous ne pouvez pas vous téléporter lorsque vous êtes en prison!"));
		}
	}

	@EventHandler
	public void onPlayerFoodChange(FoodLevelChangeEvent event) {
		if (event.isCancelled()) {
			return;
		}

		ProfileModule profileManager = ProfileModule.getInstance();

		Player player = (Player) event.getEntity();
		Profile profile = profileManager.getProfile(player.getName());

		if (profile == null) {
			return;
		}

		if (profile.isJail() || profile.isGodMode()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(event.getPlayer().getName());

		if (profile == null) {
			return;
		}

		if (profile.isJail()) {
			event.setRespawnLocation(profileManager.getServerProfile().getJails().get(0).getCubo().getCenter());
		} else {
			event.setRespawnLocation(profileManager.getServerProfile().getSpawn());
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(event.getPlayer().getName());

		if (profile == null) {
			return;
		}

		if (profile.isVanish()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled() || !(event.getEntity() instanceof Player)) {
			return;
		}
		Player damaged = (Player) event.getEntity();
		if (damaged.hasMetadata("NPC"))
			return; // <- npc

		Profile profile = ProfileModule.getInstance().getProfile(damaged.getName());

		if (profile == null) {
			return;
		}

		if (profile.isGodMode() || profile.isVanish()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onLeafDecay(LeavesDecayEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onWitherLaunch(ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof WitherSkull) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPortalCreate(PortalCreateEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public boolean onPortalEnter(EntityPortalEnterEvent e) {
		return false;
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (!event.getPlayer().isOp()) {
			return;
		}
		event.setLine(0, ChatColor.translateAlternateColorCodes('&', event.getLine(0)));
		event.setLine(1, ChatColor.translateAlternateColorCodes('&', event.getLine(1)));
		event.setLine(2, ChatColor.translateAlternateColorCodes('&', event.getLine(2)));
		event.setLine(3, ChatColor.translateAlternateColorCodes('&', event.getLine(3)));
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();
		if (location.getBlockY() <= -15) {
			CooldownUtils.addCooldown((String) "Void", (Player) player, (int) 10);
			World world = Bukkit.getWorld((String) "world");
			player.teleport(world.getSpawnLocation());
			player.sendMessage(Utils.color((String) "&eT'es vraiment un §6malade §eBernard !"));
		}
	}

	@EventHandler
	public void onLavaFlow(BlockFromToEvent event) {
		Material type;
		Block block = event.getBlock();
		if (block != null && ((type = block.getType()) == Material.LAVA || type == Material.STATIONARY_LAVA)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void damageEvent(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (CooldownUtils.isOnCooldown((String) "Void", (Player) p)) {
				e.setCancelled(true);
			}
			FLocation location = new FLocation(p.getLocation());
			Faction faction = Board.getFactionAt((FLocation) location);
			if (e.getCause() == EntityDamageEvent.DamageCause.FALL && faction.isWarZone()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		ItemStack item = event.getItem();
		if (item.getType() == Material.INK_SACK && item.getDurability() == 15) {
			if (event.getBlock().getType() == Material.DISPENSER) {
				event.setCancelled(true);
			}
		}

		InventoryHolder inventoryHolder = (InventoryHolder) event.getBlock().getState();
		for (ItemStack items : inventoryHolder.getInventory()) {
			if (items == null
					|| items.getType() == Material.INK_SACK && items.getAmount() > 0 && item.getDurability() == 15) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryItemMove(InventoryMoveItemEvent event) {
		if (event.getSource().getType() == InventoryType.DISPENSER
				|| event.getSource().getType() == InventoryType.DROPPER) {
			for (ItemStack item : event.getSource().getContents()) {
				if (item != null && item.getAmount() <= 0) {
					item.setAmount(1);
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getItem() != null && event.getItem().getAmount() <= 0) {
				event.getItem().setAmount(1);
			}
		}
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		if (event.getEntity().getItemStack().getAmount() <= 0) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onBlockPlaceInOutpost(BlockPlaceEvent event) {
		Block block;
		Player player = event.getPlayer();
		if (!player.hasPermission("core.outposts.place")
				&& this.materials.contains((Object) (block = event.getBlockPlaced()).getType())) {
			FLocation location = new FLocation(block.getLocation());
			FPlayer fPlayer = FPlayers.i.get(player);
			if (!fPlayer.hasFaction()) {
				return;
			}
			Faction faction = fPlayer.getFaction();
			if (FactionUtils.isApAt(location, faction)) {
				player.sendMessage(Utils.color(
						(String) "&eD\u00e9sol\u00e9 mais vous ne pouvez pas poser ce &6&lBlock &f&e dans votre &6&lAvant-Poste&f&e!"));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (player.getGameMode() != GameMode.CREATIVE) {
			if (event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
				if (event.getClick() == ClickType.NUMBER_KEY) {
					if (CooldownUtils.isOnCooldown("TntGlitch", player)) {
						event.setCancelled(true);
					} else {
						CooldownUtils.addCooldown("TntGlitch", player, 2);
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		if (event.isCancelled()) {
			if (player.getGameMode() != GameMode.CREATIVE && !player.getAllowFlight()) {
				if (block.getType().isSolid() && !(block.getState() instanceof Sign)) {
					Location loc = player.getLocation();
					Location blockLoc = block.getLocation();
					if (loc.getBlockY() > blockLoc.getBlockY()) {
						player.teleport(player.getLocation());
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();

			if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
				ItemStack item = player.getItemInHand();
				if (BLOCK_BLOCKS.contains(item.getType())) {
					Block block = event.getClickedBlock().getRelative(event.getBlockFace());
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							for (int z = -1; z <= 1; z++) {
								Material material = block.getRelative(x, y, z).getType();
								if (!PROTECTED_BLOCKS.contains(material))
									continue;
								player.updateInventory();
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onSplash(PlayerInteractEvent e) {

		if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
				&& (e.getPlayer().getItemInHand().getType() == Material.EXP_BOTTLE)) {
			Player p = e.getPlayer();

			if (p.getItemInHand().hasItemMeta()) {
				ItemMeta im = p.getItemInHand().getItemMeta();

				if ((im.getLore().get(0) != null) && (im.getLore().get(1) != null)
						&& (((String) im.getLore().get(0)).equals("§8• §aNiveaux §8•"))) {
					e.setCancelled(true);

					int xp = Integer.parseInt(ChatColor.stripColor((String) im.getLore().get(1)));

					if (p.getItemInHand().getAmount() > 1) {
						p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
					} else {
						p.getInventory().removeItem(new ItemStack[] { p.getItemInHand() });
					}

					p.giveExpLevels(xp);
					p.sendMessage("§6" + xp + " §eniveau(x) ajout\u00e9(s) !");
					p.updateInventory();
				}
			}
		}
	}
}
