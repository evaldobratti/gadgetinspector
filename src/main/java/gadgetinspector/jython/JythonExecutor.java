package gadgetinspector.jython;

import java.io.IOException;
import java.io.OutputStream;

import gadgetinspector.Executor;
import org.python.core.PyException;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;


public class JythonExecutor implements Executor {

    private JythonContext contexto;

    public JythonExecutor(final JythonContext contexto) {
        this.contexto = contexto;
        interpretador = new PythonInterpreter(contexto);
	}

	@Override
	public void execute(final Object receiver, final String comando) {
		interpretador.set("self", receiver);
		execute(comando);
	}

	@Override
	public Object inspect(final Object receiver, final String comando) {
		try {
			interpretador.set("self", receiver);
			return inspect(comando);
		} catch (final PyException e) {
			throw new RuntimeException(formattedMessageForPyException(e), e);
		} catch (final Exception e) {
			throw new RuntimeException(formattedMessageForRawException(e), e);
		}
	}


	private final PythonInterpreter interpretador;
	private OutputStream out;

	public void setOut(final OutputStream stream) {
		this.out = stream;
	}

	private void execute(final String code) {
		try {
//			interpretador.setOut(out);
			interpretador.exec(code);
		} catch (final PyException e) {
			printIntoOutStream(formattedMessageForPyException(e));
			e.printStackTrace();
		} catch (final Exception e) {
			printIntoOutStream(formattedMessageForRawException(e));
			e.printStackTrace();
		}
		finally {
			interpretador.setOut(System.out);
		}
	}

	protected String formattedMessageForRawException(final Exception e) {
		return formatErrorMessage(e.getClass().getSimpleName(), e.getMessage());
	}

	protected String formattedMessageForPyException(final PyException e) {
		if (e.value instanceof PyString)
			return formatErrorMessage("InternalPythonError", (String)((PyString)e.value.__tojava__(PyString.class)).__tojava__(String.class));

		return formatErrorMessage(String.valueOf(e.value), e.toString());
	}

	private void printIntoOutStream(final String message) {
		try {
			if (out != null)
				out.write((message + "\n").getBytes());
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

	private String formatErrorMessage(final String className, final String message) {
		return String.format("Errors had occurred: %s | %s", className, message);
	}

	private Object inspect(final String code) {
		return interpretador.eval(code).__tojava__(Object.class);
	}

    @Override
    public JythonContext getContext() {
        return contexto;
    }
}
