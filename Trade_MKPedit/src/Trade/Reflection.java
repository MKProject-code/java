// 
// Decompiled by Procyon v0.5.36
// 

package Trade;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

public class Reflection
{
    public static Class<?> getNMSClass(final String name) {
        return getClass("net.minecraft.server." + getVersion() + "." + name);
    }
    
    public static Class<?> getCraftClass(final String name) {
        return getClass("org.bukkit.craftbukkit." + getVersion() + "." + name);
    }
    
    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
    
    public static String getRawVersion() {
        final String pkg = Bukkit.getServer().getClass().getPackage().getName();
        return pkg.substring(pkg.lastIndexOf(".") + 1);
    }
    
    public static String getNMSVersion() {
        return "net.minecraft.server." + getRawVersion() + ".";
    }
    
    public static Class<?> wrapperToPrimitive(final Class<?> clazz) {
        if (clazz == Boolean.class) {
            return Boolean.TYPE;
        }
        if (clazz == Integer.class) {
            return Integer.TYPE;
        }
        if (clazz == Double.class) {
            return Double.TYPE;
        }
        if (clazz == Float.class) {
            return Float.TYPE;
        }
        if (clazz == Long.class) {
            return Long.TYPE;
        }
        if (clazz == Short.class) {
            return Short.TYPE;
        }
        if (clazz == Byte.class) {
            return Byte.TYPE;
        }
        if (clazz == Void.class) {
            return Void.TYPE;
        }
        if (clazz == Character.class) {
            return Character.TYPE;
        }
        return clazz;
    }
    
    public static Class<?>[] toParamTypes(final Object... params) {
        final Class<?>[] classes = (Class<?>[])new Class[params.length];
        for (int i = 0; i < params.length; ++i) {
            classes[i] = wrapperToPrimitive(params[i].getClass());
        }
        return classes;
    }
    
    public static Enum<?> getEnum(final String enumFullName) {
        final String[] x = enumFullName.split("\\.(?=[^\\.]+$)");
        if (x.length == 2) {
            final String enumClassName = x[0];
            final String enumName = x[1];
            final Class<Enum> cl = (Class<Enum>)getClass(enumClassName);
            return Enum.valueOf(cl, enumName);
        }
        return null;
    }
    
    public static Class<?> getClass(final String name) {
        try {
            return Class.forName(name);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setValue(final Object instance, final String fieldName, final Object value) {
        try {
            final Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Object getValue(final Object o, final String fieldName) {
        try {
            final Field field = o.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(o);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object callMethod(final Object object, final String method, final Object... params) {
        try {
            final Class<?> clazz = object.getClass();
            final Method m = clazz.getDeclaredMethod(method, toParamTypes(params));
            m.setAccessible(true);
            return m.invoke(object, params);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Method getMethod(final Object o, final String methodName, final Class<?>... params) {
        try {
            final Method method = o.getClass().getMethod(methodName, params);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Field getField(final Field field) {
        field.setAccessible(true);
        return field;
    }
}
