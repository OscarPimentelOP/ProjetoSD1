package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

public class ArrivalTerminalExit {
	
	private Repo repo;
	
	private ArrivalLounge al;
	
	private ArrivalTerminalTransferQuay attq;
	
	private DepartureTerminalEntrance dte;
	
	protected static int cntPassengersEnd;
	
	private boolean timeToWakeUp;
	
	public ArrivalTerminalExit(ArrivalLounge al,ArrivalTerminalTransferQuay attq,Repo repo) {
		this.repo = repo;
		this.al = al;
		this.attq = attq;
		this.timeToWakeUp = false;
		ArrivalTerminalExit.cntPassengersEnd = 0;
	}
	
	//Passenger functions
	
	public synchronized void goHome(int flight) {
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
		ArrivalTerminalExit.cntPassengersEnd++;
		if(ArrivalTerminalExit.cntPassengersEnd == SimulatorParam.NUM_PASSANGERS) {
			this.timeToWakeUp = true;
			notifyAll();
			dte.wakeUpAll();
			if(flight+1 == SimulatorParam.NUM_FLIGHTS) {
				al.setEndOfWork();
				attq.setEndOfWord();
			}
		}
		while(!this.timeToWakeUp) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrivalTerminalExit.cntPassengersEnd -= 1;
		if(ArrivalTerminalExit.cntPassengersEnd == 0) {
			this.timeToWakeUp = false;
			dte.setTimeToWakeUpToFalse();
		}
		//Waiting for porter and bus driver to fall asleep before changing the passenger state to NO_STATE
		try {
			wait(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.setPassengerState(PassengerState.NO_STATE);
		repo.setPassengerState(id, PassengerState.NO_STATE);
	}
	
	public synchronized void setDepartureEntrance(DepartureTerminalEntrance dte) {
		this.dte = dte;
	}
	
	public synchronized void wakeUpAll() {
		this.timeToWakeUp = true;
		notifyAll();
	}
	
	public synchronized void setTimeToWakeUpToFalse() {
		this.timeToWakeUp = false;
	}
}
