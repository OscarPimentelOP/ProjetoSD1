package SharedRegions;

import AuxTools.MemStack;
import AuxTools.SharedException;
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
     * Temporary storage area's instantiation
     * @param repo -> repository of information
    */
	public TemporaryStorageArea(Repo repo) {
		this.repo = repo;
		this.numOfBagsAtStoreroom = 0;
		Bag[] bags = new Bag[SimulatorParam.NUM_PASSANGERS*SimulatorParam.MAX_NUM_OF_BAGS];
		try {
			bagStorage = new MemStack<Bag>(bags);
		} catch (MemException e) {
			System.out.println(e);
		}
	}
	
	//Porter functions
	
	/**
     * The porter adds a bag to the storage.
     * @param bag -> the bag to be stored
	 * @throws SharedException if the bag the porter tries to carry is null
    */
	public synchronized void carryItToAppropriateStore(Bag bag) throws SharedException {
		try
		{ if (bag == null)                         /* check for proper parameter range */
			 throw new SharedException ("The porter could not carry the: " + bag + " bag because it didn't exist.");
		}
		catch (SharedException e)
		{ System.out.println ("Thread " + ((Thread) Thread.currentThread ()).getName () + "terminated.");
		   System.out.println ("carryItToAppropriateStore()" + e.getMessage ());
		   System.exit (1);
		}
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
