


Download the excellent [RSocket Client CLI](https://github.com/making/rsc) by 
[Toshiaki Maki](https://github.com/making) into the rsocket-server folder. 
There is an official RSocket CLI [elsewhere](https://github.com/rsocket/rsocket-cli), but Toshiaki’s is a little easier to use. 

In the terminal, download the JAR file as follows:


```bash
cd rsocket-server
wget -O rsc.jar https://github.com/making/rsc/releases/download/0.4.2/rsc-0.4.2.jar
```


## Running a sample request-response:

```bash
java -jar rsc.jar --debug --request --data "{\"origin\":\"Client\",\"interaction\":\"Request\"}" --route request-response tcp://localhost:7000
```

Output:

```bash
2020-03-08 17:53:54.071 DEBUG --- [actor-tcp-nio-1] i.r.FrameLogger : sending -> 
Frame => Stream ID: 1 Type: REQUEST_RESPONSE Flags: 0b100000000 Length: 69
Metadata:
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 10 72 65 71 75 65 73 74 2d 72 65 73 70 6f 6e 73 |.request-respons|
|00000010| 65                                              |e               |
+--------+-------------------------------------------------+----------------+
Data:
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 7b 22 6f 72 69 67 69 6e 22 3a 22 43 6c 69 65 6e |{"origin":"Clien|
|00000010| 74 22 2c 22 69 6e 74 65 72 61 63 74 69 6f 6e 22 |t","interaction"|
|00000020| 3a 22 52 65 71 75 65 73 74 22 7d                |:"Request"}     |
+--------+-------------------------------------------------+----------------+
2020-03-08 17:53:54.212 DEBUG --- [actor-tcp-nio-1] i.r.FrameLogger : receiving -> 
Frame => Stream ID: 1 Type: NEXT_COMPLETE Flags: 0b1100000 Length: 81
Data:
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 7b 22 6f 72 69 67 69 6e 22 3a 22 53 65 72 76 65 |{"origin":"Serve|
|00000010| 72 22 2c 22 69 6e 74 65 72 61 63 74 69 6f 6e 22 |r","interaction"|
|00000020| 3a 22 52 65 73 70 6f 6e 73 65 22 2c 22 69 6e 64 |:"Response","ind|
|00000030| 65 78 22 3a 30 2c 22 63 72 65 61 74 65 64 22 3a |ex":0,"created":|
|00000040| 31 35 38 33 36 38 32 38 33 34 7d                |1583682834}     |
+--------+-------------------------------------------------+----------------+
{"origin":"Server","interaction":"Response","index":0,"created":1583682834}
```

You’ll notice that the command has an RSocket message route declared (which is achieved by adding the `--route` option and specifying a name for the route). 
In this case, the route is `request-response` which matches the @MessageMapping declared in the request-response handler method in the `RSocketController.kt`.

When the command runs, you will see some debug information in the terminal window explaining what happened during the request-response interaction.

The debug output you see is split into three ‘message frames’. 
The first message frame is labeled Metadata. In this case, it shows the routing metadata (request-response) being sent to the server. 

The second frame shows the Data message that the client is sending to the server (a JSON string). 

The third frame shows the server’s response message back to the client (also a JSON string).

On the very last line, you can see the JSON formatted response from the server printed in isolation, 
confirming that our command message was successfully received and acknowledged by the server:

```json
{"origin":"Server","interaction":"Response","index":0,"created":1582802421}
```
