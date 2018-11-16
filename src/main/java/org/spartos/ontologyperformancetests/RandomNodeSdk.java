package org.spartos.ontologyperformancetests;

import com.github.ontio.OntSdk;
import com.github.ontio.sdk.exception.SDKException;

import java.util.Random;

public class RandomNodeSdk {

    private String restPort;
    private String rpcPort;

    private String[] nodeUrls;

    public RandomNodeSdk(String restPort, String rpcPort, String[] nodeUrls) {
        this.restPort = restPort;
        this.rpcPort = rpcPort;
        this.nodeUrls = nodeUrls;
    }

    public OntSdk getRandom() {

        Random random = new Random();

        OntSdk sdk = OntSdk.getInstance();
        int randomIndex = random.nextInt(nodeUrls.length);
        String ip = nodeUrls[randomIndex];

        String restUrl = ip + ":" + restPort;
        String rpcUrl = ip + ":" + rpcPort;

        sdk.setRpc(rpcUrl);
        sdk.setRestful(restUrl);

        try {
            sdk.setDefaultConnect(sdk.getRestful());
        } catch (SDKException e) {
            throw new RuntimeException(e);
        }

        return sdk;
    }

}
