from boa.interop.System.Runtime import Log, GetTrigger, CheckWitness
from boa.interop.System.ExecutionEngine import GetScriptContainer, GetExecutingScriptHash
from boa.interop.System.Blockchain import GetHeight, GetHeader, GetTimestamp
from boa.interop.System.Storage import GetContext, Get, Put, Delete
from boa.interop.System.Header import GetMerkleRoot, GetTimestamp, GetHash, GetVersion, GetConsensusData, GetNextConsensus
from boa.builtins import concat
from boa.interop.System.Runtime import GetTime

OWNER_ADDRESS = "OwnerAddress"

BET_ADDRESS_KEY = "address_key"
BET_AMOUNT_KEY= "amount_key"
BET_OUTCOME_KEY = "outcome_key"

BALANCE_KEY = "balance_key"

SETTLEMENT_BEGIN_TIMESTAMP_KEY = "SettlementBegin"
SETTLEMENT_END_TIMESTAMP_KEY = "SettlementEnd"
SETTLEMENT_COUNT_KEY = "settlement_key"

BETS_COUNT_KEY = "Count"
BET_BEGIN_TIMESTAMP_KEY = "Begin"
BET_END_TIMESTAMP_KEY = "End"

context = GetContext()

OnBetPlaced = RegisterAction('bet_placed', 'address', 'amount', 'outcome')

INITIAL_OWNER_BALANCE = 1000000

def Main(operation, args):
    if operation == 'PlaceBet':
        address = args[0]
        amount = args[1]
        outcome = args[2]
        return PlaceBet(args[0], args[1], args[2])
    if operation == 'Settle':
        return Settle(args[0])
    if operation == 'Reset':
        return Reset(args[0])
    return False

def Reset(deleteBets):
    Put(context, concat(BALANCE_KEY, OWNER_ADDRESS), INITIAL_OWNER_BALANCE)

    Delete(context, SETTLEMENT_BEGIN_TIMESTAMP_KEY)
    Delete(context, SETTLEMENT_END_TIMESTAMP_KEY)
    Delete(context, SETTLEMENT_COUNT_KEY)

    if deleteBets:
        count = Get(context, BETS_COUNT_KEY)
        if len(count) == 0:
            count = 0
        i = 1
        while i < count:
            betAddressKey = concat(BET_ADDRESS_KEY, i)
            Delete(context, betAddressKey)
            betAmountKey = concat(BET_AMOUNT_KEY, i)
            Delete(context, betAmountKey)
            betOutcomeKey = concat(BET_OUTCOME_KEY, i)
            Delete(context, betOutcomeKey)
            i += 1

    Delete(context, BETS_COUNT_KEY)
    Delete(context, BET_BEGIN_TIMESTAMP_KEY)
    Delete(context, BET_END_TIMESTAMP_KEY)
    return True

def Settle(outcome):
    settlementBeginTimestamp = Get(context, SETTLEMENT_BEGIN_TIMESTAMP_KEY)
    if settlementBeginTimestamp == None:
        date = currentTimestamp()
        Put(context, SETTLEMENT_BEGIN_TIMESTAMP_KEY, date)

    settlementsCount = Get(context, SETTLEMENT_COUNT_KEY)
    if len(settlementsCount) == 0:
        settlementsCount = 1
    else:
        settlementsCount += 1
    Put(context, SETTLEMENT_COUNT_KEY, settlementsCount);

    count = Get(context, BETS_COUNT_KEY)
    i = 0
    while i < count:
        betOutcomeKey = concat(BET_OUTCOME_KEY, i)
        betOutcome = Get(context, betOutcomeKey)
        if (betOutcome == outcome):
            #Player won
            playerAddressKey = concat(BET_ADDRESS_KEY, i)
            playerAddress = Get(context, playerAddressKey)

            betAmountKey = concat(BET_AMOUNT_KEY, i)
            betAmount = Get(context, betAmountKey)

            transfer(OWNER_ADDRESS, playerAddress, betAmount)
        i += 1

    Put(context, SETTLEMENT_END_TIMESTAMP_KEY, currentTimestamp())
    return True

def transfer(addressFrom, addressTo, amount):
    senderAmount = Get(context, concat(BALANCE_KEY, addressFrom))
    if (senderAmount >= amount):
        senderAmount -= amount;
        Put(context, concat(BALANCE_KEY, addressFrom), senderAmount)
        receiverAmount = Get(context, concat(BALANCE_KEY, addressTo))
        receiverAmount += amount
        Put(context, concat(BALANCE_KEY, addressTo), receiverAmount)
        return True
    else:
        return False


def PlaceBet(address, amount, outcome):
    #Get bets counter
    count = Get(context, BETS_COUNT_KEY)
    if len(count) == 0:
        count = 0

    #Save bet
    betAddressKey = concat(BET_ADDRESS_KEY, count)
    betAmountKey = concat(BET_AMOUNT_KEY, count)
    betOutcomeKey = concat(BET_OUTCOME_KEY, count)

    Put(context, betAddressKey, address)
    Put(context, betAmountKey, amount)
    Put(context, betOutcomeKey, outcome)

    OnBetPlaced(address, amount, outcome)

    #Update counter
    count += 1
    Put(context, BETS_COUNT_KEY, count)

    #Performance metrics
    date = currentTimestamp()
    firstBlockTime = Get(context, BET_BEGIN_TIMESTAMP_KEY)
    if firstBlockTime == None:
        Put(context, BET_BEGIN_TIMESTAMP_KEY, date)
    Put(context, BET_END_TIMESTAMP_KEY, date)
    return True

def currentTimestamp():
    return GetTime()