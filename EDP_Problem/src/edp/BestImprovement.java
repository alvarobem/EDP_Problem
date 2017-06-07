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
public class BestImprovement implements LocalSearch {

    @Override
    public Solution improvementSolution(Solution sol, int rep, Builder builder, int numDesc) {
        Solution bestSolution = new Solution(sol);
        int size = bestSolution.getRoutes().size();
        ArrayList<int[]> matrixCopy;
        ArrayList<int[]> disconected = new ArrayList<>();
        int pos = 1;

        Collections.shuffle(bestSolution.getRoutes(), new Random(6));
        ArrayList<ArrayList<Integer>> routes = bestSolution.getRoutes();
        while (pos <= size) {
            Solution auxBestSolution = new Solution(bestSolution);
            if (auxBestSolution.isRouteConected(pos - 1)) {
                int countDesc = 0;
                int auxPos = pos;
                Instance auxInstance = new Instance(sol.getI());
                Solution auxSolution = new Solution(sol);
                auxSolution.setI(auxInstance);
                if (auxBestSolution.getI() == null) {
                    auxBestSolution.setI(auxInstance);
                }
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

                builder.build(0, numDesc, auxSolution);

                auxBestSolution = auxSolution.whoIsBetter(auxBestSolution);

                if (auxBestSolution.getConn() >= 1) {
                    Solution aux = new Solution(auxBestSolution);
                    aux.setI(auxBestSolution.getI());
                    aux.getI().setNodeMatrix(disconected);
                    builder.build(0, numDesc, aux);
                    auxBestSolution.getI().setNodeMatrix(matrixCopy);
                    if (aux.getConn() > 0) {
                        int newConn = auxBestSolution.getConn() + aux.getConn();
                        int newNotConn = auxBestSolution.getNotConn() - aux.getConn();
                        //bestSolution.getI().setNodeMatrix(auxBestSolution.getI().getNodeMatrix());
                        for (ArrayList<Integer> route : aux.getRoutes()) {
                            if (route.size() > 0) {
                                auxBestSolution.getI().getG().setAdjacent(Utils.deleteEdges(auxBestSolution.getI().getG().getAdjacent(), route));
                            }
                        }
                        auxBestSolution.setConn(newConn);
                        auxBestSolution.setNotConn(newNotConn);
                    }
                }

            }
            bestSolution = auxBestSolution.whoIsBetter(bestSolution);
            pos++;
            if (bestSolution.getConn() == sol.getI().getNodeMatrix().size()) {
                break;
            }
        }
        if (bestSolution.getConn() > 0) {
            int newConn = sol.getConn() + bestSolution.getConn();
            int newNotConn = sol.getNotConn() - bestSolution.getConn();
            //bestSolution.getI().setNodeMatrix(auxBestSolution.getI().getNodeMatrix());
            if (bestSolution.getI() == null) {
                bestSolution.setI(new Instance(sol.getI()));
            }
            for (ArrayList<Integer> route : bestSolution.getRoutes()) {
                if (route.size() > 0) {
                    bestSolution.getI().getG().setAdjacent(Utils.deleteEdges(bestSolution.getI().getG().getAdjacent(), route));
                }
            }
            bestSolution.setConn(newConn);
            bestSolution.setNotConn(newNotConn);

        }
        return bestSolution;
    }

}
