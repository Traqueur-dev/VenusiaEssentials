package fr.traqueur.venusiaessentials.modules.groups;

import java.util.List;

public class Group {

	private String name, prefix;
	private List<String> permissions;
	
	public Group() {}
	
	public Group(String name, String prefix, List<String> perm) {
		this.name = name;
		this.prefix = prefix;
		permissions = perm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	
}
