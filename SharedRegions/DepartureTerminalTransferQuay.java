package SharedRegions;

import Entities.Passenger;
import Entities.PassengerState;

public class DepartureTerminalTransferQuay {
	//Variable that will unblock the passengers when the bus driver parks the bus
	boolean parked = false;
	
	//Contagem dos passegeiros que sairam do autocarro
	int cntPassengersOut;
	
	//Passengers functions
	
	//The passengers will be blocked
	//When the bus driver parks unblocks the passengers
	public void leaveTheBus() {
		while(!parked) {
			Passenger m = (Passenger) Thread.currentThread(); 
			m.setPassengerState(PassengerState.TERMINAL_TRANSFER);
			//inserir wait()
		}
		cntPassengersOut++;
		//When the last passenger exits the bus
		//NÃO SEI VER QUANTAS PESSOAS LEVA O AUTOCARRO ://
		if(cntPassengersOut==6) {
			parked = false;
			cntPassengersOut = 0;
		}
	}


	//Bus driver functions
	
	
	public void parkTheBusAndLetPassOff() {
		parked = true;
	}
}
