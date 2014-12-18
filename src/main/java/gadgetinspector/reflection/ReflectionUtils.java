package gadgetinspector.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReflectionUtils {
    public static List<Field> loadFields(final Class<?> clazz) {
        if (clazz == Object.class)
            return Collections.EMPTY_LIST;

        final List<Field> fields = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));
        for (final Field field : fields)
            field.setAccessible(true);

        fields.addAll(loadFields(clazz.getSuperclass()));

        return fields;
    }

    public static List<Method> loadMethods(Class<? extends Object> clazz) {
        if (clazz == Object.class)
            return Collections.EMPTY_LIST;

        final List<Method> methods = new ArrayList<Method>(Arrays.asList(clazz.getDeclaredMethods()));
        for (final Method field : methods)
            field.setAccessible(true);

        methods.addAll(loadMethods(clazz.getSuperclass()));

        return methods;
    }
}
