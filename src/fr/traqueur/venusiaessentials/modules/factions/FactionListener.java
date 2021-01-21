package fr.traqueur.venusiaessentials.modules.factions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;
import com.massivecraft.factions.event.LandClaimEvent;
import com.massivecraft.factions.event.LandUnclaimAllEvent;
import com.massivecraft.factions.event.LandUnclaimEvent;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.utils.FactionUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class FactionListener implements Listener {

	private FactionModule factionManager = FactionModule.getInstance();
	
	@EventHandler
	public void onFactionCreate(FactionCreateEvent event) {
		final String tag = event.getFactionTag();
		Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) VenusiaEssentials.getInstance(), new Runnable() {

			@Override
			public void run() {
				Faction faction = Factions.i.getByTag(tag);
				factionManager.createProfile(faction);
			}
		}, 10L);
	}

	@EventHandler
	public void onFactionDisband(FactionDisbandEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Faction faction = event.getFaction();
		this.factionManager.deleteProfile(faction);
	}

	@EventHandler
	public void onLandClaim(LandClaimEvent event) {
		if (event.isCancelled()) {
			return;
		}
		FLocation claim = event.getLocation();
		if (FactionUtils.isNextToWarzone(claim)) {
			FactionProfile oldFaction;
			Faction old = Board.getFactionAt(claim);
			Faction faction = event.getFaction();
			if (FactionUtils.isValid(faction)) {
				FactionProfile newFaction = this.factionManager.getProfile(faction);
				if (!faction.getTag().equalsIgnoreCase("Staff")) {
					if (newFaction.isCappedOutposts()) {
						Player player = event.getPlayer();
						player.sendMessage(Utils.color(
								(String) ("&eD\u00e9sol\u00e9, votre faction poss\u00e8de d\u00e9j\u00e0 plus de &c"
										+ FactionUtils.OUTPOSTS_MAX_COUNT + " &eClaims d'&6Avant-Poste&f&e!")));
						event.setCancelled(true);
						return;
					}
					newFaction.incrementOutposts();
				}
			}
			if (FactionUtils.isValid(old) && (oldFaction = this.factionManager.getProfile(old)) != null) {
				oldFaction.decrementOutposts();
			}
		}
	}

	@EventHandler
	public void onLandUnclaim(LandUnclaimEvent event) {
		Faction faction;
		if (event.isCancelled()) {
			return;
		}
		FLocation claim = event.getLocation();
		if (claim == null) {
			return;
		}
		if (FactionUtils.isNextToWarzone(claim) && FactionUtils.isValid(faction = event.getFaction())) {
			FactionProfile profile = this.factionManager.getProfile(faction);
			profile.decrementOutposts();
		}
	}

	@EventHandler
	public void onUnclaimAll(LandUnclaimAllEvent event) {
		FactionProfile profile;
		Faction faction = event.getFaction();
		if (FactionUtils.isValid(faction) && (profile = this.factionManager.getProfile(faction)) != null) {
			profile.setOutposts(0);
		}
	}

}
