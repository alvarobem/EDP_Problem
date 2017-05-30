
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
    private Builder builder;
    private Solution solution;
    private LocalSearch localSearch;
    private Instance instance;
    private int numRep;
            
    public static class VnsAlgorithmCreator{
        protected Builder builder;
        protected LocalSearch localSearch;
        protected Instance instance;
        
       //Opcional parameters
        protected Solution solution = null;
        protected int numRep = 1;
        
        public VnsAlgorithmCreator (Builder builder, LocalSearch localSearch, Instance instance){
            this.builder = builder;
            this.localSearch = localSearch;
            this.instance = instance;
        }
        
        public VnsAlgorithmCreator setSolution(Solution solution){
            this.solution = solution;
            return this;
        }
        
        public VnsAlgorithmCreator setNumRep (int numRep){
            this.numRep = numRep;
            return this;
        }
        
        public VnsAlgorithm create(){
            return new VnsAlgorithm(this);
        }
        
    }
    
    public VnsAlgorithm (VnsAlgorithmCreator creator){
        this.builder = creator.builder;
        this.localSearch = creator.localSearch;
        this.numRep = creator.numRep;
        this.solution = creator.solution;
        this.instance = creator.instance;
    }

    @Override
    public Solution solve() {
        if (solution == null) {
            //create the first solution
            Instance solutionInstance = new Instance(instance);
            solution.setI(solutionInstance);
            solution.setI(solutionInstance);
            ArrayList<Integer> del;
            for (int i = 0; i < solutionInstance.getNodeMatrix().size(); i++) {
                del = builder.build(i, solution);
                solution.addRoute(del);
                solutionInstance.getG().setAdjacent(Utils.deleteEdges(solutionInstance.getG().getAdjacent(), del));
            }
        }
        double kMaxAux = solution.getConn() * PERCENTAGE;
        int kMax = (int) kMaxAux;
        int i = 0;
        while (i <= kMax) {
            Instance auxInstance = new Instance(instance);
            solution.setI(auxInstance);
            Solution VNSBestSolution = localSearch.improvementSolution(solution, numRep, builder, i);
            if (!solution.isBetterOrEqual(VNSBestSolution)) {
                solution = VNSBestSolution;
                i = 0;
            }
            if (solution.getConn() == solution.getI().getNodeMatrix().size()) {
                i = kMax + 1;
            }
            i++;
        }
        return solution;
    }
    
}
