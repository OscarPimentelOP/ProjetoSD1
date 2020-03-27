package SharedRegions;

import AuxTools.CAM;
import AuxTools.Bag;
import Entities.Porter;
import Entities.PorterState;
import Entities.Passenger;
import Entities.PassengerState;

public class BaggageCollectionPoint {
	
	//CAM<idPassanger, array of bags>
	private CAM<Integer, Bag[]> convoyBelt;
	
	//
	private int numOfBagsInConveyBelt;
	
	private Repo repo;
	
	public BaggageCollectionPoint(Repo repo) {
		this.repo = repo;
		this.numOfBagsInConveyBelt = 0;
		this.convoyBelt = new CAM<Integer, Bag[]>();
	}
	
	//Porter functions
	
	//Unlocks the passenger 'x' that the id appears in the bag
	public synchronized void carryItToAppropriateStore(Bag bag) {
		Porter p = (Porter) Thread.currentThread();
		p.setPorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
		repo.setPorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
		int passengerId = bag.getPassegerId();
		Bag[] sBags = new Bag[2];
		Bag[] retrieveTest = new Bag[2];
		retrieveTest = this.convoyBelt.retreive(passengerId);
		numOfBagsInConveyBelt++;
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
		repo.setNumOfBagsInTheConvoyBelt(this.numOfBagsInConveyBelt);
		notifyAll();
	}
	
	
	//Passenger functions
	
	//All passengers are blocked until the bag x unlocks individually the respective passenger
	public synchronized boolean goCollectABag() {
		Passenger p = (Passenger) Thread.currentThread(); 
		p.setPassengerState(PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
		int id = p.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
		while(this.convoyBelt.retreive(id) == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		repo.setNumOfBagsCollected(id);
		return true;
	}
	
}
