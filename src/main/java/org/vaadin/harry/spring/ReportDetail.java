package org.vaadin.harry.spring;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.harry.spring.views.reportoverview.ReportOverview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReportDetail {

    private Report reportUpdated = new Report();
    private Select<Report.Priority> selectPriority = new Select<>();
    private Select<Report.Status> status = new Select<>();
    private Select<ProjectVersion> versionSelected = new Select<>();
    private Select<Report.Type> selectType = new Select<>();
    private Set<ProjectVersion> projectVersions = new HashSet<ProjectVersion>();
    private Text author = new Text("");
    private Paragraph reportDetails = new Paragraph();
    private Button btnUpdate = new Button("Update");
    private Button btnRevert = new Button("Revert");
    Binder<Report> binderReport = new Binder<Report>();

    public void setReportUpdated(Report reportUpdated) {
        this.reportUpdated = reportUpdated;
    }

    public Binder<Report> getBinderReport() {
        return binderReport;
    }

    public ConfirmDialog getDialogUpdateSucceed() {
        return dialogUpdateSucceed;
    }

    public List<Report> getReportsUpdated() {
        return reportsUpdated;
    }

    ConfirmDialog dialogUpdateSucceed = new ConfirmDialog("Update Report",
            "The report is updated!", "OK", this::onOKUpdate);

    public Report getReportUpdated() {
        return reportUpdated;
    }

    public Select<Report.Priority> getSelectPriority() {
        return selectPriority;
    }

    public Select<Report.Status> getStatus() {
        return status;
    }

    public Select<ProjectVersion> getVersionSelected() {
        return versionSelected;
    }

    public Select<Report.Type> getSelectType() {
        return selectType;
    }

    public Set<ProjectVersion> getProjectVersions() {
        return projectVersions;
    }

    public Text getAuthor() {
        return author;
    }

    public Paragraph getReportDetails() {
        return reportDetails;
    }

    public Button getBtnUpdate() {
        return btnUpdate;
    }

    public Button getBtnRevert() {
        return btnRevert;
    }


    public void implementSelectOneRow(Set<Report> getReport) {
        reportUpdated = getReport.iterator().next();
        Report oldReport = reportUpdated;
        String currentVersion = reportUpdated.getVersion().getVersion();

        versionSelected.removeAll();
        versionSelected.setItems(this.projectVersions);

        selectPriority.setValue(reportUpdated.getPriority());
        selectType.setValue(reportUpdated.getType());
        status.setValue(reportUpdated.getStatus());
        versionSelected.setValue(reportUpdated.getVersion());

        //render report detail in overview report in footer with author and description of the selected report
        author.setText(reportUpdated.getAuthor() != null ? reportUpdated.getAuthor().getName() : "Unknown");
        reportDetails.setText(reportUpdated.getDescription() != null ? reportUpdated.getDescription() : "No description");

        //binding data to 4 fields Priority, Status, Type and Version
        this.bindingDataForFileds();

        // trigger Revert Button
        this.clickRevertBtnForOneRow(oldReport);


    }

    // Method binding data to 4 fields Priority, Status, Type and Version
    private void bindingDataForFileds() {
        selectPriority.addValueChangeListener(e -> {
            binderReport.forField(selectPriority)
                    .bind(Report::getPriority, Report::setPriority);
        });
        selectType.addValueChangeListener(e -> {
            binderReport.forField(selectType)
                    .bind(Report::getType, Report::setType);
        });
        status.addValueChangeListener(e -> {
            binderReport.forField(status)
                    .bind(Report::getStatus, Report::setStatus);
        });
        versionSelected.addValueChangeListener(e -> {
            binderReport.forField(versionSelected)
                    .bind(Report::getVersion, Report::setVersion);
        });
    }

    // Revert button (function) for clicking one row
    private void clickRevertBtnForOneRow(Report oldReport) {
        btnRevert.addClickListener(e -> {
            selectPriority.setValue(oldReport.getPriority());
            selectType.setValue(oldReport.getType());
            status.setValue(oldReport.getStatus());
            versionSelected.setValue(oldReport.getVersion());
        });
    }

    private void onOKUpdate(ConfirmDialog.ConfirmEvent confirmEvent) {
    }

    // Revert button (function) for clicking one row
    private void clickRevertBtnForMultiRows(Report report) {
        btnRevert.addClickListener(e -> {
            selectPriority.setValue(report.getPriority());
            selectType.setValue(report.getType());
            this.status.setValue(report.getStatus());
            versionSelected.setValue(report.getVersion());
        });
    }

    private List<Report> reportsUpdated = new ArrayList<Report>();

    private boolean[] checkFieldsAreDiff (boolean isAllPrioritySame , boolean isAllStatusSame, boolean isAllTypeSame ) {
        for (int i = 0; i < reportsUpdated.size() - 1; i++) {
            for (int k = i + 1; k < reportsUpdated.size(); k++) {

                // Check Priority, Status and Type whether diff or not
                //Priority
                if (reportsUpdated.get(i).getPriority() != null && reportsUpdated.get(k).getPriority() != null) {
                    isAllPrioritySame = reportsUpdated.get(i).getPriority().toString()
                            .equalsIgnoreCase(reportsUpdated.get(k).getPriority().toString());
                }

                // Status
                if (reportsUpdated.get(i).getStatus() != null && reportsUpdated.get(k).getStatus() != null) {
                    isAllStatusSame = reportsUpdated.get(i).getStatus().toString()
                            .equalsIgnoreCase(reportsUpdated.get(k).getStatus().toString());
                }

                // Type
                if (reportsUpdated.get(i).getType() != null && reportsUpdated.get(k).getType() != null) {
                    isAllTypeSame = reportsUpdated.get(i).getType().toString()
                            .equalsIgnoreCase(reportsUpdated.get(k).getType().toString());
                }
            }
        }
        boolean isStatusTemp[] = new boolean[3];
        isStatusTemp[0] = isAllPrioritySame;
        isStatusTemp[1] = isAllStatusSame;
        isStatusTemp[2] = isAllTypeSame;

        return isStatusTemp;
    }

    private void checkFieldsHasSameValueAndGiveValues (
            boolean isAllPrioritySame,
            boolean isAllStatusSame,
            boolean isAllTypeSame,
            boolean isDiffFieldsValue,
            Report firstReport) {

        if (isAllPrioritySame) {
            selectPriority.setValue(firstReport.getPriority());
        } else {
            selectPriority.setValue(null);
            isDiffFieldsValue =  true;
        }

        if (isAllStatusSame) {
            status.setValue(firstReport.getStatus());
        } else {
            status.setValue(null);
            isDiffFieldsValue =  true;
        }

        if (isAllTypeSame) {
            selectType.setValue(firstReport.getType());
        } else {
            selectType.setValue(null);
            isDiffFieldsValue =  true;
        }

        // check buttons Revert and Update should be hidden or not
        if (isDiffFieldsValue) {
            btnUpdate.setVisible(false);
        }
        else {
            btnUpdate.setVisible(true);
            // handle Revert button (function) for multi rows
            this.clickRevertBtnForMultiRows(firstReport);
        }
    }

    private void checkAuthorAndDescriptionAreDiff(boolean isSameAuthor,
                                                  boolean isSameDescription,
                                                  Report firstReport) {
        for (int i = 0; i < reportsUpdated.size() - 1; i++) {
            for (int k = i + 1; k < reportsUpdated.size(); k++) {

                // Author
                if (reportsUpdated.get(i).getAuthor() != null && reportsUpdated.get(k).getAuthor() != null) {
                    isSameAuthor = reportsUpdated.get(i).getAuthor().toString()
                            .equalsIgnoreCase(reportsUpdated.get(k).getAuthor().toString());
                }

                // Description
                if (reportsUpdated.get(i).getDescription() != null && reportsUpdated.get(k).getDescription() != null) {
                    isSameDescription = reportsUpdated.get(i).getDescription().toString()
                            .equalsIgnoreCase(reportsUpdated.get(k).getDescription().toString());
                }

                //render report detail in overview report in footer with author and description of the selected report
                author.setText( firstReport.getAuthor() != null && isSameAuthor
                        ? firstReport.getAuthor().getName().toString() : "Unknown");
                reportDetails.setText(firstReport.getDescription() != null && isSameDescription
                        ? firstReport.getDescription().toString() : "No description");
            }
        }
    }

    public void implementSelectMultiRows(Set<Report> getReport) {
        reportsUpdated = (List<Report>) getReport.stream().collect(Collectors.toList());
        Report firstReport = reportsUpdated.stream().findFirst().get();
        Report oldReport = firstReport;
        String currentVersion = firstReport.getVersion().getVersion();

        versionSelected.removeAll();
        versionSelected.setItems(this.projectVersions);
        boolean isAllPrioritySame = true, isAllStatusSame = true, isAllTypeSame = true;

        boolean []statusFieldsDiff = this.checkFieldsAreDiff(isAllPrioritySame, isAllStatusSame, isAllTypeSame);
        isAllPrioritySame = statusFieldsDiff[0];
        isAllStatusSame = statusFieldsDiff[1];
        isAllTypeSame =  statusFieldsDiff[2];
        // set value to version field
        versionSelected.setValue(firstReport.getVersion());

        // Check if field Priority  has all elements are same, set value to the field,
        // same with 2 others fields
        boolean isDiffFieldsValue = false;
        this.checkFieldsHasSameValueAndGiveValues(isAllPrioritySame, isAllStatusSame, isAllTypeSame, isDiffFieldsValue, firstReport);


        // check authors and descriptions are same of all reports
        boolean isSameAuthor = false, isSameDescription = false;
        this.checkAuthorAndDescriptionAreDiff(isSameAuthor, isSameDescription, firstReport);

//
        // binding data to 4 fields Priority, Status, Type and Version
        this.bindingDataForFileds();

        // trigger Revert Button
        this.clickRevertBtnForOneRow(oldReport);
    }

}
