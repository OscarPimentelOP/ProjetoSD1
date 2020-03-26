package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;

public class DepartureTerminalEntrance {

	
	private Repo repo;
	
	public DepartureTerminalEntrance(Repo repo) {
		this.repo = repo;
	}
	
	//Passenger functions
	
	public void prepareNextLeg(int flight) {
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
	}
}
