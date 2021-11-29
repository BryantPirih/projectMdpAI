package com.bryant.projectmdpai.Class.ExpertSystem;

public class Solution {
    String foreignvariable;
    String solution;

    public Solution(String foreignvariable, String solution) {
        this.foreignvariable = foreignvariable;
        this.solution = solution;
    }

    public String getForeignvariable() {
        return foreignvariable;
    }

    public void setForeignvariable(String foreignvariable) {
        this.foreignvariable = foreignvariable;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
