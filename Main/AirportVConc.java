package Main;

import SharedRegions.*;
import Entities.*;
import java.util.Random;
import AuxTools.*;
import java.util.Date;

public class AirportVConc {

	public String fileName = "log" + new Date().toString().replace(' ', '_') + ".txt";
	
	public static void main(String[] args) {
		
		Random rand = new Random();
		
		//Number of bags per passenger and flight
		int numBags[][] = new int[SimulatorParam.NUM_PASSANGERS][SimulatorParam.NUM_FLIGHTS];
		//Trip state per passenger and flight
		char tripState[] []= new char[SimulatorParam.NUM_PASSANGERS][SimulatorParam.NUM_FLIGHTS];
		//Number of bags that have been lost per passenger and flight
		int numBagsLost[][] =  new int[SimulatorParam.NUM_PASSANGERS][SimulatorParam.NUM_FLIGHTS];
		
		for(int i=0;i<SimulatorParam.NUM_PASSANGERS;i++) {
			
			//Initialize the number of bags per passenger and flight with probabilities
			for(int b=0;b<numBags.length;b++) {
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
			/*
			Initialize the number of bags that have been lost 
			per passenger and flight with probabilities
			*/
			for(int b=0;b<numBagsLost.length;b++) {
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
			for(int t=0;t<tripState.length;t++) {
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
			for(int b=0;b<numBagsFound.length;b++) {
				numBagsFound[i][b] = numBags[i][b] - numBagsLost[i][b];
				if(numBagsFound[i][b]<0) numBagsFound[i][b]=0;
				totalNumOfBags[b] += numBagsFound[i][b];
			}
		}
		
		
		//Array of stacks that represent the storage of bags per flight
		MemStack<Bag>[] sBags = (MemStack<Bag>[]) new Object[SimulatorParam.NUM_FLIGHTS];
		for(int b=0;b<SimulatorParam.NUM_FLIGHTS;b++) {
			//Bags in the storage to pass to the arrival lounge per flight
			Bag bagsToArrivalLounge[] = new Bag[totalNumOfBags[b]];
			for(int i=0;i<SimulatorParam.NUM_PASSANGERS;i++) {
				for(int j=0;j<numBagsFound[i][b];j++) {
					/*
					i+j+b : id of the bag
					i: passenger id
					*/
					bagsToArrivalLounge[i+j] = new Bag(i+j+b,i,tripState[i]);
				}
			}
			try {
				sBags[b] = new MemStack<Bag>(bagsToArrivalLounge);
			} catch (MemException e) {}
		}
 		
		//Initialize SharedRegions
		Repo repo = new Repo();
		ArrivalLounge al = new ArrivalLounge(sBags,repo);
		ArrivalTerminalExit ate = new ArrivalTerminalExit(repo);
		ArrivalTerminalTransferQuay attq = new ArrivalTerminalTransferQuay(repo);
		BaggageCollectionPoint bcp = new BaggageCollectionPoint(repo);
		BaggageReclaimOffice bro = new BaggageReclaimOffice(repo);
		DepartureTerminalEntrance dte = new DepartureTerminalEntrance(repo);
		DepartureTerminalTransferQuay dttq = new DepartureTerminalTransferQuay(repo);
		TemporaryStorageArea tsa = new TemporaryStorageArea(repo);
		
		//Initialize Entities
		BusDriver busdriver = new BusDriver(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL,
				attq, dttq);
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
		
		//End simulation
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
	}

}
