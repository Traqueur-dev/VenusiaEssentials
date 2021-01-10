package fr.traqueur.venusiaessentials.modules.punishments;

import java.io.File;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.modules.Saveable;

public class PunishModule extends Saveable {

	private static PunishModule instance;

	public PunishModule(Plugin plugin) {
		super(plugin, "Punishments");

		instance = this;

		this.registerListener(new PunishListener());
		this.registerCommand(new PunishCommand());
	}

	@Override
	public File getFile() {
		return null;
	}

	@Override
	public void loadData() {}

	@Override
	public void saveData() {}

	public static PunishModule getInstance() {
		return instance;
	}
	
	
}
