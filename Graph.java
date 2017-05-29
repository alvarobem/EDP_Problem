package edp;

import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class Graph {
    private int [][]  adjacent; 
    private int nodes;
    private boolean empty;
    final private  int MAX =100000;
    
    
    
    public Graph (int nodes){
        this.nodes= nodes;
        adjacent = new int [nodes][nodes];
        for (int i=0; i<nodes; i++){
            for (int j=0; j<nodes; j++){
                adjacent [i][j] = MAX;
            }
        }
        empty=true;
    }

    public int[][] getAdjacent() {
        return adjacent;
    }

    public int getNodes() {
        return nodes;
    }

    public void setAdjacent(int[][] adjacent) {
        this.adjacent = adjacent;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }
    
    
    
    public boolean isEmpty(){
        boolean empty= false;
        for (int i=0; i<nodes; i++){
            for (int j=0; j<nodes; j++){
                empty = adjacent [i][j]==0;
                if (!empty)
                    break;
            }
            if (!empty)
                    break;
        }
        return empty;
    }
    
    
    public int[] neighbors(int v){
        ArrayList <Integer> l= new ArrayList <>();
        for (int i =0; i<nodes ;i++){
           int aux =adjacent [v][i];
           if (aux!=MAX)
               l.add(i);
       }
        int [] n = new int [l.size()];
        
        for (int i=0 ; i< l.size();i++){
            n[i]=l.get(i);
        }
        return n;
    }
    
    public int getWeight(int v1, int v2){
       return adjacent [v1][v2];
    }
    
    public int[][] copyMatrix (){
        int [][] copy = new int [adjacent.length][adjacent.length];
        for (int i =0; i<adjacent.length; i++){
            for(int j=0; j< adjacent.length;j++){
                copy[i][j]= adjacent [i][j];
            }
        }        
        return copy;
    }

    @Override
    public String toString() {
        String s ="";
        for (int i =0; i<adjacent.length; i++){
            for(int j=0; j< adjacent.length;j++){
                
                s= s+adjacent[i][j]+" |";
            }
            s= s+ '\n';
        }   
        return s;
    }
}
