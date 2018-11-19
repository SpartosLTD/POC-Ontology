Application for performance test of Ontology network

Application measures transactions per second (TPS) metric for sample betting contract. We're interested in transaction of two types:
1. Bet placement. Transaction includes writing bet params to contract storage (player address, wager amount, odds and outcome), and performance metrics as well.     
2. Settlement. All the bets are settled here, with one transaction transferring funds to all the players who won and the rest to the operator.

Performance metrics are stored in the blockchain data and being read from there.

The app is a REST API web application, so it can be deployed to cloud servers (if transaction throughput is limited by your local machine limitations, or if you need to fire transactions from another region for example). 

Setup:

1. Network. Run ontology network allowing zero gas price (testnet most probably). Modify application.yml with node url (ontology.ip)

2. Private key. Create a private key for Ontology and put it into application.yml (owner.pk field)

3. Deployed contract. Open SmartX (Ontology online IDE), create Python contract and deploy contracts/BettingContract.py contract.

4. Copy-paste contract AVM code from SmartX to application.yml (contract.betting.bytecode field)

5. Run Spring Boot app (starts on http://localhost:5001 by default) and place bets, settle bets, observer metrics with HTTP requests.


Main endpoints (if you use Postman, you can load requests collection Ontology.postman_collection.json):

1. Place bets

curl -X POST \
  http://localhost:5001/placeBets \
  -H 'Content-Type: application/json' \
  -d '{
	"nodeUrls": ["http://38.240.41.207","http://38.189.106.39"],
	"betsCount": 10000
}'


2. Settle bets

curl -X POST \
  http://localhost:5001/settle \
  -H 'Content-Type: application/json' \
  -d '{
	"nodeUrls": ["http://38.240.41.207","http://38.189.106.39"],
	"count": 10000,
	"outcome": 1
}'

3. Get peformance metrics

curl -X GET http://localhost:5001/metrics

Metrics response example:

{
    "betsCount": 100,
    "settlementsCount": 5053,
    "betPlacementDuration": 0,
    "settlementsDuration": 45,
    "placementTps": 0,
    "settlementsTps": 112
}