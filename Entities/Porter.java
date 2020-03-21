package Entities;

import AuxTools.Bag;
import SharedRegions.ArrivalLounge;
import SharedRegions.TemporaryStorageArea;
import SharedRegions.BaggageCollectionPoint;

public class Porter extends Thread{
    
	//porter current state
    private PorterState state;
    
    private Bag bag;
    private boolean planeHoldEmpty;
    
    //Shared regions
    private final ArrivalLounge al;
    private final TemporaryStorageArea tsa;
    private final BaggageCollectionPoint bcp;
    
    public Porter(PorterState s, ArrivalLounge al, TemporaryStorageArea tsa, BaggageCollectionPoint bcp){
        this.state = s;
        this.al = al;
        this.tsa = tsa;
        this.bcp = bcp;
    }


    public void setPorterState(PorterState s){
        this.state = s;        
    }

    public PorterState getPorterState(){
        return this.state;
    }
    
    //Porter life-cycle
    @Override
    public void run() {
    	//E = End of the day
    	while(al.takeARest() != 'E') {
    		planeHoldEmpty = false;

    		while(!planeHoldEmpty){
    			bag = al.tryToCollectABag();
    			//Bag storage is empty
    			if(bag == null){
    				planeHoldEmpty=true;
    			}
    			//bag is on transit
    			else if(bag.getDestination() == 'T'){
    				tsa.carryItToAppropriateStore(bag);
    			}
    			else{
    				//bag has as its final destination that airport
    				bcp.carryItToAppropriateStore(bag);
    			}
    			al.noMoreBagsToCollect();		
    		}
    	}
    }
    
}
