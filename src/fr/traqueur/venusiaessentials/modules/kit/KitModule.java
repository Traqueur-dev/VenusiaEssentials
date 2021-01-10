package fr.traqueur.venusiaessentials.modules.kit;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.jsons.DiscUtil;
import fr.traqueur.venusiaessentials.api.modules.Saveable;
import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.modules.kit.commands.KitCommand;
import fr.traqueur.venusiaessentials.modules.kit.commands.KitCreateCommand;
import fr.traqueur.venusiaessentials.modules.kit.commands.KitDeleteCommand;
import fr.traqueur.venusiaessentials.modules.kit.commands.KitListCommand;
import fr.traqueur.venusiaessentials.modules.kit.commands.KitListKitCommand;
import fr.traqueur.venusiaessentials.modules.kit.commands.KitPreviewCommand;
import fr.traqueur.venusiaessentials.modules.kit.commands.KitRemoveKitCommand;
import fr.traqueur.venusiaessentials.modules.kit.commands.KitSetKitCommand;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.TemporaryKit;
import net.minecraft.util.com.google.gson.reflect.TypeToken;

public class KitModule extends Saveable {

	private static KitModule instance;
	private Map<UUID, Kit> kits;
	
	public Map<UUID, Kit> getKits() {
		return kits;
	}

	public void setKits(Map<UUID, Kit> kits) {
		this.kits = kits;
	}

	public static KitModule getInstance() {
		return instance;
	}

	public KitModule(Plugin plugin) {
		super(plugin, "Kits");
		
		instance = this;
		
		this.kits = Maps.newLinkedHashMap();
		
		this.registerCommand(new KitCommand());
		this.registerCommand(new KitCreateCommand());
		this.registerCommand(new KitDeleteCommand());
		this.registerCommand(new KitListCommand());
		this.registerCommand(new KitListKitCommand());
		this.registerCommand(new KitPreviewCommand());
		this.registerCommand(new KitRemoveKitCommand());
		this.registerCommand(new KitSetKitCommand());

	}

	public Kit getKit(String name) {
		for (Kit kit : kits.values()) {
			if (kit.getName().equalsIgnoreCase(name)) {
				return kit;
			}
		}
		return null;
	}

	public void addTemporaryKit(Profile profile, Kit kit, long delay) {
		TemporaryKit old = profile.getTemporaryKit(kit);
		if (old != null) {
			profile.getTemporaryKits().remove(old);
		}
		
		TemporaryKit temporaryKit = new TemporaryKit(kit.getName(), delay);
		profile.getTemporaryKits().add(temporaryKit);
		
		if (profile.isOnline()) {
			profile.msg("&7[&6&lKit&7] &eVous venez de recevoir le kit &6" + kit.getName() 
				+ " &ependant &f" + DurationFormatter.getRemaining(delay, true) +"&e.");
		}
	}
	
	@Override
	public File getFile() {
		return new File(this.getPlugin().getDataFolder(), "kits.json");
	}

	@Override
	public void loadData() {
        String content = DiscUtil.readCatch(this.getFile());
        if (content == null) return;
        
        Map<UUID, Kit> map = this.getPlugin().getGson().fromJson(content,new TypeToken<Map<UUID, Kit>>() {
        }.getType());
        
        kits.clear();
        kits.putAll(map);
	}

	@Override
	public void saveData() {
		DiscUtil.writeCatch(this.getFile(), this.getPlugin().getGson().toJson(kits));
	}


}
