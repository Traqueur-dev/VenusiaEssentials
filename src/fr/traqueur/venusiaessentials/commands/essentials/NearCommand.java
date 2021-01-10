package fr.traqueur.venusiaessentials.commands.essentials;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.JavaUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class NearCommand extends ICommand {

	@Command(name = "near", permission = "base.near")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		if (args.length() < 1) {
			near(player, 60);
		} else {
			
			if (!player.hasPermission("base.near.radius")) {
				near(player, 60);
				return;
			}
			if (!JavaUtils.isInteger(args.getArgs(0))) {
				player.sendMessage(Utils.color("&e/near <rayon>"));
				return;
			}
			int radius = Integer.parseInt(args.getArgs(0));
			near(player, radius);
		}
	}
	
	public void near(Player player, int radius) {
		if (!this.checkCooldown(player, 30)) {
			return;
		}
		
		List<Entity> entities = player.getNearbyEntities(radius, Math.max(radius, 256), radius);
		String list = "";
		
		for (Entity entity : entities) {
			if (entity instanceof Player) {
				Player target = (Player) entity;
				if (target.getUniqueId() != player.getUniqueId()) {
					int distance = (int) player.getLocation().distanceSquared(target.getLocation());
					list += "&6" + target.getName() + " &7(&c" + distance + "blocs&7)&7, ";
				}
			}
		}
		if (list.length() == 0) {
			player.sendMessage(Utils.color("&eAucun joueur n'a pu être trouver autour de vous."));
			return;
		}
		list = list.substring(0, list.length() - 2);
		
		player.sendMessage(Utils.LINE);
		player.sendMessage(Utils.color("&7§ &eJoueurs autour de vous&7: " + list));
		player.sendMessage(Utils.LINE);
	}
	
}
