package org.vaadin.harry.spring.views.reportoverview;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
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
import org.vaadin.harry.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
    private  ComboBox<String> projectsComboBox;
    @Id("vaadinButtonLogout")
    private  Button vaadinButtonLogout;
    @Id("vaadinButtonAccount")
    private  Button vaadinButtonAccount;
    @Id("search")
    private  TextField vaadinTextField;
    @Id("select-project")
    private  Select selectVersion;
//    @Id("overview-project")
//    private static ProgressBar vaadinProgressBar;

    @Id("btn-onlyme")
    private  Button btnOnlyMe;
    @Id("btn-everyone")
    private  Button btnEveryOne;
    @Id("btn-open")
    private  Button btnOpen;
    @Id("btn-allkinds")
    private  Button btnAllkinds;
    @Id("btn-custom")
    private  Button btnCustoms;
    @Id("wrapper-overview")
    private  Element wrapperOverview;
    @Id("wrapper-table")
    private  Element wrapperTable;

    private boolean isClicked_onlyMe = false;
    private boolean isClicked_Everyone = false;
    private boolean isClicked_report = false;
    @Id("table")
    private  Grid<Report> reportTable;
    //    @Id("infos-report")
//    private Element detailDiv;
//    @Id("infos-report2")
//    private Element detailDivDiff;
    @Id("wrapper-info")
    private HorizontalLayout wrapperInfo;
    private  VerticalLayout footerReport = new VerticalLayout();
    private  HorizontalLayout footerTitle = new HorizontalLayout();
    private  Select<String> selectPriority = new Select<>();
    private  Select<String> status = new Select<>();
    private  Select<String> versionSelected = new Select<>();
    private  Select<String> selectType = new Select<>();
    private  HorizontalLayout footerContent = new HorizontalLayout();
    private  Text author = new Text("");
    private  Paragraph reportDetails = new Paragraph();
    private  Button btnUpdate = new Button("Update");
    private  Button btnRevert = new Button("Revert");

    private  AtomicReference<Set<ProjectVersion>> projectVersions = new AtomicReference(new ProjectVersion());
    private  AtomicReference<String> projectSelected = new AtomicReference(new ArrayList<>());
    private  ArrayList<String> listVersions = new ArrayList<>();
    private  List<Report> reportListFilterByProjectAndVersion = new ArrayList<Report>();
    @Id("wrapper-distribution-bar")
    private HorizontalLayout wraperDistributionBar;

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

