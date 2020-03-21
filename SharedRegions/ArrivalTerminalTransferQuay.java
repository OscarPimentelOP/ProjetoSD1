package SharedRegions;

public class ArrivalTerminalTransferQuay {
	
	//Variable the warns the passengers the they can enter on the bus
	boolean announced;
	
	//Variable that warns the bus driver that he can go rest
	//Last passenger of the last flight accuses in goHome function or in prepareNextLeg function
	boolean endOfOperations;
	
	//Count of the number of passenger for the last one to warn that it is the last	
	int cntPassengers;
	
	int timequantum;
	
	
	//Passenger functions
	
	public void takeABus() {
		cntPassengers++;
	}
	
	//The passengers are blocked
	//When bus diver announces arrival, it unlocks passengers
	public void enterTheBus() {
		while (!announced);
	}
	
	
	public void setEndOfWork() {
		
	}
	
	
	//Bus driver functions
	
	//Returns E (End of the day) or W (work)
	public char hasDaysWorkEnded() {
		return 'E';
	}
	
	
	//Will unlock the passengers that are waiting to enter the bus
	//If it has not reached the end of the day it blocks the bus driver until the time quantum terminates or the bus capacity is reached
	public void announcingBusBoarding() {
		announced = true;
		long elapsedTime = 0;
		long start = System.nanoTime();
		//Blocks until reaches 10 passengers or reaches time quantum
		while(this.cntPassengers != 10 || elapsedTime < timequantum) {
			elapsedTime = System.nanoTime() - start;
		}
	}
	
	public void parkTheBus() {
		this.cntPassengers = 0;
	}
}
