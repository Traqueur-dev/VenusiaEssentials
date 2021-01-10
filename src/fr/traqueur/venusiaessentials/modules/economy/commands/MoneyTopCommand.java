package fr.traqueur.venusiaessentials.modules.economy.commands;

import java.util.List;
import java.util.Map.Entry;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class MoneyTopCommand extends ICommand {

	@Command(name = "bal.top", aliases = {"balance.top", "money.top", "argent.top", "baltop"})
	public void onCommand(CommandArgs args) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(args.getPlayer().getName());
		
		int end = 10;
		if (end > profileManager.getBaltop().size()) {
			end = profileManager.getBaltop().size();
		}

		List<Entry<String,Double>> subList = profileManager.getBaltop().subList(0, end);
		
		profile.msg(Utils.LINE);
	
		for (int i = 0; i < subList.size(); i++) {
			Entry<String,Double> entry = subList.get(i);
			
			profile.msg("&6&l" + (i+1) +". &e" + entry.getKey() + " &6avec &f&l" + entry.getValue() + "$&e.");	
		}
		
		profile.msg(Utils.LINE);
	}
}
