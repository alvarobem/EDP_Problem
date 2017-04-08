
package edp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class Instance {
    private File fileGraph;
    private String nameGraph, nameFile;
    private Graph g;
    private ArrayList<int []>  nodeMatrix;
    
    public Instance(String fileG, String nameFile){
        this.nameGraph= fileG;
        this.nameFile= nameFile;
        nodeMatrix = new ArrayList<>();
        this.ReadGraph();
        this.ReadFile();
        
    }
    public Instance (Instance instance){
        this.nameGraph= instance.nameGraph;
        this.nameFile=instance.nameFile;
        this.nodeMatrix = new ArrayList<>();
        for (int []list : instance.getNodeMatrix()){
            int [] p = new int [2];
            p[0]=list[0];
            p[1]=list[1];
            this.nodeMatrix.add(p);
        }
        this.g = new Graph(instance.getG().getNodes());
        this.g.setAdjacent(instance.getG().copyMatrix());
    }

    public File getFileGraph() {
        return fileGraph;
    }

    public Graph getG() {
        return g;
    }

    public String getNameFile() {
        return nameFile;
    }

    public ArrayList<int[]> getNodeMatrix() {
        return nodeMatrix;
    }

    public void setG(Graph g) {
        this.g = g;
    }

    public void setNodeMatrix(ArrayList<int[]> nodeMatrix) {
        this.nodeMatrix = nodeMatrix;
    }
    
    
    /**
     * Read the the file with the information of the instance
     * 
     */
    public void ReadFile() {
 
      try {
         File f = new File (nameFile);
         FileReader fr = new FileReader (f);
         BufferedReader br = new BufferedReader(fr);
 
         // Lectura del fichero
         String line;
         while((line=br.readLine())!=null){
             String[] p = line.split("\t");
             int i = Integer.parseInt(this.deleteSpaces(p[0]));
             int j = Integer.parseInt(this.deleteSpaces(p[1]));
             int [] aux ={i, j};
             nodeMatrix.add(aux);
         }
            
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
    
    /**
     * Create the instance graph 
     */
    public void ReadGraph() {
        int [][] ad;
        try {
            fileGraph = new File(nameGraph);
            FileReader fr = new FileReader(fileGraph);
            BufferedReader br = new BufferedReader(fr);

            // Lectura del fichero
            String line;
            line = br.readLine();
            line = this.deleteSpaces(line);
            g = new Graph (Integer.parseInt(line));
            line = br.readLine();
            ad = g.getAdjacent();
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\t");
                int i = Integer.parseInt(this.deleteSpaces(p[0]));
                int j = Integer.parseInt(this.deleteSpaces(p[1]));
                ad [i][j]=1;
                ad [j][i]=1;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    /**
     * Delete spaces in one String
     * @param line string to delete spaces
     * @return the string whitout spaces
     */
    protected String deleteSpaces(String line){
        char l;
        String line_aux="";
        for (int i =0; i< line.length();i++){
                l= line.charAt(i);
                if (l!=' ')
                    line_aux= line_aux+l;
            }
        return line_aux;
    }

    /**
     * 
     */
    public void deletePair(int[] pair) {
        pair[0] = 5000; // nodo no existente
        pair[1] = 0 ;       
    }
    
}
