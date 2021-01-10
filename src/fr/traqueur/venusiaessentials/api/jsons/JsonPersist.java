package fr.traqueur.venusiaessentials.api.jsons;

import java.io.File;

public interface JsonPersist {

	public abstract File getFile();

	public abstract void loadData();

	public abstract void saveData();
	
}
