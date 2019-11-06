package org.vaadin.harry.spring.views.reportoverview;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

/**
 * A Designer generated component for the report-overview template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("report-overview")
@JsModule("./src/views/report-overview/report-overview.js")
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

    }

    /**
     * This model binds properties between ReportOverview and report-overview
     */
    public interface ReportOverviewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
