package org.spartos.ontologyperformancetests;

public class SettleRequest {

    private String[] nodeUrls;

    private int count;

    private long outcome;

    public long getOutcome() {
        return outcome;
    }

    public int getCount() {
        return count;
    }

    public String[] getNodeUrls() {
        return nodeUrls;
    }
}
