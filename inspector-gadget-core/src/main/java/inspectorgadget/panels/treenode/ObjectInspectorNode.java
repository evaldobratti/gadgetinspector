package inspectorgadget.panels.treenode;

import com.google.common.base.Predicates;
import org.reflections.ReflectionUtils;

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
		for (final Field field : ReflectionUtils.getAllFields(getObject().getClass(), Predicates.alwaysTrue())) {
			try {
				final String prefix = field.getDeclaringClass() == getObject().getClass() ? "" : field.getDeclaringClass().getSimpleName() + ".";
				final Object valor = field.get(getObject());
				add(nodeFor(prefix + field.getName(), valor));
			} catch (final IllegalArgumentException ignored) {} catch (final IllegalAccessException ignored) {}
		}
	}

}
