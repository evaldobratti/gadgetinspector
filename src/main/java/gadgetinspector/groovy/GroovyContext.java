package gadgetinspector.groovy;

import gadgetinspector.Context;
import gadgetinspector.defaults.ClasspathVariableSolver;
import groovy.lang.Binding;
import org.codehaus.groovy.tools.shell.Groovysh;

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
        return new ArrayList<String>();
    }
}
