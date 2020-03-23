package Entities;

import SharedRegions.ArrivalTerminalTransferQuay;
import SharedRegions.DepartureTerminalTransferQuay;

public class BusDriver extends Thread{

	//Bus driver current state
    private BusDriverState state;
    
    //Shared regions
    private final ArrivalTerminalTransferQuay attq;
    private final DepartureTerminalTransferQuay dttq;


    public BusDriver(BusDriverState s, ArrivalTerminalTransferQuay attq, DepartureTerminalTransferQuay dttq){
        this.state = s;
        this.attq = attq;
        this.dttq = dttq;
    }


    public void setBusDriverState(BusDriverState s){
        this.state = s;        
    }

    public BusDriverState getDriverState(){
        return this.state;
    }

    //Bus driver life-cycle
    @Override
    public void run() {
    	//End of the operation
    	while(attq.hasDaysWorkEnded() != 'E'){
    		//If it has passengers to transport
    		attq.announcingBusBoarding();
    		goToDepartureTerminal();
    		dttq.parkTheBusAndLetPassOff();
    		goToArrivalTerminal();
    		attq.parkTheBus();
    	}
    }
    
    
    
    public void goToDepartureTerminal() {
    	setBusDriverState(BusDriverState.DRIVING_FORWARD);
    }
    
    public void goToArrivalTerminal() {
    	setBusDriverState(BusDriverState.DRIVING_BACKWARD);
    }

}