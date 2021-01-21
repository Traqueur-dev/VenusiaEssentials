package fr.traqueur.venusiaessentials.modules.profiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.groups.Group;
import fr.traqueur.venusiaessentials.modules.homes.Home;
import fr.traqueur.venusiaessentials.modules.kit.Kit;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.ProfileInventory;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.TemporaryKit;
import fr.traqueur.venusiaessentials.modules.punishments.Punishment;
import fr.traqueur.venusiaessentials.modules.punishments.PunishmentType;
import fr.traqueur.venusiaessentials.modules.teleports.TeleportCooldown;

public class Profile {

	// player
	private String playerName, ipAddress;
	private UUID playerId;

	private double balance;
	
	private Location logoutLocation;
	private long lastConnectionMillis;

	//whipser
	private transient String lastWhisper;
	private boolean spyMode;
	
	// modes
	private boolean flyMode, godMode, vanish, jail;

	// statistics
	private int kills;
	private int deaths;

	// groups
	private String group;
	private long expirationMillis;

	// messages
	private List<String> ignoredPlayers;


	// kits
	private List<TemporaryKit> temporaryKits;

	// homes
	private List<Home> homes;

	// punishments
	private List<Punishment> punishments;
	private List<Punishment> nonExpiredPunishments;

	// profiles
	private ProfileInventory profileInventory;

	// transient
	private transient Location lastTeleportLocation;
	private transient String replyMessage;
	private transient List<TeleportCooldown> teleportRequests = Lists.newArrayList();

	public Profile(Player player, Group group) {
		this.playerName = player.getName();
		this.playerId = player.getUniqueId();
		
		this.ipAddress = player.getAddress().getAddress().getHostAddress();
		this.logoutLocation = player.getLocation();
		this.lastConnectionMillis = System.currentTimeMillis();

		this.balance = 30;

		this.lastWhisper = null;
		this.spyMode = false;
		
		this.flyMode = false;
		this.godMode = false;
		this.vanish = false;
		this.jail = false;

		this.kills = 0;
		this.deaths = 0;

		this.ignoredPlayers = new ArrayList<>();

		this.homes = new ArrayList<>();
		this.temporaryKits = new ArrayList<>();

		this.profileInventory = new ProfileInventory(player);

		this.group = group.getName();
		this.expirationMillis = -1;

		this.punishments = Lists.newArrayList();
		this.nonExpiredPunishments = Lists.newArrayList();

		this.teleportRequests = Lists.newArrayList();
	}
	
	public boolean withdraw(double value) {
		if (value <= 0 || value > this.balance)
			return false;

		this.balance -= Math.max(0.00, value);
		return true;
	}

	public void deposit(double value) {
		if (value <= 0)
			return;

		this.balance += value;
	}

	public boolean has(double value) {
		return (this.balance < value) ? false : true;
	}
	
	public void refreshNonExpiredPunishments() {
		this.setNonExpiredPunishments(
				punishments.stream().filter(punishment -> !punishment.isExpired()).collect(Collectors.toList()));
	}

	public void msg(String msg) {
		this.msg(msg, new Object[] {});
	}

	public void msg(String msg, Object... param) {
		Player player = this.getPlayer();
		if (player == null)
			return;
		player.sendMessage(Utils.color(String.format(msg, param)));
	}

	public TemporaryKit getTemporaryKit(Kit kit) {
		if (this.temporaryKits.isEmpty())
			return null;

		for (int i = 0; i < temporaryKits.size(); i++) {
			TemporaryKit temporaryKit = temporaryKits.get(i);
			if (temporaryKit.getKitName().equalsIgnoreCase(kit.getName())) {
				return temporaryKit;
			}
		}
		return null;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(this.playerName);
	}

	public boolean isOnline() {
		Player player = this.getPlayer();
		return player != null && player.isOnline();
	}

	public void incrementKills() {
		++this.kills;
	}

	public void incrementDeaths() {
		++this.deaths;
	}

	public void resetRatio() {
		this.kills = 0;
		this.deaths = 0;
	}

	public boolean isMuted() {
		if (this.getNonExpiredPunishments().isEmpty()) {
			return false;
		}

		boolean muted = this.getNonExpiredPunishments().stream()
				.filter(punishment -> punishment.getPunishmentType() == PunishmentType.MUTE).count() > 0;

		return muted;
	}

	public boolean isBanned() {
		if (this.getNonExpiredPunishments().isEmpty()) {
			return false;
		}

		boolean banned = this.getNonExpiredPunishments().stream()
				.filter(punishment -> punishment.getPunishmentType() == PunishmentType.BAN).count() > 0;

		return banned;
	}

	public File getProfileFile() {
		return new File(ProfileModule.getInstance().getFile(), this.playerName + ".json");
	}

