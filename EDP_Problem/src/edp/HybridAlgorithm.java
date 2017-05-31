/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edp;

/**
 *
 * @author atg
 */
public class HybridAlgorithm implements Algorithm{
    private final double percentage ;
    private final Builder builder;
    private final LocalSearch localSearch;
    private final int numRep;

   
      
    public static class Creator{
        private final Builder builder;
        private final LocalSearch localSearch;
        
        
       //Opcional parameters
        private int numRep = 1;
        private double percentage = 0.5;
       
        public Creator (Builder builder, LocalSearch localSearch){
            this.builder = builder;
            this.localSearch = localSearch;
        }
        
        public Creator numRep (int numRep){
            this.numRep = numRep;
            return this;
        }
        
        public Creator percentage(double percentage){
            this.percentage = percentage;
            return this;
        }
        
        
        public HybridAlgorithm create(){
            return new HybridAlgorithm(this);
        }
        
    }
    public HybridAlgorithm (HybridAlgorithm.Creator creator){
        this.builder = creator.builder;
        this.localSearch = creator.localSearch;
        this.numRep = creator.numRep;
        this.percentage = creator.percentage;
    }
    
     @Override
    public Solution solve(Instance instance, Solution solution) {
        Algorithm algorithm = new GraspAlgorithm.Creator(localSearch).numRep(numRep).create();
        solution = algorithm.solve(instance, solution);
        algorithm = new VnsAlgorithm.Creator(builder, localSearch).numRep(numRep).percentage(percentage).create();
        solution = algorithm.solve(solution.getI(), solution);
        return solution;
    }
    
}
