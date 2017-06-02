
package edp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author alvaroberrocal
 */
public class EdpSolver {
    
    private Algorithm algorithm;
    private Solution solution;
    private String selectedOption;
    private boolean useMeta; 
    
    private ArrayList<String> graphs;
    private ArrayList<String> files;

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public void setUseMeta(boolean useMeta) {
        this.useMeta = useMeta;
    }
    
    public EdpSolver (){
        graphs = new ArrayList<>();
        files = new ArrayList<>();
        readImputFile();
    }
    
    private void readImputFile (){
      try {
         File f = new File ("entrada.txt");
         FileReader fr = new FileReader (f);
         BufferedReader br = new BufferedReader(fr);
 
         // Lectura del fichero
         String line;
         while((line=br.readLine())!=null){
             String [] splitedInput = line.split("@");
             graphs.add(splitedInput[0]);
             files.add(splitedInput[1]);
         }      
      }
      catch(Exception e){
         e.printStackTrace();
      }
   
    }
    
    public static void printBuilderMenu() {
        System.out.println("Seleccione el constructor que desea usar");
        System.out.println("Pulse 1 para Dijkstra");
        System.out.println("Pulse 2 para Dijkstra Aleatorizado");
    }
    
    public boolean solve (String graph, String matrix, String nameOutput, String nameAlgoritm){
        for (int numInstance = 1; numInstance < 21; numInstance++) {
            Instance instance = new Instance(graph, matrix+numInstance);
            double time;
            double start = System.currentTimeMillis();
            solution = algorithm.solve(instance, solution);
            double end = System.currentTimeMillis();
            time = end - start;
            time = time / 1000;
            solution.setTime(time);
            if (Utils.isValidSolution(solution)){
                //System.out.println(solution.routesToString());
                System.out.println("Conectados: "+solution.getConn()+" en : "+solution.getTime()+"s");
                Utils.writeFile(solution, "salidas/"+nameOutput+".csv", nameAlgoritm);
            }else{
                System.err.println("Solución incorrecta");
                System.err.println(solution.routesToString());
                return false;
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
        return true;
    }

    public static void main(String[] args) {
        EdpSolver edp = new EdpSolver();
        String selectedOption;
        Scanner sn = new Scanner(System.in);
        boolean useMeta= true;
        
        
        System.out.println("Seleccione la metaheurística que desea usar");
        System.out.println("Pulse 1 para GRASP");
        System.out.println("Pulse 2 para VNS");
        System.out.println("Pulse 3 para híbrido GRASP-VNS");
        System.out.println("Pulse 4 para ninguna");
        System.out.println("Pulse 5 para lanzar todo");
        selectedOption = sn.nextLine();
        Solution solution = null;
        LocalSearch localSearch = new BestImprovement();
        Algorithm algorithm = null;
        Builder builder= null;
        String builderOption;
        String nameAlgoritm="";
        switch (selectedOption){
            case "1":
                nameAlgoritm = "GRASP";
                algorithm = new GraspAlgorithm.Creator(localSearch).numRep(1).create();
                solution = new Solution();
                break;
            case "2": 
                nameAlgoritm = "VNS";
                printBuilderMenu();
                builderOption = sn.nextLine();
                builder = ("1".equals(builderOption))? new DijkstraBuilder(): new RandomizedDijkstraBuilder();
                nameAlgoritm +=("1".equals(builderOption))? "-Dijkstra": "-RandomizedDijkstra";
                algorithm = new VnsAlgorithm.Creator(builder, localSearch).numRep(10).create();
                solution = null;
                break;
            case "3":
                nameAlgoritm = "Hibrido";
                printBuilderMenu();
                builderOption = sn.nextLine();
                builder = ("1".equals(builderOption))? new DijkstraBuilder(): new RandomizedDijkstraBuilder();
                nameAlgoritm +=("1".equals(builderOption))? "-Dijkstra": "-RandomizedDijkstra";
                algorithm = new HybridAlgorithm.Creator(builder, localSearch).numRep(10).percentage(0.5).create();
                solution = new Solution();
                break;
            case "4":
                builderOption = sn.nextLine();
                builder = ("1".equals(builderOption))? new DijkstraBuilder(): new RandomizedDijkstraBuilder();
                nameAlgoritm = ("1".equals(builderOption))? "Dijkstra-": "RandomizedDijkstra-";
                useMeta = false;
        }
        
        edp.setAlgorithm(algorithm);
        edp.setSelectedOption(selectedOption);
        edp.setSolution(solution);
        edp.setUseMeta(useMeta);
        for (int i = 0; i<= edp.files.size()-1;i++){
            String fileName = edp.files.get(i);
            String [] splitedName;
            splitedName = fileName.split("\\.");
            String nameOutput= splitedName[0]+"."+splitedName[splitedName.length-1];
            nameOutput = nameOutput.replace("/", ".");
            if(!edp.solve(edp.graphs.get(i), edp.files.get(i), nameOutput, nameAlgoritm )){
                break;
            }
        }
        
       
    }
    
}
