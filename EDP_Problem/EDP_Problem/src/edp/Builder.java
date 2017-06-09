package edp;

import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public interface Builder {
    
    
    public void setIsFirstSolution (boolean b);
    public Solution build (int pos, int numRepm ,Solution solution);
    
}