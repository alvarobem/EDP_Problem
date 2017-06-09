
package edp;


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
            solution.setI(new Instance(instance));
            builder.setIsFirstSolution(true);
            solution=builder.build(0, numRep, solution);
            builder.setIsFirstSolution(false);
        }
        //Solution improveSolution = localSearch.improvementSolution(solution, numRep, builder, 1);
        double kMaxAux = solution.getConn() * percentage;
        int kMax = (int) kMaxAux;
        int i = 1;
        System.out.println("Haciendo VNS...");
        while (i <= kMax) {
            Solution VNSBestSolution = localSearch.improvementSolution(solution, numRep, builder, i);
            if (!solution.isBetterOrEqual(VNSBestSolution)) {
                solution = VNSBestSolution;
                i = 0;
            }
            if (solution.getConn() >= solution.getI().getNodeMatrix().size()) {
                i = kMax + 1;
            }
            i++;
        }
        return solution;
    }
    
}
