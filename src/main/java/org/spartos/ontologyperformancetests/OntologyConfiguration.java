package org.spartos.ontologyperformancetests;

import com.github.ontio.OntSdk;
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
        String[] ips = { ip };
        return randomNodeSdkFactory().apply(ips).getRandom();
    }

}
