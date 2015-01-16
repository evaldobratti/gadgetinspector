package inspectorgadget.panels.treenode;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.swing.tree.TreeNode;

public abstract class InspectorTreeNode<T> implements TreeNode {

	List<InspectorTreeNode> childrens;
	InspectorTreeNode parent;
	private final T object;
	protected final String name;

	protected InspectorTreeNode(final String name, final T object) {
		this.name = name;
		this.object = object;
	}

	@Override
	public TreeNode getChildAt(final int childIndex) {
		return childrens.get(childIndex);
	}

	@Override
	public int getChildCount() {
		if (childrens == null) {
			childrens = new ArrayList<InspectorTreeNode>();
			loadChildren();
		}
		return childrens.size();
	}

	protected abstract void loadChildren();

	@Override
	public TreeNode getParent() {
		return parent;
	}

	public void setParent(final InspectorTreeNode parent) {
		this.parent = parent;
	}

	public void add(final InspectorTreeNode node) {
		node.setParent(this);
		childrens.add(node);
	}

	@Override
	public int getIndex(final TreeNode node) {
		if (parent == null)
			return -1;
		return parent.childrens.indexOf(this);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return getChildCount() == 0;
	}

	@Override
	public Enumeration children() {
		return Collections.enumeration(childrens);
	}

	public T getObject() {
		return object;
	}

	@Override
	public String toString() {
		if (name == null || name.isEmpty())
			return format("%s (%s)", object, object.getClass().getSimpleName());

		return format("%s = %s (%s)", name, object, object.getClass().getSimpleName());
	}

	public static InspectorTreeNode nodeFor(final String name, final Object object) {
		if (object == null)
			return new NoneNode(name);

		if (object instanceof Map.Entry) {
			final Map.Entry entry = (Map.Entry)object;
			return new MapEntryInspectorNode(new ImmutableMapEntry(entry.getKey(), entry.getValue()));
		}

		if (Iterable.class.isAssignableFrom(object.getClass()) || Map.class.isAssignableFrom(object.getClass()))
			return new IterableInspectorNode(name, object);

		return new ObjectInspectorNode(name, object);
	}
}


