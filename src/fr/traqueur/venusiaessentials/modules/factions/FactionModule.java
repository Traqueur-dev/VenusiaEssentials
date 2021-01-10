package fr.traqueur.venusiaessentials.modules.factions;

import java.io.File;
import java.util.List;

import org.bukkit.event.Listener;

import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.jsons.DiscUtil;
import fr.traqueur.venusiaessentials.api.modules.Saveable;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import net.minecraft.util.com.google.gson.reflect.TypeToken;

public class FactionModule extends Saveable {


	public static FactionModule getInstance() {
		return instance;
	}

	public List<FactionProfile> getProfiles() {
		return profiles;
	}

	private static FactionModule instance;

	private List<FactionProfile> profiles;

	public FactionModule(Plugin plugin) {
		super(plugin, "Factions Profiles");

		instance = this;

		this.profiles = Lists.newArrayList();

		this.registerListener((Listener) new FactionListener());
	}

	public FactionProfile getProfile(Faction faction) {
		return this.getProfile(faction.getId());
	}

	public FactionProfile getProfile(String value) {
		for (int count = 0; count < this.profiles.size(); ++count) {
			FactionProfile profile = this.profiles.get(count);
			if (!profile.getUniqueId().equalsIgnoreCase(value))
				continue;
			return profile;
		}
		return null;
	}

	public void createProfile(Faction faction) {
		FactionProfile existingProfile = this.getProfile(faction);
		if (existingProfile == null) {
			FactionProfile profile = new FactionProfile(faction);
			this.profiles.add(profile);
		}
	}

	public void deleteProfile(Faction faction) {
		FactionProfile existingProfile = this.getProfile(faction);
		if (existingProfile == null) {
			this.profiles.remove(existingProfile);
		}
	}

	@Override
	public File getFile() {
		return Utils.getFormatedFile((Plugin) this.getPlugin(), (String) "/factions/");
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
				FactionProfile profile = (FactionProfile) this.getGson().fromJson(content,
						new TypeToken<FactionProfile>() {
						}.getType());
				this.profiles.add(profile);
				continue;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	@Override
	public void saveData() {
		for (FactionProfile profile : this.profiles) {
			DiscUtil.writeCatch((File) profile.getProfileFile(), (String) this.getGson().toJson((Object) profile));
		}
	}
}
