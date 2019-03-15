package org.vaadin.jonni;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

@Route("login")
public class LoginView extends VerticalLayout {
	public LoginView() {
		add(new Paragraph("Please login!"));
		add(new Button("Mock login mechanism", click -> {
			VaadinSession.getCurrent().setAttribute("user", "Jonni");
			// re-generate the session id to prevent session fixation attacks
			// https://en.wikipedia.org/wiki/Session_fixation
			VaadinService.reinitializeSession(VaadinRequest.getCurrent());
			
			addErrorHandlerToSession();
			if (VaadinSession.getCurrent().getAttribute("redirectAfterLogin") != null) {
				UI.getCurrent().navigate("" + VaadinSession.getCurrent().getAttribute("redirectAfterLogin"));
			}
			else {
				UI.getCurrent().navigate(View2.class);
			}
		}));
	}

	/**
	 * Adds a custom error handler to the Vaadin session that shows exception stack
	 * traces to the user.
	 */
	private void addErrorHandlerToSession() {
		UI.getCurrent().getSession().setErrorHandler(error -> {
			Throwable throwable = error.getThrowable();

			error.getThrowable().printStackTrace();

			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			throwable.printStackTrace(printWriter);

			Span stacktrace = new Span(writer.toString());
			stacktrace.getStyle().set("white-space", "pre-wrap");
			stacktrace.getStyle().set("font-family", "monospace");
			stacktrace.getStyle().set("overflow-y", "auto");

			String body = writer.toString();
			int limit = 500;
			if (body.length() > limit) {
				body = body.substring(0, limit);
			}

			Dialog dialog = new Dialog();
			Button closeButton = new Button("Dismiss", click -> dialog.close());
			closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			VerticalLayout content = new VerticalLayout(new H1("Internal error occurred:"),
					new H2(throwable.getClass().getSimpleName()), new Paragraph(throwable.getMessage()), stacktrace,
					new HorizontalLayout(
							new Anchor("mailto:jonni+tech-support@vaadin.com?subject=Error report&body=" + body,
									"Report to tech support"),
							closeButton));
			content.setHeight("600px");
			content.expand(stacktrace);
			dialog.add(content);
			dialog.open();

		});
	}
}
