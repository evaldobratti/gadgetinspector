package gadgetinspector;

import javax.swing.*;

public abstract class Inspector {

	private Executor executor;

    public Inspector(Executor executor) {
        this.executor = executor;
    }

	public abstract void showFrame(Object object);

    public abstract void showInitialFrame();

	public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showInitialFrame();
            }
        });
	}

	public Executor getExecutor() {
		return executor;
	}

	public void inspect(final Object self, final String codeToExecute) {
		showFrame(getExecutor().inspect(self, codeToExecute));
	}

	public void inspect(final Object self) {
		showFrame(self);
	}

    public Context getContext() {
        return executor.getContext();
    }
}
