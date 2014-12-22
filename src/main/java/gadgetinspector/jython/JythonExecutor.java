package gadgetinspector.jython;

import java.io.OutputStream;

import gadgetinspector.Executor;
import gadgetinspector.F0;
import org.python.core.PyException;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;


public class JythonExecutor implements Executor {

    private final JythonContext contexto;

    private final PythonInterpreter interpretador;

    public JythonExecutor(final JythonContext contexto) {
        this.contexto = contexto;
        this.interpretador = new PythonInterpreter(contexto);
    }

    @Override
    public void execute(final Object receiver, final String comando, OutputStream output) {
        executeBlock(receiver, new F0() {
            @Override
            public Object apply() {
                interpretador.exec(comando);
                return null;
            }
        }, output);
    }

    @Override
    public Object inspect(final Object receiver, final String comando, OutputStream output) {
        return executeBlock(receiver, new F0() {
            @Override
            public Object apply() {
                return interpretador.eval(comando).__tojava__(Object.class);
            }
        }, output);
    }

    @Override
    public JythonContext getContext() {
        return contexto;
    }

    @Override
    public void setSelf(Object self) {
        interpretador.set("self", self);
    }

    private Object executeBlock(Object receiver, F0 block, OutputStream output) {
        try {
            interpretador.setOut(output);
            interpretador.setErr(output);
            setSelf(receiver);
            return block.apply();
        } catch (final PyException e) {
            e.printStackTrace();
            return null;
        } finally {
            interpretador.setOut(System.out);
            interpretador.setErr(System.err);
        }
    }
}
