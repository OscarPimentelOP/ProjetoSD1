package Entities;

public class Porter{
    
    PorterState state;

    public Porter(PorterState s){
        state = s;
    }


    public void setState(PorterState s){
        state = s;        
    }

    public PorterState getState(){
        return state;
    }



}
