package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

/**
     * This class implements the Arrival Terminal exit shared region.
*/

public class ArrivalTerminalExit {
	
	/**
     * The repository, to store the program status
	*/
	private Repo repo;
	
	/**
     * Arrival lounge shared region
	*/
	private ArrivalLounge al;
	
	/**
     * Arrival terminal transfer quay shared region
	*/
	private ArrivalTerminalTransferQuay attq;
	
	/**
     * Departure terminal entrance shared region
	*/
	private DepartureTerminalEntrance dte;
	
	/**
     * passengers count at the end (???)
	*/
	private int cntPassengersEnd;
	
	/**
     *  (???)
	*/
	private boolean timeToWakeUp;
	

	/**
     * Arrival Terminal Exit's instantiation
     * @param al -> arrival lounge 
     * @param attq -> arrival terminal transfer quay
     * @param repo -> repository of information
    */
	public ArrivalTerminalExit(ArrivalLounge al,ArrivalTerminalTransferQuay attq,Repo repo) {
		this.repo = repo;
		this.al = al;
		this.attq = attq;
		this.timeToWakeUp = false;
		this.cntPassengersEnd = 0;
	}
	
	//Passenger functions
	/**
     * The passenger exits the arrival terminal and goes home.
	 * @param flight -> the flight number
	*/
	public synchronized void goHome(int flight) {
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
		incCntPassengersEnd();
		if(getCntPassengersEnd() == SimulatorParam.NUM_PASSANGERS) {
			dte.wakeUpAll();
			this.wakeUpAll();
		}
		while(!this.timeToWakeUp) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		decCntPassengersEnd();
		if(getCntPassengersEnd() == 0) {
			this.timeToWakeUp = false;
			dte.setTimeToWakeUpToFalse();
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
     * Sets the departure terminal entrance
	 * @param dte -> departure terminal entrance to be set
	*/
	public synchronized void setDepartureEntrance(DepartureTerminalEntrance dte) {
		this.dte = dte;
	}
	
	/**
     * ???
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
     * ???
	*/
	public synchronized void setTimeToWakeUpToFalse() {
		this.timeToWakeUp = false;
	}
	
	public synchronized int getCntPassengersEnd() {
		return this.cntPassengersEnd;
	}
	
	public synchronized void incCntPassengersEnd() {
		this.cntPassengersEnd = this.cntPassengersEnd + 1;
	}
	
	public synchronized void decCntPassengersEnd() {
		this.cntPassengersEnd = this.cntPassengersEnd - 1;
	}
}
