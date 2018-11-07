package org.spartos.ontologyperformancetests;

public class MetricsResponse {

    private long betsCount;

    private long settlementsCount;

    private long betPlacementDuration;

    private long settlementsDuration;

    private long placementTps;

    private long settlementsTps;

    public static MetricsResponse Builder() {
        return new MetricsResponse();
    }

    public MetricsResponse setBetsCount(long betsCount) {
        this.betsCount = betsCount;
        return this;
    }

    public MetricsResponse setBetTimestamps(long begin, long end) {
        betPlacementDuration = end - begin;
        if (betPlacementDuration > 0) {
            placementTps = betsCount / betPlacementDuration;
        }
        return this;
    }

    public MetricsResponse setSettlementsCount(long count) {
        settlementsCount = count;
        return this;
    }

    public MetricsResponse setSettlementTimestamps(long begin, long end) {
        settlementsDuration = end - begin;
        if (settlementsDuration > 0) {
            settlementsTps = settlementsCount / settlementsDuration;
        }
        return this;
    }


    public long getBetsCount() {
        return betsCount;
    }

    public long getSettlementsCount() {
        return settlementsCount;
    }

    public long getBetPlacementDuration() {
        return betPlacementDuration;
    }

    public long getSettlementsDuration() {
        return settlementsDuration;
    }

    public long getPlacementTps() {
        return placementTps;
    }

    public long getSettlementsTps() {
        return settlementsTps;
    }
}

