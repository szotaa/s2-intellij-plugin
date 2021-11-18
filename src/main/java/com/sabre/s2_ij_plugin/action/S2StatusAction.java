package com.sabre.s2_ij_plugin.action;

public class S2StatusAction extends BaseCliCommandWithOutputAction {

    private static final String[] S2_STATUS_COMMAND = {"pwd"};

    public S2StatusAction() {
        super(S2_STATUS_COMMAND);
    }
}
