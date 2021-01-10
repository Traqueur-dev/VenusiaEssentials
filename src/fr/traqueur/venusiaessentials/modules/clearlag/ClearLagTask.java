package fr.traqueur.venusiaessentials.modules.clearlag;

import org.bukkit.Bukkit;

import fr.traqueur.venusiaessentials.api.utils.JavaUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class ClearLagTask implements Runnable {

	private ClearLagModule clearLagManager;
	
	private int autoremovalInterval;
	private int interval;
	private int[] warnings;
	
	public ClearLagTask() {
		this.clearLagManager = ClearLagModule.getInstance();
		this.autoremovalInterval = 1200;
		this.interval = 0;
		this.warnings = new int[] { 300, 120, 60, 30, 15, 10, 5, 4, 3, 2, 1};
	}
	
	@Override
	public void run() {
		this.interval += 1;
		int time = this.autoremovalInterval - this.interval;
		if(JavaUtils.arrayContains(this.warnings, time)) {
			int timeToMinutes = time / 60;
			Bukkit.broadcastMessage(Utils.getPrefix("Lag") + "§eSuppression des §6entitées §edans §6" + (timeToMinutes == 0 ? time + " §esecondes.": timeToMinutes + " §eminutes."));			
		}
		if (this.interval >= this.autoremovalInterval) {
			int i = this.clearLagManager.removeEntities();
			Bukkit.broadcastMessage(Utils.getPrefix("Lag") + "§eSuppression de §6" + i + " §eentitée(s)");
			this.interval = 0;
		}
	}
}
