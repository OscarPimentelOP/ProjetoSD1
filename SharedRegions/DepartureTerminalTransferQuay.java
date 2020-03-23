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
	
	//Passengers functions
	
	//The passengers will be blocked
	//When the bus driver parks unblocks the passengers
	public void leaveTheBus() {
		while(!parked) {
			//inserir wait()
		}

		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
		cntPassengersOut++;
		//When the last passenger exits the bus
		if(cntPassengersOut==this.busCapacity) {
			parked = false;
			//notify?
		}
	}


	//Bus driver functions
	
	
	public void parkTheBusAndLetPassOff() {
		BusDriver b = (BusDriver) Thread.currentThread(); 
		b.setBusDriverState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
		parked = true;
		//notifyAll?
		while(cntPassengersOut!=this.busCapacity) {
			//wait;
		}
		cntPassengersOut = 0;
	}
}
