#!/usr/bin/env bash
./ontology contract deploy --name='Betting Contract' \
--code=avm.code \
--author=Spartos --desc=Spartos --email=rodion.altshuler@spartos.com --needstore --gaslimit=2000000000 \
--wallet=node0/node0_wallet.dat
