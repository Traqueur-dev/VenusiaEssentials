package fr.traqueur.venusiaessentials.modules.punishments;


public enum PunishmentReasons {

	PROVOCATION("Provocation", 5, PunishmentType.MUTE), 
	INSULTE("Insultes", 45, PunishmentType.MUTE),
	INSULTESTAFF("Insulte envers un membre du personnel", 600, PunishmentType.MUTE),
	PUB("Publicité", 60, PunishmentType.MUTE), 
	FLOOD("Flood", 60, PunishmentType.MUTE),
	SPAM("Spam", 60, PunishmentType.MUTE), 
	SPAMFOPEN("Spam du /f open", 2880, PunishmentType.BAN),
	SPAMFTAG("Spam du /f tag", 2880, PunishmentType.BAN), 
	AP("AP non-conforme", 4320, PunishmentType.BAN),
	RACISME("Racisme", 1440, PunishmentType.MUTE), 
	HOMOPHOBIE("Homophobie", 1440, PunishmentType.MUTE),
	MENACEHACK("Menace de hack", 4320, PunishmentType.BAN), 
	HACK("Hack", -1, PunishmentType.BAN),
	CHEAT("Triche", 38880, PunishmentType.BAN),
	DUPLICATION("Duplication", -1, PunishmentType.BAN),
	ANTIAFK("Anti-AFK", 60, PunishmentType.BAN),
	USURPATION("Usurpation", 2880, PunishmentType.BAN),
	PSEUDOGRAVE("Pseudonyme choquant/raciste/insultant", -1, PunishmentType.BAN),
	SKINGRAVE("Skin choquant", 2880, PunishmentType.BAN), 
	DOUBLECOMPTE("Double compte", -1, PunishmentType.BAN),
	TRAHISON("Trahison", -1, PunishmentType.BAN),
	ARNAQUE("Arnaque", -1, PunishmentType.BAN),
	INCITATIONFLOOD("Incitation au flood", 1440, PunishmentType.MUTE),
	FAKENEWS("Fausses informations", 1440, PunishmentType.MUTE),
	COMMERCEHORSSERV("Commerce hors serveur", -1, PunishmentType.BAN),
	BACKSPAWN("Retour au spawn", 60, PunishmentType.BAN), 
	BANAUTRE("Raison inconnue", -1, PunishmentType.BAN),
	KICKAUTRE("Raison inconnue", -1, PunishmentType.KICK),
	DECOCOMBAT("Déconnexion en combat", 1440, PunishmentType.BAN),
	FRAUDEPAYPAL("Fraude sur PayPal. Merci de contacter UnspecifieD_ par TS/Mail/Forum.", -1, PunishmentType.BAN),
	TPKILL("TP-Kill", 1440, PunishmentType.BAN), 
	TRAP("Trap avant 96 blocs / 6 claims", 1440, PunishmentType.BAN),
	LANGAGE("Langage incorrect", 10, PunishmentType.MUTE),
	MAJ("Majuscule", 5, PunishmentType.MUTE);

	private String name;
	private long timeInMinutes;
	private PunishmentType punishmentType;

	PunishmentReasons(String name, long time, PunishmentType type) {
		this.name = name;
		this.timeInMinutes = time;
		punishmentType = type;
	}
	
	public static boolean exists(String reason) {
		try {
			valueOf(reason.toUpperCase());
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public String getName() {
		return name;
	}

	public long getTimeInMinutes() {
		return timeInMinutes;
	}

	public PunishmentType getPunishmentType() {
		return punishmentType;
	}
	
}
