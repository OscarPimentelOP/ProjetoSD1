package SharedRegions;

import AuxTools.CAM;
import AuxTools.Bag;
import Entities.Porter;
import Entities.PorterState;
import Entities.Passenger;
import Entities.PassengerState;

public class BaggageCollectionPoint {
	
	//CAM<idPassanger, array de bags>
	CAM<Integer, Bag[]> convoyBelt;
	//Variable that warns the passenger that can go collect his bag
	
	
	//Porter functions
	
	//Unlocks the passenger 'x' that the id appears in the bag
	public void carryItToAppropriateStore(Bag bag) {
		Porter p = (Porter) Thread.currentThread();
		p.setPorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
		int passengerId = bag.getPassegerId();
		//Adicionar à CAM
	}
	
	
	//Passenger functions
	
	//All passengers are blocked until the bag x unlocks individually the respective passenger
	public boolean goCollectABag() {
		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
		//while a bag não estiver lá
		//wait
		return true;
	}
	
}
