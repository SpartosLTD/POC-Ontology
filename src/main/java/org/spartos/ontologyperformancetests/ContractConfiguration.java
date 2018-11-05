package org.spartos.ontologyperformancetests;

import com.github.ontio.common.Address;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContractConfiguration {

    @Value("${ontology.contract.betting.bytecode}")
    private String avmByteCode;

    @Qualifier("betting_contract_byte_code")
    @Bean
    public String avmByteCode() {
        return avmByteCode;
    }

    @Qualifier("betting_contract_code_address")
    @Bean
    public String codeAddress() {
        return Address.AddressFromVmCode(avmByteCode()).toHexString();
    }
}
