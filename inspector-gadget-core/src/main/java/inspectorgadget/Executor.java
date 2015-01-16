package inspectorgadget;


import java.io.OutputStream;

public interface Executor {

	Object inspect(Object self, String codeToExecute, OutputStream output);

	void execute(Object self, String codeToExecute, OutputStream output);

    Context getContext();

    void setSelf(Object self);
}
