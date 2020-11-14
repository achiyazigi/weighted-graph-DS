package ex1;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WGraph_Algo implements weighted_graph_algorithms {

    /**
     *
     */
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
        this.reset(); // J.I.C. tags and info are not reset allready...
        Queue<node_info> q = new LinkedList<node_info>();
        Collection<node_info> col = this.g.getV();
        int counter = 0;
        if(!col.isEmpty()){ //if g isn't empty
            node_info first = col.iterator().next();
            first.setTag(1); // coloring
            q.add(first);
            while(!q.isEmpty()){
                first = q.poll();
                Iterator<node_info> i = this.g.getV(first.getKey()).iterator(); //taking first node in queue and adding its uncolored neighbors
                counter++;
                while(i.hasNext()){ //adding the neighbors
                    node_info to_add = i.next();
                    if(to_add.getTag() == -1){
                        to_add.setTag(1); // coloring
                        q.add(to_add);
                    }
                }
            }
            return counter == col.size();
        }
        
        return true; //empty graph is connected
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        Queue<node_info> q = new LinkedList<node_info>();
        node_info cur = this.g.getNode(src);
        node_info d = this.g.getNode(dest);
        if (cur == null || d == null)
            return -1;
        this.reset();
        cur.setTag(0);
        q.add(cur);
        while (!q.isEmpty()) {
            cur = q.poll();
            int cur_key = cur.getKey();
            for (node_info n : this.g.getV(cur_key)) {
                int n_key = n.getKey();
                if (n.getTag() == -1 || n.getTag() > this.g.getEdge(n_key, cur_key) + cur.getTag()) {
                    n.setTag(this.g.getEdge(n_key, cur_key) + cur.getTag());
                    n.setInfo("" + cur_key);
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
        if (cur == null || d == null)
            return null;
        this.reset();
        cur.setTag(0);
        q.add(cur);
        while (!q.isEmpty()) {
            cur = q.poll();
            int cur_key = cur.getKey();
            for (node_info n : this.g.getV(cur_key)) {
                int n_key = n.getKey();
                double new_tag_candi =  this.g.getEdge(n_key, cur_key) + cur.getTag();
                if (new_tag_candi >= 0 && (n.getTag() == -1 || n.getTag() > new_tag_candi)) { //found a bug of bit drop with larg graphs. so i need to protect new_tag_candi from becoming negative
                    n.setTag(new_tag_candi);
                    n.setInfo("" + cur_key);
                    q.add(n);
                }
            }
        }
        return BuildPath(dest, src);
    }

    @Override
    public boolean save(String file_name) {
        ObjectOutputStream oos;
        boolean ans = false;
        try {
            FileOutputStream fout = new FileOutputStream(file_name, true);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this.g);
            ans = true;
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
    }

    
    @Override
    public boolean load(String file_name) {
        try {
            FileInputStream streamIn = new FileInputStream(file_name);
            ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
            weighted_graph readCase = (weighted_graph) objectinputstream.readObject();
            this.g = new WGraph_DS(readCase);
            objectinputstream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
    private void reset(){
        for (node_info n : this.g.getV()) {
            n.setTag(-1);
            n.setInfo(null);
        }
    }
}



