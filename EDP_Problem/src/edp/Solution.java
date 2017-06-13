package edp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class Solution {
    private int conn;
    private int notConn;
    private ArrayList<ArrayList<Integer>> routes;
    private List <int[]> pairsNotConn;
    private double time;
    Instance i ;
    
    public Solution(){
        conn=0;
        notConn=0;
        routes= new ArrayList<> ();
        pairsNotConn = new ArrayList<>();
        time= 0 ;
        i = null;
    }
    
    public Solution(Solution solution){
        this.conn=solution.getConn();
        this.notConn = solution.getNotConn();
        this.routes = new ArrayList<>();
        this.pairsNotConn = new ArrayList<>();
        this.routes.addAll(solution.getRoutes());
        this.pairsNotConn.addAll(solution.getPairsNotConn());
        this.i=null;
    }

    public List<int[]> getPairsNotConn() {
        return pairsNotConn;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Instance getI() {
        return i;
    }

    public void setI(Instance i) {
        this.i= i;
        
    }

    public int getConn() {
        return conn;
    }


    public int getNotConn() {
        return notConn;
    }

    public ArrayList<ArrayList<Integer>> getRoutes() {
        return routes;
    }

    public void setConn(int conn) {
        this.conn = conn;
    }

    public void setNotConn(int notConn) {
        this.notConn = notConn;
    }

    public void setRoutes(ArrayList<ArrayList<Integer>> rutes) {
        this.routes = rutes;

    }
    
    public void addConn (){
        this.conn++;
    }
    
    public void addNotConn(){
        this.notConn++;
    }
    
    public void addRoute(ArrayList<Integer> l){
        routes.add(l);
    }
    public void addRoute(ArrayList<Integer> route, int pos){
        routes.set(pos, route);
    }

    @Override
    public String toString() {
        String s= "Nodos que se han podido conectar: "+conn+"\n";
        s=s+ "Nodos que no se han podido conectar: "+notConn+"\n";
        return s;
    }
    
    public String routesToString (){
        String s="";
        for (int i = 0; i < routes.size(); i++){
            if (routes.get(i).size()==0)
                s=s+"Camino no alcanzable\n";
            else
                s=s+routes.get(i).toString()+"\n";
        }
        return s;
    }
    
    public boolean isRouteConected (int pos){
        return this.getRoutes().get(pos).size()>0;
    }
    
    public boolean isBetter (Solution s){
        return (this.getConn()>s.getConn());
    }
    public boolean isBetterOrEqual (Solution s){
        return (this.getConn()>=s.getConn());
    }
 
    public Solution whoIsBetter(Solution s){
        return (this.conn>s.conn)? this: s;
    }
    
    
    
    public boolean isEqualPair (int [] a, int []b){
        return (a[0]==b[0] && a[1]==b[1]) || (a[0]==b[1] && a[1]==b[0]);
    }
    
    public int[] deletePair(int pos){
        ArrayList<Integer> route = this.getRoutes().get(pos);
        int [][] ad =this.i.getG().getAdjacent();
        for(int i =0 ; i<route.size()-1; i++){
            int node1 = route.get(i);
            int node2 = route.get(i+1);
            ad[node1][node2]=1;
            ad[node2][node1]=1;
        }
        this.getRoutes().set(pos, new ArrayList<>());
        this.i.getG().setAdjacent(ad);
        int [] ret = new int[2];
        ret [0] = route.get(0);
        ret [1] =route.get(route.size()-1);
        
        return ret;
    }
    
    public ArrayList <int []> getRoutesNotConected(){
        ArrayList <int []> notConected = new ArrayList();
        for (int i =0; i< this.routes.size(); i++){
           ArrayList <Integer> r= routes.get(i);
           if (r.size()==0){
               notConected.add(this.i.getNodeMatrix().get(i));
           }
        }
        return notConected;
    }
    
    public ArrayList <ArrayList<Integer>> getRoutesConected(){
        ArrayList <ArrayList<Integer>> notConected = new ArrayList<>();
        for (int i =0; i< this.routes.size(); i++){
           ArrayList <Integer> r= routes.get(i);
           if (r.size()>0){
               notConected.add(routes.get(i));
           }
        }
        return notConected;
    }
    
    public int[] disconectRoute (ArrayList<Integer> route){
        int[] pair = new int [2];
        pair [0] = route.get(0);
        pair[1]= route.get(route.size()-1);
        
        int [][] ad =this.i.getG().getAdjacent();
        for(int i =0 ; i<route.size()-1; i++){
            int node1 = route.get(i);
            int node2 = route.get(i+1);
            ad[node1][node2]=1;
            ad[node2][node1]=1;
        }
        boolean routeFound = false;
        int pos = 0;
        while (!routeFound && pos<this.getRoutes().size()){
            ArrayList<Integer> routeSearch = this.getRoutes().get(pos);
            if (!routeSearch.isEmpty()){
                int[] pairSearch = new int [2];
                pairSearch [0] = routeSearch.get(0);
                pairSearch[1]= routeSearch.get(routeSearch.size()-1);
                if (isEqualPair(pair, pairSearch)){
                    routeFound = true;
                    this.getRoutes().remove(pos);
                }
            }
            pos ++;
        }  
        this.conn--;
        this.notConn++;
        this.i.getG().setAdjacent(ad);
        this.addPairNotConn(pair[0], pair[1]);
        return pair;
    }
    
    public boolean isPairConected(int a, int b){
        boolean connected = false;
        int[] pair = new int [2];
        pair[0]=a;
        pair[1]=b;
        for (ArrayList<Integer> route : this.getRoutes()){
            int[] pairSearch = new int [2];
                pairSearch [0] = route.get(0);
                pairSearch[1]= route.get(route.size()-1);
            if (isEqualPair(pairSearch, pair)){
                connected = true;
                break;
            }
        }
        return connected;
    }
    
    public void addPairNotConn (int a, int b){
        int [] pair = new int [2];
        pair[0]= a;
        pair[1]=b;
        boolean found=false;
        int pos = 0;
        while (!found && pos < pairsNotConn.size()){
            if (isEqualPair(pair, pairsNotConn.get(pos))){
                found = true;
            }
            pos++;
        }
        if (!found){
            pairsNotConn.add(pair);
        }
    }
    
    public void reovePairNotConn (int a, int b){
        int [] pair = new int [2];
        pair[0]= a;
        pair[1]=b;
        boolean found=false;
        int pos = 0;
        while (!found && pos < pairsNotConn.size()){
            if (isEqualPair(pair, pairsNotConn.get(pos))){
                found = true;
            }
            pos++;
        }
        if (found){
            pairsNotConn.remove(pos-1);
        }
    }
}
