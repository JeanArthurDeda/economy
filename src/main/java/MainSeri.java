import core.seri.Pool;
import core.seri.Seri;
import core.seri.SeriPool;

public class MainSeri {

    public static class PoolTest implements Seri, SeriPool{
        public int mValue = 0;
        public String mString = "Veta";
        public PoolTest mRef;

        public PoolTest() {
            Pool.getInstance().add(this);
        }
    }

    public static class PoolTest2 extends PoolTest{
        public int mString = 0xdeadbeaf;
    }

    public static void main (String[] args){

        PoolTest a = new PoolTest();
        PoolTest b = new PoolTest2();

        a.mRef = b;
        b.mRef = a;

        try {
            StringBuilder stream = new StringBuilder();
            Pool.getInstance().serialize("", stream);
            System.out.println (stream.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
