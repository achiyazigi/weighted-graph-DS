package ex1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class WGraph_DS implements weighted_graph, Serializable{
    

    private static class NodeInfo implements node_info, Serializable {

        private static final long serialVersionUID = 3137094734563763916L;
        private int key;
        private String info;
        private double tag;

        public NodeInfo(int key) {
            this.key = key;
            this.tag = -1;
        }

        public NodeInfo(node_info other) {
            this.key = other.getKey();
            this.tag = other.getTag();
            this.info = other.getInfo();
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.key);
        }

        @Override
        public String toString() {
            return "(key="+this.key+" , tag="+this.tag+" , info="+this.info+")";
        }
    }

    private static final long serialVersionUID = -4468900051198514988L;


    private HashMap<Integer,node_info> v;
    private HashMap<Integer,HashMap<node_info,Double>> e;
    private int MC;
    private int edges;

    public WGraph_DS(){
        this.v = new HashMap<Integer, node_info>();
        this.e = new HashMap<Integer,HashMap<node_info,Double>>();
        this.MC = 0;
    }

    public WGraph_DS(weighted_graph other){
        this.v = new HashMap<Integer, node_info>();
        this.e = new HashMap<Integer,HashMap<node_info,Double>>();
        
        for (node_info n : other.getV()) {
            int nkey = n.getKey();
            this.v.put(nkey, new NodeInfo(n));
            this.e.put(nkey, new HashMap<node_info, Double>());
        }

        for (node_info n : other.getV()) {
            int nkey = n.getKey();
            for (node_info con : other.getV(nkey)) {
                int conkey = con.getKey();
                Double edge = other.getEdge(nkey, conkey);
                this.e.get(nkey).put(this.v.get(conkey), edge);
                this.e.get(conkey).put(this.v.get(nkey), edge);
            }
        }
        this.edges = other.edgeSize();
        this.MC = other.getMC();
    }

    @Override
    public node_info getNode(int key) {
        return this.v.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        return this.e.containsKey(node1) && this.e.get(node1).containsKey(this.getNode(node2));
    }

    @Override
    public double getEdge(int node1, int node2) {
        if(this.hasEdge(node1, node2)){
            return this.e.get(node1).get(this.getNode(node2));
        }
        return -1;
    }

    @Override
    public void addNode(int key) {
        if(!this.v.containsKey(key)){   
            this.v.put(key, new NodeInfo(key));
            this.e.put(key, new HashMap<node_info, Double>());
            this.MC ++;
        }
    }

    @Override
    public void connect(int node1, int node2, double w) {
        node_info n2 = this.getNode(node2);
        node_info n1 = this.getNode(node1);
        if(node1 == node2 || n1 == null || n2 == null) return;
        if(!this.hasEdge(node1, node2) || this.e.get(node1).get(n2) != w){
            this.e.get(node2).put(n1, w);
            this.e.get(node1).put(n2, w);
            this.edges ++;
            this.MC ++;  
        }
    }

    @Override
    public Collection<node_info> getV() {
        return this.v.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if(this.v.containsKey(node_id))
            return this.e.get(node_id).keySet();
        return null;
    }

    @Override
    public node_info removeNode(int key) {
        if(this.v.containsKey(key)){
            Collection<node_info> ni = new HashSet<node_info>(this.getV(key));
            for (node_info n : ni) {
                this.removeEdge(key, n.getKey());
            }
            this.MC ++;
            this.MC -= ni.size();
        }
        return this.v.remove(key);
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if(this.hasEdge(node1, node2)){
            this.e.get(node1).remove(this.getNode(node2));
            this.e.get(node2).remove(this.getNode(node1));
            this.edges --;
            this.MC++;
        }
    }

    @Override
    public int nodeSize() {
        return this.v.size();
    }

    @Override
    public int edgeSize() {
        return this.edges;
    }

    @Override
	public int getMC() {
		return this.MC;
    }

    @Override
    public String toString() {
        String res = "{";
        for (node_info n : this.v.values()) {
            int nkey = n.getKey();
            res+="(";
            for (node_info ni : this.getV(nkey)) {
                res+=" <"+nkey+","+ni.getKey()+">"+this.e.get(nkey).get(ni)+" ";
            }
            res+=") ";
        }
        return res+"}";
    }
    
    public static void main(String[] args) {
        weighted_graph g0 = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g0.addNode(i);
        }
        g0.connect(0, 1, 1);
        g0.connect(0, 2, 2);
        g0.connect(1, 4, 1);
        g0.connect(2, 5, 5);
        g0.connect(2, 7, 10);
        g0.connect(4, 3, 1);
        g0.connect(4, 5, 3);
        g0.connect(4, 7, 4);
        g0.connect(3, 6, 1);
        g0.connect(5, 8, 2);
        g0.connect(6, 9, 1);
        g0.connect(7, 9, 4);
        g0.connect(9, 8, 10);
        System.out.println(g0);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        System.out.println(ga.shortestPath(0, 9));
    }
}