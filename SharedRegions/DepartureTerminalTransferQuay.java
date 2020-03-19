package SharedRegions;

public class DepartureTerminalTransferQuay {
	//Variable that will unblock the passengers when the bus driver parks the bus
	boolean parked;
	
	//Passengers functions
	
	//The passengers will be blocked
	//When the bus driver parks unblocks the passengers
	public void leaveTheBus() {
		while(!parked);
	}


	//Bus driver functions
	
	
	public void parkTheBusAndLetPassOff() {
		parked = true;
	}
}
