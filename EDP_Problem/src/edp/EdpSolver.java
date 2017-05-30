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

    public static void main(String[] args) {



        String selectedOption = "1";
        Scanner sn = new Scanner(System.in);

        System.out.println("Seleccione la metaheurística que desea usar");
        System.out.println("Pulse 1 para GRASP");
        System.out.println("Pulse 2 para VNS");
        System.out.println("Pulse 3 para GRASP-VNS");
        System.out.println("Pulse 4 ninguna");
        selectedOption = sn.nextLine();
        Solution solution = null;
        LocalSearch localSearch = new BestImprovement();
        Algorithm algorithm = null;
        Builder builder= null;
        switch (selectedOption){
            case "1":
                algorithm = new GraspAlgorithm.Creator(localSearch).numRep(1).create();
                solution = new Solution();
                break;
            case "2": 
                String builderOption = "1";
                System.out.println("Seleccione el constructor que desea usar");
                System.out.println("Pulse 1 para Dijkstra");
                System.out.println("Pulse 2 para Dijkstra Aleatorizado");
                builderOption = sn.nextLine();
                builder = ("1".equals(builderOption))? new DijkstraBuilder(): new RandomizedDijkstraBuilder();
                algorithm = new VnsAlgorithm.Creator(builder, localSearch).numRep(10).create();
                solution = null;
                break;
        }
        for (int numInstance = 1; numInstance < 21; numInstance++) {
            Instance instance = new Instance(Utils.NAME_GRAPH, Utils.NAME_MATRIX+numInstance);
            double time = 0;
            double start = System.currentTimeMillis();
            solution = algorithm.solve(instance, solution);
            double end = System.currentTimeMillis();
            
            time = end - start;
            time = time / 1000;
            solution.setTime(time);
            System.out.println(solution.routesToString());
            System.out.println("Conectados: "+solution.getConn()+" en : "+solution.getTime()+"s");
            //Utils.writeFile(solution, "salidavns.csv");
            switch (selectedOption){
                case "1":
                    solution = new Solution();
                    break;
                case "2":
                    solution = null;
                    break;
            }
        }   
        
    }
    
}
