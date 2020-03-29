package Main;

import java.util.Date;

/**
	 *This class has stored all the simulation parameters
	 needed to run the program.
*/

public class SimulatorParam{
	
	/**
     * Number of flights
    */
	public static final int NUM_FLIGHTS = 5;
	
	/**
     * Number of passengers
    */
	public static final int NUM_PASSANGERS = 6;
	
	/**
     * Number of maximum pieces of luggage in the plane hold
    */
	public static final int MAX_NUM_OF_BAGS = 2;
	
	/**
     * Transfer bus capacity
    */
	public static final int BUS_CAPACITY = 3;
	
	/**
     * time that the transfer bus waits for passengers at the arrival terminal in milliseconds
    */
	public static final int TIMEQUANTUM = 10;
	
	
	/**
     * Probability of passenger having taking 0 bags in %
    */
	public static final int PROB_OF_0_BAGS = 25;
	
	
	/**
     * Probability of passenger having taking 1 bags in %
    */
	public static final int PROB_OF_1_BAGS = 25;
	
	
	/**
     * Probability of passenger having taking 2 bags in %
    */
	public static final int PROB_OF_2_BAGS = 50;
	
	
	/**
     * Probability of 0 bags getting lost in %
    */
	public static final int PROB_LOSE_0_BAGS = 85;
	
	
	/**
     * Probability of 1 bags getting lost in %
    */
	public static final int PROB_LOSE_1_BAGS = 10;
	
	
	/**
     * Probability of 2 bags getting lost in %
    */
	public static final int PROB_LOSE_2_BAGS = 5;

	
	/**
     * Log file name to store the status of the program
    */
	public static final String fileName = "log" + new Date().toString().replace(' ', '_').replace(':', '_') + ".txt";
	
}