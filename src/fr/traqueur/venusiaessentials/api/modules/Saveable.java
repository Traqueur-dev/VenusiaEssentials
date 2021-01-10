package fr.traqueur.venusiaessentials.api.modules;


import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.jsons.JsonPersist;
import net.minecraft.util.com.google.gson.Gson;

public abstract class Saveable extends Module implements JsonPersist {

	public transient boolean needDir, needFirstSave;

	public Saveable(Plugin plugin, String name) {
		super(plugin, name);
	}
	
	public Gson getGson() {
		return this.getPlugin().getGson();
	}
}