package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;
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
	
	//Queue with the passengers waiting for the bus
	private MemFIFO<Passenger> waitingForBus;
	
	private Repo repo;
	
	//Bus driver waiting for passengers
	private boolean busDriveSleep;
	
	public ArrivalTerminalTransferQuay(Repo repo) {
		this.repo = repo;
		this.busDriveSleep = false;
		this.cntPassengersInBus = 0;
		this.cntPassengersInQueue = 0;
		this.endOfOperations = false;
		this.announced = false;
	}
	
	//Passenger functions
	
	public synchronized void takeABus() {
		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
		try{
			waitingForBus.write(m);
			cntPassengersInQueue++;
			repo.setPassengersOnTheQueue(cntPassengersInQueue, id);
		}
		catch(MemException e) {}
	}
	
	//The passengers are blocked
	//When bus diver announces arrival, it unlocks passengers
	public synchronized void enterTheBus() {
		Passenger p = (Passenger) Thread.currentThread(); 
		int id = p.getIdentifier();
		while (!announced) {
			//wait()?
			try {p.wait();}
			catch(InterruptedException e) {}
		}
		try{
			waitingForBus.read();
			this.cntPassengersInQueue--;
		}
		catch(MemException e) {}
		this.cntPassengersInBus++;
		repo.setPassangersOnTheBus(this.cntPassengersInBus, id);
		p.setPassengerState(PassengerState.TERMINAL_TRANSFER);
		repo.setPassengerState(id, PassengerState.TERMINAL_TRANSFER);
	}
	
	//Bus driver functions
	
	//Returns E (End of the day) or W (work)
	public synchronized char hasDaysWorkEnded() {
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
	public synchronized void announcingBusBoarding() {
		announced = true;
		notifyAll();
		//Blocks until reaches 10 passengers or reaches time quantum
		while(this.cntPassengersInBus != SimulatorParam.BUS_CAPACITY || this.busDriveSleep) {
			//wait()?
			try {
				wait(SimulatorParam.TIMEQUANTUM);
				this.busDriveSleep = true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.busDriveSleep = false;
		this.announced = false;
	}
	
	public synchronized void parkTheBus() {
		this.cntPassengersInBus = 0;
		BusDriver b = (BusDriver) Thread.currentThread(); 
		b.setBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
		repo.setBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
	}
}
