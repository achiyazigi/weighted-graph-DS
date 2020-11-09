package ex1;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class test_ex1_zigi_v1 {

    @BeforeEach
    void init(){
        System.out.println("running zigi's test");
        weighted_graph g0 = new WGraph_DS();
    }

    @Test
    public void Tuple_compare1() {
        Tuple t1 = new Tuple(5, 8);
        Tuple t2 = new Tuple(8, 5);
        assertEquals(0, t1.compareTo(t2));
    }

    @Test
    public void Tuple_compare2() {
        Tuple t1 = new Tuple(5, 8);
        Tuple t2 = new Tuple(5, 8);
        assertEquals(true, t1.hashCode() == t2.hashCode());
    }

    @Test
    public void Tuple_compare3() {
        Tuple t1 = new Tuple(5, 8);
        Tuple t2 = new Tuple(8, 5);
        assertEquals(true, t1.hashCode() == t2.hashCode());
    }

    @Test
    public void has_egde() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(2);
        g0.addNode(1);
        g0.connect(2, 1, 3.2);
        assertEquals(true, g0.hasEdge(1,2));
    }

    @Test
    public void MC() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        g0.addNode(3);
        g0.addNode(4);
        g0.addNode(4);
        g0.connect(3, 1, 3.1);
        g0.connect(1, 4, 4.1);
        g0.connect(1, 4, 4.2);
        g0.removeNode(4);
        g0.removeEdge(1, 3);
        assertEquals(8, g0.getMC());
    }

    @Test
    public void node_size() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        g0.addNode(3);
        g0.addNode(4);
        g0.addNode(4);
        g0.connect(3, 1, 3.1);
        g0.connect(1, 4, 4.1);
        g0.connect(1, 4, 4.2);
        g0.removeNode(4);
        g0.removeEdge(1, 3);
        assertEquals(3, g0.nodeSize());
    }

    @Test
    public void edge_size() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        g0.addNode(3);
        g0.addNode(4);
        g0.addNode(4);
        g0.connect(3, 1, 3.1);
        g0.connect(1, 4, 4.1);
        g0.connect(3, 4, 4.2);
        g0.removeNode(4);
        assertEquals(1, g0.edgeSize());
    }

    @Test
    public void get_v_ni() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        g0.addNode(3);
        g0.addNode(8);
        g0.connect(1, 2, 2.1);
        g0.connect(1, 3, 3.1);
        g0.connect(1, 8, 8.1);
        g0.removeNode(3);
        Collection<node_info> col = g0.getV(1);
        node_info n2 = g0.getNode(2);
        node_info n3 = g0.getNode(3);
        node_info n8 = g0.getNode(8);
        assertEquals(true, col.contains(n2) && col.contains(n8) && !col.contains(n3));
    }

    @Test
    public void node_to_itself() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(1);
        g0.connect(1, 1, 3.1);
        g0.connect(1, 1, 4.1);
        assertEquals(0, g0.edgeSize()+g0.getV(1).size());
    }

    @Test
    public void get_edge() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        g0.connect(1, 2, 2.1);
        assertEquals(2.1, g0.getEdge(1, 2));
    }

    @Test
    public void get_notEx_edge() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        assertEquals(-1, g0.getEdge(1, 2));
    }

    @Test
    public void copy_struct() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        g0.connect(2, 1, 7);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        weighted_graph g1 = ga.copy();
        boolean b = true;
        b &= (g1.getNode(1) != null);
        b &= (g1.getNode(2) != null);
        b &= g1.hasEdge(1, 2);
        b &= (g1.getEdge(2, 1) == g0.getEdge(1, 2));
        assertEquals(true, b);
    }

    @Test
    public void copy_independency1() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        g0.connect(2, 1, 7);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        weighted_graph g1 = ga.copy();

        assertEquals(false, g0.getNode(1) == g1.getNode(1));
    }

    @Test
    public void copy_independency2() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        g0.addNode(2);
        g0.connect(2, 1, 7);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        weighted_graph g1 = ga.copy();
        g0.removeEdge(1, 2);

        assertEquals(true, g1.hasEdge(1, 2) && !g0.hasEdge(1, 2));
    }

    @Test
    public void copy_independency3() {
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(1);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        weighted_graph g1 = ga.copy();
        double g1_1st_tag = g1.getNode(1).getTag();
        g0.getNode(1).setTag(g1_1st_tag+1);
        assertEquals(false, g1.getNode(1).getTag() == g0.getNode(1).getTag());
    }

    @Test
    public void SP_basic() {
        weighted_graph g0 = new WGraph_DS();
        for (int i = 0; i < 3; i++) {
            g0.addNode(i);
        }
        g0.connect(0, 1, 1);
        g0.connect(1, 2, 1);
        g0.connect(0, 2, 3);
        
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        List<node_info> SP= ga.shortestPath(0, 2);
        int[] expected = {0,1,2};
        boolean b = true;
        int i = 0;
        for (node_info n : SP) {
            b &= n.getKey() == expected[i++];
        }
        assertEquals(true, b);
    }

    @Test
    public void SP_regular() {
        weighted_graph g0 = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g0.addNode(i);
        }
        g0.connect(0, 1, 1);  //              2        10       
        g0.connect(0, 2, 2);  //          (5)---(8)__________    
        g0.connect(1, 4, 1);  //         /5 \3               \   
        g0.connect(2, 5, 5);  //      (2)----\-----\10        \
        g0.connect(2, 7, 10); //    2/       \   4  \     4    \
        g0.connect(4, 3, 1);  //  (0)   1 _ (4)-----(7)--------(9)
        g0.connect(4, 5, 3);  //    1\   /  1|                 /
        g0.connect(4, 7, 4);  //      (1)   (3)               / 
        g0.connect(3, 6, 1);  //              1\          1  /  
        g0.connect(5, 8, 2);  //                (6)---------- 
        g0.connect(6, 9, 1);  //                                
        g0.connect(7, 9, 4);  //                                
        g0.connect(9, 8, 10); //                                
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        List<node_info> SP= ga.shortestPath(0, 9);
        int[] expected = {0,1,4,3,6,9};
        boolean b = true;
        int i = 0;
        for (node_info n : SP) {
            b &= n.getKey() == expected[i++];
        }
        assertEquals(true, b);
    }

    @Test
    public void SP_reversed() {
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
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        List<node_info> SP= ga.shortestPath(9, 0);
        int[] expected = {9,6,3,4,1,0};
        boolean b = true;
        int i = 0;
        for (node_info n : SP) {
            b &= n.getKey() == expected[i++];
        }
        assertEquals(true, b);
    }

    @Test
    public void SP_no_path() {
        weighted_graph g0 = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g0.addNode(i);
        }
        g0.connect(0, 1, 1);
        g0.connect(1, 4, 1);
        g0.connect(2, 5, 5);
        g0.connect(2, 7, 10);
        g0.connect(3, 6, 1);
        g0.connect(5, 8, 2);
        g0.connect(6, 9, 1);
        g0.connect(7, 9, 4);
        g0.connect(9, 8, 10);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        List<node_info> SP= ga.shortestPath(0, 9);
        assertEquals(null, SP);
    }

    @Test
    public void SP_node_to_itself() {
        weighted_graph g0 = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g0.addNode(i);
        }
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        boolean b = true;
        for (node_info n : g0.getV()) {
            b &= ga.shortestPath(n.getKey(), n.getKey()).size() == 1;
            b &= ga.shortestPath(n.getKey(), n.getKey()).get(0) == n;
        }
        assertEquals(true, b);
    }
    @Test
    public void SP_dist_basic() {
        weighted_graph g0 = new WGraph_DS();
        for (int i = 0; i < 3; i++) {
            g0.addNode(i);
        }
        g0.connect(0, 1, 1);
        g0.connect(1, 2, 1);
        g0.connect(0, 2, 3);
        
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        assertEquals(2, ga.shortestPathDist(0, 2));
    }

    @Test
    public void SP_dist_regular() {
        weighted_graph g0 = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g0.addNode(i);
        }
        g0.connect(0, 1, 1);  //              2        10       
        g0.connect(0, 2, 2);  //          (5)---(8)__________    
        g0.connect(1, 4, 1);  //         /5 \3               \   
        g0.connect(2, 5, 5);  //      (2)----\-----\10        \
        g0.connect(2, 7, 10); //    2/       \   4  \     4    \
        g0.connect(4, 3, 1);  //  (0)   1 _ (4)-----(7)--------(9)
        g0.connect(4, 5, 3);  //    1\   /  1|                 /
        g0.connect(4, 7, 4);  //      (1)   (3)               / 
        g0.connect(3, 6, 1);  //              1\          1  /  
        g0.connect(5, 8, 2);  //                (6)---------- 
        g0.connect(6, 9, 1);  //                                
        g0.connect(7, 9, 4);  //                                
        g0.connect(9, 8, 10); //                                
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        assertEquals(5, ga.shortestPathDist(0, 9));
    }
}
