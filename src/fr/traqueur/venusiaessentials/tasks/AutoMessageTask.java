package fr.traqueur.venusiaessentials.tasks;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class AutoMessageTask implements Runnable{
	
	private ProfileModule profileManager = ProfileModule.getInstance();

	@Override
	public void run() {
		List<String> messages = profileManager.getServerProfile().getAutoMessages();
		if (!messages.isEmpty()) {
			String message = messages.get(0);
			Bukkit.broadcastMessage(Utils.color(Utils.getPrefix(VenusiaEssentials.PREFIX) + message));
			Collections.rotate(messages, -1);
		}
	}

}
