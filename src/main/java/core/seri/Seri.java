package core.seri;

import core.TokenStream;
import core.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

// ===================================================================================
// Polymorphic, Bi-directional Entity graph, text serialized, with a pool for entities
// ===================================================================================
// Supports Boolean, Integer, Long, Double, Float, String, Class
// and others Seri's
// Only entities supports bi-directional graph references and they are referenced from a pool

public interface Seri {
    // =============
    // Serialization
    // =============
    default boolean isDefault(){
        return false;
    }

    default void serialize(String prefix, List<Entity> entitiesPool, StringBuilder stream) throws IllegalAccessException {
        stream.append(SeriConf.BEGIN);
        Field[] fields = getClass().getFields();
        for (Field field : fields){
            int mod = field.getModifiers();
            if (Modifier.isFinal(mod) || Modifier.isStatic(mod) || Modifier.isTransient(mod))
                continue;
            Object object = field.get(this);
            if (null == object)
                continue;
            if (isDefault(object))
                continue;

            stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.INDENT);
            stream.append(getFieldName(field)).append(SeriConf.SEPARATOR).append(getClassName(object.getClass())).append(SeriConf.SEPARATOR);
            serialize(object, prefix + SeriConf.INDENT, entitiesPool, stream);
        }
        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.END);
    }

    default void serialize(Object object, String prefix, List<Entity> entitiesPool, StringBuilder stream) throws IllegalAccessException {
        Class<?> clasa = object.getClass();
        if (clasa == Boolean.class ||
            clasa == Integer.class ||
            clasa == Long.class ||
            clasa == Float.class ||
            clasa == Double.class ||
            clasa == String.class){
            stream.append(object.toString());
        } else if (clasa == Class.class){
            stream.append(getClassName((Class<?>)object));
        } else if (Entity.class.isAssignableFrom(clasa)){
            stream.append(entitiesPool.indexOf(object));
        } else if (Seri.class.isAssignableFrom(clasa)){
            ((Seri)object).serialize(prefix, entitiesPool, stream);
        } else if (clasa.isEnum()){
            stream.append(object.toString());
        }
    }

    // ===============
    // Deserialization
    // ===============
    default void deserialize(TokenStream stream, List<Entity> entitiesPool) throws Exception {
        stream.get(SeriConf.BEGIN);

        Field[] fields = getClass().getFields();

        do {
            String fieldName = stream.get();
            if (SeriConf.END.equals(fieldName))
                break;
            String className = stream.get();
            Class<?> clasa = getClass(className);

            Field field = getField(fieldName, fields);
            if (null == field){
                deserializationSkip (stream);
            } else {
                Object object = deserialize(clasa, entitiesPool, stream);
                field.set(this, object);
            }
        } while (true);
    }

    default Object deserialize (Class<?> clasa, List<Entity> entitiesPool, TokenStream stream) throws Exception {
        Object object = null;
        if (clasa == Integer.class){
            object = Integer.parseInt(stream.get());
        } else if (clasa == Long.class){
            object = Long.parseLong(stream.get());
        } else if (clasa == Float.class){
            object = Float.parseFloat(stream.get());
        } else if (clasa == Double.class){
            object = Double.parseDouble(stream.get());
        } else if (clasa == Boolean.class){
            object = Boolean.parseBoolean(stream.get());
        } else if (clasa == String.class){
            object = stream.get();
        } else if (Class.class == clasa) {
            object = getClass(stream.get());
        } else if (Entity.class.isAssignableFrom(clasa)) {
            int index = Integer.parseInt(stream.get());
            object = entitiesPool.get(index);
        } else if (Seri.class.isAssignableFrom(clasa)){
            object = create(clasa);
            ((Seri)object).deserialize(stream, entitiesPool);
        } else if (clasa.isEnum()) {
            Object[] enumValues = clasa.getEnumConstants();
            String value = stream.get();
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString())){
                    object = enumValue;
                    break;
                }
            }
        }
        return object;
    }

    default void deserializationSkip(TokenStream stream) {
        String token = stream.get();
        if (SeriConf.BEGIN.equals(token)){
            while (!SeriConf.END.equals(token)){
                token = stream.get();
            }
        }
    }

    // =======
    // Helpers
    // =======
    default Object create(Class<?> clasa) throws IllegalAccessException, InstantiationException {
        return clasa.newInstance();
    }

    default boolean isDefault(Object object){
        if (Double.class == object.getClass()){
            return (Double)object == 0.0;
        } else if (Float.class == object.getClass()){
            return (Float)object == 0.0f;
        } else if (Long.class == object.getClass()){
            return (Long)object == 0;
        } else if (Integer.class == object.getClass()){
            return (Integer)object == 0;
        } else if (String.class == object.getClass()){
            return (object == null) || ((String)object).isEmpty();
        } else if (object instanceof Seri)
            return ((Seri)object).isDefault();
        return false;
    }

    default String getClassName(Class<?> clasa){
        return clasa.getName();
    }

    default Class<?> getClass (String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    default String getFieldName(Field field){
        return field.getName();
    }

    default Field getField(String fieldName, Field[] fields){
        for (Field field : fields){
            String name = getFieldName(field);
            if (fieldName.equals(name))
                return field;
        }
        return null;
    }
}
