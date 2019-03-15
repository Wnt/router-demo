package org.vaadin.jonni;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.DefaultErrorHandler;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(Lumo.class)
@HtmlImport("frontend://bower_components/vaadin-lumo-styles/presets/compact.html")
public class MainLayout extends VerticalLayout implements RouterLayout, BeforeEnterObserver {

	private static final int COUNT = 16;
	private Div div;

	public MainLayout() {
		HorizontalLayout header = new HorizontalLayout(new RouterLink("View 1", View1.class),
				new Button("view 2", click -> {
					UI.getCurrent().navigate(View2.class);
				}), new Button("logout", click -> {
					VaadinSession.getCurrent().getSession().invalidate();
					// redirect user to the root of the application
					UI.getCurrent().getPage().executeJavaScript("window.location='';");
					setDefaultErrorHandler();
				}));
		header.setWidthFull();
		div = new Div();
		div.setSizeFull();
		add(header, div);

		setSizeFull();
		expand(div);
	}

	private void setDefaultErrorHandler() {
		UI.getCurrent().getSession().setErrorHandler(new DefaultErrorHandler());
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		div.getElement().appendChild(content.getElement());
	}

	public static Dto getNewMockDto(int id) {
		return new Dto(id, UUID.randomUUID().toString(), LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
	}

	public static class Dto {

		@Override
		public String toString() {
			return "Dto [id=" + id + ", property1=" + property1 + ", date=" + date + "]";
		}

		private int id;
		private String property1;
		private Instant date;

		public Dto(int id,String property1, Instant date) {
			super();
			this.id = id;
			this.property1 = property1;
			this.date = date;
		}

		public String getProperty1() {
			return property1;
		}

		public void setProperty1(String property1) {
			this.property1 = property1;
		}

		public Instant getDate() {
			return date;
		}

		public void setDate(Instant date) {
			this.date = date;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		System.out.println(event.getNavigationTarget().getSimpleName());

		if (event.getNavigationTarget() != View1.class && VaadinSession.getCurrent().getAttribute("user") == null) {
			event.rerouteTo("login");
			VaadinSession.getCurrent().setAttribute("redirectAfterLogin", event.getLocation().getPathWithQueryParameters());
		}
	}
}
