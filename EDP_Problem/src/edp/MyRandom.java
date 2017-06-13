package edp;

import java.util.Random;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class MyRandom {
    private static Random r;
    private static MyRandom random;
    
    private MyRandom (){
        r = new Random(6);
    }
    public Random getRandom (){
        return r;
    }
    
    public static MyRandom getInstance (){
        if (random == null){
            random = new MyRandom();
        }
        return random;
    }
     public int nextInt(int n){
         return r.nextInt(n);
     }
     public void restartRandom (){
         random = new MyRandom();
     }
}
