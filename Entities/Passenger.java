package Entities;

import SharedRegions.ArrivalLounge;
import SharedRegions.ArrivalTerminalExit;
import SharedRegions.ArrivalTerminalTransferQuay;
import SharedRegions.DepartureTerminalTransferQuay;
import SharedRegions.DepartureTerminalEntrance;
import SharedRegions.BaggageReclaimOffice;
import SharedRegions.BaggageCollectionPoint;

public class Passenger extends Thread{

	//Passenger current state
    private PassengerState state;
    
    // id from the passenger
    private int identifier;
  
    //number of bags the passenger has per flight
    private int numBags[];   
    
    //the state of the trip of the passenger (final destination-> F, transit -> T)
    private char tripState[]; 

    private final ArrivalLounge al;
    private final ArrivalTerminalExit ate;
    private final ArrivalTerminalTransferQuay attq;
    private final DepartureTerminalTransferQuay dttq;
    private final DepartureTerminalEntrance dte;
    private final BaggageReclaimOffice bro;
    private final BaggageCollectionPoint bcp;
    
    
    public Passenger(PassengerState s, int id, int[] nb, char[] ts,
    		 ArrivalLounge al, ArrivalTerminalExit ate, ArrivalTerminalTransferQuay attq,
    		 DepartureTerminalTransferQuay dttq, DepartureTerminalEntrance dte,
    		 BaggageReclaimOffice bro, BaggageCollectionPoint bcp){
        this.state= s;
        this.identifier = id;
        this.numBags = nb;
        this.tripState = ts;
        this.al = al;
        this.ate = ate;
        this.attq = attq;
        this.dttq = dttq;
        this.dte = dte;
        this.bro = bro;
        this.bcp = bcp;
    }


    public void setPassengerState(PassengerState s){
    	this.state = s;        
    }

    public PassengerState getPassengerState(){
        return this.state;
    }

    public int getIdentifier(){
        return this.identifier;
    }

    public int getNumBags(int voo){
        return this.numBags[voo];
    }    

    public char getTripState(int voo){
        return this.tripState[voo];
    }

    //Passenger life-cycle
    @Override
    public void run() {
    	for (int flight=1;flight<=5;flight++){
    		char a = al.whatShouldIDo(flight);

    		switch(a){
    			//Reached final destiny, has no bag to collect, goes home
    			case 'H': ate.goHome(flight);
    			
    			//Goes to the bus
    			case 'B': attq.takeABus();
    				attq.enterTheBus();
    				dttq.leaveTheBus();
    				dte.prepareNextLeg(flight);
    				
    			//Get bag(s)	
    			case 'R':
    				int numOfCollectedBags = 0;
    				while(numOfCollectedBags != numBags[flight]){
    					//Collect a bag
    					if(bcp.goCollectABag()){
    						numOfCollectedBags+=1;
    					//Reports missing bags
    					}else { bro.reportMissingBags(numBags[flight]-numOfCollectedBags);
    						break;
    					}
    				}
    				//Goes Home
    				ate.goHome(flight);
    		}
    	}
    }
}












