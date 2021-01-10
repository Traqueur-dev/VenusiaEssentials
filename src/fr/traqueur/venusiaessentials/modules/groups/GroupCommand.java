package fr.traqueur.venusiaessentials.modules.groups;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;

import com.google.common.collect.Lists;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class GroupCommand extends BaseCommand {

	@Command(name = "group", aliases = {"groupe"}, permission = "group.help", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		args.getSender().sendMessage(Utils.LINE);
		args.getSender().sendMessage("§eGroupes");
		args.getSender().sendMessage(" ");
		args.getSender().sendMessage("§a/" + args.getLabel() + " list §8: §eListe des rangs");
		args.getSender().sendMessage("§a/" + args.getLabel() + " create <group> <prefix>§8: §eCréer un rang");
		args.getSender().sendMessage("§a/" + args.getLabel() + " info <group>§8: §eVoir les informations d'un rang");
		args.getSender().sendMessage(
				"§a/" + args.getLabel() + " set <joueur> <nom> [temps en minute]§8: §eModifier le rang d'un joueur");
		args.getSender().sendMessage(
				"§a/" + args.getLabel() + " addpermission <groupe> <permission>§8: §eAjouter une permission à un rang");
		args.getSender().sendMessage("§a/" + args.getLabel()
				+ " removepermission <groupe> <permission>§8: §eEnlever une permission à un rang");
		args.getSender().sendMessage(Utils.LINE);
	}

	@Command(name = "group.create", aliases = {"groupe.create"}, permission = "group.create", inGameOnly = false)
	public void onCreateCommand(CommandArgs args) {
		if (args.getArgs().length == 2) {

			GroupModule groupManager = GroupModule.getInstance();
			String groupName = args.getArgs(0);

			if (groupManager.isGroupExisting(groupName)) {
				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cCe rang existe déjà !");
				args.getSender().sendMessage(Utils.LINE);
				return;
			}

			String prefix = args.getArgs(1);

			try {
				groupManager.createGroup(groupName, Lists.newArrayList(), prefix);
			} catch (IOException e) {

				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cUne erreur est survenue !");
				args.getSender().sendMessage(Utils.LINE);

				e.printStackTrace();
			}

			args.getSender().sendMessage(Utils.LINE);
			args.getSender().sendMessage("§eLe rang §a" + groupName + " §evient d'être §acréée.");
			args.getSender().sendMessage(Utils.LINE);
		}
	}

	@Command(name = "group.addpermission", aliases = {"groupe.addpermission"}, permission = "group.permission.add", inGameOnly = false)
	public void onAddPermissionCommand(CommandArgs args) {
		if (args.getArgs().length == 2) {

			GroupModule groupManager = GroupModule.getInstance();
			String groupName = args.getArgs(0);

			if (!groupManager.isGroupExisting(groupName)) {
				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cCe rang n'existe pas !");
				args.getSender().sendMessage(Utils.LINE);
				return;
			}

			if (groupManager.getByName(groupName).getPermissions().contains(args.getArgs(1).toLowerCase())) {
				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cCe rang a déjà cette permission !");
				args.getSender().sendMessage(Utils.LINE);
				return;
			}

			groupManager.getByName(groupName).getPermissions().add(args.getArgs(1));

			for (Profile profile : ProfileModule.getInstance().getProfiles()) {
				if (profile.getGroup() == groupManager.getByName(groupName).getName()) {
					Bukkit.getPlayer(profile.getPlayerName())
							.removeAttachment(groupManager.getAttachments().get(profile.getPlayerName()));
					groupManager.getAttachments().remove(profile.getPlayerName());
					PermissionAttachment permissionAttachment = Bukkit.getPlayer(profile.getPlayerName())
							.addAttachment(VenusiaEssentials.getInstance());
					Group group = groupManager.getByName(profile.getGroup());
					for (String permission : group.getPermissions()) {
						permissionAttachment.setPermission(permission, true);
					}
					groupManager.getAttachments().put(profile.getPlayerName(), permissionAttachment);
				}
			}

			args.getSender().sendMessage(Utils.LINE);
			args.getSender().sendMessage("§eLe rang §a" + groupName + " §evient de voir la permission \""
					+ args.getArgs(1) + "\" §aajoutée§e.");
			args.getSender().sendMessage(Utils.LINE);
		}
	}

	@Command(name = "group.removepermission", aliases = {"groupe.removepermission", "group.rmperm", "groupe.rmperm"}, permission = "group.permission.remove", inGameOnly = false)
	public void onRemovePermissionCommand(CommandArgs args) {
		if (args.getArgs().length == 2) {

			GroupModule groupManager = GroupModule.getInstance();
			String groupName = args.getArgs(0);

			if (!groupManager.isGroupExisting(groupName)) {
				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cCe rang n'existe pas !");
				args.getSender().sendMessage(Utils.LINE);
				return;
			}

			if (!groupManager.getByName(groupName).getPermissions().contains(args.getArgs(1).toLowerCase())) {
				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cCe rang n'a pas cette permission !");
				args.getSender().sendMessage(Utils.LINE);
				return;
			}

			groupManager.getByName(groupName).getPermissions().remove(args.getArgs(1));

			for (Profile profile : ProfileModule.getInstance().getProfiles()) {
				if (profile.getGroup() == groupManager.getByName(groupName).getName()) {
					Bukkit.getPlayer(profile.getPlayerName())
							.removeAttachment(groupManager.getAttachments().get(profile.getPlayerName()));
					groupManager.getAttachments().remove(profile.getPlayerName());
					PermissionAttachment permissionAttachment = Bukkit.getPlayer(profile.getPlayerName())
							.addAttachment(VenusiaEssentials.getInstance());
					Group group = groupManager.getByName(profile.getGroup());
					for (String permission : group.getPermissions()) {
						permissionAttachment.setPermission(permission, true);
					}
					groupManager.getAttachments().put(profile.getPlayerName(), permissionAttachment);
				}
			}

			args.getSender().sendMessage(Utils.LINE);
			args.getSender().sendMessage("§eLe rang §a" + groupName + " §evient de voir la permission \""
					+ args.getArgs(1) + "\" §csupprimée§e.");
			args.getSender().sendMessage(Utils.LINE);
		}
	}

	@Command(name = "group.set", aliases = {"groupe.set"}, permission = "group.set", inGameOnly = false)
	public void onSetCommand(CommandArgs args) {
		if (args.getArgs().length >= 2) {

			GroupModule groupManager = GroupModule.getInstance();

			String playerName = args.getArgs(0);
			String groupName = args.getArgs(1);

			if (!groupManager.isGroupExisting(groupName)) {
				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cCe rang n'existe pas !");
				args.getSender().sendMessage(Utils.LINE);
				return;
			}

			Profile userProfile = ProfileModule.getInstance().getProfile(playerName);

			if (!ProfileModule.getInstance().profileExist(playerName)) {
				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cIl n'y a aucun profil avec ce pseudo !");
				args.getSender().sendMessage(Utils.LINE);
				return;
			}

			userProfile.setGroup(groupManager.getByName(groupName).getName());
			if (args.getArgs().length == 3) {
				if (StringUtils.isNumeric(args.getArgs(2))) {
					userProfile.setExpirationMillis(
							System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(Long.parseLong(args.getArgs(2))));
				} else {
					args.getSender().sendMessage(Utils.LINE);
					args.getSender().sendMessage("§cLe nombre n'est pas valide !");
					args.getSender().sendMessage(Utils.LINE);
					return;
				}
			} else {
				userProfile.setExpirationMillis(-1);
			}

			args.getSender().sendMessage(Utils.LINE);
			args.getSender()
					.sendMessage("§eLe joueur §a" + playerName + " §evient de voir son rang §amodifiée pour §c\""
							+ groupName + "\"§e"
							+ (args.getArgs().length == 3 ? " pendant §c" + args.getArgs(2) + "§e minutes" : "") + ".");
			args.getSender().sendMessage(Utils.LINE);
		}
	}

	@Command(name = "group.info", aliases = {"groupe.info"}, permission = "group.info", inGameOnly = false)
	public void onInfoCommand(CommandArgs args) {
		if (args.getArgs().length == 1) {

			GroupModule groupManager = GroupModule.getInstance();
			String groupName = args.getArgs(0);

			if (!groupManager.isGroupExisting(groupName)) {
				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage("§cCe rang n'existe pas !");
				args.getSender().sendMessage(Utils.LINE);
				return;
			}

			Group group = groupManager.getByName(groupName);

			args.getSender().sendMessage(Utils.LINE);
			args.getSender().sendMessage("§eGroupe §a" + group.getName());
			args.getSender().sendMessage(" ");
			args.getSender().sendMessage("§6Préfixe§8: " + group.getPrefix());
			args.getSender().sendMessage("§e§o(ex: " + group.getPrefix() + " Pseudonyme§e§o)");
			args.getSender().sendMessage("§6Permissions§8: ");
			if (group.getPermissions().isEmpty()) {
				args.getSender().sendMessage("§cAucune permission n'est attribu§e à ce rang.");
			} else {
				group.getPermissions().forEach(s -> {
					args.getSender().sendMessage("§e- " + s);
				});
			}
			args.getSender().sendMessage(Utils.LINE);
		}
	}
	
	@Command(name = "group.list", aliases = {"groupe.list"}, permission = "group.list", inGameOnly = false)
	public void onListCommand(CommandArgs args) {
		GroupModule groupManager = GroupModule.getInstance();
		args.getPlayer().sendMessage(Utils.LINE);
		for(Group group: groupManager.getGroups()) {
			args.getPlayer().sendMessage(Utils.color(group.getPrefix()));
		}
		args.getPlayer().sendMessage(Utils.LINE);
	}
}
