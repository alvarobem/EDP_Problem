package edp;


import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class GraspAlgorithm  implements Algorithm{
    private final Builder builder;
    private  LocalSearch localSearch;
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
            Solution bestSolution = new Solution();
            bestSolution.setI(new Instance(instance));
            //create the first solution
            builder.setIsFirstSolution(true);
            bestSolution=builder.build(0, numRep, bestSolution);
            builder.setIsFirstSolution(false);
        for (int contRep = 0; contRep < numRep; contRep++) {
            //Solution partialSolution = new Solution(bestSolution);
            //partialSolution.setI(new Instance(bestSolution.getI()));
            //Instance instanceCopy = bestSolution.getI();
            //Local Search
            //localSearch = new prueba();
            Solution partialSolution= localSearch.improvementSolution(bestSolution, numRep, builder, 1);
            //bestSolution.setI(instanceCopy);
            solution = partialSolution.whoIsBetter(solution);
            
            
            if (solution.getConn() == solution.getI().getNodeMatrix().size()) {
                break;
            }
        }
        return solution;
    }
}
