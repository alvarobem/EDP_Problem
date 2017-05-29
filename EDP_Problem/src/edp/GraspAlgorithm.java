/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edp;

import java.util.ArrayList;

/**
 *
 * @author atg
 */
public class GraspAlgorithm  implements Algorithm{

    @Override
    public Solution solve(LocalSearch localSearch, int rep) {
        Builder builder = new RandomizedDijkstraBuilder();
        Solution solution = null;
        for (int numInstance = 1; numInstance < 21; numInstance++) {
            solution = new Solution();
            Instance instance = new Instance(Utils.NAME_GRAPH, Utils.NAME_MATRIX + numInstance);
            double time = 0;
            double start = System.currentTimeMillis();
            for (int contRep = 0; contRep < rep; contRep++) {
                Instance solutionInstance = new Instance(instance);
                solution.setI(solutionInstance);
                Solution firstSolution = new Solution();
                firstSolution.setI(solutionInstance);
                ArrayList<Integer> del;
                //create the first solution
                for (int i = 0; i < solutionInstance.getNodeMatrix().size(); i++) {
                    del = builder.build(i, solution);
                    firstSolution.addRoute(del);
                    solutionInstance.getG().setAdjacent(Utils.deleteEdges(solutionInstance.getG().getAdjacent(), del));
                }
                //Local Search
                Solution improvementSolution = localSearch.improvementSolution(firstSolution, rep, builder, 1);
                improvementSolution.i = firstSolution.getI();
                solution = improvementSolution.whoIsBetter(solution);
                if (solution.getConn() == solution.getI().getNodeMatrix().size()) {
                    break;
                }
            }  
        }
        return solution;
    }
    
}
