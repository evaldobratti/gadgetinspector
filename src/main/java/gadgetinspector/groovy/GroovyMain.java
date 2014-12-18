package gadgetinspector.groovy;

import gadgetinspector.Inspector;
import gadgetinspector.defaults.ObjectJFrame;
import gadgetinspector.defaults.WorkspaceJFrame;

import javax.swing.*;

public class GroovyMain extends Inspector {

    public GroovyMain() {
        super(new GroovyExecutor(new GroovyContext()));
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

    public static void main(String[] args) {
        new GroovyMain().start();
    }
}

