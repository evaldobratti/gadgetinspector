package inspectorgadget.defaults;

import inspectorgadget.panels.ObjectPanel;
import inspectorgadget.panels.WorkspacePanel;
import inspectorgadget.Inspector;

import javax.swing.*;

public class ObjectJFrame {

    private JFrame jFrame;

    public ObjectJFrame(Object object, Inspector inspector) {
        jFrame = new JFrame();
        ObjectPanel objectPanel = new ObjectPanel(object, inspector);
        WorkspacePanel workspacePanel = new WorkspacePanel(objectPanel, inspector);

        jFrame.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, objectPanel.getComponent(), workspacePanel.getComponent()));
        jFrame.setSize(600, 400);
        jFrame.setLocationByPlatform(true);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    public JFrame getFrame() {
        return jFrame;
    }
}
