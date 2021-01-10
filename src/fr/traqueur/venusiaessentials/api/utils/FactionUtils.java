package fr.traqueur.venusiaessentials.api.utils;

import com.google.common.collect.Lists;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class FactionUtils {

	public static int OUTPOSTS_MAX_COUNT = 4;

	public static boolean hasFaction(Player player) {
		FPlayer factionPlayer = FPlayers.i.get(player);
		return factionPlayer.hasFaction();
	}

	public static Faction getFaction(Player player) {
		FPlayer factionPlayer = FPlayers.i.get(player);
		Faction faction = factionPlayer.getFaction();
		return faction;
	}

	public static List<String> getOnlineMembers(Player player) {
		ArrayList<String> members = Lists.newArrayList();
		if (FactionUtils.hasFaction(player)) {
			Faction faction = FactionUtils.getFaction(player);
			List<Player> onlinePlayers = faction.getOnlinePlayers();
			for (Player online : onlinePlayers) {
				members.add(online.getName());
			}
		}
		return members;
	}

	public static String getFactionTag(Player player) {
		String tag = "Nature";
		if (FactionUtils.hasFaction(player)) {
			Faction faction = FactionUtils.getFaction(player);
			tag = faction.getTag();
		}
		return tag;
	}

	public static boolean isDefaultFaction(Faction faction) {
		return faction.isNone() || faction.isPeaceful() || faction.isSafeZone() || faction.isWarZone()
				|| !faction.isNormal();
	}

	public static boolean isValid(Faction faction) {
		return faction != null && faction.isNormal() && !faction.isPeaceful() && !faction.isPermanent()
				&& !faction.isWarZone() && !faction.isSafeZone() && !faction.isNone();
	}

	public static boolean isNextToWarzone(FLocation flocation) {
		int radius = 1;
		for (int posX = -radius; posX <= radius; ++posX) {
			for (int posZ = -radius; posZ <= radius; ++posZ) {
				if (posX == 0 && posZ == 0)
					continue;
				FLocation relative = flocation.getRelative(posX, posZ);
				Faction other = Board.getFactionAt(relative);
				if (!other.isWarZone())
					continue;
				return true;
			}
		}
		return false;
	}
	
	public static boolean isApAt(FLocation flocation, Faction faction) {
        int radius = 1;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x == 0 && z == 0) continue;
                FLocation relative = flocation.getRelative(x, z);
                Faction other = Board.getFactionAt(relative);
                if (!other.isWarZone() || other == faction) continue;
                return true;
            }
        }
        return false;
    }
}
