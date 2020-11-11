package ex1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class WGraph_DS implements weighted_graph, Serializable{
    

    private static class NodeInfo implements node_info, Serializable {

        private static final long serialVersionUID = 3137094734563763916L;
        private static int id = 0;
        private int key;
        private String info;
        private double tag;

        public NodeInfo(int key) {
            this.key = key;
            this.tag = -1;
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
        public String toString() {
            return "(key="+this.key+" , tag="+this.tag+" , info="+this.info+")";
        }
    }

    private static final long serialVersionUID = -4468900051198514988L;


    private HashMap<Integer,node_info> v;
    private HashMap<Tuple,Double> e;
    private HashMap<node_info,HashMap<Integer,node_info>> ni;
    private int MC;

    public WGraph_DS(){
        this.v = new HashMap<Integer, node_info>();
        this.e = new HashMap<Tuple,Double>();
        this.ni = new HashMap<node_info,HashMap<Integer,node_info>>();
        this.MC = 0;
    }

    public WGraph_DS(weighted_graph other){
        this.v = new HashMap<Integer, node_info>();
        this.e = new HashMap<Tuple,Double>();
        this.ni = new HashMap<node_info,HashMap<Integer,node_info>>();
        
        this.MC = other.getMC();

        for (node_info n : other.getV()) {
            this.addNode(n.getKey());
            node_info thisn = this.getNode(n.getKey());
            thisn.setInfo(n.getInfo());
            thisn.setTag(n.getTag());
        }

        for (node_info n : other.getV()) {
            int nkey = n.getKey();
            for (node_info con : other.getV(nkey)) {
                int conkey = con.getKey();
                this.connect(nkey, conkey, other.getEdge(nkey, conkey));
            }
        }
    }

    @Override
    public node_info getNode(int key) {
        return this.v.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        Tuple t = new Tuple(node1,node2);
        return this.e.containsKey(t);
    }

    @Override
    public double getEdge(int node1, int node2) {
        Tuple t = new Tuple(node1, node2);
        if(this.e.containsKey(t)) return this.e.get(t);
        return -1;
    }

    @Override
    public void addNode(int key) {
        if(!this.v.containsKey(key)){   
            this.v.put(key, new NodeInfo(key));
            this.ni.put(this.getNode(key), new HashMap<Integer,node_info>());
            this.MC ++;
        }
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if(node1 != node2){
            Tuple t = new Tuple(node1, node2);
            if(!this.e.containsKey(t)){
                this.e.put(t, w);
                node_info n1 = this.getNode(node1);
                node_info n2 = this.getNode(node2);
                this.ni.get(n1).put(node2, n2);
                this.ni.get(n2).put(node1, n1);
                this.MC ++;
            }
        }
    }

    @Override
    public Collection<node_info> getV() {
        return this.v.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        node_info cur = this.getNode(node_id);
        if(cur == null) return null; //if node_id not found
        return this.ni.get(cur).values();
    }

    @Override
    public node_info removeNode(int key) {
        if(this.v.containsKey(key)){
            for (node_info n : this.getV(key)) {
                Tuple t = new Tuple(key, n.getKey());
                this.e.remove(t);
                this.ni.get(n).remove(key);
            }
            this.ni.remove(this.getNode(key));
            this.MC ++;
        }
        return this.v.remove(key);
    }

    @Override
    public void removeEdge(int node1, int node2) {
        Tuple t = new Tuple(node1,node2);
        if(this.e.containsKey(t)){
            this.e.remove(t);
            this.ni.get(this.getNode(node1)).remove(node2);
            this.ni.get(this.getNode(node2)).remove(node1);
            this.MC++;
        }
    }

    @Override
    public int nodeSize() {
        return this.v.size();
    }

    @Override
    public int edgeSize() {
        return this.e.size();
    }

    @Override
	public int getMC() {
		return this.MC;
    }

    @Override
    public String toString() {
        if(this.e.isEmpty()) return ""+this.v;
        String res = "{";
        for (Tuple t : this.e.keySet()) {
            res+=("["+t+": "+this.e.get(t)+"] ");
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