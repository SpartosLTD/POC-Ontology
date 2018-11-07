package org.spartos.ontologyperformancetests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class PerformanceTestsController {

    private TransactionService transactionService;

    private MetricsService metricsService;

    private StorageService storageService;

    @Autowired
    public PerformanceTestsController(TransactionService transactionService, MetricsService metricsService, StorageService storageService) {
        this.transactionService = transactionService;
        this.metricsService = metricsService;
        this.storageService = storageService;
    }

    @PostMapping("/placeBets")
    public String placeBets(@RequestBody PlaceBetsRequest request) {
        List<CompletableFuture<Object>> results =  transactionService.placeBets(request);
        return results.size() + " bet placement transactions have been submitted";
    }

    @PostMapping("/settle")
    public String settle(@RequestBody SettleRequest request) {
        List<CompletableFuture<Object>> results = transactionService.settle(request);
        return results.size() + " settlement transactions have been submitted";
    }

    @GetMapping("/metrics")
    public MetricsResponse getMetrics() {
        return metricsService.getMetrics();
    }

    @GetMapping("/bet/{playerIndex}")
    public Bet getBet(@PathVariable("playerIndex") int playerIndex) {
        return storageService.getBet(playerIndex);
    }

    @GetMapping("/balance/{playerIndex}")
    public long getBalance(@PathVariable("playerIndex") int playerIndex) {
        return storageService.getPlayerBalance(playerIndex);
    }

    @GetMapping("/owner/balance")
    public long getOwnerBalance() {
        return storageService.getOwnerBalance();
    }


}
