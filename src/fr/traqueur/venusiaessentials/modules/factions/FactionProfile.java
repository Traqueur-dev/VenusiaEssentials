package fr.traqueur.venusiaessentials.modules.factions;

import java.io.File;

import com.massivecraft.factions.Faction;

import fr.traqueur.venusiaessentials.api.utils.FactionUtils;

public class FactionProfile {

	private String name;
	private String uniqueId;
	private int outposts;

	public FactionProfile(Faction faction) {
		this.outposts = 0;
		this.setName(faction.getTag());
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
		return new File(FactionModule.getInstance().getFile(), this.uniqueId + ".json");
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
