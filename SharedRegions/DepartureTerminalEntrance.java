package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

public class DepartureTerminalEntrance {

	
	private Repo repo;
	
	ArrivalTerminalTransferQuay attq;
	
	ArrivalLounge al;
	
	public DepartureTerminalEntrance(ArrivalLounge al, ArrivalTerminalTransferQuay attq, Repo repo) {
		this.repo = repo;
		this.al = al;
		this.attq = attq;
	}
	
	//Passenger functions
	
	public synchronized  void prepareNextLeg(int flight) {
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		ArrivalTerminalExit.cntPassengersEnd++;
		System.out.println("yaaaaaaaaaaa"+Integer.toString(ArrivalTerminalExit.cntPassengersEnd));
		if(ArrivalTerminalExit.cntPassengersEnd == SimulatorParam.NUM_PASSANGERS) {
			notifyAll();
			if(flight+1 == SimulatorParam.NUM_FLIGHTS) {
				al.setEndOfWork();
				attq.setEndOfWord();
			}
		}
		while(ArrivalTerminalExit.cntPassengersEnd != SimulatorParam.NUM_PASSANGERS) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m.setPassengerState(PassengerState.NO_STATE);
		repo.setPassengerState(id, PassengerState.NO_STATE);
		ArrivalTerminalExit.passengersDied++;
		if(ArrivalTerminalExit.passengersDied==SimulatorParam.NUM_PASSANGERS) {
			ArrivalTerminalExit.cntPassengersEnd = 0;
			ArrivalTerminalExit.passengersDied = 0;
		}
		System.out.println("paaaaadadwadwadwad");
	}
}
