package com.bryant.projectmdpai.Class.ExpertSystem;

public class Rules {
    String rulename;
    String variable;
    String value;
    String condition;
    String subGoal="";

    public Rules(String rulename, String variable,String value,String condition) {
        this.rulename = rulename;
        this.variable = variable;
        this.value = value;
        this.condition = condition;
    }
    public Rules(String rulename, String variable,String value,String condition,String subGoal) {
        this.rulename = rulename;
        this.variable = variable;
        this.value = value;
        this.condition = condition;
        this.subGoal = subGoal;
    }

    public String getSubGoal() {
        return subGoal;
    }

    public void setSubGoal(String subGoal) {
        this.subGoal = subGoal;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
