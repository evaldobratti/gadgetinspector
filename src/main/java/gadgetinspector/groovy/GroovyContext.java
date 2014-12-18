package gadgetinspector.groovy;

import gadgetinspector.Context;
import gadgetinspector.defaults.ClasspathVariableSolver;
import gadgetinspector.reflection.ReflectionUtils;
import groovy.lang.Binding;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.tools.shell.Groovysh;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

        List<Method> methods = ReflectionUtils.loadMethods(object.getClass());
        List<String> methodsName = new ArrayList<String>();
        for (Method method : methods)
            methodsName.add(method.getName());
        return methodsName;
    }
}
