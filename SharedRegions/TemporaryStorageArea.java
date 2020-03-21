package SharedRegions;

import AuxTools.MemStack;
import AuxTools.Bag;
import AuxTools.MemException;

public class TemporaryStorageArea {
	
	//Bag Storage in stack format
	MemStack<Bag> bagStorage;
	
	//Porter functions
	
	//Add bag to storage
	public void carryItToAppropriateStore(Bag bag) {
		try {
			bagStorage.write(bag);
		} catch(MemException e) {}
	}
}
