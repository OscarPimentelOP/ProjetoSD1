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
 * This class implements the Departure Terminal Entrance shared region.
 * The passengers can go prepare their next leg if they are in transit.
 */

public class DepartureTerminalEntrance {

    /**
     * The repository, to store the program status
     */
    private Repo repo;

    /**
     * Arrival terminal transfer quay shared region
     */
    private ArrivalTerminalTransferQuay attq;

    /**
     * Arrival terminal exit shared region
     */
    private ArrivalTerminalExit ate;

    /**
     * Arrival lounge shared region
     */
    private ArrivalLounge al;

    /**
     * ???
     */
    private boolean timeToWakeUp;

    /**
     * Departure Terminal Exit's instantiation
     *
     * @param al   -> arrival lounge
     * @param attq -> arrival terminal transfer quay
     * @param repo -> repository of information
     */
    public DepartureTerminalEntrance(ArrivalLounge al, ArrivalTerminalTransferQuay attq, Repo repo) {
        this.repo = repo;
        this.al = al;
        this.attq = attq;
        this.timeToWakeUp = false;
    }

    //Passenger functions

    /**
     * If the passenger is in transit, it must prepare his next leg.
     * This changes the state to entering departure terminal.
     * He waits until every other passenger arrives at the end.
     *
     * @param flight -> the flight number
     * @throws SharedException if the flight number is higher than the number of flights parameter defined in the parameters file.
     */
    public synchronized void prepareNextLeg(int flight) throws SharedException {
        try {
            if (flight + 1 > SimulatorParam.NUM_FLIGHTS)                         /* check for proper parameter range */
                throw new SharedException("Flight cannot exceed the defined parameter for number of flights: " + flight + ".");
        } catch (SharedException e) {
            System.out.println("Thread " + ((Thread) Thread.currentThread()).getName() + "terminated.");
            System.out.println("Error on prepareNextLeg()" + e.getMessage());
            System.exit(1);
        }

        Passenger m = (Passenger) Thread.currentThread();
        m.setPassengerState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
        int id = m.getIdentifier();
        repo.setPassengerState(id, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
        ate.incCntPassengersEnd();
        if (ate.getCntPassengersEnd() == SimulatorParam.NUM_PASSANGERS) {
            ate.wakeUpAll();
            this.wakeUpAll();
        }
        while (!this.timeToWakeUp) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
        ate.decCntPassengersEnd();
        if (ate.getCntPassengersEnd() == 0) {
            this.timeToWakeUp = false;
            ate.setTimeToWakeUpToFalse();
            if (flight + 1 == SimulatorParam.NUM_FLIGHTS) {
                al.setEndOfWork();
                attq.setEndOfWord();
            }
        }
        //Waiting for porter and bus driver to fall asleep before changing the passenger state to NO_STATE
        try {
            wait(10);
        } catch (InterruptedException e) {

            System.out.print(e);
        }
        m.setPassengerState(PassengerState.NO_STATE);
        repo.setPassengerState(id, PassengerState.NO_STATE);
    }

    /**
     * Sets the arrival terminal exit shared region
     *
     * @param ate -> the arrival terminal to set
     */
    public synchronized void setArrivalExit(ArrivalTerminalExit ate) {
        this.ate = ate;
    }

    /**
     * Wakes up all the passengers of the terminal
     */
    public synchronized void wakeUpAll() {
        this.timeToWakeUp = true;
        try {
            wait(10);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        notifyAll();
    }

    /**
     * Marks the end of the flight, so then all the passengers are blocked for the next flight
     */
    public synchronized void setTimeToWakeUpToFalse() {
        this.timeToWakeUp = false;
    }
}
