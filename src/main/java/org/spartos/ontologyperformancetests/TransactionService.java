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

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class.getSimpleName());

    private Account ownerAccount;
    private OntSdk sdk;

    private String codeAddress;

    @Autowired
    public TransactionService(OntSdk sdk, Account ownerAccount, @Qualifier("betting_contract_code_address") String codeAddress) {
        this.sdk = sdk;
        this.ownerAccount = ownerAccount;
        this.codeAddress = codeAddress;
    }

    //Performs sample transaction invoking DomainTest.py contract (one of the templates available in SmartX online Ontology IDE)
    public String performTransactionSample(String value) {

        try {
            String name = "Register";
            AbiFunction func = new AbiFunction();
            func.name = name;

            List<Parameter> parameterList = new ArrayList<>();

            String key = "Domain" + System.currentTimeMillis();

            Parameter domainParam = new Parameter();
            domainParam.type = "String";
            domainParam.name = "domain";
            domainParam.value = "";
            parameterList.add(domainParam);

            Parameter ownerParam = new Parameter();
            ownerParam.type = "String";
            ownerParam.name = "owner";
            ownerParam.value = "";
            parameterList.add(ownerParam);

            func.parameters = parameterList;

            func.returntype = "String";

            func.setParamsValue(key, value);

            LOG.debug("Saving key-value pair in contract: key=" + key, " value=" + value);

            //private test network allows zero gas price by default
            sdk.neovm().sendTransaction(Helper.reverse(codeAddress), ownerAccount, ownerAccount, sdk.DEFAULT_GAS_LIMIT, 0, func, false);

            Thread.sleep(5000); //wait for transaction to be mined

            String storageValue = sdk.getConnect().getStorage(codeAddress, Helper.toHexString(key.getBytes())); //THIS CODE WORKS!
            byte[] bytes = DatatypeConverter.parseHexBinary(storageValue);
            String storageValueDecoded = new String(bytes, Charset.defaultCharset());
            LOG.debug("Value retrieved from contract: " + storageValueDecoded);

            return storageValueDecoded;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
