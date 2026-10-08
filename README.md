# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Phase 2: Chess Server Design

[Chess Server Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWMm0egMMEo3igmB5tP5dwR5JU9SNfPUAFEAB4qbAEApkkQqU2VG7PTU062qe2O52FL3w+7Fcz1ACsTicMGG4zm6mAjIWyxtUH19QxHDUICgxCDMAgADNdRnoMSoWZOJgVSh1TAALLZVTixyKuZrX7-DhF2Bg06uqgU0pm6jegBCwA4BKJYADKCd+WD1UoHvgyAjMECMfjY0TqmT83qY3Tmdl05gKMJajAVfQHFrqo1UtgmyQYHiCoGnJgwAQqocPKKB2miGhWnSAqjp65ooPUr48kOI5jjU9QKAB9ZAe06roAuS4uiG67hhg9Q7sEe4HkeqanuWdR-hh07yvIaroPeNYQSa0FVG6cEwGgPgIAgSHuvcHH0nm9boq+7IDDy3LaMa6iCsYtQKBwvYIdohoKX666IhaRgFGI+mGKJOmQbUElyCgCg+J+GLAHZ8RydpvqQcpwpqb2tmfohYlQShSKGWgxmweuMJPHR2JoniagCVgEVwnpq50a8P5KksJ7Assjmfu0EAsWgmXLJcQ5rlxxFgPU4S7qMEzpZ8MBZd8uXxPlhXFfsVwPqYXi+AE0DsIyMQinANrSHACgwAAMhAWSFJVgohvUzRtF0vQGOoy7xh2KBdvofw7FchH3Il3ojLt+1bEdmBnfCME8fUCBzeKGKzfNs63qSJmCv5DJMtJu3yW5nGVCpYoSpp8iyvKu1PvWGrNltDjflMSp9jA3Y7MJpkoZO06fTkeFBmVoaVJVpG1Qm-JUSeZ7QPUei9tec5sY+dYNjy2qGHq0Cuby7lcSZlrmfyxPLjjyXjnR-niwRKVEZuJEwNGsYUTTKZ07RWY5qoeYFsuRalrzUCVt1NYcy+Ax8RAzDFr47Gi0pQuwfU-GCZLLvS-B0xOdASAAF4oBwcsrtLislFV25OAAjOrSaa2m2sKr7n7+0Huzm4+-lS8OBlQ8A8MNgAkmgIDQCi4CYwdPae3jdHMqn8Tp8HoekxHW47nHdWUYnNHnj4Tct5n1aPj9Zkg+Jqroj58QOU5Lk5-cKleTAs9+U7AUPXnvEcEZdeVHd9TveKGSqPFt2PElXuoTAaVo41zUgq17WsU1JVVidYZK1HNVOHuu1OrZTGC-Aqb9mpZ16t4Pw-gvAoHQDEOIiQ4EIPer4LAi164rWkDaaaNp2g2m6D0TarYCjDFAYVdup0r7elaunPIBR6gAB4KHoHKJfWE5Vt5BWevYdBb05roMJmAb6YUKh-RgIyMAs9555TAWgYGAtQZCnqBDbyC9tAwz-E5V+hRLY+l0PoHmtF+aKS3txHeItJ7+gdIuEm48sEGLMW3L+5Mf5RhjHGHuGtjxJ3PNmXM+Z8JoCNmWfUZtR5Fw1KwkJaBbYwHtoPbOm9c5BXdkJBx29vSNzofmDOLiFYVXcdHbu1ME6+P7gzFOuTA7BzZqY3SN8grry0uPKkm96g5m4DPJysi2ryMUWYjyloUDdMMC0+QDTBaBQMnvEKB8HicLomguyZ8L53VzudY6hTv6R2qlTSBnhoEBBRL2fw2BxQammmiGAABxJUGhMFZLog0W5+CiH2CVOQnR8iqGHxoXRGpAcGFoGYTE9hGyZm8WQDke5iYMQwrAMI0RPFKQSKkTImJgzGlg2FDANRa8NHQzlNouRlD9FcyMWEvmS8oVWKUbaWxwSFnLScX6Ap4cil7JVp4+Oh4+70zogEvWQTCwlmpabep+iYk2ztg7ZJ1jUkGXSSylKPsgWtyZfYnZbjuVdz5bTPxVTB4apHj1Wl3CDITMLm0tlFkYCIrhWoDE2LBa4tUeKbygk7now3oqppBknUykRQso+M00RrIQAlAFmzUrLE+YmVMDRxgJpQMXaQqYY7hGCIEEEmx4gfhQK+TkexvjJFAGqYtGVFjfFTQAOWrWMKEMBOjbM5bsrcf94zxoeUmlNSp02Zuzbm5Y+bC1Vs+DWkE5aQCVoaseJtIJ62Nuba2yJRz+r+A4AAdjcE4FAsZ-A2mCHAMaAA2eA09DBOpgEUH+S01WNFaB0D5Xym66PjMuuYbbbjUKWfUIFIKwU-MKhCmNdKYBWXRE6jEUGUBOuRZ7co6KmSYpA+gV1yiVL4s9YS3ymiSUxKiaS-phVZUJPlVMziEGVWZIsdkoeeTNWBglq4jcerY4GoFcnE1fsmNmots+Ax3MJVUedhB2WWrWNiMcZJlj8t226q3KrLxZT+UVMFTrQJBsCihJNhEnq+ikathRqmviSpVXexgFOGcKAbxEykwp39HblY7n-t48p1FNMXmZnZ1mkCLUWOaUSm1Yj2nWPqHBmDX6uRiagu6te6kfVzD9Qy8xwtJEDukKGgF9Q4BXoQ3FKNHDIqxpeD2uYg76hZpzTAH95UXO-yphVtNGbqvDrq+uvqMDLCjOepsRBSAEhgF64JCAA2ABSEBxTJcMP4Gdao72RwfVZ5ozJ1o9FTd8sl6B4zYAQMAXrUA4AQGelANYqb031fuosyKAG+OByAzAFh6G0BgaWStyxMAABW020Awd++KQr9mRFIZQ9I3pWK4vDJwxKa1WiiPSte+RxJjt-U0YEhkmTzz1UPfyY5sOzmlOuc4x59TXmeOMdqQJ9mQnKU6hNtDgNvE5N2Ok6im+3pWfMrYxTHlasyeGsqUK3W+tgl6dogZwTCMmwtjbJlyr0gMb7cO5QE7Z3LO3xs8IjlRP2OdypvuHxFPzxMyvH5289TAsZetUhu1-J-pgGi1lzDzsEsErMyS9NxGb1xLlUkpnGOPZ0dZdri3Dn5OE4a8TqObmuMaeTmblmluAspOZ-UINWl9Hq+gPRQCTEcKE+QjjteDEsKF919H-XJPyKjGWL3BP55-z55gMxN+kDbUSL8FoaDSoMSZ-kK7+LKjJTYB79epUqWhnp+CqFDnf67swCm0DpUkbo0fc56la7HdlZdtGIc7rAQvCHcG8N4-8pED1lgMAbA+3CAgtvU8+jLycF4IIUQ4wfzbtwki9wPA5uQcStr5LVeIQA-8oAXU7cJEwCr9ICl4EtpBRkmRDB-wEBJRZJtA1gB9gA1hbd9Ee9DEGcTFrdXZ7dGVI9NcudN5K8yZq8o4VN48TcqlhUxcxVjZJcrc08IM5k58d5wpcsNwr818gCq9WURht8uVO0DlIkgA
)

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
