package org.spartos.ontologyperformancetests;

import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;

@Service
public class StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(StorageService.class.getSimpleName());

    private static final String BET_ADDRESS_KEY = "address_key";
    private static final String BET_AMOUNT_KEY = "amount_key";
    private static final String BET_OUTCOME_KEY = "outcome_key";

    private static final String OWNER_ADDRESS = "OwnerAddress";

    private static final String BALANCE_KEY = "balance_key";


    private Account ownerAccount;
    private OntSdk sdk;

    private String codeAddress;

    @Autowired
    public StorageService(OntSdk sdk, Account ownerAccount, @Qualifier("betting_contract_code_address") String codeAddress) {
        this.sdk = sdk;
        this.ownerAccount = ownerAccount;
        this.codeAddress = codeAddress;
    }

    public Bet getBet(int playerIndex) {
        String playerAddress = getValueFromStorage(getStorageKeyForPlayer(BET_ADDRESS_KEY, playerIndex));
        Long amount = getLongValueFromStorage(getStorageKeyForPlayer(BET_AMOUNT_KEY, playerIndex));
        Long outcome = getLongValueFromStorage(getStorageKeyForPlayer(BET_OUTCOME_KEY, playerIndex));
        return new Bet(playerAddress, amount, outcome);

    }

    public long getPlayerBalance(int playerIndex) {
        return getLongValueFromStorage(getStorageKeyForPlayer(BALANCE_KEY, playerIndex));
    }

    public long getOwnerBalance() {
        return getLongValueFromStorage(BALANCE_KEY + OWNER_ADDRESS);
    }

    private String getStorageKeyForPlayer(String prefix, int playerIndex) {
        return prefix + playerIndex;
    }

    public Long getLongValueFromStorage(String key) {
        try {
            String storageValue = sdk.getConnect().getStorage(codeAddress, Helper.toHexString(key.getBytes()));
            if (storageValue.isEmpty()) return 0L;
            return Long.parseLong(Helper.reverse(storageValue), 16);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getValueFromStorage(String key) {
        try {
            String storageValue = sdk.getConnect().getStorage(codeAddress, Helper.toHexString(key.getBytes()));
            byte[] bytes = DatatypeConverter.parseHexBinary(storageValue);
            return new String(bytes, Charset.defaultCharset());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
