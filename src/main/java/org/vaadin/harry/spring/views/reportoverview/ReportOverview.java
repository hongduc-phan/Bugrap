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
import org.apache.commons.lang3.StringUtils;
import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;


import org.vaadin.bugrap.domain.entities.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    // connect to Bugrap domain service
    private static BugrapRepository bugrapRepository = new BugrapRepository("/tmp/bugrap;create=true");

    @Id("vaadinComboBox")
    private static ComboBox<String> projectsComboBox;
    @Id("vaadinButtonLogout")
    private static Button vaadinButtonLogout;
    @Id("vaadinButtonAccount")
    private static Button vaadinButtonAccount;
    @Id("search")
    private static TextField vaadinTextField;
    @Id("select-project")
    private static Select selectVersion;
    @Id("overview-project")
    private static ProgressBar vaadinProgressBar;
    @Id("btn-onlyme")
    private static Button btnOnlyMe;
    @Id("btn-everyone")
    private static Button btnEveryOne;
    @Id("btn-open")
    private static Button btnOpen;
    @Id("btn-allkinds")
    private static Button btnAllkinds;
    @Id("btn-custom")
    private static Button btnCustoms;
    @Id("wrapper-overview")
    private static Element wrapperOverview;
    @Id("wrapper-table")
    private static Element wrapperTable;

    private boolean isClicked_onlyMe = false;
    private boolean isClicked_Everyone = false;
    private boolean isClicked_report = false;
    @Id("table")
    private static Grid<Report> reportTable;
    //    @Id("infos-report")
