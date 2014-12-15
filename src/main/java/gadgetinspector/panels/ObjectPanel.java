package gadgetinspector.panels;

import gadgetinspector.Inspector;
import gadgetinspector.SelfProvider;
import gadgetinspector.panels.treenode.InspectorTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class ObjectPanel implements SelfProvider {

	private JTree tree;

	private final Object object;

	private final JScrollPane component;

	private final Inspector inspector;


	public ObjectPanel(final Object object, final Inspector inspector) {
		this.object = object;
		this.inspector = inspector;

		component = createComponent();
	}

	private JScrollPane createComponent() {
		final JScrollPane arvorePanel = new JScrollPane();
		tree = new JTree(new DefaultTreeModel(InspectorTreeNode.nodeFor("inspected", object)));
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					inspector.inspect(getSelf());
				}
			}
		});


		openFirstNode();
		arvorePanel.setViewportView(tree);
        Dimension size = new Dimension(250, 0);
        arvorePanel.setMinimumSize(size);
        arvorePanel.setPreferredSize(size);

		new InspectorEventCallback(tree) {

			@Override
			public void inspect() {
				inspector.inspect(getSelectedValues());
			}
		}.install();

		return arvorePanel;
	}

	private void openFirstNode() {
		tree.setSelectionInterval(0, 0);
	}


	private List<Object> getSelectedValues() {
		final List<Object> inspectList = new ArrayList<Object>();
		final TreeSelectionModel model = tree.getSelectionModel();
		for (final TreePath path : model.getSelectionPaths()) {
			final InspectorTreeNode node = (InspectorTreeNode)path.getLastPathComponent();
			inspectList.add(node.getObject());
		}
		return inspectList;
	}

	public JComponent getComponent() {
		return component;
	}

	@Override
	public Object getSelf() {
        final InspectorTreeNode inspectorNode = (InspectorTreeNode)tree.getLastSelectedPathComponent();
        if (inspectorNode == null)
            return null;
        return inspectorNode.getObject();
	}

}
