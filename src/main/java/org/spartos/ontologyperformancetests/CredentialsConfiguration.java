package org.spartos.ontologyperformancetests;

import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CredentialsConfiguration {

    @Value("${ontology.owner.pk}")
    private String ownerPrivateKey;

    private OntSdk sdk;

    @Autowired
    public CredentialsConfiguration(OntSdk sdk) {
        this.sdk = sdk;
    }

    @Bean
    public Account ownerAccount() {
        try {
            return new Account(Helper.hexToBytes(ownerPrivateKey), sdk.defaultSignScheme);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
