/**
 * @author Óscar Pimentel, nºmec 80247
 * @author Rafael Maio, nºmec 84909
 */

package SharedRegions;

import AuxTools.MemStack;
import AuxTools.SharedException;
import AuxTools.Bag;
import AuxTools.MemException;
import Entities.Passenger;
import Entities.PassengerState;
import Entities.Porter;
import Entities.PorterState;
import Main.SimulatorParam;

/**
     * This class implements the Arrival Lounge shared region.
	 * This is where the passengers arrive when the plane lands
	 * and the porter is waiting for collecting bags.
*/

public class ArrivalLounge {
	
	
	/**
     * Variable the warns the porter than he can go rest.
	 * The last passenger of the last flight accuses in goHome function or in prepareNextLeg function.
	*/
	private boolean endOfOperations;
	
	
	/**
     * Count of the number of passenger for the last one to warn that he is the last
	*/
	private int cntPassengers;
	
	
	/**
     * Passenger bags in stack format
	*/
	private MemStack<Bag> sBags[];
	
	/**
     * The repository, to store the program status
	*/
	private Repo repo;
	
	/**
     * Number of flight
	*/
	private int flight;
	
	/**
     * Number of passengers that have that Airport as their final destination.
	*/
	private int passengersFinalDest;
	
	/**
     * Number of passengers that are in transit
	*/
	private int passengersTransit;
	
	/**
     * Baggage collection point shared region
	*/
	private BaggageCollectionPoint bcp;
	
	/**
     * Trip states for each passenger
	*/
	private char[][] passengersTripState;
	
	/**
     * Number of bags per flight
	*/
	private int[] numOfBagsPerFlight;
	
	/**
     * Arrival Lounge's instantiation
     * @param sBags -> bags 
     * @param numOfBagsPerFlight -> number of bags per flight
     * @param tripState -> trip states for the passengers
     * @param bcp -> baggage collection point
     * @param repo -> repository of information
    */
	public ArrivalLounge(MemStack<Bag> sBags[], int[] numOfBagsPerFlight, char[][] tripState, BaggageCollectionPoint bcp ,Repo repo){
		this.sBags = sBags;
		this.repo = repo;
		this.flight = 0;
		this.passengersFinalDest = 0;
		this.passengersTransit = 0;
		this.passengersTripState = tripState;
		this.bcp = bcp;
		this.endOfOperations = false;
		this.cntPassengers = 0;
		this.numOfBagsPerFlight = numOfBagsPerFlight;
	}
	
	//PORTER FUNCTIONS


	/**
     * The porter is taking a rest, waiting for a plane to land.
	 * @return 'E' end of the day
	 * @return 'W' work
	*/
	public synchronized char takeARest() {
		Porter p = (Porter) Thread.currentThread();
		while(cntPassengers != SimulatorParam.NUM_PASSANGERS && !this.endOfOperations) {
			try {
				wait();
			}catch(InterruptedException e) {}
		}
		if(this.endOfOperations) {
			return 'E';
		}
		else {
			p.setPorterState(PorterState.AT_THE_PLANES_HOLD);
			repo.setPorterState(PorterState.AT_THE_PLANES_HOLD);
			return 'W';
		}
	}
	
	
	/**
     * The porter tries to collect a bag from the plane's hold.
	 * @return the bag that the porter has collected
	*/
	public synchronized Bag tryToCollectABag() {
		try  {
			Bag bag = sBags[this.flight].read();
			repo.decNumOfBagsAtPlaneHold();
			int passengerId = bag.getPassegerId();
			bag.setDestination(passengersTripState[passengerId][flight]);
			return bag;
		}
		catch (MemException e) {
			return null; 
		}
	}
	
	/**
     * At the plane's hold, the porter realizes that he has no more bags to collect.
	 * It informs the baggage collection point that there are no more bags to collect.
	*/
	public synchronized void noMoreBagsToCollect() {
		cntPassengers=0;
		bcp.setMoreBags(false);
		Porter p = (Porter) Thread.currentThread();
		p.setPorterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
		repo.setPorterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
	}
	
	//PASSENGER FUNCTIONS
	
	
	/**
     * The porter is taking a rest, waiting for a plane to land.
	 * @param flight -> the flight number
	 * @return 'T' if the passenger is going to take a bus (means he is in transit)
	 * @return 'B' if the passenger is going to collect a bag
	 * @throws SharedException if flight exceeds the parameter, or if the passengers count is negative or exceeds the total
	*/
	public synchronized char whatShouldIDo(int flight) throws SharedException{
		try
      { if (flight+1 > SimulatorParam.NUM_FLIGHTS)                         /* check for proper parameter range */
           throw new SharedException ("Flight cannot exceed the defined parameter for number of flights: " + flight + ".");
      }
      catch (SharedException e)
      { System.out.println ("Thread " + ((Thread) Thread.currentThread ()).getName () + "terminated.");
         System.out.println ("Error on whatShouldIDo()" + e.getMessage ());
         System.exit (1);
      }
		//Initialize a new flight
		try
		{ if (cntPassengers < 0 || cntPassengers > SimulatorParam.NUM_PASSANGERS)                         /* check for proper parameter range */
			 throw new SharedException ("Passenger count has exceeded the limits defined on the parameters file: " + cntPassengers + ".");
		}
		catch (SharedException e)
		{ System.out.println ("Thread " + ((Thread) Thread.currentThread ()).getName () + "terminated.");
		   System.out.println ("Error on whatShouldIDo()" + e.getMessage ());
		   System.exit (1);
		}

		if(cntPassengers == 0) {
			bcp.setMoreBags(true);
			this.flight = flight;
			repo.setFlightNumber(flight);
			repo.setNumOfBagsAtPlaneHold(flight, this.numOfBagsPerFlight[flight]);
		}
		Passenger p = (Passenger) Thread.currentThread(); 
		p.setPassengerState(PassengerState.AT_THE_DISEMBARKING_ZONE);
		int id = p.getIdentifier();
		cntPassengers++;
		if(cntPassengers == SimulatorParam.NUM_PASSANGERS) {
			notifyAll();
		}
		char tripState = p.getTripState(flight);
		//Passenger in transit
		if(tripState == 'T') {
			this.passengersTransit++;
			repo.setPassengersTransit(this.passengersTransit);
			repo.setPassengerDestination(id, "TRT");
			repo.setNumOfBagsAtTheBegining(id, p.getNumBags(flight));
			repo.setPassengerState(id, PassengerState.AT_THE_DISEMBARKING_ZONE);
			//Take a bus
			return 'T';
		}
		//Passenger reached final destination
		else {
			this.passengersFinalDest++;
			repo.setPassengersFinalDest(this.passengersFinalDest);
			repo.setPassengerDestination(id, "FDT");
			int nBags = p.getNumBags(flight);
			repo.setNumOfBagsAtTheBegining(id, nBags);
			repo.setPassengerState(id, PassengerState.AT_THE_DISEMBARKING_ZONE);
			//Has bags to collect
			if(nBags != 0) {
				//Go collect bag
				return 'B';
			}
			//No bags to collect
			else {
				//Go home
				return 'H';
			}
		}
	}
	
	/**
     * Sets the porter's end of operations
	*/
	public synchronized void setEndOfWork() {
		this.endOfOperations = true;
		notifyAll();
	}
}