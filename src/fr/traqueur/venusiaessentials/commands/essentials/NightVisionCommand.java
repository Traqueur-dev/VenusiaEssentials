package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;

public class NightVisionCommand extends ICommand {

	@Command(name = "nightvision", aliases = {"nv", "vision" })
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player.sendMessage("§eVision nocturne §7§l§ §cDésactivée.");
		} else {
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 99999));
			player.sendMessage("§eVision nocturne §7§l§ §aActivée.");
		}
	}
}
