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
public class prueba implements LocalSearch {

    @Override
    public Solution improvementSolution(Solution sol, int rep, Builder builder, int numDesc) {

        ArrayList<ArrayList<Integer>> routes = sol.getRoutesConected();
        
        Collections.shuffle(routes, new Random(6));
        Solution bestSolution = new Solution(sol);
        bestSolution.setI(new Instance(sol.getI()));
        for (int pos = 0; pos < routes.size(); pos++) {
            Solution partialSolution = new Solution(sol);
            partialSolution.setI(new Instance(sol.getI()));
            for (int contDesc = 0; contDesc < numDesc; contDesc++) {
                int routeToDes = (pos + contDesc) % routes.size();
            }
            partialSolution = builder.build(0, numDesc, partialSolution);    
            if (partialSolution.isBetter(bestSolution)){
                partialSolution = builder.build(0, numDesc, partialSolution);
                int newConn = partialSolution.getConn();
                int newNotConn = bestSolution.getNotConn() - (partialSolution.getConn() -bestSolution.getConn());
                bestSolution = partialSolution;
                bestSolution.setConn(newConn);
                bestSolution.setNotConn(newNotConn);
            }
        }

        return bestSolution;
    }

}
