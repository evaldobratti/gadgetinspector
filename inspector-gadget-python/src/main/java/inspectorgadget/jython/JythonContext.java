package inspectorgadget.jython;


import inspectorgadget.Context;
import inspectorgadget.VariableSolver;
import org.python.core.*;

import java.util.ArrayList;
import java.util.List;


public class JythonContext extends PyObject implements Context {

	private final PyStringMap locals;
	private final VariableSolver variableSolver;

    static {
        Options.respectJavaAccessibility = false;
    }

	public JythonContext(final VariableSolver variableSolver) {
		this.variableSolver = variableSolver;
		locals = new PyStringMap();
	}

	@Override
	public PyObject __finditem__(final PyObject key) {
		return locals.__finditem__(key);
	}

	@Override
	public PyObject __finditem__(final int key) {
		return locals.__finditem__(key);
	}

	@Override
	public PyObject __finditem__(final String key) {
		final Object solve = variableSolver.solve(key);
		if (solve != null) {
            if (solve instanceof Class<?>)
                return PyJavaType.fromClass((Class<?>) solve);
            else
                return PyJavaType.wrapJavaObject(solve);
        }
		return locals.__finditem__(key);
	}

	@Override
	public void __setitem__(final int key, final PyObject value) {
		locals.__setitem__(key, value);
	}

	@Override
	public void __setitem__(final String key, final PyObject value) {
		locals.__setitem__(key, value);
	}

	@Override
	public void __setitem__(final PyObject key, final PyObject value) {
		locals.__setitem__(key, value);
	}

	@Override
	public PyObject __getitem__(final int key) {
		return locals.__getitem__(key);
	}

	@Override
	public PyObject __getitem__(final PyObject key) {
		return locals.__getitem__(key);
	}

	@Override
	public void __delitem__(final PyObject key) {
		locals.__delitem__(key);
	}

	@Override
	public void __delitem__(final String key) {
		locals.__delitem__(key);
	}

	public PyList keys() {
		return locals.keys();
	}

	@Override
	public int __len__() {
		return keys().size();
	}

    public List<String> getPossibleMethodsOf(String subject) {
        PyObject pyObject = __finditem__(subject);
        if (pyObject == null)
            return new ArrayList();

        List<String> methods = new ArrayList<String>();

        for (Object methodName : ((PyList) pyObject.__dir__()).toArray())
            methods.add(methodName.toString());

        return methods;
    }
}
