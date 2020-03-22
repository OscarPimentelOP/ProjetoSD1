package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;

public class ArrivalTerminalExit {
	
	//Passenger functions
	
	public void goHome(int flight) {
		Passenger m = (Passenger) Thread.currentThread(); 
		m.setPassengerState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
	}
}
