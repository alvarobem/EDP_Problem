package edp;


import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class GraspAlgorithm  implements Algorithm{
    private Builder builder;
    private Solution solution;
    private LocalSearch localSearch;
    private Instance instance;
    private int numRep;
            
    public static class GraspAlgorithmCreator{
        protected Builder builder;
        protected LocalSearch localSearch;
        protected Instance instance;
        
       //Opcional parameters
        protected Solution solution = null;
        protected int numRep = 1;
        
        public GraspAlgorithmCreator (LocalSearch localSearch, Instance instance){
            this.builder= new RandomizedDijkstraBuilder();
            this.localSearch = localSearch;
            this.instance = instance;
        }
        
        public GraspAlgorithmCreator setSolution(Solution solution){
            this.solution = solution;
            return this;
        }
        
        public GraspAlgorithmCreator setNumRep (int numRep){
            this.numRep = numRep;
            return this;
        }
        
        public GraspAlgorithm create(){
            return new GraspAlgorithm(this);
        }
        
    }
    
    public GraspAlgorithm (GraspAlgorithmCreator creator){
        this.builder = creator.builder;
        this.localSearch = creator.localSearch;
        this.numRep = creator.numRep;
        this.solution = creator.solution;
        this.instance = creator.instance;
    }

    @Override
    public Solution solve() {
        for (int numInstance = 1; numInstance < 21; numInstance++) {
            for (int contRep = 0; contRep < numRep; contRep++) {
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
                Solution improvementSolution = localSearch.improvementSolution(firstSolution, numRep, builder, 1);
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
