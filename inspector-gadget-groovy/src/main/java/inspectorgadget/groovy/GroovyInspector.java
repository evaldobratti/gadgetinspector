package inspectorgadget.groovy;

import inspectorgadget.defaults.DefaultInspector;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class GroovyInspector extends DefaultInspector {

    public GroovyInspector() {
        super(new GroovyExecutor(new GroovyContext()));
    }

    @Override
    public String getSyntaxStyle() {
        return SyntaxConstants.SYNTAX_STYLE_GROOVY;
    }

    public static void main(String[] args) {
        new GroovyInspector().start();
    }
}

