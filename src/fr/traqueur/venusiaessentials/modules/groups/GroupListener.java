package fr.traqueur.venusiaessentials.modules.groups;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class GroupListener implements Listener {

	private ProfileModule profileModule = ProfileModule.getInstance();
	private GroupModule groupManager = GroupModule.getInstance();

	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {

		Player player = event.getPlayer();
		Profile profile = this.profileModule.getProfile(player.getName());

		if (profile.isRankExpired()) {
			profile.setGroup(this.groupManager.getDefaultGroup().getName());
		}

		if (this.groupManager.getAttachments().get(profile.getPlayerName()) != null) {
			player.removeAttachment(this.groupManager.getAttachments().get(profile.getPlayerName()));
			this.groupManager.getAttachments().remove(profile.getPlayerName());
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(!this.profileModule.profileExist(player.getName())) {
			this.profileModule.createProfile(player); // a test
		}
		
		Profile profile = this.profileModule.getProfile(player.getName());

		if (profile.isRankExpired()) {
			profile.setGroup(this.groupManager.getDefaultGroup().getName());
		}

		this.groupManager.getAttachments().put(player.getName(),
				player.addAttachment(VenusiaEssentials.getInstance()));

		Group group = this.groupManager.getByName(profile.getGroup());
		if (!group.getPermissions().isEmpty()) {
			for (String permission : group.getPermissions()) {
				this.groupManager.getAttachments().get(player.getName()).setPermission(permission, true);
			}
		}
	}
}
