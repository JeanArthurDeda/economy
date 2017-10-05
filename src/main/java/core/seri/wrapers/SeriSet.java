package core.seri.wrapers;

import core.seri.Seri;
import core.seri.SeriConf;
import core.seri.TokenStream;

import java.util.HashSet;

// Seri wrapper for set

public class SeriSet<T> extends HashSet<T> implements Seri {
    @Override
    public boolean isDefault() {
        return size() == 0;
    }

    @Override
    public void serialize(String prefix, StringBuilder stream) throws IllegalAccessException {
        stream.append(SeriConf.BEGIN);
        for (Object object : this){
            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT);
            stream.append(getClassName(object.getClass())).append(SeriConf.SEPARATOR);
            serialize(object, prefix + SeriConf.INDENT, stream);
        }
        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.END);
    }

    @Override
    public void deserialize(TokenStream stream) throws Exception {
        clear();

        stream.get(SeriConf.BEGIN);
        do {
            String className = stream.get();
            if (SeriConf.END.equals(className))
                break;
            Class clasa = getClass(className);
            Object object = deserialize(clasa, stream);
            add ((T)object);
        } while (true);
    }
}
