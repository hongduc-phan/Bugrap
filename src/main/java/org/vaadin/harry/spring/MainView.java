package org.vaadin.harry.spring;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.vaadin.harry.spring.views.reportoverview.ReportOverview;

@Route
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class MainView extends VerticalLayout {

    public MainView(@Autowired MessageBean bean) {
        ReportOverview reportOverView =  new ReportOverview();
        add(reportOverView);
    }
}
