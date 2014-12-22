package gadgetinspector.jython;

import gadgetinspector.defaults.ClasspathVariableSolver;
import gadgetinspector.defaults.DefaultInspector;
import gadgetinspector.defaults.ObjectJFrame;
import gadgetinspector.defaults.WorkspaceJFrame;
import gadgetinspector.Inspector;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;

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
