package folksent.storage;

import folksent.model.Folksonomy;

public interface StorageAdapter {

	public Folksonomy loadFolksonomy();
	public void storeFolksonomy(Folksonomy folksonomy);

}
