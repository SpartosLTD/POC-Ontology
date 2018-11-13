package org.spartos.ontologyperformancetests;

import com.github.ontio.OntSdk;
import com.github.ontio.sdk.exception.SDKException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class OntologyConfiguration {

    @Value("${ontology.ip}")
    private String ip;

    @Value("${ontology.restPort}")
    private String restPort;

    @Value("${ontology.rpcPort}")
    private String rpcPort;

    @Bean
    public Function<String[], RandomNodeSdk> randomNodeSdkFactory() {
         return nodeIps -> new RandomNodeSdk(restPort, rpcPort, nodeIps);
    }

    @Bean
    public OntSdk ontSdk() {

        String restUrl = ip + ":" + restPort;
        String rpcUrl = ip + ":" + rpcPort;

        OntSdk sdk = OntSdk.getInstance();
        sdk.setRpc(rpcUrl);
        sdk.setRestful(restUrl);
        try {
            sdk.setDefaultConnect(sdk.getRestful());
        } catch (SDKException e) {
            throw new RuntimeException(e);
        }

        return sdk;
    }

    //Wallet address


}
