# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[First version of Phase 2 Sequence Diagram](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwdFNp43h+P4XgoOgMRxIk+mGYJ9i+FgomCqB9QNNIEb8RG7QRt0PRyaoCnDBRVHoI2DHacx-gouu-jYOKGr8WiMAAOJKho1niTUdkxc5bn2Eq3mXr5hT0YxOkBBwADsbhOCgTgxBGwRwFxABs8AToYcVzDARTIOYNnVJJrQdOlmXTNliHoFmGVzAAckqmkBUxumWCgfYQJsRlIAkYBzQtS0AFIQOKsUVv4ySgGqbUlGJvpdaWzTMjJPSjSgWXwUNimjNgCDAHNUBwBACDQGsd0AJLSFN+VBV473LatYPyogwawMA2CvYQeQFK1iXnRJl0OU5LlucY-mYEAA)

[Final version of Phase 2 Sequence Diagram](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkEkSYCkm+hi7jS+4MkyU76XOnl3kuwpihKboynKZbvEqmAqsGGrMtMl7QEgABeKAcPUADq3gODAak7DeAUOkmvrOl2G7ulubkgdU-oJRRyVpRwUYxnGhRaSV8DIKmMDpgAjAROaqHm8zQUWJauoleqNelDb0YVvKBRZVlhVVZWCsO-JjhOKDPvE56Xtem2LpUD5rgGh1rZ27Y6aWDnihkqgAZgt01RJYGEeWxGkd8FFUfWJGoQ2HXJt12EwLh+GjJ9UXfUDsGXv9yGA7RjYMZ43h+P4XgoOgMRxIkON4w5vhYKJgqgfUDTSBG-ERu0EbdD0cmqApwx-Yh6DofClS3S8HNIaZsIYctLrWUJpP2RLp5OWoLnbu5VK3ltMCMmAe0HfBnNoP5i3FadwXiuuDVQKleQFLK8oC1zsXqpKU3xDNDHHQKnVufUe2vutbu1aW9VJabTUtSgsYKdzmFg2AaZOAN0NDSNBZjON0CTSbqWzXRTYu29lli57V254rMjK-SHAoNwx6XprlHa7rC6uwbtTSOXTKGPn8iW8aiPawrN1mf6JMy-+CCAf38IWbpmm+xHJRRxDeFZpn6NMVjKLrv42Dihq-FojAADiSoaOTE+lg0e-00z9hKuz3dIeHH5j-zt-oELX4U2V3lgAfOYYmr39qLLEkvcPJ63pGrDW1sdYLXrveYU+8mQXRfNoTukCYqqnig7J20D9w5ysu3YAwCT6pwDunZq0YQ5tXvqDWe0dY7Zn5AnRYyc-aYMDhnNG2ClqlU7B7S68hgFKyKvSayaJ-6qAxHXHBQV6h7wQSaBAqtD6cOKqLcq-8d45EIbzR++8lQaNHsLce3CagvGWFfHMBYGjjHMSgAAktIAsfVwjBECCCTY8RdQoDdJyPY3xkigDVN4yCixvg2IAHJKhCRcGAnQp7vRnj1SGWYzGH0sdYpU9jHHONccsdxnigmfBCSCfxIBAlEVGmMUJSoIlzCiTEpejFMYBA4AAdjcE4FATgYgRmCHALiAA2eAO1dFzBgEUSO793pU1aB0S+18HZIyzOEpUcTEzaMMfUSBSzqkrJemPSZhduwoE2P-DEaBjn-0AfLaqID661HAVXSBkjAqN3gcwfBKDn6FFthgtOTVlGLlUbwpB-CblEPtn89KwdQ7xhBpUUStDBoMPzEw4sKcIUkKanNLOJdXZAsQVeAug5BGgNHDAQ8cgUCnP-kdXFsD6hwGGeczYijRn9HFI4GlRL3zrK-Eck5h9HrPVej7KZMARgpLmJk+oTiXEwFWSLahiSF6jElXYhxMrsnyo4RjZi-hLDlxsiy2ISAEhgANX2CALKABSEBxQjMMP4EpapxmzwOZJJozIZI9BsTfLWSEszYAQMAA1UA4AQBslANYNj7EKp5g8DZ4qg0hsoOGyN0aMnSFfuZYxVkABWdq0CnILeKS5KBCRy1ct7EldyHlnieQChuQoZEII+RFVBPyMXTTYc7OlorDkVS9tdft-pbFoBANAFE4AmhGXUtCyhcKuo0N6jHJFuYUVJzRSwyFuwGnZ37XgvhBCbk1q8qrJk1KlS0qEU25cMAQrrhsZ3GN0g0FxXgBG6AlYzQ1nDIUfd+KuWgu9uCwMP6wxIXnWHRdCKV2ZjjsiipzD6jtGDDMD9kbv3BnNOS4NSA5i7o4QB3NYsgPHurcXG99QS1FqVBiMjzz9bNqqMwZNoa03QAKYYSAMAUQVtxFAAA5AKfSWiE18pgDRstT0R57MMTnSeVD4WRxwiqkYDTdVYy8CG-GprCZQB04gYMsBgDYCDYQc2aAxnH2MZJGmdMGZM2MPfXlcJahybfvikA3A8AYjLhXFAVyq3DtPSOeo3njNkYkY2+lMgW7ogdTKCAOg80oHABoE9lHSXhZ81AfB0X92vObgFglSWUtpbABlijLscvGdWvIArfaivxcMPV4AMBkupfS2JvmRgWtCtkyK8FSml3KqhupjhQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
