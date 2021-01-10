package fr.traqueur.venusiaessentials.modules.profiles.server.clazz;

public class TemporaryKit {
	
private String kitName;
private long expirationMillis;

	public TemporaryKit(String kitName, long expirationMillis) {
		this.kitName = kitName;
		this.expirationMillis = System.currentTimeMillis() + expirationMillis;
	}

	public boolean hasExpired() {
		return this.expirationMillis - System.currentTimeMillis() <= 0L;
	}

	public String getKitName() {
		return kitName;
	}

	public long getExpirationMillis() {
		return expirationMillis;
	}
	
	

}
