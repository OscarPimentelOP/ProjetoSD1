package SharedRegions;

import genclass.GenericIO;
import genclass.TextFile;
import java.util.Date;
import Entities.PorterState;
import Entities.PassengerState;
import Entities.BusDriverState;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

import Main.AirportVConc;
import Main.SimulatorParam;

public class Repo{


    public static String fileName = "log" + new Date().toString().replace(' ', '_') + ".txt";
    private File file;
    private PrintWriter pw;
    private final String[] porterStates = {"WPTL", "APLH", "ALCB", "ASTR"};
    private final String[] passengerStates = {"WSD", "ATT", "TRT", "DTT", "EDT", "LCP", "BRO", "EAT"};
    private final String[] busDriverStates = {"PKAT", "DRFW", "PKDT", "DRBW"};
    private int passengersFinalDest;
    private int passengersTransit;
    private int totalBags;
    private int lostBags;
    private int flightNum;
    private PorterState porterSt;
    private PassengerState[] passengerSt;
    private BusDriverState busDriverSt;

    private int passengerCount;


    public Repo() throws FileNotFoundException{
      file = new File(AirportVConc.fileName);
      pw = new PrintWriter(file);

      passengerCount = 0;
      flightNum = 1;

      //defining initial states for the entities
      porterSt = PorterState.WAITING_FOR_A_PLANE_TO_LAND;
      busDriverSt = BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL;
      passengerSt = new PassengerState[SimulatorParam.NUM_PASSANGERS];

      for (int p = 0; i < SimulatorParam.NUM_PASSANGERS; p++){
          passengerSt[p] = PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
      }
      

    }



    private void reportInitialStatus ()
   {
      TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

      if (!log.openForWriting (".", fileName))
         { GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
           System.exit (1);
         }
      log.writelnString("AIRPORT RHAPSODY - Description of the internal state of the problem");
      log.writelnString("PLANE PORTER DRIVER");
      log.writelnString("FN BN Stat CB SR Stat Q1 Q2 Q3 Q4 Q5 Q6 S1 S2 S3");
      log.writelnString(" PASSENGERS");
      log.writelnString("St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6");
      
      
      if (!log.close ())
         { GenericIO.writelnString ("A operação de fecho do ficheiro " + fileName + " falhou!");
           System.exit (1);
         }
      reportStatus ();
   }



    private void reportStatus ()
    {
       TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto
 
       String lineStatus = "";                              // linha a imprimir
 
       if (!log.openForAppending (".", fileName))
          { GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit (1);
          }
       for (int i = 0; i < nBarber; i++)
         switch (stateBarber[i])
         { case SLEEPING: lineStatus += " DORMINDO ";
                          break;
           case WORKING:  lineStatus += " ACTIVIDA ";
                          break;
         }
       for (int i = 0; i < nCustomer; i++)
         switch (stateCustomer[i])
         { case LIVNORML: lineStatus += " VIVVNRML ";
                          break;
           case WANTCUTH: lineStatus += " QUERCORT ";
                          break;
           case WAITTURN: lineStatus += " ESPERAVZ ";
                          break;
           case CUTHAIR:  lineStatus += " CORTACBL ";
                          break;
         }
       log.writelnString (lineStatus);
       if (!log.close ())
          { GenericIO.writelnString ("A operação de fecho do ficheiro " + fileName + " falhou!");
            System.exit (1);
          }
    }       
 

 private void reportFinalStatus(){
  log.writelnString("Final report");
  log.writelnString("N. of passengers which have this airport as their final destination = " + Integer.toString(this.passengersFinalDest));
  log.writelnString("N. of passengers in transit = " + Integer.toString(this.passengersTransit));
  log.writelnString("N. of bags that should have been transported in the the planes hold = " + Integer.toString(this.totalBags)); 
  log.writelnString("N. of bags that were lost = " + Integer.toString(this.lostBags));


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

  }




public synchronized void setBeginningNumBags(){

}









    
}