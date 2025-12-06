package dev.krc.piboard.model;

public enum IssueState {
    Open("Open"),
    Backlog("Backlog"),
    Iteration_Backlog("Iteration Backlog"),
    In_Progress("In Progress"),
    Verify("Verify"),
    Closed("Closed");

    private final String label;

    IssueState(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}