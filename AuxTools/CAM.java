package AuxTools;


import java.util.Arrays;

import Main.SimulatorParam;

/**
     * This file is an implementation of a Map from scratch.
     * It is named as CAM - content addressable memory, or associative memory.
     * 
     * It is meant to emulate the place where bags are placed to be collected by the porter
*/

public class CAM<Key, Value> {
    
    /**
     * CAM's capacity (may be changed)   
    */
    private static final int cap = SimulatorParam.NUM_PASSANGERS*SimulatorParam.MAX_NUM_OF_BAGS;

    /**
     * The storage, composed by nodes    
    */
    private Node<Key, Value>[] storage = new Node[cap];

    /**
     * size   
    */
    private int size;

   

    /**
     * Stores a value and a associated key
     * @param key -> the key 
     * @param value -> value 
    */
    public void store(Key key, Value value){

        /**
         * if the object is stored   
        */
        boolean stored = true;
        
        for(int i = 0; i< size; i++){
           
            
                if(storage[i].getKey().equals(key)){
                    storage[i].setValue(value);
                    stored = false;
                }
        }
            if(stored){             //ensure the capacity of the map, by readjusting the size
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
