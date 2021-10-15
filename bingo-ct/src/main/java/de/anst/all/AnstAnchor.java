package de.anst.all;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;

/**
 * @author asteinkamp
 *
 */
public class AnstAnchor extends Anchor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2192554470899015569L;

	/**
	 * @param href String url
	 * @param title String title
	 * @param target String target window name
	 */
	public AnstAnchor(final String href, final String title, final String target) {
		super(href, title);
		setTarget(target);
	}
	
	public void click() {
		UI.getCurrent().getPage().executeJs("$0.click();", getElement());

	}
}
