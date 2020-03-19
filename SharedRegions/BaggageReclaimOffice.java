package SharedRegions;

public class BaggageReclaimOffice {
	//Sum of all missing bags
	int totalNumOfMissingBags;
	
	//PASSENGER FUNCTIONS
	
	//Adds the number of missing bags to the count of the total missing bags in the reclaim office
	public void reportMissingBags(int numMissingBags) {
		totalNumOfMissingBags+=numMissingBags;
	}
}
