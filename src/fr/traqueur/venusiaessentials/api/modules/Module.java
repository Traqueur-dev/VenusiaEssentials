package fr.traqueur.venusiaessentials.api.modules;

import org.bukkit.event.Listener;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.commands.CommandFramework;
import fr.traqueur.venusiaessentials.api.commands.ICommand;

public abstract class Module {
	
	private Plugin plugin;
	private String name;

	public Module(Plugin plugin, String name) {
		this.plugin = plugin;
		this.name = name;
	}

	public void registerCommand(ICommand command) {
		CommandFramework framework = this.plugin.getFramework();
		framework.registerCommands(command);
	}

	public void registerListener(Listener listener) {
		this.plugin.registerListener(listener);
	}

	public String getName() {
		return name;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
}
