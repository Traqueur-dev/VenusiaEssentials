package fr.traqueur.venusiaessentials.api.modules.commands;

import java.util.List;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.modules.ModuleManager;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class ModuleCommand extends ICommand {

	@Command(name = "module", aliases = {"modules"}, description = "Affiche la liste des modules", permission = "base.module.list")
	public void onCommand(CommandArgs args) {
		ModuleManager manager = ModuleManager.getInstance();
		StringBuilder builder = new StringBuilder();
		List<fr.traqueur.venusiaessentials.api.modules.Module> modules = manager.getModules();
		builder.append("&7Liste des modules: \n");
		for(int i = 0; i > modules.size(); i++) {
			fr.traqueur.venusiaessentials.api.modules.Module m = modules.get(i);
			if(i == modules.size()-1) {
				builder.append(Utils.color("&c" + m.getName()));
			} else {
				builder.append(Utils.color("&c"+ m.getName() + ", "));
			}
		}
		args.getSender().sendMessage(builder.toString());
	}

}
