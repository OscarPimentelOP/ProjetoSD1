package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;

public class ArrivalTerminalExit {
	
	private Repo repo;
	
	public ArrivalTerminalExit(Repo repo) {
		this.repo = repo;
	}
	
	//Passenger functions
	
	public void goHome(int flight) {
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
	}
}
