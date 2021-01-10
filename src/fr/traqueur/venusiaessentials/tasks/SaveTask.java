package fr.traqueur.venusiaessentials.tasks;

import fr.traqueur.venusiaessentials.api.Plugin;

public class SaveTask implements Runnable {

	@Override
	public void run() {
		Plugin.getInstance().save();
	}

}
