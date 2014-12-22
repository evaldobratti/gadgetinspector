package gadgetinspector.panels;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import gadgetinspector.Inspector;
import gadgetinspector.SelfProvider;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;


public class WorkspacePanel {

    private final SelfProvider selfProvider;
    private final JSplitPane split;
    private final Inspector inspector;
    private RSyntaxTextArea workspaceTextArea;
    private JTextArea transcriptTextArea;

    public WorkspacePanel(final SelfProvider selfProvider, final Inspector inspector) {
        this.selfProvider = selfProvider;
        this.inspector = inspector;
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createSourcePanel(), createTranscriptPanel());
    }

    private Component createTranscriptPanel() {
        transcriptTextArea = new JTextArea();
        transcriptTextArea.setEnabled(false);
        final DefaultCaret caret = (DefaultCaret) transcriptTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        final JScrollPane transcriptScrollPanel = new JScrollPane();
        transcriptScrollPanel.setViewportView(transcriptTextArea);
        transcriptScrollPanel.setMinimumSize(new Dimension(0, 100));
        return transcriptScrollPanel;
    }

    private Component createSourcePanel() {
        workspaceTextArea = new RSyntaxTextArea();
        workspaceTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
        workspaceTextArea.setCodeFoldingEnabled(true);
        workspaceTextArea.setAntiAliasingEnabled(true);

        workspaceTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                inspector.setSelf(selfProvider.getSelf());
            }
        });

        final AutoCompletion completion = new AutoCompletion(new InspectorCompletion(inspector.getContext()));
        completion.setParameterAssistanceEnabled(true);
        completion.install(workspaceTextArea);

        final TranscriptOutputStream output = new TranscriptOutputStream();

        new InspectorEventCallback(workspaceTextArea) {

            @Override
            public void inspect() {
                inspector.inspect(selfProvider.getSelf(), getCodeToExecute(), output);
            }

            @Override
            public void execute() {
                new SwingWorker<Object, Object>() {

                    @Override
                    protected Object doInBackground() throws Exception {
                        workspaceTextArea.getRootPane().setEnabled(false);
                        inspector.getExecutor().execute(selfProvider.getSelf(), getCodeToExecute(), output);
                        workspaceTextArea.getRootPane().setEnabled(true);
                        return null;
                    }
                }.execute();

            }
        }.install();
        workspaceTextArea.setText(String.valueOf(selfProvider.getSelf()));
        final RTextScrollPane scrollPane = new RTextScrollPane(workspaceTextArea);
        scrollPane.setMinimumSize(new Dimension(320, 200));
        return scrollPane;
    }

    public String getCodeToExecute() {
        if (workspaceTextArea.getSelectedText() == null || workspaceTextArea.getSelectedText().isEmpty())
            return workspaceTextArea.getText();

        return workspaceTextArea.getSelectedText();
    }

    public JComponent getComponent() {
        return split;
    }

    private class TranscriptOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            throw new RuntimeException("It should not be called");
        }

        @Override
        public void write(byte[] bytes, int offset, int length) throws IOException {
            transcriptTextArea.append(new String(Arrays.copyOfRange(bytes, offset, length)));
        }
    }

}
