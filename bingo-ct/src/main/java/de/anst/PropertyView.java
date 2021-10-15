package de.anst;

import com.vaadin.flow.router.Route;

import de.anst.data.ValueLists;

@Route("props")
public class PropertyView extends BaseView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3524221429207445927L;


	public PropertyView() {
		super();
		
		setItems(ValueLists.getProperties());
	}
	

	@Override
	public String getPageTitle() {
		return PropertyView.class.getSimpleName();
	}
}
