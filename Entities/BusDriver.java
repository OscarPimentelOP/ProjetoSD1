package Entities;

public class BusDriver{

    BusDriverState state;


    public BusDriver(BusDriverState s){
        state = s;
    }


    public void setState(BusDriverState s){
        state = s;        
    }

    public BusDriverState getState(){
        return state;
    }



}




