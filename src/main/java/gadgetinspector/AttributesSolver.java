package gadgetinspector;

import java.util.Map;
import java.util.Set;

public interface AttributesSolver {
    Set<Map.Entry<String,Object>> getAttributes(Object objeto);
}
