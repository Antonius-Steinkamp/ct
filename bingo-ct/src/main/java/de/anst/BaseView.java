package de.anst;

import java.util.List;
import java.util.logging.Logger;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;

import de.anst.data.KeyValue;

/**
 * {@link VerticalLayout} mit {@link KeyValue}-{@link Grid}
 * @author Antonius
 *
 */
public abstract class BaseView extends VerticalLayout implements HasDynamicTitle {
	/**
	 * {@value #serialVersionUID}
	 */
	private static final long serialVersionUID = 4899618389092666174L;
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final Logger LOG = Logger.getLogger(BaseView.class.getName());
	private static final boolean COLUMNS_RESIZABLE = true;

	protected Grid<KeyValue<String, String>> grid = new Grid<>();

	public BaseView() {
        grid.addColumn(KeyValue::getKey).setHeader("Key").setResizable(COLUMNS_RESIZABLE).setSortable(true);
        // grid.addColumn(KeyValue::getValue).setHeader("Value").setResizable(COLUMNS_RESIZABLE);
        grid.addColumn(new ComponentRenderer<>(item -> {
        	final String[] split = item.getValue().split(LINE_SEPARATOR);
        	if ( split.length > 1) {
        		final VerticalLayout l = new VerticalLayout();
        		for (String one: split) {
        			if (one.length() > 0) {
        				l.add(new Paragraph(one));
        			}
        		}
        		return l;
        	}
            return new Paragraph(item.getValue());
        })).setHeader("Value").setResizable(COLUMNS_RESIZABLE);
        add(grid);
        setSizeFull();

	}
	
	/**
	 * Set items for {@link Grid}.
	 * 
	 * @param items List<KeyValue<String, String>> the izems
	 */
	public void setItems(List<KeyValue<String, String>> items) {
		final String msg = "Set " + items.size() + " items in grid"; 
		LOG.info(msg);
        grid.setItems(items);
	}

	@Override
	abstract public String getPageTitle();
}
