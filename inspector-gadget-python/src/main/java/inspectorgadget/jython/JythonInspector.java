package inspectorgadget.jython;


import inspectorgadget.defaults.ClasspathVariableSolver;
import inspectorgadget.defaults.DefaultInspector;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class JythonInspector extends DefaultInspector {

    public JythonInspector() {
        super(new JythonExecutor(new JythonContext(new ClasspathVariableSolver())));
    }

    @Override
    public String getSyntaxStyle() {
        return SyntaxConstants.SYNTAX_STYLE_PYTHON;
    }

    public static void main(final String[] args) {
        new JythonInspector().start();
    }
}
