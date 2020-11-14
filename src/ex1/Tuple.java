package ex1;

import java.io.Serializable;

public class Tuple implements Comparable<Tuple>, Serializable{

    private static final long serialVersionUID = -2018922364822746333L;
    private int a, b;

    public Tuple(int a, int b){
        this.a = a;
        this.b = b;
    }


    @Override
    public int hashCode() {
        int res = 31+this.a;
        res *= 31+this.b;

        return res;
    }

    @Override
    public int compareTo(Tuple o) {
        if((o.a == this.a && o.b == this.b)||(o.a == this.b && o.b == this.a))
            return 0;
        return -1;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Tuple)){
            return false;
        }

        Tuple other_ = (Tuple) other;

        // this may cause NPE if nulls are valid values for x or y. The logic may be improved to handle nulls properly, if needed.
        return (other_.a == this.a && other_.b == this.b) || (other_.b == this.a && other_.a == this.b);
    }
    
    @Override
    public String toString() {
        return "("+this.a+","+this.b+")";
    }

}
