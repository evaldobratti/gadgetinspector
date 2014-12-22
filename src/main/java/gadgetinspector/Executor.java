package gadgetinspector;


import java.io.OutputStream;
import java.io.Writer;

public interface Executor {

	Object inspect(Object self, String codeToExecute, OutputStream output);

	void execute(Object self, String codeToExecute, OutputStream output);

    Context getContext();

    void setSelf(Object self);
}
