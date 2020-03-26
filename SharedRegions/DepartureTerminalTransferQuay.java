package SharedRegions;

import Entities.BusDriver;
import Entities.BusDriverState;
import Entities.Passenger;
import Entities.PassengerState;

public class DepartureTerminalTransferQuay {
	//Variable that will unblock the passengers when the bus driver parks the bus
	private boolean parked = false;
	
	//Contagem dos passegeiros que sairam do autocarro
	private int cntPassengersOut;
	
	//Bus capacity
	private final int busCapacity = 10;
	
	private Repo repo;
	
	public DepartureTerminalTransferQuay(Repo repo) {
		this.repo = repo;
	}
	
	//Passengers functions
	
	//The passengers will be blocked
	//When the bus driver parks unblocks the passengers
	public void leaveTheBus() {
		Passenger p = (Passenger) Thread.currentThread(); 
		while(!parked) {
			//wait()?
			try {
				p.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		p.setPassengerState(PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
		int id = p.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
		//When the last passenger exits the bus
		repo.
			parked = false;
			notifyAll();
		}
	}


	//Bus driver functions
	
	
	public void parkTheBusAndLetPassOff() {
		BusDriver b = (BusDriver) Thread.currentThread(); 
		b.setBusDriverState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
		parked = true;
		notifyAll();
		while(cntPassengersOut!=this.busCapacity) {
			//wait;
		}
		cntPassengersOut = 0;
	}
}
