package Entities;

import SharedRegions.ArrivalTerminalTransferQuay;
import SharedRegions.DepartureTerminalTransferQuay;
import SharedRegions.Repo;

/**
     * This file implements the BusDriver entity/thread.
     * His life cycle is composed by the following tasks:
     * If he has passengers to be transported
     * he announces the bus boarding,
     * then he drives the passengers to the departure terminal
     * and returns to the arrival terminal 
     * to repeat this until the days of work are ended.
     */

public class BusDriver extends Thread{
    
    /**
     * BusDriver's current state    
    */
    private BusDriverState state;
    
    /**
     * Shared regions   
    */
    private final ArrivalTerminalTransferQuay attq;
    private final DepartureTerminalTransferQuay dttq;

    /**
     * Repository   
    */
    private final Repo repo;


    /**
     * BusDriver instanciation
     * @param s -> state
     * @param attq -> arrival terminal transfer quay
     * @param dttq -> departure terminal transfer quay
     * @param repo -> repository
    */
    public BusDriver(BusDriverState s, ArrivalTerminalTransferQuay attq, DepartureTerminalTransferQuay dttq, Repo repo){
        this.state = s;
        this.attq = attq;
        this.dttq = dttq;
        this.repo = repo;
    }


    /**
     * Sets the BusDriver's state 
     * @param s -> the state to be set
    */
    public void setBusDriverState(BusDriverState s){
        this.state = s;        
    }

    /**
     * Returns the BusDriver's state 
     * @return BusDriver's state
    */
    public BusDriverState getDriverState(){
        return this.state;
    }

    /**
     * BusDriver's life-cycle
    */
    @Override
    public void run() {
    	
    	while(attq.hasDaysWorkEnded() != 'E'){   //while there's no end of the operation
    		attq.announcingBusBoarding();        //If it has passengers to transport gets the passengers, drives them and returns to the ATTQ to park the bus
    		goToDepartureTerminal();
    		dttq.parkTheBusAndLetPassOff();
    		goToArrivalTerminal();
    		attq.parkTheBus();
    	}
    }
    
    /**
     * Sets the BusDriver's state to Driving_Forward,
     * simulating his trip to the Departure Terminal
    */
    public synchronized void goToDepartureTerminal() {
    	setBusDriverState(BusDriverState.DRIVING_FORWARD);
    	repo.setBusDriverState(BusDriverState.DRIVING_FORWARD);
    }
    
    /**
     * Sets the BusDriver's state to Driving_Backward,
     * simulating his return to the Arrival Terminal
    */
    public synchronized void goToArrivalTerminal() {
    	setBusDriverState(BusDriverState.DRIVING_BACKWARD);
    	repo.setBusDriverState(BusDriverState.DRIVING_BACKWARD);
    }

}