package org.vaadin.jonni;

import org.vaadin.jonni.MainLayout.Dto;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "view2", layout = MainLayout.class)
@PageTitle("secondary view")
public class View2 extends VerticalLayout implements HasUrlParameter<Integer> {
	public View2() {

		add(new H1("view2"));
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Integer parameter) {
		
		add(new Paragraph("parameter was: "+parameter));
		for (Dto dto : View1.list) {
			if (dto.getId() == parameter) {
				add(new Paragraph("DTO: " + dto));
			}
		}
	}
}
