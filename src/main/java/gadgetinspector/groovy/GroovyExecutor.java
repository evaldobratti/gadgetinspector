package gadgetinspector.groovy;

import gadgetinspector.Context;
import gadgetinspector.Executor;
import groovy.lang.GroovyShell;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

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
    public Object inspect(Object self, String codeToExecute, OutputStream output) {
        PrintWriter printWriter = new PrintWriter(output);
        shell.setVariable("self", self);
        shell.setVariable("outputString", "");
        shell.evaluate("interceptor.start();");
        Object retorno = shell.evaluate(codeToExecute);
        shell.evaluate("interceptor.stop();");
        Object outputString = shell.getVariable("outputString");
        printWriter.write(outputString.toString());
        return retorno;
    }

    @Override
    public void execute(Object self, String codeToExecute, OutputStream output) {
        shell.setVariable("self", self);
        shell.setVariable("outputString", "");
        shell.evaluate("interceptor.start();");
        shell.evaluate(codeToExecute);
        shell.evaluate("interceptor.stop();");
        String outputString = shell.getVariable("outputString").toString();
        try {
            output.write(outputString.toString().getBytes(), 0, outputString.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getContext() {
        return context;
    }
}
