package fr.traqueur.venusiaessentials.modules.punishments;

import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class Punishment {

	private String playerName, date;
	private PunishmentType punishmentType;
	private String reason, punisher;
	private long time;

	public Punishment(String playerName, PunishmentType punishmentType, String reason, String punisher, long time) {
		this.playerName = playerName;
		this.punishmentType = punishmentType;
		this.reason = reason;
		this.punisher = punisher;
		this.time = time;
		this.date = DurationFormatter.getCurrentDate();
	}
	
	public boolean isExpired() {

		if (punishmentType == PunishmentType.KICK) {
			return true;
		}

		return time != -1 && System.currentTimeMillis() > time;
	}

	public void revoke() {
		Profile userProfile = ProfileModule.getInstance().getProfile(this.playerName);
		userProfile.getPunishments().remove(this);
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public PunishmentType getPunishmentType() {
		return punishmentType;
	}

	public void setPunishmentType(PunishmentType punishmentType) {
		this.punishmentType = punishmentType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPunisher() {
		return punisher;
	}

	public void setPunisher(String punisher) {
		this.punisher = punisher;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
}
