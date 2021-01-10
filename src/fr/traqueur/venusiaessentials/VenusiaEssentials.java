package fr.traqueur.venusiaessentials;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;

import com.google.common.collect.Lists;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.commands.CommandFramework;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.modules.ModuleManager;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.commands.BlockCommand;
import fr.traqueur.venusiaessentials.commands.admin.ClaimCommand;
import fr.traqueur.venusiaessentials.commands.admin.DayCommand;
import fr.traqueur.venusiaessentials.commands.admin.EnchantCommand;
import fr.traqueur.venusiaessentials.commands.admin.FlyCommand;
import fr.traqueur.venusiaessentials.commands.admin.GamemodeCommand;
import fr.traqueur.venusiaessentials.commands.admin.GodModeCommand;
import fr.traqueur.venusiaessentials.commands.admin.HatCommand;
import fr.traqueur.venusiaessentials.commands.admin.HealCommand;
import fr.traqueur.venusiaessentials.commands.admin.JailCommand;
import fr.traqueur.venusiaessentials.commands.admin.KillCommand;
import fr.traqueur.venusiaessentials.commands.admin.MoreCommand;
import fr.traqueur.venusiaessentials.commands.admin.NightCommand;
import fr.traqueur.venusiaessentials.commands.admin.RepairCommand;
import fr.traqueur.venusiaessentials.commands.admin.ReportCommand;
import fr.traqueur.venusiaessentials.commands.admin.SeenCommand;
import fr.traqueur.venusiaessentials.commands.admin.SkullCommand;
import fr.traqueur.venusiaessentials.commands.admin.SpeedCommand;
import fr.traqueur.venusiaessentials.commands.admin.SudoCommand;
import fr.traqueur.venusiaessentials.commands.admin.SunCommand;
import fr.traqueur.venusiaessentials.commands.admin.VanishCommand;
import fr.traqueur.venusiaessentials.commands.essentials.BottleCommand;
import fr.traqueur.venusiaessentials.commands.essentials.CorbeilleCommand;
import fr.traqueur.venusiaessentials.commands.essentials.CraftCommand;
import fr.traqueur.venusiaessentials.commands.essentials.FeedCommand;
import fr.traqueur.venusiaessentials.commands.essentials.HelpCommand;
import fr.traqueur.venusiaessentials.commands.essentials.ListCommand;
import fr.traqueur.venusiaessentials.commands.essentials.NearCommand;
import fr.traqueur.venusiaessentials.commands.essentials.NightVisionCommand;
import fr.traqueur.venusiaessentials.commands.essentials.PingCommand;
import fr.traqueur.venusiaessentials.commands.essentials.RandomTPCommand;
import fr.traqueur.venusiaessentials.commands.essentials.SuicideCommand;
import fr.traqueur.venusiaessentials.commands.inventory.ClearInventoryCommand;
import fr.traqueur.venusiaessentials.commands.inventory.EnderchestCommand;
import fr.traqueur.venusiaessentials.commands.inventory.InventorySeeCommand;
import fr.traqueur.venusiaessentials.commands.server.LagCommand;
import fr.traqueur.venusiaessentials.commands.server.SaveCommand;
import fr.traqueur.venusiaessentials.listeners.RandomTPListener;
import fr.traqueur.venusiaessentials.modules.chat.ChatModule;
import fr.traqueur.venusiaessentials.modules.chat.commands.ChatCommand;
import fr.traqueur.venusiaessentials.modules.clearlag.ClearLagModule;
import fr.traqueur.venusiaessentials.modules.economy.provider.EconomyProvider;
import fr.traqueur.venusiaessentials.modules.factions.FactionModule;
import fr.traqueur.venusiaessentials.modules.groups.GroupModule;
import fr.traqueur.venusiaessentials.modules.homes.HomeCommand;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.profiles.server.ServerProfileModule;
import fr.traqueur.venusiaessentials.modules.punishments.PunishModule;
import fr.traqueur.venusiaessentials.modules.teleports.TeleportModule;
import fr.traqueur.venusiaessentials.modules.warps.WarpModule;
import fr.traqueur.venusiaessentials.modules.warps.commands.WarpCommand;
import fr.traqueur.venusiaessentials.tasks.AutoMessageTask;
import fr.traqueur.venusiaessentials.tasks.JailTask;
import fr.traqueur.venusiaessentials.tasks.SaveTask;
import net.milkbowl.vault.economy.Economy;

