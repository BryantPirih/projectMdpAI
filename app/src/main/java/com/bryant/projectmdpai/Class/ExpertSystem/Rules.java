package com.bryant.projectmdpai.Class.ExpertSystem;

public class Rules {
    String rulename;
    String variable;

    public Rules(String rulename, String variable) {
        this.rulename = rulename;
        this.variable = variable;
    }

    public String getRulename() {
        return rulename;
    }

    public void setRulename(String rulename) {
        this.rulename = rulename;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
