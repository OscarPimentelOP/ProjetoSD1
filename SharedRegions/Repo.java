package SharedRegions;
import java.io.File;
import Entities.PorterState;
import Entities.PassengerState;
import Entities.BusDriverState;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import Main.SimulatorParam;

public class Repo{


    private File file;
    private PrintWriter pw;
    private final String[] porterStates = {"WPTL", "APLH", "ALCB", "ASTR"};
    private final String[] passengerStates = {"WSD", "ATT", "TRT", "DTT", "EDT", "LCP", "BRO", "EAT","---"};
    private final String[] busDriverStates = {"PKAT", "DRFW", "PKDT", "DRBW"};
    
    //State of the porter
    private PorterState porterSt;
    
    //States of the passengers
    private PassengerState[] passengerSt = new PassengerState[SimulatorParam.NUM_PASSANGERS];
    
    //States of the bus driver
    private BusDriverState busDriverSt;
    
    //Number of the current flight
    private int flightNum;
    
    //Number of bags at presently at the plane's hold
    private int numOfBagsAtPlaneHold;
    
    //Number of bags in the convoy belt
    private int numOfBagsInTheConvoyBelt;
    
    //Number of bags in the temporary storage area
    private int numOfBagsInTheTempArea;
    
    //Passengers (identified by the id) on the queue waiting for the bus
    private int[] passengersOnTheQueue = new int[SimulatorParam.NUM_PASSANGERS];
    
    //Passengers (identified by the id) on the bus
    private int[] passangersOnTheBus = new int[SimulatorParam.BUS_CAPACITY];
    
    //Destination of the each passenger 
    private String[] passengerDestination = new String[SimulatorParam.NUM_PASSANGERS];
    
    //Number of bags carried at the start of her journey
    private int[] numOfBagsAtTheBegining = new int[SimulatorParam.NUM_PASSANGERS];
    
    //Number of bags that the passenger has presently collected
    private int[] numOfBagsCollected = new int[SimulatorParam.NUM_PASSANGERS];
    
    //Number of passengers which have this airport as their final destination
    private int passengersFinalDest;
    
    //Number of passengers in transit
    private int passengersTransit;
    
    //Number of bags that should have been transported in the the planes hold
    private int totalBags;
    
    //Number of bags that were lost 
    private int lostBags;


    public Repo() throws FileNotFoundException{
      file = new File(SimulatorParam.fileName);
      pw = new PrintWriter(file);

      flightNum = 1;
      numOfBagsAtPlaneHold = 0;
      numOfBagsInTheConvoyBelt = 0;
      numOfBagsInTheTempArea = 0;
      for (int p=0;p<SimulatorParam.NUM_PASSANGERS;p++) {
    	  passengersOnTheQueue[p] = 0;
          passengerDestination[p] = "TRT";
          numOfBagsAtTheBegining[p] = 0;
          numOfBagsCollected[p] = 0;
      }
      for (int p=0;p<SimulatorParam.BUS_CAPACITY;p++) {
          passangersOnTheBus[p] = 0;
      }
      passengersFinalDest = 0;
      passengersTransit = 0;
      totalBags = 0;
      lostBags = 0;

      //defining initial states for the entities
      porterSt = PorterState.WAITING_FOR_A_PLANE_TO_LAND;
      busDriverSt = BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL;
      passengerSt = new PassengerState[SimulatorParam.NUM_PASSANGERS];
      for (int p = 0; p < SimulatorParam.NUM_PASSANGERS; p++){
          passengerSt[p] = PassengerState.NO_STATE;
      }
      
      reportInitialStatus();
    }



    private void reportInitialStatus ()
   {
      pw.write("AIRPORT RHAPSODY - Description of the internal state of the problem");
      pw.write("PLANE PORTER DRIVER");
      pw.write("FN BN Stat CB SR Stat Q1 Q2 Q3 Q4 Q5 Q6 S1 S2 S3");
      pw.write(" PASSENGERS");
      pw.write("St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6");
      reportStatus ();
   }



