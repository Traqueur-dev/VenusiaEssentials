package fr.traqueur.venusiaessentials.modules.profiles.server.clazz;

import java.util.Map;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.traqueur.venusiaessentials.api.utils.Cuboid;
import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class Jail {

private String name;
private Cuboid cubo;
private Map<String, Long> jailedPlayers;

	public Jail() {}

	public Jail(String name, Selection selection) {
		this.name = name;
		this.cubo = new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint());
		this.jailedPlayers = Maps.newHashMap();
	}
	
	public void jailPlayer(Player player, long value, String raison) {
		Profile profile = ProfileModule.getInstance().getProfile(player.getName());
		player.teleport(cubo.getCenter());
		profile.setJail(true);
		long time = System.currentTimeMillis() + value;
		jailedPlayers.put(player.getName(), time);
		player.sendMessage(Utils.color("&eVous êtes maintenant en prison pour encore &c" + DurationFormatter.getRemaining(time - System.currentTimeMillis(), true) + " &epour &f" + raison + "&e."));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Cuboid getCubo() {
		return cubo;
	}

	public void setCubo(Cuboid cubo) {
		this.cubo = cubo;
	}

	public Map<String, Long> getJailedPlayers() {
		return jailedPlayers;
	}

	public void setJailedPlayers(Map<String, Long> jailedPlayers) {
		this.jailedPlayers = jailedPlayers;
	}
	
	

}
