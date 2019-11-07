package org.vaadin.harry.spring.data;

import java.io.Serializable;

public class Report implements Serializable {
    private int id;
    private int priority;
    private String type;
    private String summary;
    private String assign;
    private String lastModified;
    private String time;
    public Report(int id, int priority, String type, String summary, String assign, String lastModified, String time) {
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.summary = summary;
        this.assign = assign;
        this.lastModified = lastModified;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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