
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
    private final double percentage;
    private final Builder builder;
    private final LocalSearch localSearch;
    private final int numRep;
      
    public static class Creator{
        private final Builder builder;
        private final LocalSearch localSearch;
        
       //Opcional parameters
        private int numRep = 1;
        private double percentage= 0.5;
        
        public Creator (Builder builder, LocalSearch localSearch){
            this.builder = builder;
            this.localSearch = localSearch;
        }
        public Creator percentage(double percentage){
            this.percentage = percentage;
            return this;
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
        this.percentage = creator.percentage;
    }
    
    @Override
    public Solution solve(Instance instance, Solution solution) {
        
        if (solution == null) {
            //create the first solution
            System.out.println("Creando la primera solución");
            solution = new Solution();
            Instance solutionInstance = new Instance(instance);
            solution.setI(solutionInstance);
            ArrayList<Integer> del;
            for (int i = 0; i < solutionInstance.getNodeMatrix().size(); i++) {
                del = builder.build(i, solution);
                solution.addRoute(del);
                try {
                    solutionInstance.getG().setAdjacent(Utils.deleteEdges(solutionInstance.getG().getAdjacent(), del));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        Solution improveSolution = localSearch.improvementSolution(solution, numRep, builder, 1);
        double kMaxAux = improveSolution.getConn() * percentage;
        int kMax = (int) kMaxAux;
        int i = 0;
        System.out.println("Haciendo VNS...");
        while (i <= kMax) {
            Solution VNSBestSolution = localSearch.improvementSolution(improveSolution, numRep, builder, i);
            if (!improveSolution.isBetterOrEqual(VNSBestSolution)) {
                improveSolution = VNSBestSolution;
                i = 0;
            }
            if (improveSolution.getConn() >= improveSolution.getI().getNodeMatrix().size()) {
                i = kMax + 1;
            }
            i++;
        }
        solution = improveSolution;
        return solution;
    }
    
}
