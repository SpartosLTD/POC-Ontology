package org.spartos.ontologyperformancetests;

import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.smartcontract.neovm.abi.AbiFunction;
import com.github.ontio.smartcontract.neovm.abi.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class.getSimpleName());

    private Account ownerAccount;
    private OntSdk sdk;

    private String codeAddress;

    private StorageService storageService;

    @Autowired
    public TransactionService(OntSdk sdk, Account ownerAccount, @Qualifier("betting_contract_code_address") String codeAddress, StorageService storageService) {
        this.sdk = sdk;
        this.ownerAccount = ownerAccount;
        this.codeAddress = codeAddress;
        this.storageService = storageService;
    }

    public List<CompletableFuture<Object>> settle(SettleRequest request) {
        List<CompletableFuture<Object>> results = IntStream.range(0, request.getCount())
                .mapToObj(value -> CompletableFuture.supplyAsync(() -> {
                    AbiFunction func = new AbiFunction("Settle", new Parameter("outcome", Parameter.Type.Integer, request.getOutcome()));
                    return sendTransaction(func);
                })).collect(Collectors.toList());
        return results;
    }


    public List<CompletableFuture<Object>> placeBets(PlaceBetsRequest request) {
        reset();
        return IntStream.range(0, request.getBetsCount())
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    String address = "Player" + i;
                    long amount = 50;
                    long outcome = 1;

                    Parameter addressParam = new Parameter("address", Parameter.Type.String, address);
                    Parameter amountParam = new Parameter("amount", Parameter.Type.Integer, amount);
                    Parameter outcomeParam = new Parameter("outcome", Parameter.Type.Integer, outcome);

                    AbiFunction func = new AbiFunction("PlaceBet", addressParam, amountParam, outcomeParam);
                    return sendTransaction(func);
                })).collect(Collectors.toList());
    }

    private void reset() {
        try {
            AbiFunction func = new AbiFunction("Reset", new Parameter("deleteBets", Parameter.Type.Boolean, false));

            sendTransaction(func);
            Thread.sleep(7000);
            long countAfrerReset = storageService.getLongValueFromStorage("Count");

            LOG.info("Bets count after reset: " + countAfrerReset);

        } catch (Exception e) {
            //Error is OK, we're deleting Count during Reset() from Storage
            LOG.error(e.getMessage());
        }
    }

    private Object sendTransaction(AbiFunction func) {
        try {
            return sdk.neovm().sendTransaction(Helper.reverse(codeAddress), ownerAccount, ownerAccount, sdk.DEFAULT_GAS_LIMIT * 1000, 0, func, false);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
