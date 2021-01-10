package fr.traqueur.venusiaessentials.api.jsons.adapters;

import fr.traqueur.venusiaessentials.api.Plugin;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.TypeAdapter;

public abstract class GsonAdapter<T> extends TypeAdapter<T> {

	private Plugin plugin;

	public GsonAdapter(Plugin plugin) {
		this.plugin = plugin;
	}

	public Gson getGson() {
		return this.plugin.getGson();
	}
}