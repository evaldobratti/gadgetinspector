package inspectorgadget.jython;

import inspectorgadget.VariableSolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.python.core.PyJavaType;
import org.python.core.PyObjectDerived;
import org.python.util.PythonInterpreter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.python.core.PyJavaType.wrapJavaObject;

public class JythonContextTest {

    @Before
    public void init() {
        new PythonInterpreter();
    }

    @Test
    public void findItemShouldReturnPyJavaTypeForClasses() {
        VariableSolver mock = mock(VariableSolver.class);
        when(mock.solve(anyString())).thenReturn(Object.class);

        Assert.assertEquals(PyJavaType.class.getSimpleName(), new JythonContext(mock).__finditem__("any").getClass().getSimpleName());
    }

    @Test
    public void findItemShouldReturnPyObjectDerivedForCommonObjects() {
        VariableSolver mock = mock(VariableSolver.class);
        when(mock.solve(anyString())).thenReturn(new Object());

        Assert.assertEquals(PyObjectDerived.class.getSimpleName(), new JythonContext(mock).__finditem__("any").getClass().getSimpleName());
    }

    @Test
    public void getPossibleMethodsOfObjectShouldReturnAllMethodsAvailableOnAObject() {
        VariableSolver mock = mock(VariableSolver.class);
        when(mock.solve(anyString())).thenReturn(new Dummy());

        Assert.assertTrue(new JythonContext(mock).getPossibleMethodsOf("any").contains("dummyMethod"));
    }

    @Test
    public void getPossibleMethodsOfClassShouldReturnAllMethodsAvailableOnAClass() {
        VariableSolver mock = mock(VariableSolver.class);
        when(mock.solve(anyString())).thenReturn(Dummy.class);

        Assert.assertTrue(new JythonContext(mock).getPossibleMethodsOf("any").contains("dummyStaticMethod"));
    }

    @Test
    public void contextShouldBreakEncapsulation() {
        VariableSolver mock = mock(VariableSolver.class);
        when(mock.solve(anyString())).thenReturn(new PrivateDummy());

        Assert.assertTrue(new JythonContext(mock).getPossibleMethodsOf("any").contains("privateDummyMethod"));
    }

    @Test
    public void contextAsksAndReturnAttributeSolverValueEvenIfItHasTheValue() {
        VariableSolver mock = mock(VariableSolver.class);
        when(mock.solve("blah")).thenReturn(1);

        JythonContext jythonContext = new JythonContext(mock);
        jythonContext.__setitem__("blah", wrapJavaObject(2));

        Assert.assertEquals(wrapJavaObject(1), jythonContext.__finditem__("blah"));
    }

    public static class Dummy {
        public static void dummyStaticMethod() {
        }

        public void dummyMethod() {
        }
    }

    private static class PrivateDummy {
        private int privateDummyMethod() {
            return 123;
        }
    }
}
