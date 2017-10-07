import core.seri.SeriGraphPool;
import core.seri.Seri;
import core.seri.SeriGraph;

public class MainSeri {

    public static class GraphTest implements Seri, SeriGraph {
        public int mValue = 0;
        public String mString = "Veta";
        public GraphTest mRef;

        public GraphTest() {
            SeriGraphPool.getInstance().add(this);
        }
    }

    public static class GraphTest2 extends GraphTest {
        public int mString = 0xdeadbeaf;
    }

    public static void main (String[] args){

        GraphTest a = new GraphTest();
        GraphTest b = new GraphTest2();

        a.mRef = b;
        b.mRef = a;

        try {
            StringBuilder stream = new StringBuilder();
            SeriGraphPool.getInstance().serialize("", stream);
            System.out.println (stream.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
