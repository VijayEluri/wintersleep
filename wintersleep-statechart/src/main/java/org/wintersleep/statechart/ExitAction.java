package org.wintersleep.statechart;

public class ExitAction extends EntryExitAction {

    public static final ExitAction[] NO_EXIT_ACTIONS = new ExitAction[0];

    public ExitAction(Class clazz, String name) {
        super(clazz, name);
    }

}
