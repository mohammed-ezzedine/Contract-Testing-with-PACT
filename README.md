# How To Run:
1. Spin up the required containers: `docker-compose up -d`
2. Run the Provider Application
3. Run the Consumer Application

# To Generate the Contracts:
We need to run the consumer contract Tests:

    gradlew runConsumerContractTests

# To Publish the Contracts to the Broker:

    gradlew pactPublish

# To Validate the Contracts:
we need to run the provider contract tests:

    gradlew runProviderContractTests