package AuxTools;

import java.util.Arrays;
public class CAM<Key, Value> {
    
    private static final int cap = 16;
    private Node<Key, Value>[] storage = new Node[cap];
    private int size;

   


    public void store(Key key, Value value){
        boolean stored = true;
        
        for(int i = 0; i< size; i++){
           
            
                if(storage[i].getKey().equals(key)){
                    storage[i].setValue(value);
                    stored = false;
                }
        }
            //ensure the capacity of the map
            if(stored){
                if(size == storage.length){
                    int newSize = storage.length*2;
                    storage = Arrays.copyOf(storage, newSize);
                }
                storage[size++] = new Node<Key, Value>(key, value);
            }

        }
        


    public Value retreive(Key key){
        for(int i=0; i< size; i++){
            if(storage[i] != null){
                if(storage[i].getKey().equals(key)){
                    return storage[i].getValue();
                }
            }
        }
        return null;
    }

    public void remove(Key key){
        for(int i = 0; i< size; i++){
            if(storage[i].getKey().equals(key)){
                storage[i] = null;
                size--;
                for(int k= i; k< size; k++){
                    storage[k]=storage[k+1];
                }
            }           
    
        }

    }

    public int size() {
        return size;
    }

    public void printElems() {
        for(int i = 0; i< size; i++){
            System.out.println(storage[i].getKey());
        }
    }
}


class Node<Key, Value> {
    private final Key key;
    private Value value;
    

    public Node(Key key, Value value){
        this.key = key;
        this.value = value;
    }

    public Key getKey(){
        return key;
    }

    public Value getValue(){
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Value value) {
        this.value = value;
    }

}
