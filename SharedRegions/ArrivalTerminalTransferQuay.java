package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Entities.BusDriver;
import Entities.BusDriverState;
import AuxTools.MemFIFO;
import AuxTools.MemException;

public class ArrivalTerminalTransferQuay {
	
	//Variable the warns the passengers the they can enter on the bus
	private boolean announced;
	
	//Variable that warns the bus driver that he can go rest
	//Last passenger of the last flight accuses in goHome function or in prepareNextLeg function
	private boolean endOfOperations;
	
	//Count of the number of passengers waiting to enter the bus
	private int cntPassengersInQueue;
	
	//Count of the number of passengers that entered in the bus	
	private int cntPassengersInBus;
	
	//TODO
	private final int timequantum = 10;
	
	//TODO
	private final int busCapacity = 10;
	
	private MemFIFO<Passenger> waitingForBus;
	
	//Passenger functions
	
	public void takeABus() {

		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
		try{waitingForBus.write(m);}
		catch(MemException e) {}
		cntPassengersInQueue++;
	}
	
	//The passengers are blocked
	//When bus diver announces arrival, it unlocks passengers
	public void enterTheBus() {
		while (!announced) {
			//wait();
		}
		try{waitingForBus.read();}
		catch(MemException e) {}
		this.cntPassengersInBus++;
		this.cntPassengersInQueue--;
		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.TERMINAL_TRANSFER);
	}
	
	
	public void setEndOfWork() {
		
	}
	
	
	//Bus driver functions
	
	//Returns E (End of the day) or W (work)
	public char hasDaysWorkEnded() {
		boolean workEnded = false;
		
		//if(last passenger of last flight leaves the bus){
			//return 'E';
			//workEnded = true;
		//}
		//else return 'E';
		return ' ';

		
	}
	
	
	//Will unlock the passengers that are waiting to enter the bus
	//If it has not reached the end of the day it blocks the bus driver until the time quantum terminates or the bus capacity is reached
	public void announcingBusBoarding() {
		announced = true;
		long elapsedTime = 0;
		long start = System.nanoTime();
		//Blocks until reaches 10 passengers or reaches time quantum
		while(this.cntPassengersInBus != busCapacity || elapsedTime < timequantum) {
			elapsedTime = System.nanoTime() - start;
			//wait()?
		}
		announced = false;
	}
	
	public void parkTheBus() {
		this.cntPassengersInBus = 0;
		BusDriver b = (BusDriver) Thread.currentThread(); 
		b.setBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
	}
}
