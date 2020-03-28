package SharedRegions;

import AuxTools.MemStack;
import AuxTools.Bag;
import AuxTools.MemException;
import Entities.Passenger;
import Entities.PassengerState;
import Entities.Porter;
import Entities.PorterState;
import Main.SimulatorParam;

public class ArrivalLounge {
	//Variable the warns the porter than can go rest
	//Last passenger of the last flight accuses in goHome function or in prepareNextLeg function
	private boolean endOfOperations;
	
	//Count of the number of passenger for the last one to warn that it is the last
	private int cntPassengers;
	
	//Passenger bags in stack format
	private MemStack<Bag> sBags[];
	
	private Repo repo;
	
	private int flight;
	
	private int passengersFinalDest;
	
	private int passengersTransit;
	
	private BaggageCollectionPoint bcp;
	
	private char[][] passengersTripState;
	
	private int[] numOfBagsPerFlight;
	
	public ArrivalLounge(MemStack<Bag> sBags[], int[] numOfBagsPerFlight, char[][] tripState, BaggageCollectionPoint bcp ,Repo repo){
		this.sBags = sBags;
		this.repo = repo;
		this.flight = 0;
		this.passengersFinalDest = 0;
		this.passengersTransit = 0;
		this.passengersTripState = tripState;
		this.bcp = bcp;
		this.endOfOperations = false;
		this.cntPassengers = 0;
		this.numOfBagsPerFlight = numOfBagsPerFlight;
	}
	
	//PORTER FUNCTIONS
	
	///Returns 'E' (End of the day) or 'W' (Work) 
	public synchronized char takeARest() {
		Porter p = (Porter) Thread.currentThread();
		while(cntPassengers != SimulatorParam.NUM_PASSANGERS && !this.endOfOperations) {
			try {
				wait();
			}catch(InterruptedException e) {}
		}
		if(this.endOfOperations) {
			return 'E';
		}
		else {
			p.setPorterState(PorterState.AT_THE_PLANES_HOLD);
			repo.setPorterState(PorterState.AT_THE_PLANES_HOLD);
			return 'W';
		}
	}
	
	
	//Returns a bag if any 
	//Returns null if there are no bags in the stack
	public synchronized Bag tryToCollectABag() {
		try  {
			Bag bag = sBags[this.flight].read();
			repo.decNumOfBagsAtPlaneHold();
			int passengerId = bag.getPassegerId();
			bag.setDestination(passengersTripState[passengerId][flight]);
			return bag;
		}
		catch (MemException e) {
			return null; 
		}
	}
	
	
	public synchronized void noMoreBagsToCollect() {
		cntPassengers=0;
		bcp.setMoreBags(false);
		Porter p = (Porter) Thread.currentThread();
		p.setPorterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
		repo.setPorterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
	}
	
	//PASSENGER FUNCTIONS
	
	//Returns H if passenger goes home, T if is going to take a bus or B if is going to collect a bag
	public synchronized char whatShouldIDo(int flight){
		//Initialize a new flight
		if(cntPassengers == 0) {
			bcp.setMoreBags(true);
			this.flight = flight;
			repo.setFlightNumber(flight);
			repo.setNumOfBagsAtPlaneHold(flight, this.numOfBagsPerFlight[flight]);
		}
		Passenger p = (Passenger) Thread.currentThread(); 
		p.setPassengerState(PassengerState.AT_THE_DISEMBARKING_ZONE);
		int id = p.getIdentifier();
		cntPassengers++;
		if(cntPassengers == SimulatorParam.NUM_PASSANGERS) {
			notifyAll();
		}
		char tripState = p.getTripState(flight);
		//Passenger in transit
		if(tripState == 'T') {
			this.passengersTransit++;
			repo.setPassengersTransit(this.passengersTransit);
			repo.setPassengerDestination(id, "TRT");
			repo.setNumOfBagsAtTheBegining(id, p.getNumBags(flight));
			repo.setPassengerState(id, PassengerState.AT_THE_DISEMBARKING_ZONE);
			//Take a bus
			return 'T';
		}
		//Passenger reached final destination
		else {
			this.passengersFinalDest++;
			repo.setPassengersFinalDest(this.passengersFinalDest);
			repo.setPassengerDestination(id, "FDT");
			int nBags = p.getNumBags(flight);
			repo.setNumOfBagsAtTheBegining(id, nBags);
			repo.setPassengerState(id, PassengerState.AT_THE_DISEMBARKING_ZONE);
			//Has bags to collect
			if(nBags != 0) {
				//Go collect bag
				return 'B';
			}
			//No bags to collect
			else {
				//Go home
				return 'H';
			}
		}
	}
	
	public synchronized void setEndOfWork() {
		this.endOfOperations = true;
		notifyAll();
	}
}