public class VenusiaEssentials extends Plugin {

	public static final String PREFIX = "Venusia";
	private Economy economy;	

	private static VenusiaEssentials instance;
	
	public VenusiaEssentials() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.setupEconomy();
	}

	@Override
	public void onDisable() {
		super.onDisable();

		for (Player player : Utils.getOnlinePlayers()) {
			player.kickPlayer("§eRedémarrage de §6l'infrastructure §een §acours§e...");
		}
	}

	private boolean setupEconomy() {
		if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
			this.getServer().shutdown();
			return false;
		}
		this.getServer().getServicesManager()
				.register(Economy.class, new EconomyProvider(), this, ServicePriority.Normal);
		this.economy = new EconomyProvider();
		return this.economy != null;
	}
	
	@Override
	public void registerManagers() {
		ModuleManager moduleManager = this.getModuleManager();

		moduleManager.registerPersist(new ProfileModule(this));
		moduleManager.registerPersist(new ServerProfileModule(this));
		moduleManager.registerPersist(new FactionModule(this));
		moduleManager.registerPersist(new GroupModule(this));
		moduleManager.registerPersist(new PunishModule(this));
		moduleManager.registerPersist(new TeleportModule(this));

		moduleManager.registerPersist(new ClearLagModule(this));
		moduleManager.registerPersist(new KitModule(this));
		moduleManager.registerPersist(new ChatModule(this));
		moduleManager.registerPersist(new WarpModule(this));

	}

	@Override
	public void registerOthers() {

		List<ICommand> commands = Lists.newArrayList();
		CommandFramework framework = this.getFramework();

		commands.add(new HomeCommand());
		commands.add(new SaveCommand());
		commands.add(new LagCommand());


		commands.add(new ClearInventoryCommand());
		commands.add(new EnderchestCommand());
		commands.add(new InventorySeeCommand());

		commands.add(new SunCommand());
		commands.add(new GamemodeCommand());
		commands.add(new FlyCommand());
		commands.add(new HatCommand());
		commands.add(new HealCommand());
		commands.add(new GodModeCommand());
		commands.add(new KillCommand());
		commands.add(new CraftCommand());
		commands.add(new FeedCommand());
		commands.add(new PingCommand());
		commands.add(new EnchantCommand());
		commands.add(new NearCommand());
		commands.add(new SpeedCommand());
		commands.add(new ListCommand());
		commands.add(new ReportCommand());
		commands.add(new RepairCommand());
		commands.add(new SudoCommand());
		commands.add(new SuicideCommand());
		commands.add(new JailCommand());
		commands.add(new SeenCommand());
		commands.add(new VanishCommand());
		commands.add(new MoreCommand());
		commands.add(new HomeCommand());
		commands.add(new WarpCommand());
		commands.add(new SkullCommand());
		commands.add(new ClaimCommand());
		commands.add(new HelpCommand());
		commands.add(new CorbeilleCommand());
		commands.add(new BottleCommand());
		commands.add(new NightVisionCommand());
		commands.add(new DayCommand());
		commands.add(new NightCommand());
		commands.add(new ChatCommand());
		commands.add(new RandomTPCommand());
		commands.add(new BlockCommand());
		
		for (ICommand iCommand : commands) {
			framework.registerCommands(iCommand);
			if (iCommand instanceof Listener) {
				this.registerListener((Listener) iCommand);
			}
		}
		
		this.registerListener(new RandomTPListener());

		// Register all tasks
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new SaveTask(), 20, 20 * 60 * 60);
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new JailTask(), 1l, (20L * 60));
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AutoMessageTask(), 1l,
				(20L * 60) * ServerProfileModule.getInstance().getServerProfile().getAutoMessageDelay());
	}

	public Economy getEconomy() {
		return economy;
	}
	
	public static VenusiaEssentials getInstance() {
		return instance;
	}
}
