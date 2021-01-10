package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.CooldownUtils;
import fr.traqueur.venusiaessentials.api.utils.ItemBuilder;
import fr.traqueur.venusiaessentials.listeners.RandomTPListener.LoreUpdater;

public class RandomTPCommand extends ICommand {

	@Command(name = "randomtp", aliases =  { "rtp" })
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		Inventory inv = Bukkit.createInventory(null, 27, "§dRandom TP");
		boolean cooldown = CooldownUtils.isOnCooldown("RTP", player);
		if (cooldown) {
			String time = CooldownUtils.getCooldownRemaining(player, "RTP");
			inv.setItem(13,
					new ItemBuilder(Material.STAINED_GLASS_PANE).durability((short) 14).displayname("§c§l✖ COOLDOWN ✖")
							.lore(" ", "§4» §eVous êtes encore en cooldown pour§7:", "§6Temps§7: §c" + time).build());
			new LoreUpdater(player, inv.getContents()[13]);
		} else {
			inv.setItem(13, new ItemBuilder(Material.ENDER_PORTAL_FRAME).displayname("§a§lTéléportation aléatoire")
					.lore(" ", "§4» §eCliquez-ici pour lancer la téléportation !").build());
		}
		player.openInventory(inv);
	}
}
