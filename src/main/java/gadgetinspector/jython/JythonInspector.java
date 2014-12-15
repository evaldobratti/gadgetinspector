package gadgetinspector.jython;

import gadgetinspector.defaults.ClasspathVariableSolver;
import gadgetinspector.defaults.ObjectJFrame;
import gadgetinspector.defaults.WorkspaceJFrame;
import gadgetinspector.Inspector;

import javax.swing.*;

public class JythonInspector extends Inspector {


    public JythonInspector() {
        super(new JythonExecutor(new JythonContext(new ClasspathVariableSolver())));
    }

    @Override
    public void showFrame(final Object object) {
        new ObjectJFrame(object, this).getFrame().setVisible(true);
    }

    @Override
    public void showInitialFrame() {
        JFrame frame = new WorkspaceJFrame("", this).getFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
