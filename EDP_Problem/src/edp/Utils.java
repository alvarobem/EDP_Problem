package edp;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class Utils {
    protected final static String  NAME_GRAPH = "instancias/AS-BA.R-Wax.v100e217.bb";
    protected final static String NAME_MATRIX = "instancias/AS-BA.R-Wax.v100e217.rpairs.40.";       
    public static void printPaths (int[] preds, int origen, int dest, int[] distancias, ArrayList<Integer> del, Solution s) {
         if (distancias[dest]>=100000||distancias[dest]<0){ /*valor tomado como infinito*/
            s.addNotConn();
         }
         else{
            printOnePath(preds,origen,dest, del);
            s.addConn();
         }
   }
   
   public static void printOnePath (int[] preds, int origen, int j, ArrayList<Integer> del) {
      if (j!=origen)
         printOnePath (preds,origen,preds[j], del);
      del.add(j);
      
   }
    
    public static int [][] deleteEdges (int [][] m, ArrayList<Integer> e){
        for (int i = 0 ; i<e.size()-1; i++){
            int a1= e.get(i);
            int a2 = e.get(i+1);
            m[a1][a2]= -1;
            m[a2][a1]= -1;
        }
        return m;
    }
    
    public static void writeFile(Solution s, String f) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(f, true);
            pw = new PrintWriter(fichero);
            pw.println(s.getI().getNameFile() + ";" + s.getConn() + ";" + s.getNotConn() + ";" + s.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
