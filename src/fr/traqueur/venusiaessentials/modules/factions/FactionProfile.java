package fr.traqueur.venusiaessentials.modules.factions;

import java.io.File;

import com.massivecraft.factions.Faction;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.utils.FactionUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class FactionProfile {

	private String uniqueId;
	private int outposts;

	public FactionProfile(Faction faction) {
		this.uniqueId = faction.getId();
	}

	public void incrementOutposts() {
		++this.outposts;
	}

	public void decrementOutposts() {
		--this.outposts;
		if (this.outposts <= 0) {
			this.outposts = 0;
		}
	}

	public boolean isCappedOutposts() {
		return this.outposts >= FactionUtils.OUTPOSTS_MAX_COUNT;
	}

	public File getProfileFile() {
		return Utils.getFormatedFile((Plugin) VenusiaEssentials.getInstance(),
				(String) ("/factions/" + this.uniqueId + ".json"));
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public int getOutposts() {
		return outposts;
	}

	public void setOutposts(int outposts) {
		this.outposts = outposts;
	}
	
}