    private String reportStatus ()
    {
    	String[] Q = new String[SimulatorParam.NUM_PASSANGERS];
    	for(int p=0;p<SimulatorParam.NUM_PASSANGERS;p++) {
    		if(this.passengersOnTheQueue[p]==0) {
    			Q[p] = "-";
    		}
    		else {
    			Q[p] = Integer.toString(this.passengersOnTheQueue[p]);
    		}
    	}
    	String[] S = new String[SimulatorParam.NUM_FLIGHTS];
    	for(int s=0;s<SimulatorParam.BUS_CAPACITY;s++) {
    		if(this.passangersOnTheBus[s]==0) {
    			S[s] = "-";
    		}
    		else {
    			S[s] = Integer.toString(this.passangersOnTheBus[s]);
    		}
    	}
	    String lineStatus = "";                              // linha a imprimir
	    lineStatus += " "+Integer.toString(this.flightNum)+"  "+
	    		Integer.toString(this.numOfBagsAtPlaneHold)+"  "+
	    		this.porterStates[this.porterSt.ordinal()]+
	    		"  "+Integer.toString(this.numOfBagsInTheConvoyBelt)+"  "+
	    		Integer.toString(this.numOfBagsInTheTempArea)+"   "+
	 		   this.busDriverStates[this.busDriverSt.ordinal()]+"   "+Q[0]+"  "+Q[1]+"  "+Q[2]+
	 		   "  "+Q[3]+"  "+Q[4]+"  "+Q[5]+"   "+S[0]+"  "+S[1]+"  "+S[2]+"\n";
	    
	    for(int p=0;p<SimulatorParam.NUM_PASSANGERS;p++) {
	    	if(this.passengerSt[p].ordinal() == 9) {
		    	lineStatus+=this.passengerStates[this.passengerSt[p].ordinal()]+" "+
		    			"---"+"  +"+
		    			"-"+"   "+
		    			"-"+"  ";
	    	}
	    	else {
	    		lineStatus+=this.passengerStates[this.passengerSt[p].ordinal()]+" "+
		    			this.passengerDestination[p]+"  +"+
		    			Integer.toString(this.numOfBagsAtTheBegining[p])+"   "+
		    			Integer.toString(this.numOfBagsCollected[p])+"  ";
	    	}
	    }
	    return lineStatus+"\n";
    }       
 

 public void reportFinalStatus(){
	pw.write("Final report");
	pw.write("N. of passengers which have this airport as their final destination = " + Integer.toString(this.passengersFinalDest));
	pw.write("N. of passengers in transit = " + Integer.toString(this.passengersTransit));
	pw.write("N. of bags that should have been transported in the the planes hold = " + Integer.toString(this.totalBags)); 
	pw.write("N. of bags that were lost = " + Integer.toString(this.lostBags));
	pw.close();
}

public synchronized void setPassengerState(int id, PassengerState ps){
  if(passengerSt[id] != ps){
    passengerSt[id] = ps;
    printInfo();
  }
}

public synchronized void setPorterState(PorterState ps){
  if(this.porterSt != ps){
    this.porterSt = ps;
    printInfo();
  }
}

public synchronized void setBusDriverState(BusDriverState bds){
  if(this.busDriverSt != bds){
    this.busDriverSt = bds;
    printInfo();
  }
}

private void printInfo(){
  String infoToPrint = reportStatus();
  System.out.println(infoToPrint);
  pw.write(infoToPrint);
  pw.flush();

}

public synchronized void setFlightNumber(int flight){
  flightNum = flight+1; 
  printInfo();
}

public synchronized void setNumOfBagsAtPlaneHold(int numOfBagsAtPlaneHold) {
	this.numOfBagsAtPlaneHold = numOfBagsAtPlaneHold;
	printInfo();
}


public synchronized void setNumOfBagsInTheConvoyBelt(int numOfBagsInTheConvoyBelt) {
	this.numOfBagsInTheConvoyBelt = numOfBagsInTheConvoyBelt;
	printInfo();
}

public synchronized void setNumOfBagsInTheTempArea(int numOfBagsInTheTempArea) {
	this.numOfBagsInTheTempArea = numOfBagsInTheTempArea;
	printInfo();
}

public synchronized void setPassengersOnTheQueue(int queueNum, int passengerId) {
	this.passangersOnTheBus[queueNum] = passengerId;
	printInfo();
}

public synchronized void setPassangersOnTheBus(int seatNum, int passengerId){
	this.passangersOnTheBus[seatNum] = passengerId;
	printInfo();
}

public synchronized void setPassengerDestination(int passengerId, String destination){
	this.passengerDestination[passengerId] = destination;
	printInfo();
}

public synchronized void setNumOfBagsAtTheBegining(int passengerId, int numOfBags){
	this.numOfBagsAtTheBegining[passengerId] = numOfBags;
	printInfo();
}

public synchronized void setNumOfBagsCollected(int passengerId, int numOfBagsCollected){
	this.numOfBagsCollected[passengerId] = numOfBagsCollected;
	printInfo();
}

public synchronized void setPassengersFinalDest(int passengersFinalDest){
	this.passengersFinalDest = passengersFinalDest;
	printInfo();
}

public synchronized void setPassengersTransit(int passengersTransit) {
	this.passengersTransit = passengersTransit;
	printInfo();
}

public synchronized void setTotalBags(int totalBags) {
	this.totalBags = totalBags;
	printInfo();
}

public synchronized void setLostBags(int lostBags) {
	this.lostBags = lostBags;
	printInfo();
}

}