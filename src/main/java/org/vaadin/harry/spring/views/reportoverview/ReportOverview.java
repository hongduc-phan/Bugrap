package org.vaadin.harry.spring.views.reportoverview;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import org.vaadin.harry.spring.data.Report;
import org.vaadin.harry.spring.data.ReportDetail;
import org.vaadin.harry.spring.data.ReportDetails;
import org.vaadin.harry.spring.data.Reports;

import java.util.*;

/**
 * A Designer generated component for the report-overview template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("report-overview")
@JsModule("./src/views/report-overview/report-overview.js")
@CssImport(value = "styles/views/report-overview/report-overview.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")
public class ReportOverview extends PolymerTemplate<ReportOverview.ReportOverviewModel>  implements AfterNavigationObserver {

    public static  List<Report> data = null;

    @Id("vaadinComboBox")
    private ComboBox<String> vaadinComboBox;
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

    private  boolean isClicked_onlyMe = false;
    private  boolean isClicked_Everyone = false;
    private boolean isClicked_report = false;
    @Id("table")
    private Grid<Report> gridTable;
    @Id("infos-report")
    private Element detailDiv;

    /**
     * Creates a new ReportOverview.
     */
    public ReportOverview() {
        if (gridTable.getSelectedItems().isEmpty()){
            wrapperOverview.setVisible(false);
        }

        // You can initialise any data required for the connected UI components here.
        vaadinComboBox.setItems("Google Chrome", "Mozilla Firefox", "Opera",
                "Apple Safari", "Microsoft Edge");
        vaadinComboBox.setValue("Google Chrome");

        vaadinTextField.addValueChangeListener((e -> System.out.println(e.getValue())));
        vaadinSelect.setItems("Jose", "Manolo", "Pedro");
        vaadinSelect.addValueChangeListener(
                e -> System.out.println(e.getValue()));
        vaadinProgressBar.setValue(0.15);

        btnOnlyMe.addClickListener(this::showButtonClickedMessage_onlyMe);
        btnEveryOne.addClickListener(this::showButtonClickedMessage_everyone);

        gridTable.addColumn(Report::getPriority).setHeader("PRIORITY");
        gridTable.addColumn(Report::getType).setFlexGrow(0).setWidth("100px").setHeader("TYPE");
        gridTable.addColumn(Report::getSummary).setHeader("SUMMARY");
        gridTable.addColumn(Report::getAssign).setFlexGrow(0).setWidth("100px").setHeader("ASSIGNED TO");
        gridTable.addColumn(Report::getLastModified).setHeader("LAST MODIFIED").setWidth("140px");
        gridTable.addColumn(Report::getTime).setHeader("REPORTED").setWidth("140px");

        gridTable.setItems(Reports.getReports());
        //System.out.println( reportList);
//        Table.getSelectedItems();
//        System.out.println(Table.getSelectedItems());
        gridTable.setSelectionMode(Grid.SelectionMode.MULTI);
        gridTable.asMultiSelect().addValueChangeListener(this::clickRow);
    }

    private void clickRow(AbstractField.ComponentValueChangeEvent<Grid<Report>, Set<Report>> gridSetComponentValueChangeEvent) {
            System.out.println(gridTable.getSelectedItems());
            if (gridTable.getSelectedItems().isEmpty()){
                wrapperOverview.setVisible(false);
                return;
            }
            else {
                wrapperOverview.setVisible(true);
            }
        System.out.println(gridSetComponentValueChangeEvent.getValue());

        Set<Report> reportSet =  gridSetComponentValueChangeEvent.getValue();

        data = Arrays.asList(reportSet.toArray(new Report[0]));
        getModel().setPersons(data);

        if (data.size() == 1) {
            Optional<ReportDetail> detail = ReportDetails.getReportDetail(data.get(0).getId());
//            detail.ifPresent(reportDetail -> detailDiv.setText(reportDetail.getDetail()));
            detail.ifPresent(rd -> getModel().setReportDetail(rd));
        }
        else {
            // ask to handle whether in frontend or java server side
            wrapperOverview.setVisible(false);
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
        public void setPersons(List<Report> data);
        public void setReportDetail(ReportDetail detail);
    }

}
