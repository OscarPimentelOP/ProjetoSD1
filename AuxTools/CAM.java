package AuxTools;

public class CAM<Key, Value> {
    
    private Node<Key, Value>[] buckets;
    private static final int cap = 16;
    private int size = 0;

    public CAM(int cap){
        this.buckets = new Node[cap];
    }

    public CAM(){
        this(cap);
    }    


    public void store(Key key, Value value){
        Node<Key, Value> node = new Node<>(key, value, null);

        int b = getHash(key) % buckets.length;

        Node<Key, Value> existing = buckets[b];
        if(existing == null){
            buckets[b] = node;
            size++;
        }
        else{
            //check if key exists already
            while(existing.next != null){
                if(existing.key.equals(key)){
                    existing.value = value;
                    return;
                }
                existing = existing.next;
            }
            if(existing.key.equals(key)){
                existing.value = value;
            }    
            else{
                existing.next = node;
                size++;
            }
        }
    }

    public Value retreive(Key key){
        Node<Key, Value> b = buckets[getHash(key) % buckets.length];

        while(b != null){
            if(key == b.key){
                return b.value;
            }
            b = b.next;
        }
        return null;
    }



}


class Node<Key, Value> {
    final Key key;
    Value value;
    Node<Key, Value> next;

    public Node(Key key, Value value, Node<Key, Value> next){
        this.key = key;
        this.value = value;
        this.next = next;
    }

}
