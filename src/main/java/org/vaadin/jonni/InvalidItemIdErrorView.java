package org.vaadin.jonni;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;

public class InvalidItemIdErrorView extends VerticalLayout implements HasErrorParameter<InvalidItemId> {

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<InvalidItemId> parameter) {
		add("Invalid item id");
		return 502;
	}
}
