public class Porter{
    
    PorterState state;

    public Porter(int s){
        state = s;
    }


    public void setState(int s){
        state = s;        
    }

    public int getState(){
        return state;
    }



}
