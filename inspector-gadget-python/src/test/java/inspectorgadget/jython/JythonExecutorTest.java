package inspectorgadget.jython;

import inspectorgadget.VariableSolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.python.core.PyJavaType;
import org.python.core.PyObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JythonExecutorTest {

    OutputStream output;
    VariableSolver variableSolverMock;
    JythonContext contexto;
    JythonExecutor jythonExecutor;

    @Before
    public void init() {
        output = spy(new ByteArrayOutputStream());
        variableSolverMock = mock(VariableSolver.class);
        contexto = new JythonContext(variableSolverMock);
        jythonExecutor = new JythonExecutor(contexto);
    }

    @Test
    public void receiverObjectShouldBeSetToselfVariable() {
        PyObject pyObject = PyJavaType.wrapJavaObject(123);
        jythonExecutor.execute(pyObject, "", output);

        assertEquals(pyObject, contexto.__finditem__("self"));
    }

    @Test
    public void inspectShouldGetVariableValueOnTheContext() {
        PyObject pyObject = PyJavaType.wrapJavaObject(123);
        contexto.__setitem__("blah", pyObject);

        assertEquals(123, jythonExecutor.inspect(null, "blah", output));
    }

    @Test
    public void inspectOnAMethodShouldEvaluateThatMethod() {
        assertEquals(321, jythonExecutor.inspect(new Dummy(), "self.method()", output));
    }

    @Test
    public void inspectOnOperatorsShouldEvaluateThatExpression() {
        assertEquals(3, jythonExecutor.inspect(1, "self + 2", output));
    }

    @Test
    public void executeMethodShouldExecuteTheCode() {
        Dummy innerDummy = spy(new Dummy());

        Dummy receiver = new Dummy(innerDummy);

        jythonExecutor.execute(receiver, "self.callInnerDummyMethod()", output);

        verify(innerDummy, times(1)).method();
    }

    @Test
    public void printCommandSendsOutputToStream() throws Exception {
        StringBuilder outputString = new StringBuilder();
        accumulateWriteCallsOn(outputString);

        jythonExecutor.execute(null, "print 'a'", output);

        assertEquals("a\n", outputString.toString());

    }

    @Test
    public void syntaxErrorSendsMessageToStream() throws Exception {
        StringBuilder outputString = new StringBuilder();
        accumulateWriteCallsOn(outputString);

        jythonExecutor.execute(null, "abc a()", output);

        assertEquals("  File \"<string>\", line 1\n" +
                "    abc a()\n" +
                "       ^\n" +
                "SyntaxError: no viable alternative at input 'a'\n", outputString.toString());
    }

    @Test
    public void executionErrorSendsMessageToStream() throws Exception {
        StringBuilder outputString = new StringBuilder();
        accumulateWriteCallsOn(outputString);

        jythonExecutor.execute(new Dummy(), "self.nonExistingMethod()", output);

        assertEquals("Traceback (most recent call last):\n" +
                "  File \"<string>\", line 1, in <module>\n" +
                "AttributeError: 'inspectorgadget.jython.JythonExecutorTest$Dummy' object has no attribute 'nonExistingMethod'\n", outputString.toString());
    }

    private void accumulateWriteCallsOn(final StringBuilder builder) throws IOException {
        doAnswer(
                new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        byte[] bytes = (byte[]) invocation.getArguments()[0];
                        int offset = (Integer) invocation.getArguments()[1];
                        int length = (Integer) invocation.getArguments()[2];

                        String str = new String(Arrays.copyOfRange(bytes, offset, length));

                        builder.append(str);
                        return null;
                    }
                }).when(output).write(any(byte[].class), anyInt(), anyInt());

    }

    private static class Dummy {

        private Dummy innerDummy;

        public Dummy() {

        }

        public Dummy(Dummy innerDummy) {
            this.innerDummy = innerDummy;
        }

        public int method() {
            return 321;
        }

        public void callInnerDummyMethod() {
            innerDummy.method();
        }
    }


}
