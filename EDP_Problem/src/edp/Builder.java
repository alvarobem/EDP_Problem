package edp;

import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public interface Builder {
    
    public ArrayList<Integer> build (int pos, Instance instance, Solution solution);
    
}
