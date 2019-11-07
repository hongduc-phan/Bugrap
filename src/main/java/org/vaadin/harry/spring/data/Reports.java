package org.vaadin.harry.spring.data;

import java.util.ArrayList;
import java.util.List;

public class Reports {

    public static List<Report> getReports() {
        List<Report> reportList = new ArrayList<>();
        reportList.add(new Report(1, 3, "Bug", "1111111", "aaaaa", "2012", "15m ago"));
        reportList.add(new Report(2, 4, "Feature", "22222", "bbbbb", "2013", "15m ago"));
        reportList.add(new Report(3, 3, "Feature", "33333", "ccccc", "2014", "15m ago"));
        reportList.add(new Report(4, 2, "Bug", "44444", "dddddd", "1011", "15m ago"));
        return reportList;
    }

}
