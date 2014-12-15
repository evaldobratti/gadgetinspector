package gadgetinspector.panels.treenode;

import java.util.Map;

class MapEntryInspectorNode extends InspectorTreeNode<Map.Entry> {

	public MapEntryInspectorNode(final Map.Entry object) {
		super("IGNORED", object);
	}

	@Override
	protected void loadChildren() {
		add(nodeFor("key", getObject().getKey()));
		add(nodeFor("value", getObject().getValue()));
	}

	@Override
	public String toString() {
		return getObject().toString();
	}
}
