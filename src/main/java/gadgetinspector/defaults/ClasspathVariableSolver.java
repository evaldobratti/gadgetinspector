package gadgetinspector.defaults;

import gadgetinspector.VariableSolver;
import org.reflections.Reflections;
import org.reflections.scanners.AbstractScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executors;

public class ClasspathVariableSolver implements VariableSolver {

    private final Map<String, Collection<String>> classNameToQualifiedName;

    public ClasspathVariableSolver() {
        AbstractScanner scanner = new AbstractScanner() {
            @Override
            public void scan(Object o) {
                String className = getMetadataAdapter().getClassName(o);
                getStore().put(className.substring(className.lastIndexOf(".")).substring(1), className);
            }
        };
        new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()).setScanners(scanner).setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())));
        classNameToQualifiedName = scanner.getStore().asMap();
    }


    @Override
    public Object solve(String variableName) {
        Collection<String> qualifiedNames = classNameToQualifiedName.get(variableName);
        if (qualifiedNames != null && qualifiedNames.size() == 1) {
            try {
                return Class.forName(qualifiedNames.iterator().next());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
