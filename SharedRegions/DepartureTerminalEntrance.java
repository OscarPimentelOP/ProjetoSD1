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
			System.out.println("aaaaaaaaaaaaaa1");
			this.timeToWakeUp = true;
			System.out.println("bbbbbbbbbbbbbb1");
			notifyAll();
			System.out.println("cccccccccccccc1");
			ate.wakeUpAll();
			System.out.println("dddddddddddddd1");
			if(flight+1 == SimulatorParam.NUM_FLIGHTS) {
				System.out.println("AVIAOOOOOOOOOOOOOOO1");
				al.setEndOfWork();
				attq.setEndOfWord();
			}
		}
		System.out.println(!this.timeToWakeUp);
		while(!this.timeToWakeUp) {
			try {
				wait();
			} catch (InterruptedException e) {
				
			}
		}
		ate.decCntPassengersEnd();
		if(ate.getCntPassengersEnd() == 0) {
			System.out.println("Saiuuuuuuuuuuuuuuuu1");
			this.timeToWakeUp = false;
			ate.setTimeToWakeUpToFalse();
		}
		//Waiting for porter and bus driver to fall asleep before changing the passenger state to NO_STATE
		/*try {
			wait(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		m.setPassengerState(PassengerState.NO_STATE);
		repo.setPassengerState(id, PassengerState.NO_STATE);
		System.out.println("bwawawa");
	}
	
	public synchronized void setArrivalExit(ArrivalTerminalExit ate) {
		this.ate = ate;
	}
	
	public synchronized void wakeUpAll() {
		System.out.println("eeeeeeeeeeeeeeee1");
		this.timeToWakeUp = true;
		System.out.println("ffffffffffffffff1");
		notifyAll();
		System.out.println("gggggggggggggggg1");
	}
	
	public synchronized void setTimeToWakeUpToFalse() {
		this.timeToWakeUp = false;
	}
}
