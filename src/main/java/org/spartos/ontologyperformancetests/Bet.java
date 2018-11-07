package org.spartos.ontologyperformancetests;

public class Bet {

    private String address;
    private long amount;
    private long outcome;

    public Bet(String address, long amount, long outcome) {
        this.address = address;
        this.amount = amount;
        this.outcome = outcome;
    }

    public String getAddress() {
        return address;
    }

    public long getAmount() {
        return amount;
    }

    public long getOutcome() {
        return outcome;
    }
}
