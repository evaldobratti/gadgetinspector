package inspectorgadget.panels;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class InspectorEventCallback {

	private final JComponent component;

	public InspectorEventCallback(final JComponent component) {
		this.component = component;
	}

	public void install() {
		component.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_I)
					inspect();

				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E)
					execute();
			}
		});
	}


	public void inspect() {}

	public void execute() {}
}