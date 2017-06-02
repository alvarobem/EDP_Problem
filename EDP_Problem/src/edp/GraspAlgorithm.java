package edp;


import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class GraspAlgorithm  implements Algorithm{
    private final Builder builder;
    private final LocalSearch localSearch;
    private final int numRep;
            
    public static class Creator{
        private Builder builder;
        private LocalSearch localSearch;
        
       //Opcional parameters
        private int numRep = 1;
        
        public Creator (LocalSearch localSearch){
            this.builder= new RandomizedDijkstraBuilder();
            this.localSearch = localSearch;
        }
        
        public Creator numRep (int numRep){
            this.numRep = numRep;
            return this;
        }
        
        public GraspAlgorithm create(){
            return new GraspAlgorithm(this);
        }
        
    }
    
    public GraspAlgorithm (Creator creator){
        this.builder = creator.builder;
        this.localSearch = creator.localSearch;
        this.numRep = creator.numRep;
    }

    @Override
    public Solution solve(Instance instance, Solution solution) {
        
        for (int contRep = 0; contRep < numRep; contRep++) {
            Solution bestSolution = new Solution();
            Instance solutionInstance = new Instance(instance);
            bestSolution.setI(solutionInstance);
            ArrayList<Integer> del;
            //create the first solution
            bestSolution = builder.build(0, numRep, bestSolution);
            //Instance instanceCopy = bestSolution.getI();
            //Local Search
            bestSolution= localSearch.improvementSolution(bestSolution, numRep, builder, 1);
            //bestSolution.setI(instanceCopy);
            solution = bestSolution.whoIsBetter(solution);
            
            
            if (solution.getConn() == solution.getI().getNodeMatrix().size()) {
                break;
            }
        }
        return solution;
    }
}
