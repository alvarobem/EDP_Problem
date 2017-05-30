package edp;

import java.util.ArrayList;

/**
 *
 * @author Alvaro Berrocal Martin - URJC
 */
public class Solution {
    private int conn;
    private int notConn;
    private ArrayList<ArrayList<Integer>> routes;
    private double time;
    Instance i ;
    
    public Solution(){
        conn=0;
        notConn=0;
        routes= new ArrayList<> ();
        time= 0 ;
    }
    
    public Solution(Solution solution){
        this.conn=0;
        this.notConn = 0;
        this.routes = new ArrayList<>();
        this.routes.addAll(solution.getRoutes());
        this.i=solution.getI();
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
    
    public void mergueRoutes (Solution s){
        for (int i=0; i < this.routes.size(); i++){
            if(this.getRoutes().get(i).size()==0){
                this.getRoutes().set(i, s.getRoutes().get(i));
            }
        }
    }
    
    public void deletePair(int pos){
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
        this.i.deletePair(this.i.getNodeMatrix().get(pos));
        this.conn--;
        this.notConn ++;
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
    
}
