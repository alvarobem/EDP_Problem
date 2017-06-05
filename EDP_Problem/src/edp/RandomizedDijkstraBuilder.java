
package edp;

import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class RandomizedDijkstraBuilder implements Builder{
    
    private boolean isFirstSolution;

    
    public void setIsFirstSolution(boolean isFirstSolution) {
        this.isFirstSolution = isFirstSolution;
    }
    
    private ArrayList<Integer> doRandomizedDijkstra (int pos, Solution s){
        int ini = s.getI().getNodeMatrix().get(pos)[0];
        int fin = s.getI().getNodeMatrix().get(pos)[1];
        int [][] m = s.getI().getG().getAdjacent();
        Instance ins = s.getI();
       // valores iniciales
         if (ini== 5000){
             s.addNotConn();
             return new ArrayList<>();
             
         }
        int n = ins.getG().getNodes();
        
        boolean [] visitados= new boolean [n];
        int [] costes = new int [n];
        int ultimo []= new int [n]; // ultimo vertice que se visito
        int [][] matrix = m;
        for (int i=0; i<n ; i++){
            visitados [i]=false;
            costes [i] = matrix[ini][i];
            ultimo[i]=matrix[ini][i]<100000?ini:0;
        }
        
        visitados[ini]=true;
        costes[ini]=0;

        ultimo[ini]=0;
        
        // marcar los n-1 vertices
        ArrayList <Integer> l = new ArrayList<> ();
        for (int i=0; i<n-1;i++){
         
        int v;
             for (v=0; (v<visitados.length) && (visitados[v]); v++) {}
         
         int men=v;
         
         for (; v<visitados.length; v++) {
             int rand = (int) (MyRandom.nextInt(1000)) % 2;
            if (!(visitados[v]) && (costes[v]<costes[men]) && (costes[v]>0) && rand !=0)
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
        Utils.printPaths (ultimo, ini,fin,  costes, del, s);
        return del;
        
    }
    @Override
    public void build(int pos, int numRep, Solution solution) {
        Solution bestSol = new Solution(solution);
        int auxCont = pos;
        ArrayList<Integer> nodesToDelete = new ArrayList<>();
        for (int aux = 0; aux < numRep; aux++) {
            for (int j = 0; j < solution.getI().getNodeMatrix().size(); j++) {
                nodesToDelete = doRandomizedDijkstra(auxCont, solution);
                if (!nodesToDelete.isEmpty()) {
                    if (isFirstSolution){
                        solution.addRoute(nodesToDelete);
                    }else{
                        solution.addRoute(nodesToDelete, j);
                    }
                    solution.getI().getG().setAdjacent(Utils.deleteEdges(solution.getI().getG().getAdjacent(), nodesToDelete));
                }else{
                    if (isFirstSolution){
                        solution.addRoute(new ArrayList<>());
                    }else{
                        solution.addRoute(new ArrayList<>(),j);
                    }
                }
                auxCont++;
                if (auxCont >= solution.getI().getNodeMatrix().size()) {
                    break;
                }
            }
            bestSol = solution.whoIsBetter(bestSol);
            auxCont = pos;
        }
        solution = bestSol;
    }
}
