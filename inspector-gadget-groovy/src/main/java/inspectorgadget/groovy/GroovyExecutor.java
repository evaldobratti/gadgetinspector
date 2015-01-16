package inspectorgadget.groovy;

import inspectorgadget.Context;
import inspectorgadget.Executor;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GroovyExecutor implements Executor {

    GroovyContext context;
    GroovyShell shell;

    GroovyExecutor(GroovyContext context) {
        this.context = context;
        shell = new GroovyShell(context);
        shell.evaluate("import groovy.ui.SystemOutputInterceptor;\n" +
                "outputString ='';" +
                "interceptor = new SystemOutputInterceptor({ outputString += it; false});\n");
    }


    @Override
    public void setSelf(Object self) {
        shell.setVariable("self", self);
    }

    @Override
    public Object inspect(Object self, final String codeToExecute, OutputStream output) {
        Object retorno = null;
        try {
            setSelf(self);
            shell.evaluate("interceptor.start();");
            retorno = shell.evaluate(codeToExecute);
        } catch (MultipleCompilationErrorsException e) {
            if (e.getMessage().contains("unable to resolve class")) {
                String className = getClassnameImportError(e);
                Object solve = context.classpath.solve(className);

                if (solve instanceof Class<?>) {
                    Class<?> clazz = (Class<?>) solve;
                    return inspect(self, "import " + clazz.getCanonicalName() + "\n" + codeToExecute, output);
                }
            } else {
                throw new RuntimeException(e);
            }
        } finally {
            shell.evaluate("interceptor.stop();");
            String outputString = shell.getVariable("outputString").toString();

            try {
                output.write(outputString.toString().getBytes(), 0, outputString.length());
            } catch (IOException e) {}
        }
        shell.setVariable("outputString", "");
        return retorno; }

    private String getClassnameImportError(MultipleCompilationErrorsException e) {
        Pattern compile = Pattern.compile("unable to resolve class (\\w*) ");
        Matcher matcher = compile.matcher(e.getMessage());
        matcher.find();
        return matcher.group(1);
    }


    @Override
    public void execute(Object self, String codeToExecute, OutputStream output) {
        inspect(self, codeToExecute, output);
    }

    @Override
    public Context getContext() {
        return context;
    }
}
