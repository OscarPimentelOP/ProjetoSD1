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
	private MemStack<Bag> sBags;
	
	private Repo repo;
	
	
	public ArrivalLounge(MemStack<Bag> sBags, Repo repo){
		this.sBags = sBags;
		this.repo = repo;
	}
	
	//PORTER FUNCTIONS
	
	///Returns 'E' (End of the day) or 'W' (Work) 
	public char takeARest() {
		Porter p = (Porter) Thread.currentThread(); 
		while(cntPassengers != SimulatorParam.NUM_PASSANGERS) {
			p.setPorterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
			//Introduzir wait()
		}
		if(endOfOperations) {
			return 'E';
		}
		else {
			p.setPorterState(PorterState.AT_THE_PLANES_HOLD);
			return 'W';
		}
	}
	
	
	//Returns a bag if any 
	//Returns null if there are no bags in the stack
	public Bag tryToCollectABag() {
		try  {
			return sBags.read();
		}
		catch (MemException e) { return null; }
	}
	
	
	public void noMoreBagsToCollect() {
		cntPassengers=0;
	}
	
	//PASSENGER FUNCTIONS
	
	//Returns H if passenger goes home, T if is going to take a bus or B if is going to collect a bag
	public char whatShouldIDo(int flight){
		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.AT_THE_DISEMBARKING_ZONE);
		if (flight == SimulatorParam.NUM_FLIGHTS && !endOfOperations){
			endOfOperations = true;
		}
		cntPassengers++;
		char tripState = m.getTripState(flight);
		//Passenger in transit
		if(tripState == 'T') {
			//Take a bus
			return 'T';
		}
		//Passenger reached final destination
		else {
			int nBags = m.getNumBags(flight);
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
}