package gadgetinspector.panels;

import gadgetinspector.AttributesSolver;
import gadgetinspector.SelfProvider;
import gadgetinspector.Inspector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map.Entry;
import java.util.Set;



public class EntityPanel implements SelfProvider {

	private final Object objeto;
    private AttributesSolver attributesSolver;

    private final JComponent component;
	private final Inspector inspector;

	public EntityPanel(final Object objeto, final Inspector inspector, AttributesSolver attributesSolver) {
		this.objeto = objeto;
		this.inspector = inspector;
        this.attributesSolver = attributesSolver;

        component = createPanel();
	}


	private JScrollPane createPanel() {
		final Set<Entry<String, Object>> atributos = attributesSolver.getAttributes(objeto);
		final Object[][] valores = new Object[atributos.size()][3];

        int i = 0;
		for (final Entry<String, Object> tupla : atributos) {
			valores[i][0] = tupla.getKey();
			valores[i][1] = tupla.getValue();
			valores[i][2] = tupla.getValue() != null ? tupla.getValue().getClass().getSimpleName() : "";

			i++;
		}

		final JTable atributosTable = new JTable(new NonEditableTableModel(valores, new String[] { "Nome", "Valor", "Tipo" }));
		atributosTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2)
					inspector.inspect(atributosTable.getModel().getValueAt(atributosTable.getSelectedRow(), 1));
			}
		});

		final JScrollPane atributosPanel = new JScrollPane();
		atributosPanel.setViewportView(atributosTable);
		return atributosPanel;
	}


	private static class NonEditableTableModel extends DefaultTableModel {

		public NonEditableTableModel(final Object[][] valores, final String[] colunas) {
			super(valores, colunas);
		}

		@Override
		public boolean isCellEditable(final int row, final int column) {
			return false;
		}
	}

	public JComponent getComponent() {
		return component;
	}


	@Override
	public Object getSelf() {
		return objeto;
	}


}
