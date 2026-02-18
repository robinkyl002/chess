# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](SequenceDiagram.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7gyTJTspXI3l5d5LsKYoSm6MpymW7xKpgKrBhqAByEDRdqur6v5MBWjagW8sFlnWT2fbbh5ln+sy0yXtASAAF4oBwUYxnGhRaUmlSiWmTgAIwETmqh5vM0FFiWrqVXq1V1bsdFNvF6owE0+h-DsMAAJJoCA0AouAuULgKbUdlZLobu6W7uSB1TlWN8QTfVjUoLGCnofCybIKmMDpj1oxjH1A0FmMw3QKNFE3VNjYMcODr7e5rozqdzoeVSt78mOE4oM+8Tnpe14Q4ulQPmuAZY3DnbtjppaOeKGSqABmBk+dElgYR+nzCRqHfBRVH1qztFPZhr3YTAuH4V9-k0WRYwc4hXOkQ29GMd4fj+F4KDoDEcSJMrquOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMktIbzH7mf69voLTzvwr68P1LZ9g6w5Qk685aiuSVgo46OMCMmA6OY-BUtoHOQWQ3joXik+RPyLK8qu4Us1JSl6NZdaOS2kji4FUdRX9mdUMXaWFXA1AtW3dG93NY7L0lGAnWfdm-K-UNxaA5KV0g7LTYR-Th1doTL7EzP4fl6OHAoNwx6XnHV7aEneUp0KtTSGvTKGOjr7w6THv1Nrp5UzTdN1wzLyafXfPdzheFZtNDGeArAQouufw2BxQan4miGAABxJUGg9ZlVLA0CBZtLb2CVHbS8nMWqvydrCF26CE7uxwZ7A61lkA5CgTmDEpCwDkLUMHEkYdPJ73pFHJksdc6712veYUMAwoZ3nlnKKuc4qqg1I3KqzdJo7X3NPQqvYa4X0fjUIG4iW4NTbg9eMrUu5vQ+gRAGDcx4SPqhPcGy8ZFHTPluKeIV6gQKZJWBAUdoFSPyl7TstQaE8jATkBh2CvzXzRDQ1Qd8ECAQ9tPXSywUE5gLA0cY0SUArWkAWLq4RgiBBBJseIuoUBuk5Hsb4yRQBqjyZBRY3wEmJSVOUi4MBOgvwZm-N6QssxROgbE+JSokkpLSRk5YWScmlIMmMQpCBilDMGiMkElSykjNqfUsG8tmL+A4AAdjcE4FATgYgRmCHALiAA2eAqNIEViKPzMSbilGNFaB0ZBqCroYKzDMuYDTEyVDJvUXOBEXmDTaXMKpnwzKEP1t7GAh45AoBoRiOAqMaF0NDrXRGydmHRzYXgpCHDpE2J4enOe28BE5wxW7fOo8m6qJcZDSus9q6+KuZdclk07oaMwY0-aHV3rdT0cPAxjLjHf0pRXK51lLHyAYciphkcIXomhb8rFwVU71EfKcuY2cnEAtirXPxcJ6iwqPFCpUISwkgsUbpMYCTun1FSekt5GF2UXI-sLTpcxLUwGtYEAVv9lmWDXrZTYaskAJDAD6vsEB-UACkIDihVYYfwRSQBqnOd3UFT8bnMhkj0BJaD45ISzNgMZPqoBwAgLZKAawLXSFtUQz5xpiWKVGPm4Ahbi2lvLV0ytBCvwppnvUAAVlGtA0LI3inhSgQkIc3IKIlbtWoaLN7sMFXtRVkD7GiuAGqoRpKxHjSMaYlFe1qUw03GKrV9LeUqKZeojuWj2oOs5X3b6A98yLH0condqiTGLvMbPNd4qZDLx8tQpUGJPE7y-Ti3h6rDBRQrcIhK8AS3QErGaGs4ZCjWMPTG8+JNTWlkDChsMSFmXXqwdogW6ZMxfR+s+-6PL6jtGDDMBDpbkPBnNOCsZSA5igzlhh4VR1QNiuscuigzBG3NsQ1ACZMBIAwBROO3EUAADkAp-J0oeIQ2ow7B2Gv-KEztFk4G1BGFWpp2EWmjE9UxRWXgm0BqDbZ+UiBgywGANgfNhA8gFBgEm8w3bJLG1NubS2xhHYfKvjAEzBm7X8dnqvdeKAeQYknTh6d3kjDHw3vw4AyXwPLoyDMCANAHH4uw4vRhM6MsJcEzl+V+9lwwAK0VwwJpHE1b-RHeo8WT4RW0Ll4TB9GtHGayV3rJ6FHat0sCrtuHn6d1ve-QWn9LOLKAA)]

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
