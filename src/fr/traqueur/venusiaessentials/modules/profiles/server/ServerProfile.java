package fr.traqueur.venusiaessentials.modules.profiles.server;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.Jail;

public class ServerProfile {
	
	private Location spawn;
	private List<Jail> jails;
	private List<String> autoMessages;
	private int autoMessageDelay;

	public ServerProfile() {
		this.spawn = null;
		this.autoMessages = new ArrayList<>();
		this.autoMessageDelay = 5;
		this.jails = new ArrayList<Jail>();
	}

	public Jail getJail(String name) {
		for (Jail jail : jails) {
			if (jail.getName().equalsIgnoreCase(name)) {
				return jail;
			}
		}
		return null;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public List<Jail> getJails() {
		return jails;
	}

	public void setJails(List<Jail> jails) {
		this.jails = jails;
	}

	public List<String> getAutoMessages() {
		return autoMessages;
	}

	public void setAutoMessages(List<String> autoMessages) {
		this.autoMessages = autoMessages;
	}

	public int getAutoMessageDelay() {
		return autoMessageDelay;
	}

	public void setAutoMessageDelay(int autoMessageDelay) {
		this.autoMessageDelay = autoMessageDelay;
	}
	
	
}
