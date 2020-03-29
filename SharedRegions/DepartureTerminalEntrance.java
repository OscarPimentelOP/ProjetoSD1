package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

public class DepartureTerminalEntrance {

	
	private Repo repo;
	
	private ArrivalTerminalTransferQuay attq;
	
	private ArrivalTerminalExit ate;
	
	private ArrivalLounge al;
	
	private boolean timeToWakeUp;
	
	public DepartureTerminalEntrance(ArrivalLounge al, ArrivalTerminalTransferQuay attq, Repo repo) {
		this.repo = repo;
		this.al = al;
		this.attq = attq;
		this.timeToWakeUp = false;
	}
	
	//Passenger functions
	
	public synchronized void prepareNextLeg(int flight){
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		ate.incCntPassengersEnd();
		if(ate.getCntPassengersEnd() == SimulatorParam.NUM_PASSANGERS) {
			this.timeToWakeUp = true;
			notifyAll();
			ate.wakeUpAll();
			if(flight+1 == SimulatorParam.NUM_FLIGHTS) {
				al.setEndOfWork();
				attq.setEndOfWord();
			}
		}
		while(!this.timeToWakeUp) {
			try {
				wait();
			} catch (InterruptedException e) {
				
			}
		}
		ate.decCntPassengersEnd();
		if(ate.getCntPassengersEnd() == 0) {
			this.timeToWakeUp = false;
			ate.setTimeToWakeUpToFalse();
		}
		//Waiting for porter and bus driver to fall asleep before changing the passenger state to NO_STATE
		/*try {
			wait(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		m.setPassengerState(PassengerState.NO_STATE);
		repo.setPassengerState(id, PassengerState.NO_STATE);
	}
	
	public synchronized void setArrivalExit(ArrivalTerminalExit ate) {
		this.ate = ate;
	}
	
	public synchronized void wakeUpAll() {
		this.timeToWakeUp = true;
		notifyAll();
	}
	
	public synchronized void setTimeToWakeUpToFalse() {
		this.timeToWakeUp = false;
	}
}
