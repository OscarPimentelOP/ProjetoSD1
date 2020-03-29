package SharedRegions;

import AuxTools.MemStack;
import AuxTools.Bag;
import AuxTools.MemException;
import Entities.PorterState;
import Main.SimulatorParam;
import Entities.Porter;

/**
     * This class implements the Temporary Storage Area shared region.
	 * In this region, the porter carries the bags at the storeroom.
*/

public class TemporaryStorageArea {
	

	/**
     * Bag storage, emulated with a Stack.
	*/
	private MemStack<Bag> bagStorage;
	
	/**
     * The repository, to store the program status
	*/
	private Repo repo;
	
	/**
     * Number of bags at the storeroom
	*/
	private int numOfBagsAtStoreroom;
	
	/**
     * Temporary storage area's instanciation
     * @param repo -> repository of information
    */
	public TemporaryStorageArea(Repo repo) {
		this.repo = repo;
		this.numOfBagsAtStoreroom = 0;
		Bag[] bags = new Bag[SimulatorParam.NUM_PASSANGERS*SimulatorParam.MAX_NUM_OF_BAGS];
		try {
			bagStorage = new MemStack<Bag>(bags);
		} catch (MemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Porter functions
	
	/**
     * The porter adds a bag to the storage.
     * @param bag -> the bag to be stored
    */
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
