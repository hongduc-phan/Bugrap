package org.vaadin.harry.spring.data;

public class ReportDetail {
    private int id;
    private int reportId;
    private String detail;

    public ReportDetail(int id, int reportId, String detail) {
        this.id = id;
        this.reportId = reportId;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
