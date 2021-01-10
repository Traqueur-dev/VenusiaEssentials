package fr.traqueur.venusiaessentials.modules.profiles;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.jsons.DiscUtil;
import fr.traqueur.venusiaessentials.api.modules.Saveable;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.economy.commands.MoneyAdminCommand;
import fr.traqueur.venusiaessentials.modules.economy.commands.MoneyCommand;
import fr.traqueur.venusiaessentials.modules.economy.commands.MoneyTopCommand;
import fr.traqueur.venusiaessentials.modules.groups.GroupModule;
import fr.traqueur.venusiaessentials.modules.profiles.commands.RatioCommand;
import fr.traqueur.venusiaessentials.modules.profiles.commands.WelcomeCommand;
import fr.traqueur.venusiaessentials.modules.profiles.server.ServerProfile;
import fr.traqueur.venusiaessentials.modules.profiles.server.ServerProfileModule;
import net.minecraft.util.com.google.gson.reflect.TypeToken;

public class ProfileModule extends Saveable {

	private static ProfileModule instance;
	private List<Profile> profiles;
	private Player newPlayer;

	private List<Entry<String, Double>> baltop;
	
	public ProfileModule(Plugin plugin) {
		super(plugin, "Players Profiles");

		instance = this;

		this.registerCommand(new RatioCommand());
		this.registerCommand(new WelcomeCommand());

		this.registerCommand(new MoneyAdminCommand());
		this.registerCommand(new MoneyTopCommand());
		this.registerCommand(new MoneyCommand());
		
		this.registerListener(new ProfileListener());

		this.profiles = Lists.newArrayList();
		this.baltop = Lists.newArrayList();
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				ProfileModule.getInstance().update();
				
			}
		}, 20, 20*60*30);
	}

	@Override
	public File getFile() {
		return new File(this.getPlugin().getDataFolder(), "/profiles/");
	}

	@Override
	public void loadData() {
		if (!this.getFile().exists()) {
			this.getFile().mkdir();
		}
		File[] files = this.getFile().listFiles();
		for (int count = 0; count < files.length; ++count) {
			File file = files[count];
			String content = DiscUtil.readCatch((File) file);
			if (content == null)
				continue;
			try {
				Profile profile = (Profile) this.getGson().fromJson(content, new TypeToken<Profile>() {
				}.getType());
				profile.setTeleportRequests(Lists.newArrayList()); // set because is transient
				this.createProfile(profile);
				continue;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	@Override
	public void saveData() {
		for (Profile profile : this.profiles) {
			DiscUtil.writeCatch(profile.getProfileFile(), this.getGson().toJson(profile));
		}
	}
	
	public void update() {
		long time = System.currentTimeMillis();
		
		this.baltop.clear();
		
		for (Profile profile : this.getProfiles()) {
				this.baltop.add(new SimpleEntry<String, Double>(profile.getPlayerName(), profile.getBalance()));
		}
	
		Collections.sort(this.baltop, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				double m1 = o1.getValue();
				double m2 = o2.getValue();
				if (m1 < m2) {
					return 1;
				} else if (m1 > m2) {
					return -1;
				}
				return 0;
			}
		});
		
		time = System.currentTimeMillis() - time;
		Bukkit.broadcastMessage(
				Utils.getPrefix("Classement") + "§eMise à jour du §6classement §cArgent §8(§7" + time + "ms§8)");
	}
	

	public ServerProfile getServerProfile() {
		ServerProfileModule serverProfileManager = ServerProfileModule.getInstance();
		return serverProfileManager.getServerProfile();
	}

	public Profile getProfile(String value) {
		for (Profile profile : this.profiles) {
			if (profile.getPlayerName().equalsIgnoreCase(value)) {
				return profile;
			}
		}
		return null;
	}

	public void createProfile(Profile profile) {
		if (!this.profileExist(profile.getPlayerName())) {
			this.profiles.add(profile);
		}
	}

	public void createProfile(Player player) {
		GroupModule groupManager = GroupModule.getInstance();
		Profile profile = new Profile(player, groupManager.getDefaultGroup());
		this.createProfile(profile);
	}

	public void removeProfile(Player player) {
		Profile profile = this.getProfile(player.getName());
		this.profiles.remove(profile);
	}

	public void removeProfile(Profile profile) {
		this.profiles.remove(profile);
	}

	public boolean profileExist(String name) {
		for (Profile profile : getProfiles()) {
			if (profile.getPlayerName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValid(Player player) {
		if (player == null) {
			return false;
		}
		return true;
	}

	public boolean isValid(OfflinePlayer player) {
		if (player == null) {
			return false;
		}
		return true;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public Player getNewPlayer() {
		return newPlayer;
	}

	public void setNewPlayer(Player newPlayer) {
		this.newPlayer = newPlayer;
	}

	public static ProfileModule getInstance() {
		return instance;
	}

	public List<Entry<String, Double>> getBaltop() {
		return baltop;
	}
	
	
}
