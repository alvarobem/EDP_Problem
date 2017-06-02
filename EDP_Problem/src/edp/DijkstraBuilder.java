
package edp;

import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class DijkstraBuilder implements Builder{
    
    
    public ArrayList<Integer> doDijkstra (int pos,  Solution solution) {
        Instance instance = solution.getI();
        int start = solution.getI().getNodeMatrix().get(pos)[0];
        int end = solution.getI().getNodeMatrix().get(pos)[1];
        if (start== 5000){
            solution.addNotConn();
            return new ArrayList<>();
             
         }
       // valores iniciales
        int n = instance.getG().getNodes();
        
        boolean [] visitados= new boolean [n];
        int [] costes = new int [n];
        int ultimo []= new int [n]; // ultimo vertice que se visito
        int [][] matrix = solution.getI().getG().getAdjacent();
        for (int i=0; i<n ; i++){
            visitados [i]=false;
            costes [i] = matrix[start][i];
            ultimo[i]=matrix[start][i]<100000?start:0;
        }
        
        visitados[start]=true;
        costes[start]=0;

        ultimo[start]=0;
        
        // marcar los n-1 vertices
        for (int i=0; i<n-1;i++){
            int v;
            for (v=0; (v<visitados.length) && (visitados[v]); v++) {}
         
         int men=v;
         for (; v<visitados.length; v++) {
            if (!(visitados[v]) && (costes[v]<costes[men]) && (costes[v]>0))
               men = v;
         }
            visitados[men]=true;
            //actualiza la distancia de vertices no marcados
            if (costes[men]==-1)
                continue;
            for (int w = 0; w < n; w++) {
                if (!visitados[w]) {
                    if ((matrix [men][w]==-1))
                        continue;
                    if ((costes[men] + matrix[men][w] < costes[w])) {
                        costes[w] = costes[men] + matrix[men][w];
                        ultimo[w] = men;
                    }

                }
            }
    }
    ArrayList<Integer> del = new ArrayList<>();
    Utils.printPaths (ultimo, start,end,  costes, del, solution);
    return del;
        
    }   

    @Override
    public Solution build(int pos, int numRepm, Solution solution) {
        ArrayList<Integer> nodesToDelete = new ArrayList<>();
        for (int j = 0; j < solution.getI().getNodeMatrix().size(); j++) {
            nodesToDelete = doDijkstra(pos, solution);
            if (!nodesToDelete.isEmpty()) {
                solution.addRoute(nodesToDelete, j);
                solution.getI().getG().setAdjacent(Utils.deleteEdges(solution.getI().getG().getAdjacent(), nodesToDelete));
            }
            pos++;
            if (pos >= solution.getI().getNodeMatrix().size()) {
                break;
            }
        }
        return solution;
    } 
}
