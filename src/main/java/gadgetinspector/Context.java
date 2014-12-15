package gadgetinspector;

import java.util.List;

public interface Context {
    List<String> getPossibleMethodsOf(String subject);
}
