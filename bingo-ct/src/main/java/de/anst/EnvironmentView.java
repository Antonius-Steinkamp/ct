package de.anst;

import com.vaadin.flow.router.Route;

import de.anst.data.ValueLists;

@Route("env")
public class EnvironmentView extends BaseView {

	/**
	 * {@value #serialVersionUID}
	 */
	private static final long serialVersionUID = -8521925982625982053L;

	public EnvironmentView() {
		super();
		setItems(ValueLists.getEnvironmen());
	}

	@Override
	public String getPageTitle() {
		return EnvironmentView.class.getSimpleName();
	}
	
}
