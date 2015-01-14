package gadgetinspector;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.reflections.ReflectionUtils;

import javax.swing.*;
import java.io.OutputStream;

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

	public void inspect(final Object self, final String codeToExecute, OutputStream output) {
		showFrame(getExecutor().inspect(self, codeToExecute, output));
	}

	public void inspect(final Object self) {
		showFrame(self);
	}

    public Context getContext() {
        return executor.getContext();
    }

    public void setSelf(Object self) {
        executor.setSelf(self);
    }

    public String getSyntaxStyle() {
        return SyntaxConstants.SYNTAX_STYLE_NONE;
    }
}
