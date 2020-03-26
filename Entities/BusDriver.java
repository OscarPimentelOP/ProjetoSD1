package Entities;

import SharedRegions.ArrivalTerminalTransferQuay;
import SharedRegions.DepartureTerminalTransferQuay;
import SharedRegions.Repo;

public class BusDriver extends Thread{

	//Bus driver current state
    private BusDriverState state;
    
    //Shared regions
    private final ArrivalTerminalTransferQuay attq;
    private final DepartureTerminalTransferQuay dttq;
    private final Repo repo;


    public BusDriver(BusDriverState s, ArrivalTerminalTransferQuay attq, DepartureTerminalTransferQuay dttq,
    		Repo repo){
        this.state = s;
        this.attq = attq;
        this.dttq = dttq;
        this.repo = repo;
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
 
    public synchronized void goToDepartureTerminal() {
    	setBusDriverState(BusDriverState.DRIVING_FORWARD);
    	repo.setBusDriverState(BusDriverState.DRIVING_FORWARD);
    }
    
    public synchronized void goToArrivalTerminal() {
    	setBusDriverState(BusDriverState.DRIVING_BACKWARD);
    	repo.setBusDriverState(BusDriverState.DRIVING_BACKWARD);
    }

}