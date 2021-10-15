package de.anst.all;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteData;

import lombok.extern.java.Log;

@Route("all")
@Log
public class KnownRoutes extends VerticalLayout implements HasDynamicTitle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809014867500373595L;

	public KnownRoutes() {
		final List<RouteData> availableRoutes = getAvailableRoutes();
		for (RouteData routeData : availableRoutes) {
			add(new AnstAnchor(routeData.getTemplate(), routeData.getTemplate(), routeData.getTemplate()));
		}
	}

	public static List<RouteData> getAvailableRoutes() {
		return RouteConfiguration.forApplicationScope().getAvailableRoutes();
	}

	public static Select<AnstAnchor> createSelectWithRoutes() {
		Select<AnstAnchor> select = new Select<>();
		select.setLabel("Andere");
		List<RouteData> availableRoutes = getAvailableRoutes();
		List<AnstAnchor> theAnchors = new ArrayList<>();
		for (RouteData routeData : availableRoutes) {
			theAnchors.add(new AnstAnchor(routeData.getTemplate(), routeData.getTemplate(), routeData.getTemplate()));
		}
		

		log.info(availableRoutes.size() + " routes added");
		// Choose which property from Route is the presentation value
		select.setItemLabelGenerator(AnstAnchor::getText);
		select.setItems(theAnchors);

		select.addValueChangeListener(event -> event.getValue().click());

		return select;
	}

	@Override
	public String getPageTitle() {
		return KnownRoutes.class.getSimpleName();
	}
}
