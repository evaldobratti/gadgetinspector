package gadgetinspector.groovy;

import gadgetinspector.Inspector;
import gadgetinspector.defaults.DefaultInspector;
import gadgetinspector.defaults.ObjectJFrame;
import gadgetinspector.defaults.WorkspaceJFrame;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;

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

