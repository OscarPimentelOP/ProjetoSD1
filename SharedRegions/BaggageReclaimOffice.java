package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;

/**
     * This class implements the Baggage Reclaim Office shared region.
	 * The passengers can report missing bags here.
*/

public class BaggageReclaimOffice {

	
	/**
     * Sum of all missing bags
	*/
	private int totalNumOfMissingBags;
	
	/**
     * The repository, to store the program status
	*/
	private Repo repo;
	
	/**
     * Baggage ReclaimOffice's instantiation
     * @param repo -> repository of information
    */
	public BaggageReclaimOffice(Repo repo) {
		this.repo = repo;
	}
	
	//PASSENGER FUNCTIONS
	
	/**
     * A passenger reports missing bags.
	 * Adds the number of missing bags to the count of the total missing bags in the reclaim office.
     * @param numMissingBags -> number of missing bags from a passenger
    */
	public synchronized  void reportMissingBags(int numMissingBags) {
		Passenger p = (Passenger) Thread.currentThread(); 
		p.setPassengerState(PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		int id = p.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		totalNumOfMissingBags+=numMissingBags;
		this.repo.setLostBags(totalNumOfMissingBags);
	}
}
