package fr.traqueur.venusiaessentials.modules.warps.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;
import fr.traqueur.venusiaessentials.modules.warps.Warp;
import fr.traqueur.venusiaessentials.modules.warps.WarpModule;

public class WarpCommand extends BaseCommand { 
	
	@Command(name = "warp", aliases = {"warps"})
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		WarpModule module = WarpModule.getInstance();
		if (args.length() < 1) {
			player.sendMessage(Utils.LINE);
			player.sendMessage(Utils.color("&e/warp <nom>"));
			player.sendMessage(Utils.LINE);
			if (player.hasPermission("base.warp.edit")) {
				player.sendMessage(Utils.color("&e/warp create <nom>"));
				player.sendMessage(Utils.color("&e/warp delete <nom>"));
				player.sendMessage(Utils.color("&e/warp toggle <nom>"));
				player.sendMessage(Utils.color("&e/warp list"));
			}
		} else {
			Warp warp = module.getWarp(args.getArgs(0));
			if (warp == null) {
				player.sendMessage(Utils.color("&eLe warp &c" + args.getArgs(0) + " &eest introuvable."));
				return;
			}
			if (!warp.isVisible() && !player.hasPermission("base.warp." + warp.getName())) {
				player.sendMessage(Utils.color("&eVous n'avez pas acc�s au warp &c" + warp.getName() + "&e."));
				return;
			}
			module.teleport(player, warp);
		}
	}
	
	@Command(name = "warp.create", aliases = {"warps.create"}, permission = "base.warp.edit")
	public void warpCreate(CommandArgs args) {
		Player player = args.getPlayer();
		WarpModule module = WarpModule.getInstance();
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/warp create <nom>"));
			return;
		}
		String name = args.getArgs(0);
		if (module.getWarp(name) != null) {
			player.sendMessage(Utils.color("&eLe warp &c" + name + " &eexiste déjà."));
			return;
		}
		module.getWarps().add(new Warp(name, player.getLocation()));
		player.sendMessage(Utils.color("&eVous venez de créer le warp &c" + name + "&e."));
	}
	
	@Command(name = "warp.delete", aliases = {"warps.delete"}, permission = "base.warp.edit")
	public void warpDelete(CommandArgs args) {
		Player player = args.getPlayer();
		WarpModule module = WarpModule.getInstance();
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/warp delete <nom>"));
			return;
		}
		Warp warp = module.getWarp(args.getArgs(0));
		if (warp == null) {
			player.sendMessage(Utils.color("&eLe warp &c" + args.getArgs(0) + " &eest introuvable."));
			return;
		}
		module.getWarps().remove(warp);
		player.sendMessage(Utils.color("&eVous venez de supprimer le warp &c" + warp.getName() + "&e."));
	}
	
	
	@Command(name = "warp.toggle", aliases = {"warps.toggle"}, permission = "base.warp.edit")
	public void warpOpen(CommandArgs args) {
		Player player = args.getPlayer();
		WarpModule module = WarpModule.getInstance();
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/warp toggle <nom>"));
			return;
		}
		Warp warp = module.getWarp(args.getArgs(0));
		if (warp == null) {
			player.sendMessage(Utils.color("&eLe warp &c" + args.getArgs(0) + " &eest introuvable."));
			return;
		}
		boolean newStatut = !warp.isVisible();
		warp.setVisible(newStatut);
		Bukkit.broadcastMessage(Utils.color("&c• &eLe warp &c" + warp.getName() + " &eest maintenant " + (newStatut ? "&adisponible" : "&cindisponible")));
	}
	
	@Command(name = "warp.list", aliases = {"warps.list"}, permission = "base.warp.edit")
	public void warpList(CommandArgs args) {
		Player player = args.getPlayer();
		WarpModule module = WarpModule.getInstance();
		if (module.getWarps().isEmpty()) {
			player.sendMessage(Utils.color("&eAucun warp de disponible."));
			return;
		}
		
		player.sendMessage(Utils.color("&eAffichage de &c" + module.getWarps().size() + " &ewarps&7:"));
		
		for (Warp warp :module.getWarps()) {
			player.sendMessage(Utils.color("&6- &e" + warp.getName() + " &7(" + (warp.isVisible() ? "&aOUVERT" : "&cFERMÉE") + "&7)"));
		}
	}

	
}
