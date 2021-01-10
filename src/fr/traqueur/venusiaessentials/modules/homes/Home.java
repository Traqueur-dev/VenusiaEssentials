package fr.traqueur.venusiaessentials.modules.homes;

import org.bukkit.Location;

public class Home {

	private String name;
	private Location location;
	
	public Home() {}
	
	public Home(String name, Location loc) {
		this.name = name;
		location = loc;
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}
	
	
}
