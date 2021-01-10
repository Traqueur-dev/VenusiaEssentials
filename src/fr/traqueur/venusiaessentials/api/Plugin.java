package fr.traqueur.venusiaessentials.api;

import java.lang.reflect.Modifier;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import fr.traqueur.venusiaessentials.api.commands.CommandFramework;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.inventory.InventoryManager;
import fr.traqueur.venusiaessentials.api.jsons.JsonPersist;
import fr.traqueur.venusiaessentials.api.jsons.adapters.ItemStackAdapter;
import fr.traqueur.venusiaessentials.api.jsons.adapters.LocationAdapter;
import fr.traqueur.venusiaessentials.api.modules.ModuleManager;
import fr.traqueur.venusiaessentials.api.modules.commands.ModuleCommand;
import fr.traqueur.venusiaessentials.api.utils.LoggerUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;

public abstract class Plugin extends JavaPlugin {
	
	private static Plugin instance;
	
	private Gson gson;
	private CommandFramework framework;
	private ModuleManager moduleManager;
	private List<JsonPersist> persists;
	
	private static InventoryManager invManager;
	
	public Plugin() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		LoggerUtils.log("Vérification du dossier de données...");
		this.getDataFolder().mkdir();
		LoggerUtils.success("Vérification effectuée avec succés.");

		LoggerUtils.log("Vérification du module GSON...");
		this.gson = this.createGsonBuilder().create();
		LoggerUtils.success("Vérification effectuée avec succés.");

		this.persists = Lists.newArrayList();

		LoggerUtils.log("Initialisation du module des commandes...");
		this.framework = new CommandFramework(this);
		LoggerUtils.success("Initialisation effectuée avec succés.");

		invManager = new InventoryManager(this);
		invManager.init();
		
		LoggerUtils.log("Initialisation des modules...");
		this.moduleManager = new ModuleManager();
		LoggerUtils.success("Initialisation effectuée avec succés.");
		
		framework.registerCommands(new ModuleCommand());
		
		this.registerManagers();
		this.registerOthers();

		LoggerUtils.log("Chargement des configurations...");
		this.loadPersists();
		LoggerUtils.success("Chargement effectuée avec succés.");
	}

	@Override
	public void onDisable() {
		this.savePersists();
	}
	
	public static InventoryManager manager() { return invManager; }
	
	private GsonBuilder createGsonBuilder() {
		GsonBuilder ret = new GsonBuilder();

		ret.setPrettyPrinting();
		ret.disableHtmlEscaping();
		ret.excludeFieldsWithModifiers(Modifier.TRANSIENT);

		ret.registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter(this));
		ret.registerTypeAdapter(Location.class, new LocationAdapter(this)).create();

		return ret;
	}

	public void registerPersist(JsonPersist persist) {
		this.persists.add(persist);
	}

	public void registerListener(Listener listener) {
		LoggerUtils.log("Enregistrement du listener " + listener.getClass().getName() + "...");
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(listener, this);
		LoggerUtils.success("Enregistrement effectuée avec succés.");
	}

	public void registerCommand(ICommand command) {
		LoggerUtils.log("Enregistrement du listener " + command.getClass().getName() + "...");
		getFramework().registerCommands(command);
		LoggerUtils.success("Enregistrement effectuée avec succés.");
	}
	
	public void loadPersists() {
		for (JsonPersist persist : this.persists) {
			persist.loadData();
		}
	}

	public void savePersists() {
		for (JsonPersist persist : this.persists) {
			persist.saveData();
		}
	}

	public abstract void registerManagers();

	public abstract void registerOthers();

	
	public static Plugin getInstance() {
		return instance;
	}
	
	public Gson getGson() {
		return this.gson;
	}

	public CommandFramework getFramework() {
		return framework;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}
	
	public void save() {
		long time = System.currentTimeMillis();
		this.savePersists();
		time = System.currentTimeMillis() - time;
		Bukkit.broadcastMessage(Utils.getPrefix("Sauvegarde") + "§eSauvegarde des §6données §8(§7" + time + "ms§8)");
	}
}
