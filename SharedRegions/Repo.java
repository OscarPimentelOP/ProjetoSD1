package SharedRegions;

import genclass.GenericIO;
import genclass.TextFile;
import java.util.Date;

public class Repo{


    private String fileName = "log_" + new Date().toString().replace(' ', '_') + ".txt";
    private int passengersFinalDest;
    private int passengersTransit;
    private int totalBags;
    private int lostBags;

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
 
 }

 private void reportFinalStatus(){
  log.writelnString("Final report");
  log.writelnString("N. of passengers which have this airport as their final destination = " + Integer.toString(this.passengersFinalDest));
  log.writelnString("N. of passengers in transit = " + Integer.toString(this.passengersTransit));
  log.writelnString("N. of bags that should have been transported in the the planes hold = " + Integer.toString(this.totalBags)); 
  log.writelnString("N. of bags that were lost = " + Integer.toString(this.lostBags));


}


    
}