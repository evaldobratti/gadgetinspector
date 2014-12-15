package gadgetinspector.panels.treenode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class IterableInspectorNode extends ObjectInspectorNode {

	public IterableInspectorNode(final String name, final Object object) {
		super(name, object);
	}

	@Override
	protected void loadChildren() {
		loadIterable();

//		if (JythonCommons.getLoadInheritedMembers())
		super.loadChildren();
	}

	@SuppressWarnings("unused")
	private void loadIterable() {
		try {
			tryToLoadAsEntrySet();
		} catch (final SecurityException e) {
			loadIterable((Iterable)getObject());
		} catch (final IllegalArgumentException e) {
			loadIterable((Iterable)getObject());
		} catch (final NoSuchMethodException e) {
			loadIterable((Iterable)getObject());
		} catch (final IllegalAccessException e) {
			loadIterable((Iterable)getObject());
		} catch (final InvocationTargetException e) {
			loadIterable((Iterable)getObject());
		}
	}

	private void tryToLoadAsEntrySet() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		loadIterable(getEntrySetFrom(getObject()));
	}

	protected void loadIterable(final Iterable iterable) {
		for (final Object filho : iterable)
			add(nodeFor("", filho));
	}

	private Iterable getEntrySetFrom(final Object objeto) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		//to load any type of map
		final Method entrySetMethod = objeto.getClass().getMethod("entrySet");

		entrySetMethod.setAccessible(true);
		return (Iterable)entrySetMethod.invoke(objeto);
	}
}
