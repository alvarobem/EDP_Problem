/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edp;

import java.util.Scanner;

/**
 *
 * @author alvaroberrocal
 */
public class EdpSolver {
    
    public static void printBuilderMenu() {
        System.out.println("Seleccione el constructor que desea usar");
        System.out.println("Pulse 1 para Dijkstra");
        System.out.println("Pulse 2 para Dijkstra Aleatorizado");
    }

    public static void main(String[] args) {
        String selectedOption;
        Scanner sn = new Scanner(System.in);

        System.out.println("Seleccione la metaheurística que desea usar");
        System.out.println("Pulse 1 para GRASP");
        System.out.println("Pulse 2 para VNS");
        System.out.println("Pulse 3 para híbrido GRASP-VNS");
        System.out.println("Pulse 4 para ninguna");
        selectedOption = sn.nextLine();
        Solution solution = null;
        LocalSearch localSearch = new BestImprovement();
        Algorithm algorithm = null;
        Builder builder= null;
        String builderOption;
        switch (selectedOption){
            case "1":
                algorithm = new GraspAlgorithm.Creator(localSearch).numRep(1).create();
                solution = new Solution();
                break;
            case "2": 
                printBuilderMenu();
                builderOption = sn.nextLine();
                builder = ("1".equals(builderOption))? new DijkstraBuilder(): new RandomizedDijkstraBuilder();
                algorithm = new VnsAlgorithm.Creator(builder, localSearch).numRep(10).create();
                solution = null;
                break;
            case "3":
                printBuilderMenu();
                builderOption = sn.nextLine();
                builder = ("1".equals(builderOption))? new DijkstraBuilder(): new RandomizedDijkstraBuilder();
                algorithm = new HybridAlgorithm.Creator(builder, localSearch).numRep(10).percentage(0.5).create();
                solution = new Solution();
        }
        for (int numInstance = 1; numInstance < 21; numInstance++) {
            Instance instance = new Instance(Utils.NAME_GRAPH, Utils.NAME_MATRIX+numInstance);
            double time;
            double start = System.currentTimeMillis();
            solution = algorithm.solve(instance, solution);
            double end = System.currentTimeMillis();
            time = end - start;
            time = time / 1000;
            solution.setTime(time);
            if (Utils.isValidSolution(solution)){
                System.out.println(solution.routesToString());
                System.out.println("Conectados: "+solution.getConn()+" en : "+solution.getTime()+"s");
                Utils.writeFile(solution, Utils.OUTPUT_NAME_FILE);
            }else{
                System.err.println("Solución incorrecta");
                System.err.println(solution.routesToString());
                break;
            }
            
            switch (selectedOption){
                case "1":
                case "3":
                    solution = new Solution();
                    break;
                case "2":
                    solution = null;
                    break;
            }
        }   
        
    }
    
}
