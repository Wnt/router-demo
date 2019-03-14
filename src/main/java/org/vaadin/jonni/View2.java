package org.vaadin.jonni;

import org.vaadin.jonni.MainLayout.Dto;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "view2", layout = MainLayout.class)
@PageTitle("secondary view")
public class View2 extends VerticalLayout
		implements HasUrlParameter<Integer>, BeforeLeaveObserver, AfterNavigationObserver {
	private boolean askBeforeLeave = false;
	private String originalLocation;
	private Div parameterArea;

	public View2() {

		TextField textField = new TextField("input field", valueChange -> {
			setAskBeforeLeave(true);
		});
		parameterArea = new Div();
		add(new H1("view2"), textField, parameterArea);

	}

	private void setAskBeforeLeave(boolean askBeforeLeave) {
		this.askBeforeLeave = askBeforeLeave;
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Integer parameter) {
		if (parameter != null) {
			boolean found = false;
			parameterArea.add(new Paragraph("parameter was: " + parameter));
			for (Dto dto : View1.list) {
				if (dto.getId() == parameter) {
					parameterArea.add(new Paragraph("DTO: " + dto));
					found = true;
					break;
				}
			}
			if (!found) {
				throw new InvalidItemId();
			}
		} else {
			parameterArea.removeAll();
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// store the current location so that we can restore that in beforeLeave if needed
		originalLocation = event.getLocation().getPathWithQueryParameters();
	}

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		// if user has unsaved changes
		if (askBeforeLeave) {
			// postpone the view change
			ContinueNavigationAction postponedNavigationAction = event.postpone();
			// and also replace the top most history state in the browser with this view's location
			// https://github.com/vaadin/flow/issues/3619
			UI.getCurrent().getPage().executeJavaScript("history.replaceState({},'','" + originalLocation + "');");
			
			// show the user a dialog where they can continue or cancel the exit action
			Dialog dialog = new Dialog();
			Button cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create(), cancel -> {
				dialog.close();
			});
			Button confirmButton = new Button("Confirm", VaadinIcon.CHECK.create(), confirm -> {
				dialog.close();
				
				// change to the view where the user was trying to navigate to
				postponedNavigationAction.proceed();
				// and also update the address bar to reflect that location 
				String destination = event.getLocation().getPathWithQueryParameters();
				UI.getCurrent().getPage().executeJavaScript("history.replaceState({},'','" + destination + "');");
			});
			dialog.add(new H1("Are you sure?"), new Paragraph("Discard unsaved changes and leave this view?"),
					cancelButton, confirmButton);
			dialog.open();
		}
	}
}
