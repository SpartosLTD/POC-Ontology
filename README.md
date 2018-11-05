Application for performance test of Ontology network

Application will measure transactions per second (TPS) metric for sample betting contract. We're interested in transaction of two types:
1. Bet placement. Transaction includes writing bet params to contract storage (player address, wager amount, odds and outcome), and performance metrics as well.     
2. Settlement. All the bets are settled here, with one transaction transferring funds to all the players who won and the rest to the operator.

Performance metrics will be stored in the blockchain data and read from there.

The app is a REST API web application, so it can be deployed to cloud servers (if transaction throughput is limited by your local machine limitations, or if you need to fire transactions from another region for example). 

Main endpoints:

POST /placeBets

POST /settle

GET /metrics
