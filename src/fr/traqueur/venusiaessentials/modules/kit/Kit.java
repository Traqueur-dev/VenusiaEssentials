package fr.traqueur.venusiaessentials.modules.kit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.api.utils.ItemBuilder;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.TemporaryKit;

public class Kit {

	private UUID id;
	private String name; // nom
	private ItemStack[] items; // items du kit
	private long delay; // délais en millis d'utilisation
	private int maxUse; // utilisation maximal (par défaut INFINI)
	private Map<UUID, Long> cooldownMap; // stockage des cooldowns individuel
	private Map<UUID, Integer> useMap; // stockage des utilisations individuel [ne servant que si le field maxUse !=
										// Integer.MAX_VALUE]
	private ItemBuilder icon;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemStack[] getItems() {
		return items;
	}

	public void setItems(ItemStack[] items) {
		this.items = items;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public int getMaxUse() {
		return maxUse;
	}

	public void setMaxUse(int maxUse) {
		this.maxUse = maxUse;
	}

	public Map<UUID, Long> getCooldownMap() {
		return cooldownMap;
	}

	public void setCooldownMap(Map<UUID, Long> cooldownMap) {
		this.cooldownMap = cooldownMap;
	}

	public Map<UUID, Integer> getUseMap() {
		return useMap;
	}

	public void setUseMap(Map<UUID, Integer> useMap) {
		this.useMap = useMap;
	}

	public ItemBuilder getIcon() {
		return icon;
	}

	public void setIcon(ItemBuilder icon) {
		this.icon = icon;
	}

	// Constructeur -> GSON
	public Kit() {
	}

	/**
	 * Création d'un kit
	 * 
	 * @param name   -> Nom du Kit
	 * @param player -> L'ensemble de l'inventaire du joueur établit constitura les
	 *               items du Kit
	 * @param delay  -> Intervale en millis d'utilisation entre chaque kit [Utiliser
	 *               {@link java.util.concurrent.TimeUnit} pour la gestion du temps
	 *               en millis]
	 */
	public Kit(UUID id, String name, Player player, long delay, ItemBuilder icon) {
		this.id = id;
		this.name = name;
		this.items = player.getInventory().getContents();
		this.delay = delay;
		this.maxUse = Integer.MAX_VALUE;
		this.cooldownMap = Maps.newHashMap();
		this.useMap = Maps.newHashMap();
		this.icon = icon;
	}

	/**
	 * Création d'un kit avec l'attribution d'une valeur d'utilisation maximal
	 * 
	 * @param name   -> Nom du Kit
	 * @param player -> L'ensemble de l'inventaire du joueur établit constitura les
	 *               items du Kit
	 * @param delay  -> Intervale en millis d'utilisation entre chaque kit [Utiliser
	 *               {@link java.util.concurrent.TimeUnit} pour la gestion du temps
	 *               en millis]
	 * @param maxUse -> Utilisation maximal d'un kit pour chaque joueur
	 */
	public Kit(UUID id, String name, Player player, long delay, int maxUse, ItemBuilder icon) {
		this(id, name, player, delay, icon);
		this.maxUse = maxUse;
	}

	/**
	 * Application d'un kit auprés d'un utilisateur
	 * 
	 * @param player -> L'utilisateur qui va recevoir le kit
	 */
	public void applyPlayer(Player player, boolean force) {
		if (maxUse != Integer.MAX_VALUE && getUses(player) >= maxUse && !force) {
			player.sendMessage(Utils.color("&eVous avez atteint la limite d'utilisation du &6Kit &c" + name + "&e."));
			return;
		}
		if (getRemaining(player) > 0L && !force) {
			player.sendMessage(Utils.color(
					"&eVeuillez patienter encore &6&l" + DurationFormatter.getRemaining(getRemaining(player), true)
							+ " &eavant " + "de pouvoir utiliser le &6Kit &c" + name + "&e."));
			return;
		}
		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if (item != null && item.getType() != Material.AIR) {
				HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(item);
				if (!leftOver.isEmpty()) {
					player.getWorld().dropItem(player.getLocation(), item);
				}
			}
		}

		player.updateInventory();
		player.sendMessage(Utils.color("&eVous venez d'utiliser le &6&lKit &9" + name + "&e."));

		long newDelay = System.currentTimeMillis() + delay;
		if (newDelay - System.currentTimeMillis() > 0L && !force) {
			player.sendMessage(Utils.color("&eProchaine utilisation du kit dans " + "&6"
					+ DurationFormatter.getRemaining(newDelay - System.currentTimeMillis(), true) + "&e."));
			cooldownMap.put(player.getUniqueId(), newDelay);
		}
		if (maxUse != Integer.MAX_VALUE) {
			addKitUse(player);

			if (this.getUses(player) < maxUse) {
				player.sendMessage(Utils.color(
						"&eVous pouvez utiliser ce kit encore &f" + this.getUses(player) + "/" + maxUse + "&e."));
			}
		}
	}

	public void addKitUse(Player player) {
		this.useMap.put(player.getUniqueId(), this.getUses(player) + 1);
	}

	public boolean hasPermission(Player player) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(player.getName());
		TemporaryKit temporaryKit = profile.getTemporaryKit(this);
		if (temporaryKit != null) {
			if (temporaryKit.hasExpired()) {
				profile.getTemporaryKits().remove(temporaryKit);
				profile.msg("&eVotre kit &6&l" + this.getName()
						+ " &evient d'expiré&e.");
				return false;
			}

			return true;
		}
		return player.hasPermission("base.kits." + name);
	}

	public boolean hasAccess(Player player) {
		return this.getRemaining(player) < 0L && getUses(player) < this.maxUse;
	}

	public long getRemaining(Player player) {
		if (!this.cooldownMap.containsKey(player.getUniqueId()))
			return 0L;
		long time = cooldownMap.get(player.getUniqueId());
		return time - System.currentTimeMillis();
	}

	public int getUses(Player player) {
		if (!this.useMap.containsKey(player.getUniqueId()))
			return 0;
		return this.useMap.get(player.getUniqueId());
	}

}
