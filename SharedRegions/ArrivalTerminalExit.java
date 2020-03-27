package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

public class ArrivalTerminalExit {
	
	private Repo repo;
	
	private ArrivalLounge al;
	
	private ArrivalTerminalTransferQuay attq;
	
	protected static int cntPassengersEnd;
	
	public ArrivalTerminalExit(ArrivalLounge al,ArrivalTerminalTransferQuay attq,Repo repo) {
		this.repo = repo;
		this.al = al;
		this.attq = attq;
		
	}
	
	//Passenger functions
	
	public synchronized void goHome(int flight) {
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
		cntPassengersEnd++;
		if(flight == SimulatorParam.NUM_FLIGHTS) {
			if(ArrivalTerminalExit.cntPassengersEnd == SimulatorParam.NUM_PASSANGERS) {
				al.setEndOfWork();
				attq.setEndOfWord();
			}
		}
		while(cntPassengersEnd != SimulatorParam.NUM_PASSANGERS) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
