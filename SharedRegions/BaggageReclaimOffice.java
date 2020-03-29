/**
 * @author Óscar Pimentel, nºmec 80247
 * @author Rafael Maio, nºmec 84909
 */

package SharedRegions;

import AuxTools.SharedException;
import Entities.Passenger;
import Entities.PassengerState;
import Main.SimulatorParam;

/**
     * This class implements the Baggage Reclaim Office shared region.
	 * The passengers can report missing bags here.
*/

public class BaggageReclaimOffice {

	
	/**
     * Sum of all missing bags
	*/
	private int totalNumOfMissingBags;
	
	/**
     * The repository, to store the program status
	*/
	private Repo repo;
	
	/**
     * Baggage ReclaimOffice's instantiation
     * @param repo -> repository of information
    */
	public BaggageReclaimOffice(Repo repo) {
		this.repo = repo;
	}
	
	//PASSENGER FUNCTIONS
	
	/**
     * A passenger reports missing bags.
	 * Adds the number of missing bags to the count of the total missing bags in the reclaim office.
     * @param numMissingBags -> number of missing bags from a passenger
	 * @throws SharedException if the number of missing bags declared by the passenger is negative or higher than the amount of bags declared on the Params file.
    */
	public synchronized  void reportMissingBags(int numMissingBags) throws SharedException {
		try
		{ if (numMissingBags < 0 || numMissingBags > SimulatorParam.MAX_NUM_OF_BAGS)                         /* check for proper parameter range */
			 throw new SharedException ("Flight cannot exceed the defined parameter for number of flights: " + numMissingBags + ".");
		}
		catch (SharedException e)
		{ System.out.println ("Thread " + ((Thread) Thread.currentThread ()).getName () + "terminated.");
		   System.out.println ("Error on reportMissingBags()" + e.getMessage ());
		   System.exit (1);
		}

		Passenger p = (Passenger) Thread.currentThread(); 
		p.setPassengerState(PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		int id = p.getIdentifier();
		repo.setPassengerState(id, PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		totalNumOfMissingBags+=numMissingBags;
		this.repo.setLostBags(totalNumOfMissingBags);
	}
}
