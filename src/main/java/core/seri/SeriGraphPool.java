package core.seri;

import core.seri.wrapers.SeriList;

import java.lang.ref.WeakReference;

public class SeriGraphPool implements Seri {
    protected static SeriGraphPool mInstance = new SeriGraphPool();

    protected SeriList<WeakReference<SeriGraph>> mPool = new SeriList<>();

    public static SeriGraphPool getInstance () {
        return mInstance;
    }

    public void add (SeriGraph seriGraph){
        mPool.add(new WeakReference<>(seriGraph));
    }

    public int indexOf (SeriGraph seriGraph){
        int i = 0;
        for (WeakReference<SeriGraph> seriGraphWeakReference : mPool) {
            SeriGraph object = seriGraphWeakReference.get();
            if (seriGraph == object)
                return i;
            i ++;
        }
        return -1;
    }

    public SeriGraph get (int index){
        return mPool.get(index).get();
    }

    @Override
    public void serialize(String prefix, StringBuilder stream) throws IllegalAccessException {
        stream.append(SeriConf.BEGIN);

        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT).append(mPool.size());

        for (WeakReference<SeriGraph> seriGraphWeakReference : mPool){
            Seri seri = (Seri)seriGraphWeakReference.get();
            if (seri == null)
                continue;
            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT).append(getClassName(seri.getClass()));
        }

        for (WeakReference<SeriGraph> seriGraphWeakReference : mPool){
            Seri seri = (Seri)seriGraphWeakReference.get();
            if (seri == null)
                continue;
            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT);
            seri.serialize(prefix + SeriConf.INDENT, stream);
        }
        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.END);
    }

    @Override
    public void deserialize(TokenStream stream) throws Exception {
        mPool.clear();

        stream.get(SeriConf.BEGIN);

        // Create the entire entities pool for linking
        int size = Integer.parseInt(stream.get());
        for (int i = 0; i < size; ++i) {
            String className = stream.get();
            Class<?> clasa = getClass(className);
            create(clasa);
        }

        // deserialize each entity
        for (WeakReference<SeriGraph> seriGraphWeakReference : mPool){
            Seri seri = (Seri)seriGraphWeakReference.get();
            seri.deserialize(stream);
        }

        stream.get(SeriConf.END);
    }
}
