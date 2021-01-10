package fr.traqueur.venusiaessentials.modules.clearlag;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.modules.Saveable;

public class ClearLagModule extends Saveable {

	private static ClearLagModule instance;

	public ClearLagModule(Plugin plugin) {
		super(plugin, "ClearLag");

		instance = this;

		this.registerCommand(new ClearLagCommand());

		Bukkit.getScheduler().runTaskTimerAsynchronously(this.getPlugin(), new ClearLagTask(), 20,20);
	}

	public boolean isRemovable(Entity e) {
		if ((e instanceof Item) || (e instanceof Projectile)) {
			return true;
		}
		if ((e instanceof LivingEntity) && (!(e instanceof HumanEntity)) && (!(e instanceof Villager))
			) {
			return true;
		}
		if ((e instanceof Painting) || (e instanceof TNTPrimed) || (e instanceof ExperienceOrb) || (e instanceof Boat)
				|| (e instanceof FallingBlock) || (e instanceof ItemFrame)) {
			return false;
		}
		return false;
	}

	public int removeEntities() {
		int removed = 0;
		for (World w : Bukkit.getWorlds()) {
			removed += this.removeEntities(this.getRemovables(w.getEntities(), w), w);
		}
		return removed;
	}

	public List<Entity> getRemovables(List<Entity> list, World w) {
		List<Entity> en = new LinkedList<Entity>();
		for (Entity ent : list) {
			if (this.isRemovable(ent)) {
				en.add(ent);
			}
		}
		return en;
	}

	public int removeEntities(List<Entity> removables, World w) {
		for (Entity en : removables) {
			en.remove();
		}
		return removables.size();
	}

	@Override
	public File getFile() {
		return null;
	}

	@Override
	public void loadData() {}

	@Override
	public void saveData() {}
	
	public static ClearLagModule getInstance() {
		return instance;
	}
}
