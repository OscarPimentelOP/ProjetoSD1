package SharedRegions;

import AuxTools.MemStack;
import AuxTools.Bag;
import AuxTools.MemException;
import Entities.PorterState;
import Entities.Porter;

public class TemporaryStorageArea {
	
	//Bag Storage in stack format
	private MemStack<Bag> bagStorage;
	
	private Repo repo;
	
	//
	private int numOfBagsAtStoreroom;
	
	public TemporaryStorageArea(Repo repo) {
		this.repo = repo;
		this.numOfBagsAtStoreroom = 0;
	}
	
	//Porter functions
	
	//Add bag to storage
	public synchronized void carryItToAppropriateStore(Bag bag) {
		Porter p = (Porter) Thread.currentThread();
		p.setPorterState(PorterState.AT_THE_STOREROOM);
		repo.setPorterState(PorterState.AT_THE_STOREROOM);
		try {
			bagStorage.write(bag);
			this.numOfBagsAtStoreroom++;
		} catch(MemException e) {}
		repo.setNumOfBagsInTheTempArea(numOfBagsAtStoreroom);
	}
}
