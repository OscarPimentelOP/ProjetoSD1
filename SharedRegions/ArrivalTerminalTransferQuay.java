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
	protected static int cntPassengersInBus;
	
	 //Queue with the passengers waiting for the bus
	private MemFIFO<Passenger> waitingForBus;
	
	 //Queue with the passengers in the bus
	protected static MemFIFO<Passenger> inTheBus;
	
	private Repo repo;
	
	//Bus driver waiting for passengers
	private boolean busDriveSleep;
	
	public ArrivalTerminalTransferQuay(Repo repo) {
		this.repo = repo;
		this.busDriveSleep = true;
		ArrivalTerminalTransferQuay.cntPassengersInBus = 0;
		this.cntPassengersInQueue = 0;
		this.endOfOperations = false;
		this.announced = false;
		try {
			waitingForBus = new MemFIFO<Passenger>(new Passenger [SimulatorParam.NUM_PASSANGERS]);
		}catch(MemException e) {}

		try {
			inTheBus = new MemFIFO<Passenger>(new Passenger [SimulatorParam.BUS_CAPACITY]);
		}catch(MemException e) {}
	}
	
	//Passenger functions
	
	public synchronized void takeABus() {
		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
		try{
			this.waitingForBus.write(m);
			repo.setPassengersOnTheQueue(cntPassengersInQueue, id);
			System.out.println("taking the buuuuuuuuuuuus");
			cntPassengersInQueue++;
			if(!announced) {
				System.out.println("NOOOOOOOOTIFY");
				notifyAll();
			}
		}
		catch(MemException e) {System.out.println(e);}
	}
	
	//The passengers are blocked
	//When bus diver announces arrival, it unlocks passengers
	public synchronized void enterTheBus() {
		Passenger p = (Passenger) Thread.currentThread(); 
		int id = p.getIdentifier();
		while (!announced || ArrivalTerminalTransferQuay.cntPassengersInBus==SimulatorParam.BUS_CAPACITY) {
			try {wait();}
			catch(InterruptedException e) {}
		}
		try{
			System.out.println("enteeeeeeeeeering the bus");
			this.waitingForBus.read();
			this.cntPassengersInQueue--;
			ArrivalTerminalTransferQuay.inTheBus.write(p);
			repo.setPassengersOnTheQueue(this.cntPassengersInQueue, -1);
			repo.setPassangersOnTheBus(ArrivalTerminalTransferQuay.cntPassengersInBus, id);
			ArrivalTerminalTransferQuay.cntPassengersInBus++;
		}
		catch(MemException e) {}
		if(!this.busDriveSleep)
			notifyAll();
		else if(ArrivalTerminalTransferQuay.cntPassengersInBus==SimulatorParam.BUS_CAPACITY) {
			notifyAll();
		}
		p.setPassengerState(PassengerState.TERMINAL_TRANSFER);
		repo.setPassengerState(id, PassengerState.TERMINAL_TRANSFER);
		
	}
	
	//Bus driver functions
	
	//Returns E (End of the day) or W (work)
	public synchronized char hasDaysWorkEnded() {
		while(!this.endOfOperations && cntPassengersInQueue==0) {
			System.out.println("SLEEEEEEEEEEEPING");
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(this.endOfOperations){
			return 'E';
		}
		else return 'W';
	}
	
	
	//Will unlock the passengers that are waiting to enter the bus
	//If it has not reached the end of the day it blocks the bus driver until the time quantum terminates or the bus capacity is reached
	public synchronized void announcingBusBoarding() {
		announced = true;
		notifyAll();
		//Blocks until reaches 10 passengers or reaches time quantum
		while(this.busDriveSleep) {
			try {
				wait(SimulatorParam.TIMEQUANTUM);
				this.busDriveSleep = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(ArrivalTerminalTransferQuay.cntPassengersInBus<1) {
			try {
				System.out.println("WAAAAAAAAAITING");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.busDriveSleep = true;
		this.announced = false;
	}
	
	public synchronized void parkTheBus() {
		//this.cntPassengersInBus = 0;
		BusDriver b = (BusDriver) Thread.currentThread(); 
		b.setBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
		repo.setBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
	}
	
	public synchronized void setEndOfWord() {
		this.endOfOperations = true;
		notifyAll();
	}
}
