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
	public synchronized void leaveTheBus() {
		Passenger p = (Passenger) Thread.currentThread(); 
		while(!parked) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		try {
			Passenger m = ArrivalTerminalTransferQuay.inTheBus.read();
			repo.setPassangersOnTheBus(this.cntPassengersOut, -1);
			this.cntPassengersOut++;
			ArrivalTerminalTransferQuay.cntPassengersInBus-=1;
		} catch (MemException e) {
			System.out.println(e);
		}
		//When the last passenger exits the bus
		if(ArrivalTerminalTransferQuay.cntPassengersInBus == 0) {
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
		while(ArrivalTerminalTransferQuay.cntPassengersInBus != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cntPassengersOut = 0;
	}
}
