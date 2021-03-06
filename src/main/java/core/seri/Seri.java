package core.seri;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

// ========================================================
// Polymorphic, graph, text serialization & deserialization
// ========================================================
// Supports Boolean, Integer, Long, Double, Float, String, Class, Enum
// and others Seri's
// Seri's that are used in a graph manner should implement SeriGraph, they are serialized & deserialized by index,
// and are responsible to add themselves to the pool by calling SeriGraphPool.getInstance().add (this) in the default constructor

public interface Seri {
    // =============
    // Serialization
    // =============
    default boolean isDefault(){
        return false;
    }

    default void serialize(String prefix, StringBuilder stream) throws IllegalAccessException {
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
            serialize(object, prefix + SeriConf.INDENT, stream);
        }
        stream.append(SeriConf.NEWLINE).append(prefix).append(SeriConf.END);
    }

    default void serialize(Object object, String prefix, StringBuilder stream) throws IllegalAccessException {
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
        } else if (SeriGraph.class.isAssignableFrom(clasa)){
            stream.append(SeriGraphPool.getInstance().indexOf((SeriGraph)object));
        } else if (Seri.class.isAssignableFrom(clasa)){
            ((Seri)object).serialize(prefix, stream);
        } else if (clasa.isEnum()){
            stream.append(object.toString());
        }
    }

    // ===============
    // Deserialization
    // ===============
    default void deserialize(TokenStream stream) throws Exception {
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
                Object object = deserialize(clasa, stream);
                field.set(this, object);
            }
        } while (true);
    }

    default Object deserialize (Class<?> clasa, TokenStream stream) throws Exception {
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
        } else if (SeriGraph.class.isAssignableFrom(clasa)) {
            int index = Integer.parseInt(stream.get());
            object = SeriGraphPool.getInstance().get(index);
        } else if (Seri.class.isAssignableFrom(clasa)){
            object = create(clasa);
            ((Seri)object).deserialize(stream);
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
