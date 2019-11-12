package org.vaadin.harry.spring.views.reportoverview;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.harry.spring.data.Report;
import org.vaadin.harry.spring.data.ReportDetail;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A Designer generated component for the report-overview template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("report-overview")
@JsModule("./src/views/report-overview/report-overview.js")
@CssImport(value = "./styles/views/report-overview/report-overview.css")
public class ReportOverview extends PolymerTemplate<ReportOverview.ReportOverviewModel> implements AfterNavigationObserver {

    public static List<Report> data = null;

    @Id("vaadinComboBox")
    private ComboBox<String> projectsComboBox;
    @Id("vaadinButtonLogout")
    private Button vaadinButtonLogout;
    @Id("vaadinButtonAccount")
    private Button vaadinButtonAccount;
    @Id("search")
    private TextField vaadinTextField;
    @Id("select-project")
    private Select vaadinSelect;
    @Id("overview-project")
    private ProgressBar vaadinProgressBar;
    @Id("btn-onlyme")
    private Button btnOnlyMe;
    @Id("btn-everyone")
    private Button btnEveryOne;
    @Id("btn-open")
    private Button btnOpen;
    @Id("btn-allkinds")
    private Button btnAllkinds;
    @Id("btn-custom")
    private Button btnCustoms;
    @Id("wrapper-overview")
    private Element wrapperOverview;
    @Id("wrapper-table")
    private Element wrapperTable;

    private boolean isClicked_onlyMe = false;
    private boolean isClicked_Everyone = false;
    private boolean isClicked_report = false;
    @Id("table")
    private Grid<Report> gridTable;
    //    @Id("infos-report")
//    private Element detailDiv;
//    @Id("infos-report2")
//    private Element detailDivDiff;
    @Id("wrapper-info")
    private HorizontalLayout wrapperInfo;

