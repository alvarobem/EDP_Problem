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
        int pos = 1;
        
        Collections.shuffle(bestSolution.getRoutes(), new Random(6));
        while (pos <= size) {
            Solution auxBestSolution = new Solution(bestSolution);
            for (int aux = 0; aux < rep; aux++) {
                if (auxBestSolution.isRouteConected(pos-1)) { 
                    Instance auxInstance = new Instance(sol.getI());
                    auxBestSolution.setI(auxInstance);
                    Solution auxSolution = new Solution(auxBestSolution);
                    auxSolution.setI(auxInstance);
                    int countDesc = 1;
                    int auxPos = pos;
                    auxSolution.deletePair(pos-1);
                    while (countDesc < numDesc && auxPos <=size ){
                        if(auxBestSolution.isRouteConected(auxPos-1)){
                            auxSolution.deletePair(auxPos-1);
                            countDesc ++;
                        }
                        auxPos++;
                    } 
                    
                    ArrayList<int[]> notCon = auxBestSolution.getRoutesNotConected();
                    if(notCon.isEmpty())
                        break;
                    auxSolution.getI().setNodeMatrix(notCon);
                    int cont = 0;
                    ArrayList<Integer> del = new ArrayList<>();
                    for (int j = 0; j < sol.getI().getNodeMatrix().size(); j++) {
                        if (sol.getI().getNodeMatrix().get(j)[0]== auxSolution.getI().getNodeMatrix().get(cont)[0]){
                            del = builder.build(cont, auxSolution);
                            if (!del.isEmpty()){
                                auxSolution.addRoute(del, j);
                                auxInstance.getG().setAdjacent(Utils.deleteEdges(auxInstance.getG().getAdjacent(), del));
                            } 
                            cont++;
                        }
                        if (cont >= auxSolution.getI().getNodeMatrix().size())
                            break;
                    }
                    auxBestSolution = auxSolution.whoIsBetter(auxBestSolution);
                }
            }
            int conn = auxBestSolution.getConn();
            int newNotConn =sol.getNotConn()- auxBestSolution.getConn();
            int newConn = auxBestSolution.getConn()+ sol.getConn();
            auxBestSolution.setConn(newConn);
            auxBestSolution.setNotConn(newNotConn);
            if (conn>=1){
                ArrayList<int[]> pair = new ArrayList<>();
                pair.add(sol.getI().getNodeMatrix().get(pos-1));
                for( int i =0; i<= rep; i++){
                    Solution aux = new Solution(auxBestSolution);
                    aux.setI(auxBestSolution.getI());
                    aux.getI().setNodeMatrix(pair);
                    ArrayList<Integer> del = builder.build(0, aux);
                    if (!del.isEmpty()){
                        auxBestSolution.addRoute(del, pos-1);
                        auxBestSolution.addConn();
                        auxBestSolution.setNotConn(auxBestSolution.getNotConn()-1);
                        break;
                    }
                }
            }
            
            if (auxBestSolution.isBetter(bestSolution)){   
                bestSolution = auxBestSolution;    
            }
            pos ++;
            if (bestSolution.getConn()==10){
                break;
            }
            
        }
        return bestSolution;
    }
    
}
