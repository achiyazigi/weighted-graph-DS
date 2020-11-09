package ex1;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WGraph_Algo implements weighted_graph_algorithms {


    private weighted_graph g;

    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.g;
    }

    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this.g);
    }

    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        Queue<node_info> q = new LinkedList<node_info>();
        node_info cur = this.g.getNode(src);
        node_info d = this.g.getNode(dest);
        if(cur == null || d == null) return -1;
        cur.setTag(0);
        q.add(cur);
        while(!q.isEmpty()){
            cur = q.poll();
            int cur_key = cur.getKey();
            for (node_info n : this.g.getV(cur_key)) {
                int n_key = n.getKey();
                if(n.getTag() == -1 || n.getTag() > this.g.getEdge(n_key, cur_key)+ cur.getTag()){
                    n.setTag(this.g.getEdge(n_key, cur_key)+ cur.getTag());
                    n.setInfo(""+cur_key);
                    q.add(n);
                }
            }
        }
        return d.getTag();
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        Queue<node_info> q = new LinkedList<node_info>();
        node_info cur = this.g.getNode(src);
        node_info d = this.g.getNode(dest);
        if(cur == null || d == null) return null;
        cur.setTag(0);
        q.add(cur);
        while(!q.isEmpty()){
            cur = q.poll();
            int cur_key = cur.getKey();
            for (node_info n : this.g.getV(cur_key)) {
                int n_key = n.getKey();
                if(n.getTag() == -1 || n.getTag() > this.g.getEdge(n_key, cur_key)+ cur.getTag()){
                    n.setTag(this.g.getEdge(n_key, cur_key)+ cur.getTag());
                    n.setInfo(""+cur_key);
                    q.add(n);
                }
            }
        }
        // System.out.println(this.g.getV());
        return BuildPath(dest, src);
    }

    @Override
    public boolean save(String file) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean load(String file) {
        // TODO Auto-generated method stub
        return false;
    }

    private LinkedList<node_info> BuildPath(int src, int dest) {
        LinkedList<node_info> res = new LinkedList<node_info>();
        node_info cur = this.g.getNode(src);
        while(cur.getKey() != dest){
            res.push(cur);
            if(cur.getInfo() == null) return null; // no path!
            cur = this.g.getNode(Integer.parseInt(cur.getInfo()));
        }
        res.push(cur);
        return res;
    }
}



