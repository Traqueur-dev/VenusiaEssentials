package fr.traqueur.venusiaessentials.modules.groups;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.permissions.PermissionAttachment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.jsons.DiscUtil;
import fr.traqueur.venusiaessentials.api.modules.Saveable;
import net.minecraft.util.com.google.gson.reflect.TypeToken;

public class GroupModule extends Saveable {

	private static  GroupModule instance;

	private  Map<String, PermissionAttachment> attachments;
	public Map<String, PermissionAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, PermissionAttachment> attachments) {
		this.attachments = attachments;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Group getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(Group defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	public static GroupModule getInstance() {
		return instance;
	}

	private  List<Group> groups;

	private  Group defaultGroup;

	public GroupModule(Plugin plugin) {
		super(plugin, "Groups");

		instance = this;

		this.attachments = Maps.newHashMap();
		this.groups = Lists.newArrayList();

		this.defaultGroup = new Group("default", "§7[§fDéfaut§7]§f", Arrays.asList(new String[] {}));

		if (!this.isGroupExisting(this.defaultGroup.getName())) {
			this.groups.add(this.defaultGroup);
		}

		this.registerListener(new GroupListener());
		this.registerCommand(new GroupCommand());
	}

	public void createGroup(String groupName, List<String> permissions, String prefix) throws IOException {
		Group group = new Group(groupName, prefix.replace("&", "§"), permissions);
		this.groups.add(group);
	}

	public boolean isGroupExisting(String groupName) {
		Group group = this.getByName(groupName);
		if (group == null) {
			return false;
		}
		return true;
	}

	public Group getByName(String name) {
		for (Group group : this.getGroups()) {
			if (group.getName().equalsIgnoreCase(name)) {
				return group;
			}
		}
		return null;
	}

	@Override
	public File getFile() {
		return new File(this.getPlugin().getDataFolder(), "groups.json");
	}

	@Override
	public void loadData() {
		String content = DiscUtil.readCatch(this.getFile());
		if (content != null) {
			try {
				List<Group> groups = this.getGson().fromJson(content, new TypeToken<List<Group>>() {
				}.getType());
				this.groups = groups;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	@Override
	public void saveData() {
		DiscUtil.writeCatch(this.getFile(), VenusiaEssentials.getInstance().getGson().toJson(this.groups));
	}
}
