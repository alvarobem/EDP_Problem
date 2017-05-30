
package edp;

import edp.Algorithm;
import edp.Builder;
import edp.Instance;
import edp.LocalSearch;
import edp.Solution;
import edp.Utils;
import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class VnsAlgorithm implements Algorithm{
    private final double PERCENTAGE = 0.5;
    private final Builder builder;
    private final LocalSearch localSearch;
    private final int numRep;
      
    public static class Creator{
        private final Builder builder;
        private final LocalSearch localSearch;
        
       //Opcional parameters
        private int numRep = 1;
        
        public Creator (Builder builder, LocalSearch localSearch){
            this.builder = builder;
            this.localSearch = localSearch;
        }
        
        public Creator numRep (int numRep){
            this.numRep = numRep;
            return this;
        }
        
        
        public VnsAlgorithm create(){
            return new VnsAlgorithm(this);
        }
        
    }
    
    public VnsAlgorithm (Creator creator){
        this.builder = creator.builder;
        this.localSearch = creator.localSearch;
        this.numRep = creator.numRep;
    }
    
    @Override
    public Solution solve(Instance instance, Solution solution) {
        
        if (solution == null) {
            //create the first solution
            solution = new Solution();
            Instance solutionInstance = new Instance(instance);
            solution.setI(solutionInstance);
            ArrayList<Integer> del;
            for (int i = 0; i < solutionInstance.getNodeMatrix().size(); i++) {
                del = builder.build(i, solution);
                solution.addRoute(del);
                solutionInstance.getG().setAdjacent(Utils.deleteEdges(solutionInstance.getG().getAdjacent(), del));
            }
        }
        Solution improveSolution = localSearch.improvementSolution(solution, numRep, builder, 1);
        double kMaxAux = improveSolution.getConn() * PERCENTAGE;
        int kMax = (int) kMaxAux;
        int i = 0;
        while (i <= kMax) {
            Instance auxInstance = new Instance(solution.getI());
            improveSolution.setI(auxInstance);
            Solution VNSBestSolution = localSearch.improvementSolution(improveSolution, numRep, builder, i);
            if (!improveSolution.isBetterOrEqual(VNSBestSolution)) {
                improveSolution = VNSBestSolution;
                i = 0;
            }
            if (improveSolution.getConn() == improveSolution.getI().getNodeMatrix().size()) {
                i = kMax + 1;
            }
            i++;
        }
        solution = improveSolution;
        return solution;
    }
    
}
