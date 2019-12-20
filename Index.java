
public class Index {
    public int key;
    public long  offset;

    public Index(){}

    public Index(int k, long o)
    {
        key=k;
        offset=o;
    }
    public String toString() {
        return String.format("(  %d %d )",key,offset);
    }


}
