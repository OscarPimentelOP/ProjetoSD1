package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

public class DepartureTerminalEntrance {

	
	private Repo repo;
	
	private ArrivalTerminalTransferQuay attq;
	
	private ArrivalTerminalExit ate;
	
	private ArrivalLounge al;
	
	//private int cntPassengersEnd;
	
	private boolean timeToWakeUp;
	
	public DepartureTerminalEntrance(ArrivalLounge al, ArrivalTerminalTransferQuay attq, Repo repo) {
		this.repo = repo;
		this.al = al;
		this.attq = attq;
		this.timeToWakeUp = false;
	}
	
	//Passenger functions
	
	public synchronized  void prepareNextLeg(int flight){
		System.out.println("LEEEEEEEEEEG");
		Passenger m = (Passenger) Thread.currentThread();
		System.out.println("AQQQQQQQQQQQQUI");
		m.setPassengerState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
		ArrivalTerminalExit.cntPassengersEnd++;
		System.out.println("yoooooooooooo"+Integer.toString(ArrivalTerminalExit.cntPassengersEnd));
		if(ArrivalTerminalExit.cntPassengersEnd == SimulatorParam.NUM_PASSANGERS) {
			System.out.println("abcdefghij2");
			this.timeToWakeUp = true;
			notifyAll();
			ate.wakeUpAll();
			if(flight+1 == SimulatorParam.NUM_FLIGHTS) {
				al.setEndOfWork();
				attq.setEndOfWord();
			}
		}
		System.out.println("truefalseeeeeeeeeee2");
		while(!this.timeToWakeUp) {
			try {
				System.out.println("qqqqqqqqqq2");
				wait();
			} catch (InterruptedException e) {
				
			}
		}
		m.setPassengerState(PassengerState.NO_STATE);
		repo.setPassengerState(id, PassengerState.NO_STATE);
		ArrivalTerminalExit.cntPassengersEnd -= 1;
		if(ArrivalTerminalExit.cntPassengersEnd == 0) {
			this.timeToWakeUp = false;
		}
		System.out.println("paaaaadadwadwadwad"+Integer.toString(id));
	}
	
	public synchronized void setArrivalExit(ArrivalTerminalExit ate) {
		this.ate = ate;
	}
	
	public synchronized void wakeUpAll() {
		this.timeToWakeUp = true;
		notifyAll();
	}
}
