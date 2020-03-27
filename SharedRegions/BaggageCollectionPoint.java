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
	
	private boolean moreBags;
	
	public BaggageCollectionPoint(Repo repo) {
		this.repo = repo;
		this.numOfBagsInConveyBelt = 0;
		this.convoyBelt = new CAM<Integer, Bag[]>();
		this.moreBags = true;
	}
	
	//Porter functions
	
	//Unlocks the passenger 'x' that the id appears in the bag
	public synchronized void carryItToAppropriateStore(Bag bag) {
		if(!this.moreBags) {
			this.moreBags = true;
		}
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
		while(this.convoyBelt.retreive(id) == null && this.moreBags) {
			try {
				System.out.println("laalalalalalaalalall");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(this.convoyBelt.retreive(id) != null) {
			Bag[] bag  = this.convoyBelt.retreive(id);
			if(bag[1]== null) {
				this.convoyBelt.remove(id);
			}
			else {
				bag[1] = null;
				this.convoyBelt.store(id, bag);
			}
			repo.setNumOfBagsCollected(id);
			System.out.println("tenhooooooooo malaaaaaaaaaaaaaaaaaaaa");
			return true;
		}
		else {
			System.out.println("mala????????????????????????????");
			return false;
		}
	}
	
	public synchronized void setMoreBags(boolean moreBags) {
		this.moreBags = moreBags;
		notifyAll();
	}
	
}
