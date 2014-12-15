package gadgetinspector;


public interface Executor {

	Object inspect(Object self, String codeToExecute);

	void execute(Object self, String codeToExecute);

    Context getContext();
}
