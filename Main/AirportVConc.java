package Main;

import SharedRegions.*;
import Entities.*;

import java.io.FileNotFoundException;
import java.util.Random;
import AuxTools.*;

/**
     * AirportVConc stands for Airport Concurrent Version
	 * and it is the main program where the threads start and flow.
*/

public class AirportVConc {
	

	/**
		 * AirportVConc main's thread
		 @throws FileNotFoundException when the file isn't found
	*/
	public static void main(String[] args) throws FileNotFoundException {
		
		Random rand = new Random();
		
		
		/**
     	* Number of bags per passenger and flight
    	*/
		int numBags[][] = new int[SimulatorParam.NUM_PASSANGERS][SimulatorParam.NUM_FLIGHTS];

		
		/**
     	* Trip state per passenger and flight
    	*/
		char tripState[] []= new char[SimulatorParam.NUM_PASSANGERS][SimulatorParam.NUM_FLIGHTS];

		
		/**
     	* Number of bags that have been lost per passenger and flight
    	*/
		int numBagsLost[][] =  new int[SimulatorParam.NUM_PASSANGERS][SimulatorParam.NUM_FLIGHTS];
		
		for(int i=0;i<SimulatorParam.NUM_PASSANGERS;i++) {
			
			//Initialize the number of bags per passenger and flight with probabilities
			for(int b=0;b<SimulatorParam.NUM_FLIGHTS;b++) {
				int randint = rand.nextInt(101);
				if(randint <= SimulatorParam.PROB_OF_0_BAGS) {
					numBags[i][b] = 0;
				}
				else if(randint>SimulatorParam.PROB_OF_2_BAGS) {
					numBags[i][b] = 2;
				}
				else {
					numBags[i][b] = 1;
				}
			}
			/**
			 * Initialize the number of bags that have been lost 
				per passenger and flight with probabilities
			*/
			for(int b=0;b<SimulatorParam.NUM_FLIGHTS;b++) {
				int randint = rand.nextInt(101);
				if(randint <= SimulatorParam.PROB_LOSE_0_BAGS) {
					numBagsLost[i][b] = 0;
				}
				else if(randint>SimulatorParam.PROB_LOSE_2_BAGS) {
					numBagsLost[i][b] = 2;
				}
				else {
					numBagsLost[i][b] = 1;
				}
			}
		
			
			/*
			Initialize the trip state per passenger and flight
			(final destination-> F, transit -> T)
			*/
			for(int t=0;t<SimulatorParam.NUM_FLIGHTS;t++) {
				int randint = rand.nextInt(2);
				if(randint == 0) {
					tripState[i][t] = 'T';
				}
				else {
					tripState[i][t] = 'F';
				}
			}
		}
		

		//Number of bags that are found (numBags - numBagsLost)
		int numBagsFound[][] = new int[SimulatorParam.NUM_PASSANGERS][SimulatorParam.NUM_FLIGHTS];
		//Total number of bags in the storage per flight
		int totalNumOfBags[]= new int[SimulatorParam.NUM_FLIGHTS];
		//Initialize number of bags that are found
		for(int i=0;i<SimulatorParam.NUM_PASSANGERS;i++) {
			for(int b=0;b<SimulatorParam.NUM_FLIGHTS;b++) {
				numBagsFound[i][b] = numBags[i][b] - numBagsLost[i][b];
				if(numBagsFound[i][b]<0) numBagsFound[i][b]=0;
				totalNumOfBags[b] += numBagsFound[i][b];
			}
		}
		
		
		//Array of stacks that represent the storage of bags per flight
		MemStack[] sBags = new MemStack[SimulatorParam.NUM_FLIGHTS];
		int bagId=0;
		for(int b=0;b<SimulatorParam.NUM_FLIGHTS;b++) {
			//Bags in the storage to pass to the arrival lounge per flight
			Bag bagsToArrivalLounge[] = new Bag[totalNumOfBags[b]];
			try {
				sBags[b] = new MemStack<Bag>(bagsToArrivalLounge);
				for(int i=0;i<SimulatorParam.NUM_PASSANGERS;i++) {
					for(int j=0;j<numBagsFound[i][b];j++) {
						sBags[b].write(new Bag(bagId,i,tripState[i][0]));
						bagId++;
					}
				}
				} catch (MemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
 		
		//Initialize SharedRegions
		Repo repo = new Repo();
		BaggageCollectionPoint bcp = new BaggageCollectionPoint(repo);
		BaggageReclaimOffice bro = new BaggageReclaimOffice(repo);
		TemporaryStorageArea tsa = new TemporaryStorageArea(repo);
		ArrivalLounge al = new ArrivalLounge(sBags,totalNumOfBags,tripState,bcp,repo);
		ArrivalTerminalTransferQuay attq = new ArrivalTerminalTransferQuay(repo);
		DepartureTerminalTransferQuay dttq = new DepartureTerminalTransferQuay(repo);
		DepartureTerminalEntrance dte = new DepartureTerminalEntrance(al, attq, repo);
		ArrivalTerminalExit ate = new ArrivalTerminalExit(al, attq, repo);
		dte.setArrivalExit(ate);
		ate.setDepartureEntrance(dte);
		dttq.setArrivalTerminalTransferQuay(attq);
		
		//Initialize Entities
		BusDriver busdriver = new BusDriver(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL,
				attq, dttq, repo);
		Porter porter = new Porter(PorterState.WAITING_FOR_A_PLANE_TO_LAND, al, tsa, bcp);
		Passenger passengers[] = new Passenger[SimulatorParam.NUM_PASSANGERS];
		for(int i=0;i<passengers.length;i++) {
			passengers[i] = new Passenger(PassengerState.AT_THE_DISEMBARKING_ZONE, i, numBags[i],
					tripState[i], al, ate, attq, dttq, dte, bro, bcp);
		}
		
		
		//Initialize simulation
		busdriver.start();
		porter.start();
		for(Passenger p : passengers) {
			p.start();
		}
		
		//End of the simulation: waiting for the threads to end
		try {
			busdriver.join();
		}
		catch(InterruptedException e) {}
		try {
			porter.join();
		}
		catch(InterruptedException e) {}
		for(Passenger p : passengers) {
			try {
				p.join();
			}
			catch(InterruptedException e) {}
		}
		
		repo.reportFinalStatus();
	
	}
}
