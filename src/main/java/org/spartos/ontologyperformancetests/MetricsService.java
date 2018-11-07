package org.spartos.ontologyperformancetests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private static final String SETTLEMENT_COUNT_KEY = "settlement_key";
    private static final String BETS_COUNT_KEY = "Count";

    private static final String SETTLEMENT_BEGIN_TIMESTAMP_KEY = "SettlementBegin";
    private static final String SETTLEMENT_END_TIMESTAMP_KEY = "SettlementEnd";

    private static final String BET_BEGIN_TIMESTAMP_KEY = "Begin";
    private static final String BET_END_TIMESTAMP_KEY = "End";

    private StorageService storageService;

    @Autowired
    public MetricsService(StorageService storageService) {
        this.storageService = storageService;
    }

    public MetricsResponse getMetrics() {
        long betsCount = storageService.getLongValueFromStorage(BETS_COUNT_KEY);
        long settlementsCount = storageService.getLongValueFromStorage(SETTLEMENT_COUNT_KEY);

        long settleBeginTs = storageService.getLongValueFromStorage(SETTLEMENT_BEGIN_TIMESTAMP_KEY);
        long settleEndTs = storageService.getLongValueFromStorage(SETTLEMENT_END_TIMESTAMP_KEY);

        long betBeginTs = storageService.getLongValueFromStorage(BET_BEGIN_TIMESTAMP_KEY);
        long betEndTs = storageService.getLongValueFromStorage(BET_END_TIMESTAMP_KEY);

        return MetricsResponse.Builder()
                .setBetsCount(betsCount)
                .setBetTimestamps(betBeginTs, betEndTs)
                .setSettlementsCount(settlementsCount)
                .setSettlementTimestamps(settleBeginTs, settleEndTs);
    }
}
