package gadgetinspector.panels.treenode;

import gadgetinspector.reflection.ReflectionUtils;

import java.lang.reflect.Field;

class ObjectInspectorNode<T extends Object> extends InspectorTreeNode<T> {

	public ObjectInspectorNode(final String name, final T object) {
		super(name, object);
	}

	@Override
	protected void loadChildren() {
		loadFields();
	}

	@SuppressWarnings("unused")
	protected void loadFields() {
		for (final Field field : ReflectionUtils.loadFields(getObject().getClass())) {
			try {
				final String prefix = field.getDeclaringClass() == getObject().getClass() ? "" : field.getDeclaringClass().getSimpleName() + ".";
				final Object valor = field.get(getObject());
				add(nodeFor(prefix + field.getName(), valor));
			} catch (final IllegalArgumentException ignored) {} catch (final IllegalAccessException ignored) {}
		}
	}

}
