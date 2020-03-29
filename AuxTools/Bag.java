/**
 * @author Óscar Pimentel, nºmec 80247
 * @author Rafael Maio, nºmec 84909
 */

package AuxTools;

/**
 * This file implements the Bag object.
 * A bag is a piece of luggage that each passenger may possess.
 * They are identified by its Passenger ID and have the trip destination status as well.
 */

public class Bag {
    /**
     * Bag's ID
     */
    int id;

    /**
     * Bag's owner's ID
     */
    int passegerId;

    /**
     * Bag's Destination
     */
    char destination;


    /**
     * Bag instanciation
     *
     * @param id          -> passenger ID
     * @param passengerId -> Bag's owner's ID
     * @param destination -> Bag's destination
     */
    public Bag(int id, int passegerId, char destination) {
        this.id = id;
        this.passegerId = passegerId;
        this.destination = destination;
    }

    /**
     * Returns the Bag's ID
     *
     * @return Bag's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the Bag's owner's ID
     *
     * @return passenger's ID
     */
    public int getPassegerId() {
        return passegerId;
    }

    /**
     * Sets the Bag's destination
     *
     * @param destination -> the destination to be set
     */
    public void setDestination(char destination) {
        this.destination = destination;
    }

    /**
     * Returns the Bag's destination
     *
     * @return destination
     */
    public char getDestination() {
        return this.destination;
    }
}
