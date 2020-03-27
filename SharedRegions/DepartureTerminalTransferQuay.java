package SharedRegions;

import Entities.BusDriver;
import Entities.BusDriverState;
import Entities.Passenger;
import Entities.PassengerState;
import AuxTools.MemException;

public class DepartureTerminalTransferQuay {
	//Variable that will unblock the passengers when the bus driver parks the bus
	private boolean parked;
	
	//Count of passengers that leaved the bus
	private int cntPassengersOut;
	
	private Repo repo;
	
	public DepartureTerminalTransferQuay(Repo repo) {
		this.repo = repo;
		this.parked = false;
		this.cntPassengersOut = 0;
	}
	
	//Passengers functions
	
	//The passengers will be blocked
	//When the bus driver parks unblocks the passengers
	public void leaveTheBus() {
		Passenger p = (Passenger) Thread.currentThread(); 
		while(!parked) {
			//wait()?
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		p.setPassengerState(PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
		int id = p.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
		while(this.cntPassengersOut < ArrivalTerminalTransferQuay.cntPassengersInBus) {
			try {
				ArrivalTerminalTransferQuay.waitingForBus.read();
				repo.setPassangersOnTheBus(this.cntPassengersOut, 0);
				this.cntPassengersOut++;
			} catch (MemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//When the last passenger exits the bus
		if(this.cntPassengersOut == ArrivalTerminalTransferQuay.cntPassengersInBus) {
			parked = false;
			notifyAll();
			ArrivalTerminalTransferQuay.cntPassengersInBus = 0;
		}
	}


	//Bus driver functions
	
	
	public void parkTheBusAndLetPassOff() {
		BusDriver b = (BusDriver) Thread.currentThread(); 
		b.setBusDriverState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
		parked = true;
		notifyAll();
		while(cntPassengersOut < ArrivalTerminalTransferQuay.cntPassengersInBus) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
