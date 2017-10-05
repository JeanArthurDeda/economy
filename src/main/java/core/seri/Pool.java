package core.seri;

import core.seri.wrapers.SeriList;

public class Pool extends SeriList<SeriPool> {
    protected static Pool mInstance = new Pool();

    public static Pool getInstance () {
        return mInstance;
    }

    @Override
    public void serialize(String prefix, StringBuilder stream) throws IllegalAccessException {
        stream.append(SeriConf.BEGIN);

        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT).append(size());

        for (Object object : this){
            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT).append(getClassName(object.getClass()));
        }

        for (Object object : this){
            Seri seri = (Seri)object;
            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT);
            seri.serialize(prefix + SeriConf.INDENT, stream);
        }
        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.END);
    }

    @Override
    public void deserialize(TokenStream stream) throws Exception {
        stream.get(SeriConf.BEGIN);

        // Create the entire entities pool for linking
        int size = Integer.parseInt(stream.get());
        for (int i = 0; i < size; ++i) {
            String className = stream.get();
            Class<?> clasa = getClass(className);
            create(clasa);
        }

        // deserialize each entity
        for (Object object : this){
            Seri seri = (Seri)object;
            seri.deserialize(stream);
        }

        stream.get(SeriConf.END);
    }
}
