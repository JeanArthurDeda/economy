package core;

import core.seri.Seri;
import core.seri.SeriConf;

import java.util.ArrayList;
import java.util.List;

// Seri wrapper for list

public class SeriList<T> extends ArrayList<T> implements Seri {
    public SeriList(){
        super();
    }

    public SeriList (T[] objects){
        super();
        for (T object : objects) {
            add (object);
        }
    }

    @Override
    public boolean isDefault() {
        return size() == 0;
    }

    @Override
    public void serialize(String prefix, List<Entity> entitiesPool, StringBuilder stream) throws IllegalAccessException {
        stream.append(SeriConf.BEGIN);
        for (Object object : this){
            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT);
            stream.append(getClassName(object.getClass())).append(SeriConf.SEPARATOR);
            serialize(object, prefix + SeriConf.INDENT, entitiesPool, stream);
        }
        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.END);

    }

    @Override
    public void deserialize(TokenStream stream, List<Entity> entitiesPool) throws Exception {
        clear();

        stream.get(SeriConf.BEGIN);
        do {
            String className = stream.get();
            if (SeriConf.END.equals(className))
                break;
            Class clasa = getClass(className);
            Object object = deserialize(clasa, entitiesPool, stream);
            add ((T)object);
        } while (true);
    }
}
