package core;

import core.seri.Seri;
import core.seri.SeriConf;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

// seri wrapper for map

public class SeriMap<I, T> extends HashMap<I, T> implements Seri {
    @Override
    public void serialize(String prefix, List<Entity> entitiesPool, StringBuilder stream) throws IllegalAccessException {
        Set<I> keys = keySet();

        stream.append(SeriConf.BEGIN);
        for (I key : keys){
            Object object = get(key);

            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT);
            stream.append(getClassName(key.getClass())).append(SeriConf.SEPARATOR);
            serialize(key, prefix + SeriConf.INDENT, entitiesPool, stream);

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
            String keyClassName = stream.get();
            if (SeriConf.END.equals(keyClassName))
                break;
            Class<?> keyClass = getClass(keyClassName);
            Object key = deserialize(keyClass, entitiesPool, stream);

            String objectClassName = stream.get();
            Class<?> objectClass = getClass(objectClassName);
            Object object = deserialize(objectClass, entitiesPool, stream);

            put ((I)key, (T)object);
        } while (true);
    }
}