	public void punish(Punishment punishment) {
		this.getPunishments().add(punishment);
		this.refreshNonExpiredPunishments();

		if (Bukkit.getPlayer(this.playerName) != null) {
			switch (punishment.getPunishmentType()) {
			case BAN:
				this.getPlayer().kickPlayer(Utils.LINE + "\n§6Venusia\n§eVous vous êtes fait §6bannir §epar §c"
						+ punishment.getPunisher() + "§e pour \"§a" + punishment.getReason() + "§e\".\n\n"
						+ (punishment.getTime() == -1 ? "§eCette sanction est §cdfinitive§e."
								: "§eCette §csanction §eprendra §afin§e le: §c"
										+ DateFormatUtils.format(punishment.getTime(), "dd/MM/yyyy | HH:mm:ss") + "§e.")
						+ "\n\n§cEn cas de problèmes avec cette sanction, contactez un membre du personnel.\n"
						+ Utils.LINE);
				Bukkit.broadcastMessage(Utils.LINE);
				Bukkit.broadcastMessage("§eSanction:");
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(this.getPlayerName() + "§ec'est fait §6bannir §epar §c" + punishment.getPlayerName() + "§e pour \"§a"
						+ punishment.getReason() + "§e\".");
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(Utils.LINE);
				break;

			case MUTE:
				Bukkit.broadcastMessage(Utils.LINE);
				Bukkit.broadcastMessage("§eSanction:");
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(this.getPlayerName() + "§ec'est fait §6mute §epar §c" + punishment.getPlayerName() + "§e pour \"§a"
						+ punishment.getReason() + "§e\".");
				Bukkit.broadcastMessage(" ");
				this.getPlayer()
						.sendMessage(punishment.getTime() == -1 ? "§eCette sanction est §cdéfinitive§e."
								: "§eCette §csanction §eprendra §afin§e le: §c"
										+ DateFormatUtils.format(punishment.getTime(), "dd/MM/yyyy | HH:mm:ss")
										+ "§e.");
				this.msg(" ");
				this.msg(
						"§cEn cas de problèmes avec cette sanction, contactez un membre du personnel.");
				Bukkit.broadcastMessage(Utils.LINE);
				break;

			case KICK:
				this.getPlayer().kickPlayer(Utils.LINE + "\n§6Venusia\n§eVous vous êtes fait §6expuls§ §epar §c"
						+ punishment.getPunisher() + "§e pour \"§a" + punishment.getReason()
						+ "§e\".\n§cEn cas de problèmes avec cette sanction, contactez un membre du personnel.\n"
						+ Utils.LINE);
				Bukkit.broadcastMessage(Utils.LINE);
				Bukkit.broadcastMessage("§eSanction:");
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(this.getPlayerName() + "§ec'est fait §6kick §epar §c" + punishment.getPlayerName() + "§e pour \"§a"
						+ punishment.getReason() + "§e\".");
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(Utils.LINE);
				break;
			}
		}
	}

	public boolean isRankExpired() {
		return expirationMillis == -1 ? false : expirationMillis < System.currentTimeMillis();
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}

	public Location getLogoutLocation() {
		return logoutLocation;
	}

	public void setLogoutLocation(Location logoutLocation) {
		this.logoutLocation = logoutLocation;
	}

	public long getLastConnectionMillis() {
		return lastConnectionMillis;
	}

	public void setLastConnectionMillis(long lastConnectionMillis) {
		this.lastConnectionMillis = lastConnectionMillis;
	}

	public boolean isFlyMode() {
		return flyMode;
	}

	public void setFlyMode(boolean flyMode) {
		this.flyMode = flyMode;
	}

	public boolean isGodMode() {
		return godMode;
	}

	public void setGodMode(boolean godMode) {
		this.godMode = godMode;
	}

	public boolean isVanish() {
		return vanish;
	}

	public void setVanish(boolean vanish) {
		this.vanish = vanish;
	}

	public boolean isJail() {
		return jail;
	}

	public void setJail(boolean jail) {
		this.jail = jail;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}


	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public long getExpirationMillis() {
		return expirationMillis;
	}

	public void setExpirationMillis(long expirationMillis) {
		this.expirationMillis = expirationMillis;
	}

	public List<String> getIgnoredPlayers() {
		return ignoredPlayers;
	}

	public void setIgnoredPlayers(List<String> ignoredPlayers) {
		this.ignoredPlayers = ignoredPlayers;
	}

	public List<TemporaryKit> getTemporaryKits() {
		return temporaryKits;
	}

	public void setTemporaryKits(List<TemporaryKit> temporaryKits) {
		this.temporaryKits = temporaryKits;
	}

	public List<Home> getHomes() {
		return homes;
	}

	public void setHomes(List<Home> homes) {
		this.homes = homes;
	}

	public List<Punishment> getPunishments() {
		return punishments;
	}

	public void setPunishments(List<Punishment> punishments) {
		this.punishments = punishments;
	}

	public List<Punishment> getNonExpiredPunishments() {
		return nonExpiredPunishments;
	}

	public void setNonExpiredPunishments(List<Punishment> nonExpiredPunishments) {
		this.nonExpiredPunishments = nonExpiredPunishments;
	}

	public ProfileInventory getProfileInventory() {
		return profileInventory;
	}

	public void setProfileInventory(ProfileInventory profileInventory) {
		this.profileInventory = profileInventory;
	}

	public Location getLastTeleportLocation() {
		return lastTeleportLocation;
	}

	public void setLastTeleportLocation(Location lastTeleportLocation) {
		this.lastTeleportLocation = lastTeleportLocation;
	}

	public String getReplyMessage() {
		return replyMessage;
	}

	public void setReplyMessage(String replyMessage) {
		this.replyMessage = replyMessage;
	}

	public List<TeleportCooldown> getTeleportRequests() {
		return teleportRequests;
	}

	public void setTeleportRequests(List<TeleportCooldown> teleportRequests) {
		this.teleportRequests = teleportRequests;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getLastWhisper() {
		return lastWhisper;
	}

	public void setLastWhisper(String lastWhisper) {
		this.lastWhisper = lastWhisper;
	}

	public boolean isSpyMode() {
		return spyMode;
	}

	public void setSpyMode(boolean spyMode) {
		this.spyMode = spyMode;
	}

	public boolean toggleSpy() {
		spyMode = !spyMode;
		return spyMode;
	}
	

}
