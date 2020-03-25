package SharedRegions;

import genclass.GenericIO;
import genclass.TextFile;
import java.util.Date;

public class Repo{


    private String fileName = "log_" + new Date().toString().replace(' ', '_') + ".txt";

    private void reportInitialStatus ()
   {
      TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

      if (!log.openForWriting (".", fileName))
         { GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
           System.exit (1);
         }
      log.writelnString ("                Problema dos barbeiros sonolentos");
      log.writelnString ("\nNúmero de iterações = " + nIter + "\n");
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


    
}