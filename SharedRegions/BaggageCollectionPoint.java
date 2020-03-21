package SharedRegions;

import AuxTools.MemCAM;
import AuxTools.Bag;

public class BaggageCollectionPoint {
	
	//CAM<id,bag>
	MemCAM<Integer, Bag> convoyBelt;
	//Variable that warns the passenger that can go collect his bag
	
	
	//Porter functions
	
	//Unlocks the passenger 'x' that the id appears in the bag
	public void carryItToAppropriateStore(Bag bag) {
		
	}
	
	
	//Passenger functions
	
	//All passengers are blocked until the bag x unlocks individually the respective passenger
	public boolean goCollectABag() {
		return true;
	}
	
}
