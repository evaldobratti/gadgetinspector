package inspectorgadget.panels.treenode;

class NoneNode extends InspectorTreeNode {

	protected NoneNode(final String name) {
		super(name, null);
	}

	@Override
	protected void loadChildren() {}

	@Override
	public String toString() {
		return name + " = None";
	}

}
