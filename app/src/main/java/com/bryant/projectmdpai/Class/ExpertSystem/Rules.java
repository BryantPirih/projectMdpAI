package com.bryant.projectmdpai.Class.ExpertSystem;

public class Rules {
    String rulename;
    String variable;
    String value;

    public Rules(String rulename, String variable,String value) {
        this.rulename = rulename;
        this.variable = variable;
        this.value = value;
    }

    public String getRulename() {
        return rulename;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
