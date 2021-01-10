package fr.traqueur.venusiaessentials.modules.punishments;

public enum PunishmentType {

	BAN("Bannissement"), MUTE("Mute"), KICK("Expulsion");

	private String name;
	
	private PunishmentType(String n) {
		this.name = n;
	}
	
	public String getName() {
		return name;
	}
	
}
