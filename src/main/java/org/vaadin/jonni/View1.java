package org.vaadin.jonni;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.jonni.MainLayout.Dto;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "", layout = MainLayout.class)
@PageTitle("primary view")
public class View1 extends VerticalLayout {
	public static List<Dto> list;
	static {
		list = new ArrayList<Dto>();
		for (int i = 0; i < 10; i++) {
			list.add(MainLayout.getNewMockDto(i));
		}
	}

	public View1() {
		setSizeFull();
		add(new H1("view1"));
		Grid<Dto> grid = new Grid(Dto.class);
		grid.setItems(list);
		grid.addComponentColumn(item -> {
			return new RouterLink("Inspect", View2.class, item.getId());
		});
		grid.addColumn(TemplateRenderer.<Dto>of(

				"<span on-click=\"handleClick\" title=\"[[item.data]]\">[[item.data]]</span>")

				.withProperty("data", item -> item.getProperty1())

				.withEventHandler("handleClick", clickedDto -> {
					Notification.show("DTO '" + clickedDto.getId() + "' was clicked!");
				})

		).setHeader("Template renderer column");
		add(new Button("Cause an exception", click -> {
			throw new RuntimeException("Mock error");
		}));
		add(grid);
		expand(grid);
	}

}
