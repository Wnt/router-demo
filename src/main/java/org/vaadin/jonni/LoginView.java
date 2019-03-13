package org.vaadin.jonni;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("login")
public class LoginView extends VerticalLayout {
	public LoginView() {
		add(new Paragraph("Login first!"));
		add(new Button("Mock login mechanism", click ->  {
			VaadinSession.getCurrent().setAttribute("user", "Jonni");
			UI.getCurrent().navigate(View2.class);
		}));
	}
}
