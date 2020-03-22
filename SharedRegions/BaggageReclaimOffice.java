package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;

public class BaggageReclaimOffice {
	//Sum of all missing bags
	int totalNumOfMissingBags;
	
	//PASSENGER FUNCTIONS
	
	//Adds the number of missing bags to the count of the total missing bags in the reclaim office
	public void reportMissingBags(int numMissingBags) {
		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		totalNumOfMissingBags+=numMissingBags;
	}
}
