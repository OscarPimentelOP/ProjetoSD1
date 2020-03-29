package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

/**
     * This class implements the Departure Terminal Entrance shared region.
	 * The passengers can go prepare their next leg if they are in transit.
*/

public class DepartureTerminalEntrance {

	/**
     * The repository, to store the program status
	*/
	private Repo repo;
	
	/**
     * Arrival terminal transfer quay shared region
	*/
	private ArrivalTerminalTransferQuay attq;
	
	/**
     * Arrival terminal exit shared region
	*/
	private ArrivalTerminalExit ate;
	
	/**
     * Arrival lounge shared region
	*/
	private ArrivalLounge al;
	
	/**
     * ???
	*/
	private boolean timeToWakeUp;
	
	/**
     * Departure Terminal Exit's instantiation
     * @param al -> arrival lounge 
     * @param attq -> arrival terminal transfer quay
     * @param repo -> repository of information
    */
	public DepartureTerminalEntrance(ArrivalLounge al, ArrivalTerminalTransferQuay attq, Repo repo) {
		this.repo = repo;
		this.al = al;
		this.attq = attq;
		this.timeToWakeUp = false;
	}
	
	//Passenger functions
	
	/**
     * If the passenger is in transit, it must prepare his next leg.
	 * This changes the state to entering departure terminal.
	 * He waits until every other passenger arrives at the end.
	 * @param flight -> the flight number
	*/
	public synchronized void prepareNextLeg(int flight){
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		ate.incCntPassengersEnd();
		if(ate.getCntPassengersEnd() == SimulatorParam.NUM_PASSANGERS) {
			ate.wakeUpAll();
			this.wakeUpAll();
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
			if(flight+1 == SimulatorParam.NUM_FLIGHTS) {
				al.setEndOfWork();
				attq.setEndOfWord();
			}
		}
		//Waiting for porter and bus driver to fall asleep before changing the passenger state to NO_STATE
		try {
			wait(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.setPassengerState(PassengerState.NO_STATE);
		repo.setPassengerState(id, PassengerState.NO_STATE);
	}
	
	/**
     * Sets the arrival terminal exit shared region
	 * @param ate -> the arrival terminal to set
	*/
	public synchronized void setArrivalExit(ArrivalTerminalExit ate) {
		this.ate = ate;
	}
	
	/**
     * Wakes up all the passengers of the terminal
	*/
	public synchronized void wakeUpAll() {
		this.timeToWakeUp = true;
		try {
			wait(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyAll();
	}
	
	/**
     * Marks the end of the flight, so then all the passengers are blocked for the next flight
	*/
	public synchronized void setTimeToWakeUpToFalse() {
		this.timeToWakeUp = false;
	}
}
