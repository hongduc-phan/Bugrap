package org.vaadin.harry.spring.views.reportoverview;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.vaadin.harry.spring.BugrapRepo;
import org.vaadin.harry.spring.ReportDetail;
import org.vaadin.harry.spring.components.ProgressBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;


/**
 * A Designer generated component for the report-overview template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("report-overview")
@JsModule("./src/views/report-overview/report-overview.js")
@CssImport(value = "./styles/views/report-overview/report-overview.css", themeFor = "vaadin-button")
public class ReportOverview extends PolymerTemplate<ReportOverview.ReportOverviewModel> implements AfterNavigationObserver {

    @Id("vaadinComboBox")
    private ComboBox<Project> projectsComboBox;
    @Id("vaadinButtonLogout")
    private Button vaadinButtonLogout;
    @Id("vaadinButtonAccount")
    private Button vaadinButtonAccount;
    TextField searchBar = new TextField();
    @Id("select-project")
    private Select selectVersion;

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
    private Grid<Report> reportTable;
    @Id("wrapper-info")
    private HorizontalLayout wrapperInfo;
    private VerticalLayout footerReport = new VerticalLayout();
    private HorizontalLayout footerTitle = new HorizontalLayout();
    private HorizontalLayout footerContent = new HorizontalLayout();
    private Set<ProjectVersion> projectVersions = new HashSet<ProjectVersion>();
    private ArrayList<ProjectVersion> listVersions = new ArrayList<>();
    private List<Report> reportListFilterByProjectAndVersion = new ArrayList<Report>();
    @Id("wrapper-distribution-bar")
    private HorizontalLayout wraperDistributionBar;
    @Id("wrapper-search-bar")
    private HorizontalLayout wrapperSearchBar;

    ReportDetail reportDetail = new ReportDetail();

    /**
     * Creates a new ReportOverview.
     */
    public ReportOverview() {
        Icon icon = VaadinIcon.SEARCH.create();
        searchBar.setPrefixComponent(icon);
        searchBar.setClearButtonVisible(true);
        searchBar.setPlaceholder("Searching...");
        searchBar.getStyle().set("width", "80%");
        searchBar.getStyle().set("margin-right", "16px");
        wrapperSearchBar.add(searchBar);
        this.setVisibleOverviewReport();
        // find all projects
        Set<Project> allProjects = BugrapRepo.getRepo().findProjects();

        ArrayList<String> listProjects = new ArrayList<>();
        allProjects.forEach(pr -> {
            listProjects.add(pr.getName());
        });

        //set items to project combo box
        projectsComboBox.setItems(allProjects);
        projectsComboBox.setValue(allProjects.stream().findFirst().get());

        // Titles of report table with grid
        reportTable.addColumn(Report::getPriority).setHeader("PRIORITY");
        reportTable.addColumn(Report::getType).setFlexGrow(0).setWidth("100px").setHeader("TYPE");
        reportTable.addColumn(Report::getSummary).setHeader("SUMMARY");
        reportTable.addColumn(Report::getAssigned).setFlexGrow(0).setWidth("160px").setHeader("ASSIGNED TO");
        reportTable.addColumn(Report::getReportedTimestamp).setHeader("LAST MODIFIED").setWidth("140px");
        reportTable.addColumn(Report::getTimestamp).setHeader("REPORTED").setWidth("140px");

        reportDetail.getSelectPriority().setItems(Report.Priority.values());
        reportDetail.getStatus().setItems(Report.Status.values());
        reportDetail.getSelectType().setItems(Report.Type.values());

        // search bar by Priority or type, assigned to
        searchBar.addValueChangeListener(e -> {
            System.out.println(e.getValue());
            if (reportListFilterByProjectAndVersion.size() > 0) {
                List<Report> filterReportList = reportListFilterByProjectAndVersion.stream().filter(r -> (r.getVersion() != null && r.getVersion().getVersion().equalsIgnoreCase(e.getValue()))
                        || (r.getPriority() != null && r.getPriority().toString().equalsIgnoreCase(e.getValue()))
                        || (r.getType() != null && r.getType().toString().equalsIgnoreCase(e.getValue()))
                        || (r.getSummary() != null && r.getSummary().contains(e.getValue()))
                        || (r.getAssigned() != null && r.getAssigned().getName().contains(e.getValue()))).collect(Collectors.toList());
                reportTable.setItems(filterReportList);
            }

        });

        this.setValueForProjectAndVersionWithoutClickEvents(this.projectVersions,
                allProjects);

        // set grid report table can select muiti rows
        reportTable.getSelectedItems();
        reportTable.setSelectionMode(Grid.SelectionMode.MULTI);
        // listen event listener when triggering clicking
        reportTable.asMultiSelect().addValueChangeListener(this::clickRow);

        // event Click of combo box component to select project
        this.filterReportByProject(allProjects,
                listVersions);

        // event Click of Select component to select project version
        this.filterReportByVersionWhenClickSelectReportList();

//        // UIs
//        vaadinProgressBar.setValue(0.15);
        this.showStatusDistributionBar(0, 0, 0);

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
        reportDetail.getSelectPriority().setLabel("Priorrity");
        reportDetail.getSelectType().setLabel("Type");
        reportDetail.getStatus().setLabel("Label");
        reportDetail.getVersionSelected().setLabel("Version");
        // Set fields titles in Footer for report details
        reportDetail.getBtnUpdate().addClassName("custom-margin-top");
        reportDetail.getBtnRevert().addClassName("custom-margin-top");
        footerTitle.add(reportDetail.getSelectPriority(),
                reportDetail.getSelectType(),
                reportDetail.getStatus(),
                reportDetail.getVersionSelected(),
                reportDetail.getBtnUpdate(),
                reportDetail.getBtnRevert());
        //footerTitle.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        footerTitle.setFlexGrow(1, reportDetail.getSelectPriority());
        footerTitle.setFlexGrow(1, reportDetail.getSelectType());
        footerTitle.setFlexGrow(1, reportDetail.getStatus());
        footerTitle.setFlexGrow(1, reportDetail.getVersionSelected());
        footerTitle.setFlexGrow(1, reportDetail.getBtnUpdate());
        footerTitle.setFlexGrow(1, reportDetail.getBtnRevert());

        // Add elements to layout (Footer)
        footerReport.add(footerTitle);
        footerContent.add(reportDetail.getAuthor());
        footerContent.add(reportDetail.getReportDetails());
        footerReport.add(footerContent);
        wrapperInfo.add(footerReport);
    }

    private void showStatusDistributionBar(int first, int second, int third) {
        if (reportListFilterByProjectAndVersion.size() == 0) {
            wraperDistributionBar.removeAll();
            ProgressBar progressBar = new ProgressBar(200, 1000, first, second, third);
            wraperDistributionBar.add(progressBar);
        } else {
            AtomicInteger closed = new AtomicInteger();
            AtomicInteger nonResolved = new AtomicInteger();
            AtomicInteger unassigned = new AtomicInteger();
            reportListFilterByProjectAndVersion.stream().forEach(r -> {
                if (r.getStatus() == null) {
                    unassigned.addAndGet(1);
                } else if (r.getStatus().toString().toLowerCase().equals("won't fix") ||
                        r.getStatus().toString().toLowerCase().equals("duplicate")) {
                    nonResolved.addAndGet(1);
                } else {
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
            btnAllkinds.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnOpen.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnEveryOne.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnOnlyMe.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            this.setVisibleOverviewReport();
        }
    }

    private void showButtonClickedMessage_everyone(ClickEvent<Button> buttonClickEvent) {
        isClicked_onlyMe = false;
        isClicked_Everyone = true;

        if (reportListFilterByProjectAndVersion.size() > 0) {
            reportTable.setItems(reportListFilterByProjectAndVersion);
            this.setVisibleOverviewReport();
            if (isClicked_Everyone) {
                btnOpen.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
                btnAllkinds.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
                btnEveryOne.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                btnOnlyMe.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            }
        }
    }

    private void filterReportByBtnEveryOne() {
        btnEveryOne.addClickListener(this::showButtonClickedMessage_everyone);
    }

    private void showButtonClicked_openBtn(ClickEvent<Button> buttonClickEvent) {
        if (reportListFilterByProjectAndVersion.size() > 0) {
            btnOpen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnAllkinds.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnEveryOne.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnOnlyMe.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            List<Report> reportFilterByOpenStatus = reportListFilterByProjectAndVersion
                    .stream()
                    .filter(r -> r.getStatus() == null)
                    .collect(Collectors.toList());

            reportTable.setItems(reportFilterByOpenStatus);
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
                    .filter(report -> report != null && report.getAssigned() != null && report.getAssigned().getName().toLowerCase().equals("developer"))
                    .collect(Collectors.toList());
            reportTable.setItems(reportOnlyMe);
            this.setVisibleOverviewReport();
            if (isClicked_onlyMe) {
                btnOpen.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
                btnAllkinds.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
                btnOnlyMe.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                btnEveryOne.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            }
        }
    }

    private void filterReportByBtnOnlyMe() {
        btnOnlyMe.addClickListener(this::showButtonClickedMessage_onlyMe);
    }

    private void setValueToVersionProject(Set<Project> allProjects) {
        Project project = projectsComboBox.getValue();

        if (project != null) {
            this.projectVersions.clear();
            this.projectVersions = BugrapRepo.getRepo().findProjectVersions(project);
            listVersions.clear();

            listVersions.addAll(this.projectVersions);
            selectVersion.setItems(listVersions);
            selectVersion.setValue(listVersions.get(0));
        }
    }

    private void setValueForProjectAndVersionWithoutClickEvents(Set<ProjectVersion> projectVersions,
                                                                Set<Project> allProjects) {

        this.setValueToVersionProject(allProjects);
        String selectVersionValue = String.valueOf(selectVersion.getValue());
        this.filterReportByProjectAndVersion(projectsComboBox.getValue().toString(), selectVersionValue);
        this.setVisibleOverviewReport();
    }

    private void filterReportByProject(Set<Project> allProjects,
                                       ArrayList<ProjectVersion> listVersions) {
        projectsComboBox.addValueChangeListener(
                e -> {
                    this.setValueToVersionProject(allProjects);
                    this.filterReportByProjectAndVersion(e.getValue().toString(), listVersions.get(0).toString());
                    this.showStatusDistributionBar(0, 0, 0);
                });
    }

    private List<org.vaadin.bugrap.domain.entities.Report> getReportListFilterByProject(String projectSelected,
                                                                                        String version) {
        Set<org.vaadin.bugrap.domain.entities.Report> reports
                = this.listReports(null, null, BugrapRepo.getRepo());

        return reports.stream()
                .filter(rl -> rl.getProject() != null)
                .filter(r -> projectSelected.equalsIgnoreCase(r.getProject().getName()))
                .collect(Collectors.toList());
    }

    private void setValueForReportTable() {
        reportTable.getDataProvider().refreshAll();
        reportTable.setItems(reportListFilterByProjectAndVersion);
        this.setVisibleOverviewReport();
    }

    public void filterReportByProjectAndVersion(String projectSelected, String version) {
        List<org.vaadin.bugrap.domain.entities.Report> reportListFilterByProject =
                this.getReportListFilterByProject(projectSelected, version);
        // version
        reportListFilterByProjectAndVersion = reportListFilterByProject
                .stream()
                .filter(rl ->
                        rl.getVersion() != null)
                .filter(listReport -> listReport.getVersion().getVersion().equalsIgnoreCase(version))
                .collect(Collectors.toList());
        this.setValueForReportTable();
    }

    private void clickUpdateBtnForOneRow(Binder<Report> binderReport, String currentVersion) {
        reportDetail.getBtnUpdate().addClickListener(e -> {
            try {
                binderReport.writeBean(reportDetail.getReportUpdated());
                reportDetail.setReportUpdated(BugrapRepo.getRepo().save(reportDetail.getReportUpdated()));
                this.filterReportByProjectAndVersion(reportDetail.getReportUpdated().getProject().toString(), currentVersion);
                reportDetail.getDialogUpdateSucceed().open();

            } catch (ValidationException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void filterReportByVersionWhenClickSelectReportList() {
        selectVersion.addValueChangeListener(version -> {
            String projectSelected = projectsComboBox.getValue().toString();
            String projectVersionSelected = String.valueOf(version.getValue());

            List<org.vaadin.bugrap.domain.entities.Report> reportListFilterByProject =
                    this.getReportListFilterByProject(projectSelected, version.toString());
//            // version
            reportListFilterByProjectAndVersion = reportListFilterByProject
                    .stream()
                    .filter(rl -> rl.getVersion() != null
                            && !StringUtils.isEmpty(rl.getVersion().getVersion())
                            && rl.getVersion().getVersion().toLowerCase().equals(projectVersionSelected.toLowerCase()))
                    .collect(Collectors.toList());
            this.setValueForReportTable();
            this.showStatusDistributionBar(0, 0, 0);
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

        Set<Report> getReport = gridSetComponentValueChangeEvent.getValue();

        // if selected 1 row
        if (gridSetComponentValueChangeEvent.getValue().size() == 1) {
            reportDetail.implementSelectOneRow(getReport);
            // trigger Update Button
            this.clickUpdateBtnForOneRow(reportDetail.getBinderReport(), ((ProjectVersion)selectVersion.getValue()).getVersion() );
        } else if (gridSetComponentValueChangeEvent.getValue().size() > 1) {
            reportDetail.implementSelectMultiRows(getReport);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    }

    /**
     * This model binds properties between ReportOverview and report-overview
     */
    public interface ReportOverviewModel extends TemplateModel {
    }

}
