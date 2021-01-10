package fr.traqueur.venusiaessentials.commands.admin;

import org.apache.logging.log4j.core.helpers.Booleans;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class SudoCommand extends ICommand {

	@Command(name ="sudo", permission = "base.sudo", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();

		if (args.length() < 3) {
			sender.sendMessage(Utils.color("&e/sudo <joueur> <forcer la commande (true|false)> <commande>"));
			return;
		}
		Player target = Bukkit.getPlayer(args.getArgs(0));
		if (target == null) {
			sender.sendMessage(Utils.color("&eCe joueur est introuvable."));
			return;
		}
		boolean force = Booleans.parseBoolean(args.getArgs(1), target.isOp());

		StringBuffer buffer = new StringBuffer();
		for (int i = 2; i < args.length(); i++) {
			buffer.append(args.getArgs(i) + " ");
		}
		String cmd = buffer.toString();
		cmd = cmd.substring(0, cmd.length() - 1); // remove space
		cmd = (cmd.startsWith("/") ? cmd : "/" + cmd); // add / if needed

		if (executeCommand(target, cmd, force)) {
			sender.sendMessage(Utils.color("&eVous venez de faire executer la commande &6" + cmd + " &eau joueur &c" + target.getName() + "&e."));
			this.logCommand(args);
		}
	}

	private boolean executeCommand(Player target, String cmd, boolean force) {
		if (target.isOp()) {
			force = false;
		}
		try {
			if (force) {
				target.setOp(true);
			}
			target.chat(cmd);
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			if (force) {
				target.setOp(false);
			}
		}
	}

}
