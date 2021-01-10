package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import net.minecraft.util.com.google.common.primitives.Floats;

public class SpeedCommand extends ICommand {

	@Command(name = "speed", permission = "base.speed")
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();

		if (args.length() < 1) {
			sender.sendMessage(Utils.color("&e/speed [type] <valeur> [joueur]"));
			return;
		}
		if (args.length() > 2) {

			String type = args.getArgs(0);
			Float value = Floats.tryParse(args.getArgs(1));
			Player target = Bukkit.getPlayer(args.getArgs(2));
			if (target == null) {
				sender.sendMessage(Utils.color("&eCe joueur est introuvable."));
				return;
			}
			if (value == null) {
				sender.sendMessage(Utils.color("&eValeur invalide."));
				return;
			}
			applyMutiplier(sender, target, value, type);
		} else {
			Float value = Floats.tryParse(args.getArgs(0));
			if (value == null) {
				sender.sendMessage(Utils.color("&eValeur invalide."));
				return;
			}
			if (!args.isPlayer()) {
				sender.sendMessage(Utils.color("&e/speed [type] <valeur> [joueur]"));
				return;
			}
			Player player = args.getPlayer();
			if (player.isFlying()) {
				applyMutiplier(player, player, value, "fly");
			} else {
				applyMutiplier(player, player, value, "walk");
			}
		}
	}

	public void applyMutiplier(CommandSender sender, Player target, float value, String type) {
		switch (type) {
		case "walk":
		case "marcher":
		case "pied":
			if (value < 1) {
				sender.sendMessage(Utils.color("&eValeur de speed trop basse: &c" + value));
				break;
			} else if (value > 10) {
				sender.sendMessage(Utils.color("&eValeur de speed trop haute: &c" + value));
				break;
			}
			target.setWalkSpeed(this.getRealMoveSpeed(value, false));
			;
			sender.sendMessage(Utils.color("&eVous venez de définir la vitesse de &cmarche &edu joueur &c"
					+ target.getName() + " &eà &c" + value + "&e."));
			break;
		case "fly":
		case "vol":
		case "flight":
			if (value < 1) {
				sender.sendMessage(Utils.color("&eValeur de speed trop basse: &c" + value));
				break;
			} else if (value > 10) {
				sender.sendMessage(Utils.color("&eValeur de speed trop haute: &c" + value));
				break;
			}
			target.setFlySpeed(this.getRealMoveSpeed(value, true));
			sender.sendMessage(Utils.color("&eVous venez de définir la vitesse de &cvol &edu joueur &c"
					+ target.getName() + " &eà &c" + value + "&e."));
			break;
		default:
			sender.sendMessage(Utils.color("&e/speed [type] <valeur> [joueur]"));
			break;
		}
	}

	private float getRealMoveSpeed(float userSpeed, boolean isFly) {
		float defaultSpeed = isFly ? 0.1F : 0.2F;
		float maxSpeed = 1.0F;
		if (userSpeed < 1.0F) {
			return defaultSpeed * userSpeed;
		}
		float ratio = (userSpeed - 1.0F) / 9.0F * (maxSpeed - defaultSpeed);
		return ratio + defaultSpeed;
	}
}
