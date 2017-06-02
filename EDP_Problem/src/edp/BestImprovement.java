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
        bestSolution.setConn(sol.getConn());
        bestSolution.setNotConn(sol.getNotConn());
        ArrayList<int[]> matrixCopy;
        ArrayList<int[]> disconected = new ArrayList<>();
        int pos = 1;
        
        Collections.shuffle(bestSolution.getRoutes(), new Random(6));
        while (pos <= size) {
            Solution auxBestSolution = new Solution(bestSolution);
            if (auxBestSolution.isRouteConected(pos-1)) { 
                int countDesc = 0;
                int auxPos = pos;
                Instance auxInstance = new Instance(sol.getI());
                Solution auxSolution = new Solution(sol);
                auxSolution.setI(auxInstance);
                ArrayList<int[]> notCon = auxBestSolution.getRoutesNotConected();

                while (countDesc < numDesc && auxPos < size) {
                    if (auxSolution.isRouteConected(auxPos - 1)) {
                        disconected.add(auxSolution.deletePair(auxPos - 1));
                        countDesc++;
                    }
                    auxPos++;
                }

                matrixCopy = auxSolution.getI().getNodeMatrix();
                auxSolution.getI().setNodeMatrix(notCon);
            
                Solution newSolution = builder.build(0, numDesc, auxSolution);
            
                newSolution.getI().setNodeMatrix(matrixCopy);
                auxBestSolution = newSolution.whoIsBetter(auxBestSolution);
                matrixCopy = auxBestSolution.getI().getNodeMatrix();
                
                if (auxBestSolution.getConn()>=1){
                    Solution aux = new Solution(auxBestSolution);
                    aux.setI(auxBestSolution.getI());
                    aux.getI().setNodeMatrix(disconected);
                    
                    auxBestSolution.getI().setNodeMatrix(matrixCopy);
                }

                if (auxBestSolution.isBetter(bestSolution)) {
                    int newConn = bestSolution.getConn() + auxBestSolution.getConn();
                    int newNotConn = bestSolution.getNotConn() - auxBestSolution.getConn();
                    bestSolution = auxBestSolution;
                    bestSolution.setConn(newConn);
                    bestSolution.setNotConn(newNotConn);

                }

            }
            pos++;
            if (bestSolution.getConn() == sol.getI().getNodeMatrix().size()) {
                break;
            }
        }
        return bestSolution;
    }
    
}
