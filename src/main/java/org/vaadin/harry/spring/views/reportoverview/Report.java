package org.vaadin.harry.spring.views.reportoverview;

import java.io.Serializable;

public class Report implements Serializable {
    private int priority;
    private String type;
    private String summary;
    private String assign;
    private String lastModified;
    private String time;
    public Report(int priority, String type, String summary, String assign, String lastModified, String time) {
        this.priority = priority;
        this.type = type;
        this.summary = summary;
        this.assign = assign;
        this.lastModified = lastModified;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Report{" +
                "priority=" + priority +
                ", type='" + type + '\'' +
                ", summary='" + summary + '\'' +
                ", assign='" + assign + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public int getPriority() {
        return priority;
    }
    public String getType() {
        return type;
    }
    public String getSummary() {
        return summary;
    }
    public String getAssign() {
        return assign;
    }
    public String getLastModified() {
        return lastModified;
    }
    public String getTime() {
        return time;
    }
}