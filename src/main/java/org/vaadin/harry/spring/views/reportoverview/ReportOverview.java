package org.vaadin.harry.spring.views.reportoverview;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import java.util.ArrayList;
import java.util.List;

/**
 * A Designer generated component for the report-overview template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("report-overview")
@JsModule("./src/views/report-overview/report-overview.js")
@CssImport(value = "styles/views/report-overview/report-overview.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")
public class ReportOverview extends PolymerTemplate<ReportOverview.ReportOverviewModel> {

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
    private Grid<Report> Table;

    /**
     * Creates a new ReportOverview.
     */
    public ReportOverview() {
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

        Table.addColumn(Report::getPriority).setHeader("PRIORITY");
        Table.addColumn(Report::getType).setFlexGrow(0).setWidth("100px").setHeader("TYPE");
        Table.addColumn(Report::getSummary).setHeader("SUMMARY");
        Table.addColumn(Report::getAssign).setFlexGrow(0).setWidth("100px").setHeader("ASSIGNED TO");
        Table.addColumn(Report::getLastModified).setHeader("LAST MODIFIED").setWidth("140px");
        Table.addColumn(Report::getTime).setHeader("REPORTED").setWidth("140px");
        List<Report> reportList = new ArrayList<>();
        reportList.add(new Report(3, "Bug", "1111111", "aaaaa", "2012", "15m ago"));
        reportList.add(new Report(3, "Feature", "22222", "bbbbb", "2013", "15m ago"));
        reportList.add(new Report(3, "Feature", "33333", "ccccc", "2014", "15m ago"));
        reportList.add(new Report(3, "Bug", "44444", "dddddd", "1011", "15m ago"));
        Table.setItems(reportList);
        //System.out.println( reportList);
//        Table.getSelectedItems();
//        System.out.println(Table.getSelectedItems());
        Table.setSelectionMode(Grid.SelectionMode.MULTI);
        Table.asMultiSelect().addValueChangeListener(event -> {
            String message = String.format("Selection changed from %s to %s",
                    event.getOldValue(), event.getValue());
           //System.out.println( message);
            System.out.println(Table.getSelectedItems());
        });
    }

    private void clickItem(AbstractField.ComponentValueChangeEvent<Grid<Report>, Report> gridReportComponentValueChangeEvent) {
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

    /**
     * This model binds properties between ReportOverview and report-overview
     */
    public interface ReportOverviewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
