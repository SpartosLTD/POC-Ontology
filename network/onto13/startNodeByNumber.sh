#!/usr/bin/env bash
NODE_NUMBER=${1}

restport=20334
wsport=20335
rpcport=20336
nodeport=20339

./ontology \
--data-dir node${NODE_NUMBER}/ \
--networkid 3 \
--gasprice 0 \
--wallet node${NODE_NUMBER}/node${NODE_NUMBER}_wallet.dat \
--password node${NODE_NUMBER} \
--config config-vbft.json \
--rpcport $rpcport \
--rest --restport $restport  \
--ws --wsport $wsport \
--enable-consensus \
--nodeport $nodeport \
--disable-tx-pool-pre-exec \
--disable-sync-verify-tx