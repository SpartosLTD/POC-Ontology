package org.spartos.ontologyperformancetests;

import com.github.ontio.OntSdk;
import com.github.ontio.sdk.exception.SDKException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomNodeSdk {

    private List<OntSdk> sdks = new ArrayList<>();

    private String restPort;
    private String rpcPort;

    public RandomNodeSdk(String restPort, String rpcPort, String[] nodeUrls) {
        this.restPort = restPort;
        this.rpcPort = rpcPort;
        setUrls(nodeUrls);
    }

    private void setUrls(String[] nodeUrls) {
        sdks = Arrays.stream(nodeUrls)
                .map(ip -> {
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
                }).collect(Collectors.toList());
    }

    public OntSdk getRandom() {
        Random random = new Random();
        int randomIndex = random.nextInt(sdks.size() - 1);
        return sdks.get(randomIndex);
    }

}
