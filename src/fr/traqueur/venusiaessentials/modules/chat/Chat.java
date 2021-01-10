package fr.traqueur.venusiaessentials.modules.chat;

import java.util.concurrent.TimeUnit;

public class Chat {

	private boolean enabled;
	private long slowTime;
	
	public Chat() {
		this.enabled = true;
		this.slowTime = TimeUnit.SECONDS.toSeconds(3L);
	}

	public long getSlowTime() {
		return slowTime;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean bool) {
		this.enabled = bool;
	}
	
	public void setSlowTime(long time) {
		this.slowTime = time;
	}
	
}