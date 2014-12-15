package gadgetinspector.defaults;

import gadgetinspector.panels.WorkspacePanel;
import gadgetinspector.Inspector;
import gadgetinspector.SelfProvider;

import javax.swing.*;

public class WorkspaceJFrame {

    final JFrame jFrame;

    public WorkspaceJFrame(final Object object, Inspector inspector) {
        jFrame = new JFrame();
        jFrame.add(new WorkspacePanel(new SelfProvider() {
            @Override
            public Object getSelf() {
                return object;
            }
        }, inspector).getComponent());
        jFrame.pack();
        jFrame.setSize(600, 400);
        jFrame.setLocationByPlatform(true);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public JFrame getFrame() {
        return jFrame;
    }
}