    /**
     * Creates a new ReportOverview.
     */
    public ReportOverview() {

        // connect to Bugrap domain service
        BugrapRepository bugrapRepository = new BugrapRepository("/tmp/bugrap;create=true");
        bugrapRepository.populateWithTestData();

        // find all projects
        Set<Project> allProjects = bugrapRepository.findProjects();
        ArrayList<String> listProjects = new ArrayList<>();
        allProjects.stream().forEach(pr -> {
            listProjects.add(pr.getName());
//            System.out.println(pr);
//            System.out.println(listProjects);
        });
        //set items to project combo box
        projectsComboBox.setItems(listProjects);
        projectsComboBox.setValue(listProjects.get(0));
        AtomicReference<Project> refProject = new AtomicReference<>(new Project());
        AtomicReference refReport = new AtomicReference(new org.vaadin.bugrap.domain.entities.Report());
        // due to project , we have project versions
        projectsComboBox.addValueChangeListener(
                e -> {
                    Optional<Project> project = allProjects.stream().filter(p -> p.getName().equals(e.getValue())).findFirst();
                    project.ifPresent(pro -> {
                        Set<ProjectVersion> projectVersions = bugrapRepository.findProjectVersions(pro);
                        refProject.set(pro);
                        ArrayList<String> listVersions = new ArrayList<>();
                        projectVersions.stream().forEach(version -> {
                            listVersions.add(version.getVersion());
                        });
                        vaadinSelect.setItems(listVersions);
                        vaadinSelect.setValue((listVersions.get(0)));

                        vaadinSelect.addValueChangeListener(version -> {
                            Optional<ProjectVersion> first = projectVersions.stream().filter(v -> v.getVersion().equals(version.getValue())).findFirst();
                            Set<org.vaadin.bugrap.domain.entities.Report> reports
                                    = this.listReports(pro, first.get(), bugrapRepository);
                            refReport.set(first);
                            System.out.println(reports);
//                            BugrapRepository.ReportsQuery query = new BugrapRepository.ReportsQuery();
//                            query.project = pro;
                           // query.projectVersion = first.get();
//                            Set<org.vaadin.bugrap.domain.entities.Report> reportsList = bugrapRepository.findReports(query);
                           // System.out.println(reports.stream().findFirst().get().getType());

                            // add titles to grid table
//                            gridTable.addColumn(Report::getPriority).setHeader("PRIORITY");
//                            gridTable.addColumn(Report::getType).setFlexGrow(0).setWidth("100px").setHeader("TYPE");
//                                    gridTable.addColumn(Report::getSummary).setHeader("SUMMARY");
//                                    gridTable.addColumn(Report::getAssign).setFlexGrow(0).setWidth("100px").setHeader("ASSIGNED TO");
//                                    gridTable.addColumn(Report::getLastModified).setHeader("LAST MODIFIED").setWidth("140px");
//                                    gridTable.addColumn(Report::getTime).setHeader("REPORTED").setWidth("140px");
                        });
//                        List<org.vaadin.bugrap.domain.entities.Report> listReports =
//                                (List<org.vaadin.bugrap.domain.entities.Report>) vaadinSelect.addValueChangeListener(ver -> {
//                            List<org.vaadin.bugrap.domain.entities.Report> reports =
//                                    this.listReports(pro, (ProjectVersion) ver, bugrapRepository);
//
//                            System.out.println(reports);
//                                    // in order to hide the report overview detail in footer
//                                    if (gridTable.getSelectedItems().isEmpty()) {
//                                        wrapperOverview.setVisible(false);
//                                    }
//
//                                    gridTable.addColumn(Report::getPriority).setHeader("PRIORITY");
//                                    gridTable.addColumn(Report::getType).setFlexGrow(0).setWidth("100px").setHeader("TYPE");
//                                    gridTable.addColumn(Report::getSummary).setHeader("SUMMARY");
//                                    gridTable.addColumn(Report::getAssign).setFlexGrow(0).setWidth("100px").setHeader("ASSIGNED TO");
//                                    gridTable.addColumn(Report::getLastModified).setHeader("LAST MODIFIED").setWidth("140px");
//                                    gridTable.addColumn(Report::getTime).setHeader("REPORTED").setWidth("140px");
//
//                                    gridTable.setItems(Reports.getReports());
//                        });
//                        System.out.println(listReports);
                    });


                });
//        // in order to hide the report overview detail in footer
//        if (gridTable.getSelectedItems().isEmpty()) {
//            wrapperOverview.setVisible(false);
//        }

        // projectsComboBox.setValue("Google Chrome");

        vaadinTextField.addValueChangeListener((e -> System.out.println(e.getValue())));
//        vaadinSelect.addValueChangeListener(
//                e -> {
//                    Set<ProjectVersion> projectVersions = bugrapRepository.findProjectVersions((Project) e.getValue());
//                    System.out.println(projectVersions);
//                });
        vaadinProgressBar.setValue(0.15);

        btnOnlyMe.addClickListener(this::showButtonClickedMessage_onlyMe);
        btnEveryOne.addClickListener(this::showButtonClickedMessage_everyone);


        //System.out.println( reportList);
//        Table.getSelectedItems();
//        System.out.println(Table.getSelectedItems());
        gridTable.setSelectionMode(Grid.SelectionMode.MULTI);
        gridTable.asMultiSelect().addValueChangeListener(this::clickRow);

        VerticalLayout footerReport = new VerticalLayout();
        HorizontalLayout footerTitle = new HorizontalLayout();

        footerTitle.addClassName("wrapper-subject-fields");
        footerTitle.setWidth("100%");
        footerTitle.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);

        Select<String> selectPriority = new Select<>();
        selectPriority.setLabel("Priorrity");
        selectPriority.setItems("a", "b", "c");

        Select<String> selectType = new Select<>();
        selectType.setLabel("Type");
        selectType.setItems("a", "b", "c");

        Select<String> status = new Select<>();
        status.setLabel("Label");
        status.setItems("a", "b", "c");

        Select<String> selectVersion = new Select<>();
        selectVersion.setLabel("Version");
        selectVersion.setItems("a", "b", "c");

