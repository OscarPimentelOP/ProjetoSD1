package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

public class ArrivalTerminalExit {
	
	private Repo repo;
	
	private ArrivalLounge al;
	
	private ArrivalTerminalTransferQuay attq;
	
	protected static int cntPassengersEnd;
	
	protected static int passengersDied;
	
	public ArrivalTerminalExit(ArrivalLounge al,ArrivalTerminalTransferQuay attq,Repo repo) {
		this.repo = repo;
		this.al = al;
		this.attq = attq;
		ArrivalTerminalExit.passengersDied = 0;
		
	}
	
	//Passenger functions
	
	public synchronized void goHome(int flight) {
		Passenger m = (Passenger) Thread.currentThread();
		m.setPassengerState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
		int id = m.getIdentifier();
		repo.setPassengerState(id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
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
