package de.blafoo.bingo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import org.vaadin.barcodes.Barcode;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.shared.Registration;

import de.anst.all.KnownRoutes;
import lombok.extern.java.Log;

@Log
@Route
@Push
@PWA(name = "Antonius", shortName = "Antonius", offlinePath = "offline.html")
public class MainView extends VerticalLayout
		implements ComponentEventListener<ClickEvent<Button>>, BeforeLeaveObserver, HasDynamicTitle {

	private static final long serialVersionUID = -8778411782845391587L;

	private List<BitSet> matrix;

	/** Anzahl Spalten */
	private final static int numberOfCols = 4;

	/** Anzahl Zeilen */
	private final static int numberOfRows = 4;

	/** Name des Spielers */
	private String name = VaadinSession.getCurrent().getBrowser().getAddress();

	public MainView() {
		HorizontalLayout header = new HorizontalLayout();
		// Defaults defaults
		Barcode qrcode = new Barcode("https://antonius.herokuapp.com/",
		        Barcode.Type.qrcode,
		        "100px",
		        "100px");
		
		header.add(qrcode, new H2("Bingo by c't and anst"));
		add(header);

		add(new Label(
				"Langeweile bei Besprechungen im täglichen Büroalltag oder zu viel Bullshit? Mit diesem Spiel geht das vorbei. Einfach beim Bingo spielen aufmerksam zuhören. Fällt eines der auf dem Spielfeld gelisteten Worte, kann dieses markiert werden. Eine komplette Reihe horizontal, vertikal oder diagonal? Bingo!"));
		add(new H6(""));

		// Name des Spielers
		final TextField spieler = new TextField();
		spieler.setPlaceholder(name);
		spieler.setLabel("Name");
		spieler.addValueChangeListener(e -> {
			name = e.getValue();
		});
		add(spieler);

		// Bingo-Tableau
		createBingoGrid(this);

		// KnownRoutes.addRoutes(this);
		add(new KnownRoutes());
		// add(KnownRoutes.createSelectWithRoutes());
	}

	/**
	 * Bingo-Tableau erzeugen
	 * 
	 * @param parent
	 */
	private void createBingoGrid(final VerticalLayout parent) {

		List<String> data = BingoModel.getData(BingoModel.BESPRECHUNGS_DATEN);
		Collections.shuffle(data);

		HorizontalLayout layout = new HorizontalLayout();
		matrix = new ArrayList<>();

		for (int row = 0; row < numberOfRows; row++) {
			matrix.add(new BitSet(numberOfCols));
		}

		for (int col = 0; col < numberOfCols; col++) {
			createBingoColumn(layout, col, data.subList(numberOfRows * col, numberOfRows * (col + 1)));
		}

		parent.add(layout);
	}

	private void createBingoColumn(final HorizontalLayout parent, final int column, final List<String> columnValues) {

		VerticalLayout layout = new VerticalLayout();
		// Abstände um die Buttons minimieren
		layout.setPadding(false);
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
		layout.setWidth("25%");

		int row = 0;
		for (String buttonText : columnValues) {
			BingoButton button = new BingoButton(column, row, buttonText, this);
			// per Default haben Buttons nur die minimal notwendige Größe
			button.setHeight("75px");
			button.setWidth("175px");
			layout.add(button);
			row++;
		}

		parent.add(layout);
	}

	/**
	 * Callback vom BingoButton. Wird beim Click auf ein Feld ausgelöst.
	 */
	@Override
	public void onComponentEvent(ClickEvent<Button> event) {
		BingoButton button = (BingoButton) event.getSource();
		if (button.isChecked()) {
			matrix.get(button.getRow()).set(button.getCol());
		} else {
			matrix.get(button.getRow()).clear(button.getCol());
		}

		checkBingo();
	}

	/**
	 * Gibt es eine 'komplette' Reihe?
	 */
	private void checkBingo() {

		// horizontal
		for (BitSet col : matrix) {
			if (col.cardinality() == numberOfCols) {
				bingo();
			}
		}
		// vertikal
		for (int col = 0; col < numberOfCols; col++) {
			boolean isRowComplete = true;
			for (int row = 0; row < numberOfRows; row++) {
				if (!matrix.get(row).get(col)) {
					isRowComplete = false;
					break;
				}
			}
			if (isRowComplete) {
				bingo();
			}
		}

		// vertikal LO->RU
		boolean isComplete = true;
		for (int col = 0; col < numberOfCols; col++) {
			if (!matrix.get(col).get(col)) {
				isComplete = false;
				break;
			}
		}
		if (isComplete) {
			bingo();
		}

		// vertikal LU->RO
		isComplete = true;
		for (int col = 0; col < numberOfCols; col++) {
			if (!matrix.get(numberOfRows - col - 1).get(col)) {
				isComplete = false;
				break;
			}
		}
		if (isComplete) {
			bingo();
		}

	}

	/**
	 * Eine Reihe ist komplett. Wir können in Jubel ausbrechen!
	 */
	private void bingo() {
		Dialog bingo = new Dialog();
		H1 h1 = new H1("BINGO!");
		h1.getStyle().set("text-align", "center");

//		Image image = new Image("/images/bingo.png", "Bingo!");
		Image image = new Image("/images/animiertes-feuerwerk-bild-0087.gif", "Bingo!");

		AudioPlayer player = new AudioPlayer("/images/bingo.m4a", true);

		bingo.add(h1, image, player);
		bingo.open();

		Broadcaster.broadcast(name);
	}

	private Registration broadcasterRegistration;

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		UI ui = attachEvent.getUI();
		WebBrowser browser = VaadinSession.getCurrent().getBrowser();
		System.out.println("Attach " + name + " from " + browser.getAddress());
		broadcasterRegistration = Broadcaster.register(newMessage -> {
			ui.access(() -> showNotification("BINGO! '" + newMessage + "' hat gewonnen!",
					NotificationVariant.LUMO_SUCCESS));
		});
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		System.out.println("Detach " + name);
		broadcasterRegistration.remove();
		broadcasterRegistration = null;
	}

	private void showNotification(String text, NotificationVariant variant) {
		Notification notification = new Notification(text);
		if (variant != null) {
			notification.addThemeVariants(variant);
		}
		notification.setDuration(5000);
		notification.open();
	}

	// BeforeLeaveObserver
	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		log.info("Leaving " + this.getClassName());
	}

	@Override
	public String getPageTitle() {
		return "Bingo";
	}

}