        Button btnUpdate = new Button("Update");
        Button btnRevert = new Button("Revert");
        btnUpdate.addClassName("custom-margin-top");
        btnRevert.addClassName("custom-margin-top");
        footerTitle.add(selectPriority, selectType, status, selectVersion, btnUpdate, btnRevert);
        //footerTitle.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        footerTitle.setFlexGrow(1, selectPriority);
        footerTitle.setFlexGrow(1, selectType);
        footerTitle.setFlexGrow(1, status);
        footerTitle.setFlexGrow(1, selectVersion);
        footerTitle.setFlexGrow(1, btnUpdate);
        footerTitle.setFlexGrow(1, btnRevert);

        footerReport.add(footerTitle);
        HorizontalLayout footerContent = new HorizontalLayout();
        Paragraph reportDetails = new Paragraph();
        reportDetails.setText("demo......");
        footerContent.add(reportDetails);
        footerReport.add(footerContent);
        wrapperInfo.add(footerReport);


    }

    public Set<org.vaadin.bugrap.domain.entities.Report> listReports(Project project, ProjectVersion projectVersion, BugrapRepository bugrapRepository) {
        BugrapRepository.ReportsQuery query = new BugrapRepository.ReportsQuery();
        query.project = project;
        query.projectVersion = projectVersion;
        System.out.println(query);
        System.out.println(project);
        System.out.println(projectVersion);
        Set<org.vaadin.bugrap.domain.entities.Report> reports = bugrapRepository.findReports(query);
        return reports;
    }

    private void clickRow(AbstractField.ComponentValueChangeEvent<Grid<Report>, Set<Report>> gridSetComponentValueChangeEvent) {
        System.out.println(gridTable.getSelectedItems());
        if (gridTable.getSelectedItems().isEmpty()) {
            wrapperOverview.setVisible(false);
            return;
        } else {
            wrapperOverview.setVisible(true);
        }
        System.out.println(gridSetComponentValueChangeEvent.getValue());

        Set<Report> reportSet = gridSetComponentValueChangeEvent.getValue();

        data = Arrays.asList(reportSet.toArray(new Report[0]));
        getModel().setPersons(data);

        if (data.size() == 1) {
//            detailDiv.setVisible(true);
//            detailDivDiff.setVisible(false);
//            Optional<ReportDetail> detail = ReportDetails.getReportDetail(data.get(0).getId());
////            detail.ifPresent(reportDetail -> detailDiv.setText(reportDetail.getDetail()));
//            detail.ifPresent(rd -> getModel().setReportDetail(rd));
        } else {
            // ask to handle whether in frontend or java server side
//            detailDiv.setVisible(false);
//            detailDivDiff.setVisible(true);
        }

        System.out.println(data);
//        while (data.hasNext()) {
//            final Report next = data.next();
//            System.out.println(next.getSummary());
//        }

        reportSet.forEach(report -> {
            //System.out.println(report.getSummary());

        });

//        for (Report report : reportSet) {
//            System.out.println(report.getSummary());
//        }


    }


    private void showButtonClickedMessage_onlyMe(ClickEvent<Button> buttonClickEvent) {
        isClicked_onlyMe = true;
        isClicked_Everyone = false;
        if (isClicked_onlyMe) {
            btnEveryOne.setClassName("primary");
            btnOnlyMe.setClassName("clicked-active");
        }
        System.out.println(buttonClickEvent.getButton());
    }

    private void showButtonClickedMessage_everyone(ClickEvent<Button> buttonClickEvent) {
        isClicked_onlyMe = false;
        isClicked_Everyone = true;
        if (isClicked_Everyone) {
            btnOnlyMe.setClassName("primary");
            btnEveryOne.setClassName("clicked-active");
        }
        System.out.println(buttonClickEvent.getButton());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        getModel().setPersons(data);
    }

    /**
     * This model binds properties between ReportOverview and report-overview
     */
    public interface ReportOverviewModel extends TemplateModel {
        // Add setters and getters for template properties here.
        void setPersons(List<Report> data);

        void setReportDetail(ReportDetail detail);
    }

}
