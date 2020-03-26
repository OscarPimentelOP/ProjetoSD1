package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;

public class BaggageReclaimOffice {
	//Sum of all missing bags
	private int totalNumOfMissingBags;
	
	private Repo repo;
	
	public BaggageReclaimOffice(Repo repo) {
		this.repo = repo;
	}
	
	//PASSENGER FUNCTIONS
	
	//Adds the number of missing bags to the count of the total missing bags in the reclaim office
	public void reportMissingBags(int numMissingBags) {
		Passenger p = (Passenger) Thread.currentThread(); 
		p.setPassengerState(PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		int id = p.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		totalNumOfMissingBags+=numMissingBags;
		this.repo.setLostBags(totalNumOfMissingBags);
	}
}
