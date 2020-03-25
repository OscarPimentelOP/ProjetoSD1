package Main;

import SharedRegions.*;
import Entities.*;
import java.util.Random;
import AuxTools.*;
import java.util.Date;

public class AirportVConc {

	public static String fileName = "log" + new Date().toString().replace(' ', '_') + ".txt";
	
	public static void main(String[] args) {
		
		Random rand = new Random();
		
		MemStack<Bag> sBags = new MemStack<Bag>();
 		
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
			
			//Initialize the number of bags per flight with probabilities
			int numBags[] = new int[SimulatorParam.NUM_FLIGHTS];
			for(int b=0;b<numBags.length;b++) {
				int randint = rand.nextInt(101);
				if(randint <= SimulatorParam.PROB_OF_0_BAGS) {
					numBags[b] = 0;
				}
				else if(randint>SimulatorParam.PROB_OF_2_BAGS) {
					numBags[b] = 2;
				}
				else {
					numBags[b] = 1;
				}
			}
			/*
			Initialize the trip state
			(final destination-> F, transit -> T)
			*/
			char tripState[] = new char[SimulatorParam.NUM_FLIGHTS];
			for(int t=0;t<tripState.length;t++) {
				int randint = rand.nextInt(2);
				if(randint == 0) {
					tripState[t] = 'T';
				}
				else {
					tripState[t] = 'F';
				}
			}
			
			passengers[i] = new Passenger(PassengerState.AT_THE_DISEMBARKING_ZONE, i, numBags,
					tripState, al, ate, attq, dttq, dte, bro, bcp);
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
