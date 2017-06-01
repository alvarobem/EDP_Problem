/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author atg
 */
public class BestImprovement implements LocalSearch{

    @Override
    public Solution improvementSolution(Solution sol, int rep, Builder builder, int numDesc) {
        Solution bestSolution = new Solution(sol); 
        int size = bestSolution.getRoutes().size();
        ArrayList<int[]> matrixCopy;
        ArrayList<int[]> disconected = new ArrayList<>();
        int pos = 1;
        
        Collections.shuffle(bestSolution.getRoutes(), new Random(6));
        while (pos <= size) {
            Solution auxBestSolution = new Solution(bestSolution);
            for (int aux = 0; aux < rep; aux++) {
                if (auxBestSolution.isRouteConected(pos-1)) { 
                    Instance auxInstance = new Instance(sol.getI());
                    //auxBestSolution.setI(auxInstance);
                    Solution auxSolution = new Solution(sol);
                    auxSolution.setI(auxInstance);
                    int countDesc = 0;
                    int auxPos = pos;
                    ArrayList<int[]> notCon = auxBestSolution.getRoutesNotConected();
                    //disconect pairs
                    //disconected.add(auxSolution.deletePair(pos-1)) ;
                    
                    while (countDesc < numDesc && auxPos < size ){
                        if(auxSolution.isRouteConected(auxPos-1)){
                            disconected.add(auxSolution.deletePair(auxPos-1));
                            countDesc ++;
                        }
                        auxPos++;
                    } 
                    matrixCopy = auxSolution.getI().getNodeMatrix();
                    
                    if(notCon.isEmpty())
                        break;
                    auxSolution.getI().setNodeMatrix(notCon);
                    int cont = 0;
                    ArrayList<Integer> del;
                    for (int j = 0; j < auxSolution.getI().getNodeMatrix().size(); j++) {
                        del = builder.build(cont, auxSolution);
                        if (!del.isEmpty()) {
                            auxSolution.addRoute(del, j);
                            auxInstance.getG().setAdjacent(Utils.deleteEdges(auxInstance.getG().getAdjacent(), del));
                        }
                        cont++;

                        if (cont >= auxSolution.getI().getNodeMatrix().size()) {
                            break;
                        }
                    }
                    
                    auxSolution.getI().setNodeMatrix(matrixCopy);
                    auxBestSolution = auxSolution.whoIsBetter(auxBestSolution);
                }
            }
            int conn = auxBestSolution.getConn();
            int newNotConn =sol.getNotConn()- auxBestSolution.getConn();
            int newConn = auxBestSolution.getConn()+ sol.getConn();
            auxBestSolution.setConn(newConn);
            auxBestSolution.setNotConn(newNotConn);
            matrixCopy = auxBestSolution.getI().getNodeMatrix();
            if (conn>=1){
                for( int i =0; i<= rep; i++){
                    Solution aux = new Solution(auxBestSolution);
                    aux.setI(auxBestSolution.getI());
                    aux.getI().setNodeMatrix(disconected);
                    ArrayList<Integer> del = builder.build(0, aux);
                    
                    if (!del.isEmpty()){
                        
                        auxBestSolution.getI().getG().setAdjacent(Utils.deleteEdges(auxBestSolution.getI().getG().getAdjacent(), del));
                        newNotConn = auxBestSolution.getNotConn() - aux.getConn();
                        newConn = auxBestSolution.getConn()+ aux.getConn();
                        auxBestSolution.setNotConn(newNotConn);
                        auxBestSolution.setConn(newConn);
                        auxBestSolution.addRoute(del, pos-1);
                        break;
                    }
                }
                auxBestSolution.getI().setNodeMatrix(matrixCopy);
            }
            
            if (auxBestSolution.isBetter(bestSolution)){   
                bestSolution = auxBestSolution;    
            }
            pos ++;
            if (bestSolution.getConn()==sol.getI().getNodeMatrix().size()){
                break;
            }
            
        }
        return bestSolution;
    }
    
}
