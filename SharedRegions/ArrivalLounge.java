package SharedRegions;

import AuxTools.MemStack;
import AuxTools.Bag;
import AuxTools.MemException;

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
		while(cntPassengers != 6);
		if(endOfOperations) {
			return 'E';
		}
		else return 'W';
	}
	
	
	//Returns a bag if any 
	//Returns null if there are no bags in the stack
	public Bag tryToCollectABag() {
		try  {
			return sBags.read();
		}
		catch (MemException e) { return null; }
	}
	
	//Nao faço ideia
	public void noMoreBagsToCollect() {
		cntPassengers=0;
	}
	
	//PASSENGER FUNCTIONS
	
	public char whatShouldIDo(int flight){
		if (flight == 5 && !endOfOperations){
			endOfOperations = true;
		}
		cntPassengers++;
		
		//Isto é feito por probabilidades ou?
		return 'H';
	}
}
