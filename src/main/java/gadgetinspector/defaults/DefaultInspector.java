package gadgetinspector.defaults;

import gadgetinspector.Executor;
import gadgetinspector.Inspector;

import javax.swing.*;

public abstract class DefaultInspector extends Inspector {

    public DefaultInspector(Executor executor) {
        super(executor);
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
