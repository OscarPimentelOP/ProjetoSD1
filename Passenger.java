public class Passenger{


    int state; //??
    int identifier;  // id from the passenger
    int numBags[];   //number of bags the passenger has per flight
    char tripState[]; //the state of the trip of the passenger (final destination-> F, transit -> T)

    public Passenger(int s, int id, int[] nb, char[] ts){
        state= s;
        identifier = id;
        numBags = nb;
        tripState = ts;
    }


    public void setState(int s){
        state = s;        
    }

    public int getState(){
        return state;
    }

    public int getIdentifier(){
        return identifier;
    }

    public int getNumBags(int voo){
        return numBags[voo];
    }    

    public char getTripState(int voo){
        return tripState[voo];
    }






}












