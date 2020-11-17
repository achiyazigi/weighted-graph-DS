package ex1;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph g;

    /**
     * Graph to initialize.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    /**
     * A method to retrieve the initialized graph. return null if nothing has been
     * initialized before.
     * 
     * @return weighted_graph
     */
    @Override
    public weighted_graph getGraph() {
        return this.g;
    }

    /**
     * Perfom a deep copy by rebuilding the graph from scratch, copying each node to
     * a new one, adding it to the new graph, and reconnecting each node by the data
     * from the initialized graph's edge map.
     * 
     * @return weighted_graph
     */
    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this.g);
    }

    /**
     * A simple BFS concept, implemented with Queue. Coloring a node by changing its
     * tag, poping it from the Queue, and addind it's neighbors.
     * 
     * @return true iff the number of nodes poped out from the Queue equals to the
     *         number of nodes in the graph.
     */
    @Override
    public boolean isConnected() {
        this.reset(); // J.I.C. tags and info are not reset allready...
        Queue<node_info> q = new LinkedList<node_info>();
        Collection<node_info> col = this.g.getV();
        int counter = 0;
        if (this.g.edgeSize() + 1 < col.size())
            return false;
        if (!col.isEmpty()) { // if g isn't empty
            node_info first = col.iterator().next();
            first.setTag(1); // coloring
            q.add(first);
            while (!q.isEmpty()) {
                if (q.size() == col.size())
                    return true;
                first = q.poll();
                Iterator<node_info> i = this.g.getV(first.getKey()).iterator(); // taking first node in queue and adding
                                                                                // its uncolored neighbors
                counter++;
                while (i.hasNext()) { // adding the neighbors
                    node_info to_add = i.next();
                    if (to_add.getTag() == -1) {
                        to_add.setTag(1); // coloring
                        q.add(to_add);
                    }
                }
            }
            return counter == col.size();
        }

        return true; // empty graph is connected
    }


    /**
     * Combining Dijaksta and dp, this function changing each node tag on its way,
     * to the distance from starting point. when and if destination is reached, a
     * better path is being check. after first time of coloring dest, nodes with
     * colored tags grater then dest's tag are banned, and won't be added to the
     * queue, so their neighbors won't be visited again from their direction. a node
     * can be unbanned by improving it's tag.
     * 
     * @param src
     * @param dest
     * @return double
     */
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
            if (d.getTag() == -1 || cur.getTag() < d.getTag()) {
                int cur_key = cur.getKey();
                for (node_info n : this.g.getV(cur_key)) {
                    int n_key = n.getKey();
                    double new_tag_candi = this.g.getEdge(n_key, cur_key) + cur.getTag();
                    if (new_tag_candi >= 0 && (n.getTag() == -1 || n.getTag() > new_tag_candi)) { // found a bug of bit drop with larg graphs. so i need to protect new_tag_candi from becoming negative
                        n.setTag(new_tag_candi);
                        n.setInfo("" + cur_key);
                        if (n != d)
                            q.add(n);
                    }
                }
            }
        }
        return d.getTag();
    }

    /** 
     * By coloring the nodes with shortestPathDist, all work left is to build the path by backtracking and getting from each node, starting from the dest, the key that led it to the next. it stored in the info of each node in the path.
     * @param src
     * @param dest
     * @return List<node_info>
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(this.shortestPathDist(src, dest) == -1) return null;
        return BuildPath(dest, src);
    }

    
    /** 
     * saving serializable Object as ObjectOutputStream
     * @param file_name
     * @return boolean
     */
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

    
    
    /** 
     * Loading serializable Object as objectinputstream.
     * @param file_name
     * @return boolean
     */
    @Override
    public boolean load(String file_name) {
        try {
            FileInputStream streamIn = new FileInputStream(file_name);
            ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
            weighted_graph readCase = (weighted_graph) objectinputstream.readObject();
            this.g = readCase;
            objectinputstream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    
    /** 
     * Backtracking as described in shortestPth().
     * @param src
     * @param dest
     * @return LinkedList<node_info>
     */
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

    /**
     * reseting tags and info as a preperation to sortestPathDist an isConnected.
     */
    private void reset(){
        for (node_info n : this.g.getV()) {
            n.setTag(-1);
            n.setInfo(null);
        }
    }
}



