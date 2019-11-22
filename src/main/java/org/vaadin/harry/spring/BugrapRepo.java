package org.vaadin.harry.spring;

import org.vaadin.bugrap.domain.BugrapRepository;

public class BugrapRepo {
    // connect to Bugrap domain service
    private static BugrapRepository bugrapRepository;

    public static BugrapRepository getRepo() {
        if (bugrapRepository == null) {
            bugrapRepository  = new BugrapRepository("/tmp/bugrapdb111;create=true");
        }
        return bugrapRepository;
    }

}
