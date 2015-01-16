package inspectorgadget.panels;

import static java.util.Collections.emptyList;
import static java.util.Collections.sort;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import inspectorgadget.Context;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProviderBase;


public class InspectorCompletion extends CompletionProviderBase {

	private final Pattern ENTERED_TEXT_PATTERN = Pattern.compile(".*?\\b([_a-zA-Z0-9]+)\\s*\\.\\s*([_a-zA-Z0-9]*?)$");
	private final Context context;;

	public InspectorCompletion(final Context context) {
		this.context = context;
	}

	@Override
	public String getAlreadyEnteredText(final JTextComponent comp) {
		final Matcher matcher = matchEnteredText(comp);
		return matcher.find() ? matcher.group(2) : "";
	}

	@Override
	public List getCompletionsAt(final JTextComponent comp, final Point p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List getParameterizedCompletions(final JTextComponent tc) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected List getCompletionsImpl(final JTextComponent comp) {
		final Matcher matcher = matchEnteredText(comp);
		return matcher.find() ? createCompletion(matcher.group(1), matcher.group(2)) : emptyList();
	}

	private List<BasicCompletion> createCompletion(final String subject, final String method) {
		final List<BasicCompletion> result = new ArrayList<BasicCompletion>();

		final List fields = context.getPossibleMethodsOf(subject);

		for (final Object obj : fields)
			if (obj.toString().toLowerCase().startsWith(method.toLowerCase()))
				result.add(new BasicCompletion(this, obj.toString()));

		sortCompletions(result);

		return result;
	}

	private void sortCompletions(final List<BasicCompletion> result) {
		sort(result, new Comparator<BasicCompletion>() {

			@Override
			public int compare(final BasicCompletion o1, final BasicCompletion o2) {
				return o1.getReplacementText().compareTo(o2.getReplacementText());
			}
		});
	}

	private Matcher matchEnteredText(final JTextComponent comp) {
		try {
			return ENTERED_TEXT_PATTERN.matcher(comp.getText(0, comp.getCaretPosition()));
		} catch (final BadLocationException e) {
			e.printStackTrace();
		}

		return Pattern.compile("A").matcher("B");
	}

}
