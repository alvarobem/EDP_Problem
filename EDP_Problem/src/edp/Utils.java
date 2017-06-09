package edp;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class Utils {
    protected final static String  NAME_GRAPH = "instancias/AS-BA.R-Wax.v100e217.bb";
    protected final static String NAME_MATRIX = "instancias/AS-BA.R-Wax.v100e217.rpairs.40."; 
    protected final static String OUTPUT_NAME_FILE = "salida40-vns.csv"; 
    
    public static class Edge {
        int [] edge = new int [2];
        public Edge (int a, int b){
            edge [0] = a;
            edge [1] = b;
        }

        @Override
        public boolean equals(Object obj) {
            Edge e = (Edge)obj;
            return ((edge[0]==e.edge[0] && edge[1]==e.edge[1])||(edge[0]==e.edge[1] && edge[1]==e.edge[0]));
        }

        @Override
        public String toString() {
            return (edge [0]+" - "+edge [1]);
        }
        
        
        
    }
    
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
    
    public static int[][] deleteEdges(int[][] m, ArrayList<Integer> e) {
        for (int i = 0; i < e.size() - 1; i++) {
            int a1 = e.get(i);
            int a2 = e.get(i + 1);
            m[a1][a2] = -1;
            m[a2][a1] = -1;
        }
        return m;
    }
    
    public static void writeFile(Solution s, String f, String algoritm) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(f, true);
            pw = new PrintWriter(fichero);
            pw.println(algoritm+s.getI().getNameFile() + ";" + s.getConn() + ";" + s.getNotConn() + ";" + s.getTime());

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
    
    public static boolean isValidSolution(Solution solution){
        HashSet<Edge> usedEdges = new HashSet<>();
        for (ArrayList<Integer> route : solution.getRoutes()){
            for (int pos=0; pos < route.size()-1; pos ++){
                Edge edge = new Edge(route.get(pos),route.get(pos+1));
                Iterator<Edge> it = usedEdges.iterator();
                while (it.hasNext()){
                    Edge e = it.next();
                    if (e.equals(edge)){
                        System.err.println("Rerpetido:" + edge.toString());
                        return false;
                    }
                }
                usedEdges.add(edge);
            }
        }
        return true;
        
    }
}
