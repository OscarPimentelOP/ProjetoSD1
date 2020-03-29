/**
 * @author Óscar Pimentel, nºmec 80247
 * @author Rafael Maio, nºmec 84909
 */

package AuxTools;

public class testCAM{


    public static void main(String[] args){
        CAM<String, Integer> map = new CAM<String, Integer>();
        map.store("kek", 1);
        map.store("value", 2);
        map.store("lmao", 1);
        map.store("lmao", 6);
        map.printElems();

        System.out.println(map.retreive("yolo"));
        System.out.println(map.retreive("lmao"));
        
        System.out.print("numElems:" + map.size());
        map.remove("lmao");
        System.out.print("newNumElems:" + map.size());
          

    }

}