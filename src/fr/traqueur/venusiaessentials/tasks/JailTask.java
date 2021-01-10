package fr.traqueur.venusiaessentials.tasks;

import java.util.Map.Entry;

import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.Jail;

public class JailTask implements Runnable{

	@Override
	public void run() {
		for (Jail jail : ProfileModule.getInstance().getServerProfile().getJails()) {
			for (Entry<String, Long> entry : jail.getJailedPlayers().entrySet()) {
				String name = entry.getKey();
				long leftMillis = entry.getValue() - System.currentTimeMillis();
				
				if (leftMillis > 0L) continue;
				
				Profile profile = ProfileModule.getInstance().getProfile(name);
				if (profile == null) continue;
				profile.setJail(false);

				
				jail.getJailedPlayers().remove(name);
				if (profile.getPlayer() != null) {
					profile.getPlayer().teleport( (profile.getLastTeleportLocation() != null ? profile.getLastTeleportLocation() : ProfileModule.getInstance().getServerProfile().getSpawn()));
					profile.msg("&eVous n'êtes maintenant plus en prison.");
				}
			}
		}
	}

}