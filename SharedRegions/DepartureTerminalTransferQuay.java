package SharedRegions;

import Entities.BusDriver;
import Entities.BusDriverState;
import Entities.Passenger;
import Entities.PassengerState;

public class DepartureTerminalTransferQuay {
	//Variable that will unblock the passengers when the bus driver parks the bus
	private boolean parked;
	
	//Count of passengers that leaved the bus
	private int cntPassengersOut;
	
	private Repo repo;
	
	private ArrivalTerminalTransferQuay attq;
	
	public DepartureTerminalTransferQuay(Repo repo) {
		this.repo = repo;
		this.parked = false;
		this.cntPassengersOut = 0;
	}
	
	//Passengers functions
	
	//The passengers will be blocked
	//When the bus driver parks unblocks the passengers
	public synchronized void leaveTheBus() {
		Passenger p = (Passenger) Thread.currentThread(); 
		while(!this.getParked()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		attq.readFromBus();
		repo.setPassangersOnTheBus(this.getCntPassengersOut(), -1);
		this.incCntPassengersOut();
		attq.decCntPassengersInBus();
		//When the last passenger exits the bus
		if(attq.getCntPassengersInBus() == 0) {
			parked = false;
			notifyAll();
		}
		p.setPassengerState(PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
		int id = p.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
	}


	//Bus driver functions
	
	
	public synchronized void parkTheBusAndLetPassOff() {
		BusDriver b = (BusDriver) Thread.currentThread(); 
		b.setBusDriverState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
		repo.setBusDriverState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
		parked = true;
		notifyAll();
		while(attq.getCntPassengersInBus() != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.setCntPassengersOut();
	}
	
	public synchronized void incCntPassengersOut() {
		this.cntPassengersOut++;
	}
	
	public synchronized void setCntPassengersOut() {
		this.cntPassengersOut=0;
	}
	
	public synchronized int getCntPassengersOut() {
		return this.cntPassengersOut;
	}
	
	public synchronized void setArrivalTerminalTransferQuay(ArrivalTerminalTransferQuay attq) {
		this.attq = attq;
	}
	
	public synchronized boolean getParked() {
		return this.parked;
	}
}
