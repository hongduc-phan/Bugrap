package org.vaadin.harry.spring;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.harry.spring.views.reportoverview.ReportOverview;

import java.util.Set;

@Route
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class MainView extends VerticalLayout {

    public MainView(@Autowired MessageBean bean) {
//        Button button = new Button("Click me",
//                e -> Notification.show(bean.getMessage()));
//        add(button);
//        BugrapRepository bugrapRepository = new BugrapRepository("/tmp/bugrap1;create=true");
//        bugrapRepository.populateWithTestData();
//        Set<Project> projects = bugrapRepository.findProjects();
        ReportOverview reportOverView =  new ReportOverview();
        add(reportOverView);
    }
}
