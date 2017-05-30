package edp;

import java.util.Random;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class MyRandom {
    private static Random r = new Random(6);
    
    public MyRandom (){
        r = new Random(6);
    }
     public static int nextInt(int n){
         return r.nextInt(n);
     }
}
