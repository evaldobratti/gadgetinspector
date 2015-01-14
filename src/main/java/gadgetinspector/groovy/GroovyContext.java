package gadgetinspector.groovy;

import com.google.common.base.Predicates;
import gadgetinspector.Context;
import gadgetinspector.defaults.ClasspathVariableSolver;
import groovy.lang.Binding;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class GroovyContext extends Binding implements Context {

    ClasspathVariableSolver classpath = new ClasspathVariableSolver();

    @Override
    public Object getVariable(String name) {
        Object solve = classpath.solve(name);
        if (solve != null)
            return solve;
        return super.getVariable(name);
    }

    @Override
    public Object getProperty(String name) {
        Object solve = classpath.solve(name);
        if (solve != null)
            return solve;
        return super.getVariable(name);
    }

    @Override
    public List<String> getPossibleMethodsOf(String subject) {
        Object object = getVariable(subject);
        if (object == null)
            object = getProperty(subject);

        if (object == null)
            return new ArrayList<String>();

        Set<Method> methods = ReflectionUtils.getAllMethods(object.getClass(), Predicates.alwaysTrue());
        List<String> methodsName = new ArrayList<String>();
        for (Method method : methods)
            methodsName.add(method.getName());
        return methodsName;
    }
}