//    private Element detailDiv;
//    @Id("infos-report2")
//    private Element detailDivDiff;
    @Id("wrapper-info")
    private HorizontalLayout wrapperInfo;
    private static VerticalLayout footerReport = new VerticalLayout();
    private static HorizontalLayout footerTitle = new HorizontalLayout();
    private static Select<String> selectPriority = new Select<>();
    private static  Select<String> status = new Select<>();
    private static Select<String> versionSelected = new Select<>();
    private static Select<String> selectType = new Select<>();
    /**
     * Creates a new ReportOverview.
     */
    public ReportOverview() {
        bugrapRepository.populateWithTestData();

        this.setVisibleOverviewReport();
        // find all projects
        Set<Project> allProjects = this.findAllProjects();

        ArrayList<String> listProjects = new ArrayList<>();
        allProjects.stream().forEach(pr -> {
            listProjects.add(pr.getName());
        });
        //set items to project combo box
        projectsComboBox.setItems(listProjects);
        projectsComboBox.setValue(listProjects.get(0));

        AtomicReference<Set<ProjectVersion>> projectVersions = new AtomicReference(new ProjectVersion());
        AtomicReference<String> projectSelected = new AtomicReference(new ArrayList<>());

        ArrayList<String> listVersions = new ArrayList<>();

        // Titles of report table with grid
        reportTable.addColumn(Report::getPriority).setHeader("PRIORITY");
        reportTable.addColumn(Report::getType).setFlexGrow(0).setWidth("100px").setHeader("TYPE");
        reportTable.addColumn(Report::getSummary).setHeader("SUMMARY");
        reportTable.addColumn(Report::getAssigned).setFlexGrow(0).setWidth("160px").setHeader("ASSIGNED TO");
        reportTable.addColumn(Report::getReportedTimestamp).setHeader("LAST MODIFIED").setWidth("140px");
        reportTable.addColumn(Report::getTimestamp).setHeader("REPORTED").setWidth("140px");

        this.setValueForProjectAndVersionWithoutClickEvents(projectVersions,
                projectSelected,
                allProjects,
                listVersions);

        // set grid report table can select muiti rows
        reportTable.getSelectedItems();
        reportTable.setSelectionMode(Grid.SelectionMode.MULTI);
        // listen event listener when triggering clicking
        reportTable.asMultiSelect().addValueChangeListener(this::clickRow);

        // event Click of combo box component to select project
        this.filterReportByProject(projectVersions,
                projectSelected,
                allProjects,
                listVersions);

        // event Click of Select component to select project version
        this.filterReportByVersion(projectSelected);



//                        List<org.vaadin.bugrap.domain.entities.Report> listReports =
//                                (List<org.vaadin.bugrap.domain.entities.Report>) vaadinSelect.addValueChangeListener(ver -> {
//                            List<org.vaadin.bugrap.domain.entities.Report> reports =
//                                    this.listReports(pro, (ProjectVersion) ver, bugrapRepository);
//
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


//
//
//                            List<org.vaadin.bugrap.domain.entities.Report> reportListFilter = reports.stream()
//                                    .filter(r -> projectSelected.equalsIgnoreCase(r.getProject().toString()))
//                                    .filter(reportFilter -> reportFilter.getProject() != null && reportFilter.getVersion().toString().toLowerCase().equals(projectVersionSelected.toLowerCase()))
//                                    .collect(Collectors.toList());

        // filter report by project version
//                            List<org.vaadin.bugrap.domain.entities.Report> reportFilterByProjectAndVersion = reportListFilterNotNull.stream()
//                            .filter(reportFilterbyVersion -> reportFilterbyVersion.getVersion().getVersion().toString().toLowerCase().equals(projectVersionSelected.toLowerCase()))
//                            .collect(Collectors.toList());

//                            BugrapRepository.ReportsQuery query = new BugrapRepository.ReportsQuery();
//                            query.project = pro;
        // query.projectVersion = first.get();
//                            Set<org.vaadin.bugrap.domain.entities.Report> reportsList = bugrapRepository.findReports(query);

        // add titles to grid table
//                            gridTable.addColumn(Report::getPriority).setHeader("PRIORITY");
//                            gridTable.addColumn(Report::getType).setFlexGrow(0).setWidth("100px").setHeader("TYPE");
//                                    gridTable.addColumn(Report::getSummary).setHeader("SUMMARY");
//                                    gridTable.addColumn(Report::getAssign).setFlexGrow(0).setWidth("100px").setHeader("ASSIGNED TO");
//                                    gridTable.addColumn(Report::getLastModified).setHeader("LAST MODIFIED").setWidth("140px");
//                                    gridTable.addColumn(Report::getTime).setHeader("REPORTED").setWidth("140px");

//        // in order to hide the report overview detail in footer
//        if (gridTable.getSelectedItems().isEmpty()) {
//            wrapperOverview.setVisible(false);
//        }

        // projectsComboBox.setValue("Google Chrome");

//        vaadinTextField.addValueChangeListener((e -> System.out.println(e.getValue())));
////        vaadinSelect.addValueChangeListener(
////                e -> {
////                    Set<ProjectVersion> projectVersions = bugrapRepository.findProjectVersions((Project) e.getValue());
////                });
        vaadinProgressBar.setValue(0.15);

        btnOnlyMe.addClickListener(this::showButtonClickedMessage_onlyMe);
        btnEveryOne.addClickListener(this::showButtonClickedMessage_everyone);






        footerTitle.addClassName("wrapper-subject-fields");
        footerTitle.setWidth("100%");
        footerTitle.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);


        selectPriority.setLabel("Priorrity");

        selectType.setLabel("Type");
        selectType.setItems("a", "b", "c");


        status.setLabel("Label");
        status.setItems("a", "b", "c");


        versionSelected.setLabel("Version");
        versionSelected.setItems("a", "b", "c");

        Button btnUpdate = new Button("Update");
        Button btnRevert = new Button("Revert");
        btnUpdate.addClassName("custom-margin-top");
        btnRevert.addClassName("custom-margin-top");
        footerTitle.add(selectPriority, selectType, status, versionSelected, btnUpdate, btnRevert);
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

    private Set<Project> findAllProjects() {
        return bugrapRepository.findProjects();
    }

    private void setValueForProjectAndVersionWithoutClickEvents(AtomicReference<Set<ProjectVersion>> projectVersions,
                                                                AtomicReference<String> projectSelected,
                                                                Set<Project> allProjects,
                                                                ArrayList<String> listVersions) {
            projectSelected.set(projectsComboBox.getValue());
            Optional<Project> project = allProjects.stream().filter(p -> {
                return p.getName().toLowerCase().equals(projectsComboBox.getValue().toLowerCase());
            }).findFirst();
            System.out.println(project);
            project.ifPresent(pro -> {
                projectVersions.set(bugrapRepository.findProjectVersions(pro));

                projectVersions.get().forEach(version -> {
                    listVersions.add(version.getVersion());
                });

                selectVersion.setItems(listVersions);
                selectVersion.setValue(listVersions.get(0));
                System.out.println(listVersions.get(0));
            });
            this.setVisibleOverviewReport();
    }

    private void filterReportByProject(AtomicReference<Set<ProjectVersion>> projectVersions,
                                       AtomicReference<String> projectSelected,
                                       Set<Project> allProjects,
                                       ArrayList<String> listVersions) {
        projectsComboBox.addValueChangeListener(
                e -> {
                    projectSelected.set(e.getValue());
                    Optional<Project> project = allProjects.stream().filter(p -> {
                        return p.getName().equals(e.getValue());
                    }).findFirst();
                    project.ifPresent(pro -> {
                        projectVersions.set(bugrapRepository.findProjectVersions(pro));

                        projectVersions.get().stream().forEach(version -> {
                            listVersions.add(version.getVersion());
                        });
                        //vaadinSelect.setItems();
                        selectVersion.setValue((listVersions.get(0)));
                        this.setVisibleOverviewReport();
                    });
                });
    }

    private void filterReportByVersion(AtomicReference<String> projectSelected) {
        selectVersion.addValueChangeListener(version -> {

            String projectVersionSelected = (String) version.getValue();
            // projectVersions.get().stream().filter(v -> v.getVersion().equals(version.getValue())).findFirst();
            Set<org.vaadin.bugrap.domain.entities.Report> reports
                    = this.listReports(null, null, bugrapRepository);

            System.out.println(reports);
            // filter report by project and project version
            // project
            List<org.vaadin.bugrap.domain.entities.Report> reportListFilterByProject = reports.stream()
                    .filter(rl -> rl.getProject() != null)
                    .filter(r -> projectSelected.get().equalsIgnoreCase(r.getProject().getName()))
                    .collect(Collectors.toList());

//            // version
            if (!reportListFilterByProject.isEmpty()) {
                List<org.vaadin.bugrap.domain.entities.Report> reportListFilterByProjectAndVersion = reportListFilterByProject
                        .stream()
                        .filter(rl -> rl.getVersion() != null
                                && !StringUtils.isEmpty(rl.getVersion().getVersion())
                                && rl.getVersion().getVersion().toLowerCase().equals(projectVersionSelected.toLowerCase()))
                        .collect(Collectors.toList());
                System.out.println(reportListFilterByProjectAndVersion.size());
                reportTable.setItems(reportListFilterByProjectAndVersion);
                this.setVisibleOverviewReport();
            }
        });
    }

    public Set<org.vaadin.bugrap.domain.entities.Report> listReports(Project project, ProjectVersion projectVersion, BugrapRepository bugrapRepository) {
        BugrapRepository.ReportsQuery query = new BugrapRepository.ReportsQuery();
        query.project = project;
        query.projectVersion = projectVersion;
        Set<org.vaadin.bugrap.domain.entities.Report> reports = bugrapRepository.findReports(query);
        return reports;
    }

    private  void setVisibleOverviewReport () {
        if (reportTable.getSelectedItems().isEmpty()) {
            wrapperOverview.setVisible(false);
        } else {
            wrapperOverview.setVisible(true);
        }
    }
    private void clickRow(AbstractField.ComponentValueChangeEvent<Grid<Report>, Set<Report>> gridSetComponentValueChangeEvent) {
       this.setVisibleOverviewReport();

        Set<Report> reportSet = gridSetComponentValueChangeEvent.getValue();

        if (reportSet.size() == 1) {
            reportSet.forEach( r -> {
                selectPriority.setValue("prei");
            });
        }


    }


    private void showButtonClickedMessage_onlyMe(ClickEvent<Button> buttonClickEvent) {
        isClicked_onlyMe = true;
        isClicked_Everyone = false;
        if (isClicked_onlyMe) {
            btnEveryOne.setClassName("primary");
            btnOnlyMe.setClassName("clicked-active");
        }
    }

    private void showButtonClickedMessage_everyone(ClickEvent<Button> buttonClickEvent) {
        isClicked_onlyMe = false;
        isClicked_Everyone = true;
        if (isClicked_Everyone) {
            btnOnlyMe.setClassName("primary");
            btnEveryOne.setClassName("clicked-active");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        //getModel().setPersons(data);
    }

    /**
     * This model binds properties between ReportOverview and report-overview
     */
    public interface ReportOverviewModel extends TemplateModel {
        // Add setters and getters for template properties here.
//        void setPersons(List<org.vaadin.bugrap.domain.entities.Report> data);
//
//        void setReportDetail(ReportDetail detail);
    }

}
