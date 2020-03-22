package SharedRegions;

import AuxTools.MemStack;
import AuxTools.Bag;
import AuxTools.MemException;
import Entities.Passenger;
import Entities.PassengerState;
import Entities.Porter;
import Entities.PorterState;

public class ArrivalLounge {
	//Variable the warns the porter than can go rest
	//Last passenger of the last flight accuses in goHome function or in prepareNextLeg function
	boolean endOfOperations;
	
	//Count of the number of passenger for the last one to warn that it is the last
	int cntPassengers;
	
	//Passenger bags in stack format
	MemStack<Bag> sBags;
	
	
	public ArrivalLounge(MemStack<Bag> sBags){
		this.sBags = sBags;
	}
	
	//PORTER FUNCTIONS
	
	///Returns 'E' (End of the day) or 'W' (Work) 
	public char takeARest() {
		Porter p = (Porter) Thread.currentThread(); 
		while(cntPassengers != 6) {
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
		if (flight == 5 && !endOfOperations){
			endOfOperations = true;
		}
		cntPassengers++;
		int nBags = m.getNumBags(flight);
		if(nBags != 0) {
			//Go collect bag
			return 'B';
		}
		else {
			//Is the last flight of the passenger?
			if(flight==5) {
				//Goes home
				return 'H';
			}
			else {
				//Take a bus
				return 'T';
			}
		}
	}
}