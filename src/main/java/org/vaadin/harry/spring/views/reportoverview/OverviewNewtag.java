package org.vaadin.harry.spring.views.reportoverview;

import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

/**
 * A Designer generated component for the overview-newtag template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("overview-newtag")
@JsModule("./src/views/report-overview/overview-newtag.js")
public class OverviewNewtag extends PolymerTemplate<OverviewNewtag.OverviewNewtagModel> {

    @Id("div")
    private Element div;

    /**
     * Creates a new OverviewNewtag.
     */
    public OverviewNewtag() {
        // You can initialise any data required for the connected UI org.vaadin.harry.spring.components here.
    }

    /**
     * This model binds properties between OverviewNewtag and overview-newtag
     */
    public interface OverviewNewtagModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
