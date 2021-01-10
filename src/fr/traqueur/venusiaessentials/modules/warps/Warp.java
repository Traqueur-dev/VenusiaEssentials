package fr.traqueur.venusiaessentials.modules.warps;

import org.bukkit.Location;

public class Warp {

	private String name;
	private Location location;
	private boolean visible;
	
	public Warp(String name, Location location, boolean visible) {
		this.name = name;
		this.location = location;
		this.visible = visible;
	}
	
	public Warp(String name, Location location) {
		this(name, location, true);
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
}