//        // UIs
//        vaadinProgressBar.setValue(0.15);
        this.showStatusDistributionBar(0,0,0);

        // filter report by clicking Only Me
        this.filterReportByBtnOnlyMe();
        // filter report by clicking EveryOne
        this.filterReportByBtnEveryOne();
        // filter report by clicking Only Me
        this.filterReportByBtnOpen();
        // filter report by clicking All Kinds
        this.filterReportByBtnAllkinds();
        // filter report by clicking Customs
        this.filterReportByBtnCustoms();

        // Set style for footer
        footerTitle.addClassName("wrapper-subject-fields");
        footerTitle.setWidth("100%");
        footerTitle.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);

        // Set labels for overview report in Footer
        selectPriority.setLabel("Priorrity");
        selectType.setLabel("Type");
        status.setLabel("Label");
        versionSelected.setLabel("Version");
        // Set fields titles in Footer for report details
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

        // Add elements to layout (Footer)
        footerReport.add(footerTitle);
        footerContent.add(author);
        footerContent.add(reportDetails);
        footerReport.add(footerContent);
        wrapperInfo.add(footerReport);
    }

    private void showStatusDistributionBar(int first, int second, int third) {
        if (reportListFilterByProjectAndVersion.size() == 0) {
            wraperDistributionBar.removeAll();
            ProgressBar progressBar = new ProgressBar(200, 1000, first, second, third);
            wraperDistributionBar.add(progressBar);
        }
        else {
            AtomicInteger closed = new AtomicInteger();
            AtomicInteger nonResolved = new AtomicInteger();
            AtomicInteger unassigned = new AtomicInteger();
            reportListFilterByProjectAndVersion.stream().forEach( r -> {
                if (r.getStatus() == null) {
                    unassigned.addAndGet(1);
                }
                else if (r.getStatus().toString().toLowerCase().equals("won't fix") ||
                        r.getStatus().toString().toLowerCase().equals("duplicate")) {
                    nonResolved.addAndGet(1);
                }
                else {
                    closed.addAndGet(1);
                }
            });
            int firstClosed = closed.intValue();
            int secondNonResolved = nonResolved.intValue();
            int thirdUnassigned = unassigned.intValue();
            wraperDistributionBar.removeAll();
            ProgressBar progressBar = new ProgressBar(200, 1000, firstClosed, secondNonResolved, thirdUnassigned);
            wraperDistributionBar.add(progressBar);
        }

    }

    private void filterReportByBtnCustoms() {
    }

    private void filterReportByBtnAllkinds() {
        btnAllkinds.addClickListener(this::showButtonClicked_allkindsBtn);
    }

    private void showButtonClicked_allkindsBtn(ClickEvent<Button> buttonClickEvent) {
        if (reportListFilterByProjectAndVersion.size() > 0) {
            reportTable.setItems(reportListFilterByProjectAndVersion);
            this.setVisibleOverviewReport();
        }
    }

    private void showButtonClickedMessage_everyone(ClickEvent<Button> buttonClickEvent) {
        isClicked_onlyMe = false;
        isClicked_Everyone = true;

        if (reportListFilterByProjectAndVersion.size() > 0) {
            reportTable.setItems(reportListFilterByProjectAndVersion);
            this.setVisibleOverviewReport();
        }

        if (isClicked_Everyone) {
            btnOnlyMe.setClassName("primary");
            btnEveryOne.setClassName("clicked-active");
        }
    }

    private void filterReportByBtnEveryOne() {
        btnEveryOne.addClickListener(this::showButtonClickedMessage_everyone);
    }

    private void showButtonClicked_openBtn(ClickEvent<Button> buttonClickEvent) {
        if (reportListFilterByProjectAndVersion.size() > 0) {
            List<Report> reportFilterByOpenStatus = reportListFilterByProjectAndVersion
                    .stream()
                    .filter(r -> r.getStatus() == null)
                    .collect(Collectors.toList());

            reportTable.setItems(reportFilterByOpenStatus);
            System.out.println(reportFilterByOpenStatus.size());
            this.setVisibleOverviewReport();

        }
    }

    private void filterReportByBtnOpen() {
        btnOpen.addClickListener(this::showButtonClicked_openBtn);
    }

    private void showButtonClickedMessage_onlyMe(ClickEvent<Button> buttonClickEvent) {
        isClicked_onlyMe = true;
        isClicked_Everyone = false;
        if (reportListFilterByProjectAndVersion.size() > 0) {
            List<Report> reportOnlyMe = reportListFilterByProjectAndVersion
                    .stream()
                    .filter(report -> report != null && report.getAssigned() != null && report.getAssigned().getName().toString().toLowerCase().equals("developer"))
                    .collect(Collectors.toList());
            reportTable.setItems(reportOnlyMe);
            this.setVisibleOverviewReport();
        }
        if (isClicked_onlyMe) {
            btnEveryOne.setClassName("primary");
            btnOnlyMe.setClassName("clicked-active");
        }
    }

    private void filterReportByBtnOnlyMe() {
        btnOnlyMe.addClickListener(this::showButtonClickedMessage_onlyMe);
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
                reportListFilterByProjectAndVersion = reportListFilterByProject
                        .stream()
                        .filter(rl -> rl.getVersion() != null
                                && !StringUtils.isEmpty(rl.getVersion().getVersion())
                                && rl.getVersion().getVersion().toLowerCase().equals(projectVersionSelected.toLowerCase()))
                        .collect(Collectors.toList());

                reportTable.setItems(reportListFilterByProjectAndVersion);
                this.setVisibleOverviewReport();
            }
            this.showStatusDistributionBar(1,1,1);
        });
    }

    public Set<org.vaadin.bugrap.domain.entities.Report> listReports(Project project, ProjectVersion projectVersion, BugrapRepository bugrapRepository) {
        BugrapRepository.ReportsQuery query = new BugrapRepository.ReportsQuery();
        query.project = project;
        query.projectVersion = projectVersion;
        Set<org.vaadin.bugrap.domain.entities.Report> reports = bugrapRepository.findReports(query);
        return reports;
    }

    private void setVisibleOverviewReport() {
        if (reportTable.getSelectedItems().isEmpty()) {
            wrapperOverview.setVisible(false);
        } else {
            wrapperOverview.setVisible(true);
        }
    }

    private void clickRow(AbstractField.ComponentValueChangeEvent<Grid<Report>, Set<Report>> gridSetComponentValueChangeEvent) {
        this.setVisibleOverviewReport();

        Set<Report> reportSet = gridSetComponentValueChangeEvent.getValue();

        // if selected 1 row
        if (reportSet.size() == 1) {
            Report report = reportSet.stream().findFirst().get();
            selectPriority.setItems(StringUtils.isEmpty(report.getPriority().toString())
                    ? " " : report.getPriority().toString());
            selectType.setItems(StringUtils.isEmpty(report.getType().toString())
                    ? " " : report.getPriority().toString());
            status.setItems((report.getStatus() == null)
                    ? " " : report.getType().toString());
            versionSelected.setItems((report.getVersion() == null)
                    ? " " : report.getVersion().toString());

            selectPriority.setValue(StringUtils.isEmpty(report.getPriority().toString())
                    ? " " : report.getPriority().toString());
            selectType.setValue(StringUtils.isEmpty(report.getType().toString())
                    ? " " : report.getPriority().toString());
            status.setValue((report.getStatus() == null)
                    ? " " : report.getType().toString());
            versionSelected.setValue((report.getVersion() == null)
                    ? " " : report.getVersion().toString());

            //render report detail in overview report in footer with author and description of the selected report
            author.setText( report.getAuthor() != null ? report.getAuthor().getName().toString() : "Unknown");
            reportDetails.setText(report.getDescription() != null ? report.getDescription().toString() : "No description");
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
