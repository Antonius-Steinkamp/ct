package de.anst.minesweeper;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route("minesweeper")
public class MinesweeperView extends VerticalLayout implements HasDynamicTitle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MinesweeperView() {
		add(new H1("Minesweeper"));
		add(new Board(10, 10));
	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return "Minesweeper";
	}
}
