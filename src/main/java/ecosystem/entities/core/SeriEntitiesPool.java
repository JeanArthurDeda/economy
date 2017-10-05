package ecosystem.entities.core;

import core.Entity;
import core.seri.TokenStream;
import core.seri.SeriConf;

import java.util.List;

public class SeriEntitiesPool extends SeriEntities {
    @Override
    public void serialize(String prefix, List<Entity> entitiesPool, StringBuilder stream) throws IllegalAccessException {
        stream.append(SeriConf.BEGIN);

        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT).append(size());

        for (Entity entity : this){
            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT).append(getClassName(entity.getClass()));
        }

        for (Entity entity : this){
            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT);
            entity.serialize(prefix + SeriConf.INDENT, this, stream);
        }
        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.END);
    }

    @Override
    public void deserialize(TokenStream stream, List<Entity> entitiesPool) throws Exception {
        stream.get(SeriConf.BEGIN);

        // Create the entire entities pool for linking
        int size = Integer.parseInt(stream.get());
        for (int i = 0; i < size; ++i) {
            String className = stream.get();
            Class<?> clasa = getClass(className);
            add((Entity) create(clasa));
        }

        // deserialize each entity
        for (Entity entity : this){
            entity.deserialize(stream, this);
        }

        stream.get(SeriConf.END);
    }
}
