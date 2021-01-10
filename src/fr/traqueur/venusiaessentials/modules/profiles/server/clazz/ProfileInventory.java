package fr.traqueur.venusiaessentials.modules.profiles.server.clazz;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class ProfileInventory {

	private List<ItemStack> inventory, enderchest;
	private boolean shouldUpdateInventory, shouldUpdateEnderchest;

	public List<ItemStack> getInventory() {
		return inventory;
	}

	public void setInventory(List<ItemStack> inventory) {
		this.inventory = inventory;
	}

	public List<ItemStack> getEnderchest() {
		return enderchest;
	}

	public void setEnderchest(List<ItemStack> enderchest) {
		this.enderchest = enderchest;
	}

	public boolean isShouldUpdateInventory() {
		return shouldUpdateInventory;
	}

	public void setShouldUpdateInventory(boolean shouldUpdateInventory) {
		this.shouldUpdateInventory = shouldUpdateInventory;
	}

	public boolean isShouldUpdateEnderchest() {
		return shouldUpdateEnderchest;
	}

	public void setShouldUpdateEnderchest(boolean shouldUpdateEnderchest) {
		this.shouldUpdateEnderchest = shouldUpdateEnderchest;
	}

	public ProfileInventory() {}

	public ProfileInventory(Player player) {
		this.updateContents(player);
	}

	public void updateContents(Player player) {
		this.inventory = Lists.newArrayList();
		ItemStack[] invContents = player.getInventory().getContents();
		for (int i = 0; i < invContents.length; i++) {
			ItemStack item = invContents[i];
			if (item == null || item.getType() == Material.AIR)
				continue;
			this.inventory.add(item);
		}

		this.enderchest = Lists.newArrayList();
		ItemStack[] enderchestContents = player.getEnderChest().getContents();
		for (int i = 0; i < enderchestContents.length; i++) {
			ItemStack item = enderchestContents[i];
			if (item == null || item.getType() == Material.AIR)
				continue;
			this.enderchest.add(item);
		}
	}

	public void applyUpdate(Player player) {
		if (this.shouldUpdateEnderchest) {
			player.getEnderChest().setContents(this.enderchestAsArray());
			this.shouldUpdateEnderchest = false;
		}

		if (this.shouldUpdateInventory) {
			player.getInventory().setContents(this.inventoryAsArray());
			player.updateInventory();
			this.shouldUpdateInventory = false;
		}
	}

	public ItemStack[] inventoryAsArray() {
		return inventory.toArray(new ItemStack[inventory.size()]);
	}

	public ItemStack[] enderchestAsArray() {
		return enderchest.toArray(new ItemStack[enderchest.size()]);
	}
}
