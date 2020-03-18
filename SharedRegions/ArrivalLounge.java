package SharedRegions;

import AuxTools.*;

public class ArrivalLounge {
	//Variavel que avisa o porter que pode ir descansar
	//Ultimo passageiro do ultimo voo que se acusa no goHome ou no prepareNextLeg
	boolean endOfOperations;
	
	//Contagem do numero de passageiros para o ultimo avisar que é o ultimo
	int cntPassengers;
	
	//Passenger bags in stack format
	MemStack<Bag> sBags;
	
	
	public ArrivalLounge(MemStack<Bag> sBags){
		this.sBags = sBags;
	}
	
	//PORTER FUNCTIONS
	
	///Returns 'E' (End of the day) or 'W' (Work) 
	public char takeARest() {
		if(cntPassengers == 6) {
			if(endOfOperations) {
				return 'E';
			}
			else return 'W';
			
			//?
		} else return 'W';
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
