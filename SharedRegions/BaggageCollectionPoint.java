package SharedRegions;

import AuxTools.CAM;
import AuxTools.SharedException;
import AuxTools.Bag;
import Entities.Porter;
import Entities.PorterState;
import Entities.Passenger;
import Entities.PassengerState;

/**
     * This class implements the Baggage Collection Point shared region.
	 * The passengers can collect their bags from the convoy belt.
*/

public class BaggageCollectionPoint {
	
	
	/**
     * Convoy belt, emulated with a CAM (Map). <Passenger ID, Passenger's bags[]>
	*/
	private CAM<Integer, Bag[]> convoyBelt;
	
	/**
     * Number of bags in the convoy belt
	*/
	private int numOfBagsInConveyBelt;
	
	/**
     * The repository, to store the program status
	*/
	private Repo repo;
	
	/**
     * Informs if there are bags at the plane's hold
	*/
	private boolean moreBagsAtPlaneHold;
	
	/**
     * Baggage Collection Point's instantiation
     * @param repo -> repository of information
    */
	public BaggageCollectionPoint(Repo repo) {
		this.repo = repo;
		this.numOfBagsInConveyBelt = 0;
		this.convoyBelt = new CAM<Integer, Bag[]>();
		this.moreBagsAtPlaneHold = true;
	}
	
	//Porter functions
	
	/**
     * The porter unlocks the passenger 'x' that the id appears in the bag
	 * @param bag the bag to be carried by the porter
	 * @throws SharedException if the bag the porter tries to carry is null
	*/
	public synchronized void carryItToAppropriateStore(Bag bag) throws SharedException{

		try
		{ if (bag == null)                         /* check for proper parameter range */
			 throw new SharedException ("The porter could not carry the: " + bag + " bag because it didn't exist.");
		}
		catch (SharedException e)
		{ System.out.println ("Thread " + ((Thread) Thread.currentThread ()).getName () + "terminated.");
		   System.out.println ("carryItToAppropriateStore()" + e.getMessage ());
		   System.exit (1);
		}

		if(!this.moreBagsAtPlaneHold) {
			this.moreBagsAtPlaneHold = true;
		}
		Porter p = (Porter) Thread.currentThread();
		p.setPorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
		repo.setPorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);

		int passengerId = bag.getPassegerId();
		Bag[] sBags = new Bag[2];
		Bag[] retrieveTest = new Bag[2];
		retrieveTest = this.convoyBelt.retreive(passengerId);
		this.incNumOfBagsInConveyBelt();;
		//Add to CAM
		if(retrieveTest == null) {
			sBags[0] = bag;
			this.convoyBelt.store(passengerId, sBags);
		}
		else {
			sBags = this.convoyBelt.retreive(passengerId);
			sBags[1] = bag;
			this.convoyBelt.store(passengerId, sBags);
		}
		repo.setNumOfBagsInTheConvoyBelt(this.getNumOfBagsInConveyBelt());
		notifyAll();
	}
	
	
	//Passenger functions
	

	/**
     * The passenger collects a bag.
	 * All passengers are blocked until the bag x unlocks individually the respective passenger.
	 * The bag from passenger X needs to be in the conveyor belt.
	*/
	public synchronized boolean goCollectABag() {
		Passenger p = (Passenger) Thread.currentThread(); 
		p.setPassengerState(PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
		int id = p.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
		while(this.convoyBelt.retreive(id) == null && this.moreBagsAtPlaneHold) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		if(this.convoyBelt.retreive(id) != null) {
			Bag[] bag  = this.convoyBelt.retreive(id);
			if(bag[1]== null) {
				this.convoyBelt.remove(id);
			}
			else {
				this.convoyBelt.remove(id);
				bag[1] = null;
				this.convoyBelt.store(id, bag);
			}
			this.decNumOfBagsInConveyBelt();
			repo.setNumOfBagsInTheConvoyBelt(this.getNumOfBagsInConveyBelt());
			repo.setNumOfBagsCollected(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
     * Sets the more bags variable to tell if there's more bags at the plane's hold
	 * @param moreBags
	*/
	public synchronized void setMoreBags(boolean moreBags) {
		this.moreBagsAtPlaneHold = moreBags;
		notifyAll();
	}
	
	/**
     * Increments the number of bags in the convoy belt
	*/
	public synchronized void incNumOfBagsInConveyBelt() {
		numOfBagsInConveyBelt++;
	}
	
	/**
     * Decrements the number of bags in the convoy belt
	*/
	public synchronized void decNumOfBagsInConveyBelt() {
		numOfBagsInConveyBelt--;
	}
	
	/**
     * Returns the number of bags at the convoy belt
	 * @return number of bags in the convoy belt
	*/
	public synchronized int getNumOfBagsInConveyBelt() {
		return numOfBagsInConveyBelt;
	}

}
