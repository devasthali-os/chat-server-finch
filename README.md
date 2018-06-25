chat-server
------------

- API
- Schema

```bash
sbt clean assembly
```

run locally

```bash
java -jar chatServerApi/target/scala-2.12/chatServerApi.jar
Apr 26, 2018 11:33:39 PM com.twitter.finagle.Init$ $anonfun$once$1
INFO: Finagle version 18.3.0 (rev=87424ce8cee3075f9140082a0f91b4a3256a1f50) built at 20180306-113908
Apr 26, 2018 11:33:39 PM com.twitter.finagle.util.DefaultTimer$ <init>
WARNING: Can not service-load a timer. Using JavaTimer instead.

```

run using docker
----------------

```bash
sbt assembly; docker build -t chat-server .

docker run -e environment=stage -p 9090:9090 --name chat-server chat-server
```

```
curl -H "correlationID: 12345678" localhost:9090/init
{"correlationID":"94da2874-956e-4f3a-acb6-850df37adbb9","message":"Hi, How can I help you?"}
```

coffee intent
-------------

```bash
curl -v -H "x-user: prayagupd" -H "x-client-version: 1.0" -d '{"correlationID": "9c327ed6-05ad-4df6-beab-875c33906aab", "message": "coffee near me"}' localhost:9090/api/chat
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 9090 (#0)
> POST /chat HTTP/1.1
> Host: localhost:9090
> User-Agent: curl/7.54.0
> Accept: */*
> x-user: prayagupd
> x-client-version: 1.0
> Content-Length: 86
> Content-Type: application/x-www-form-urlencoded
>
* upload completely sent off: 86 out of 86 bytes
< HTTP/1.1 200 OK
< Date: Fri, 27 Apr 2018 05:41:15 GMT
< Server: Finch
< Content-Length: 94
< Content-Type: application/json
<
* Connection #0 to host localhost left intact
{"correlationID":"9c327ed6-05ad-4df6-beab-875c33906aab","displayText":"Here are coffee shops"}
```

un-recognised intent
--------------------

```bash
curl -H "x-user: prayagupd" -H "x-client-version: 1.0" -d '{"correlationID": "9c327ed6-05ad-4df6-beab-875c33906aab", "message": "can i know about renters insurance"}' localhost:9090/chat
{"correlationID":"9c327ed6-05ad-4df6-beab-875c33906aab","displayText":"Did not understand you"}
```

```
curl -v --request GET localhost:9090/chat/history?abc=1
```

Perf
----


TODOs
-----

1) pass in app properties like `-environment=dev` jar running inside docker

2) version docker images

~~2) add typesafe config to read intent-names~~

~~3) expose client-api as a `chat-server-client.jar` (to maven not ivy), make sure it can be used as sbt deps - https://github.com/duwamish-os/chat-server-api-client~~

4) provide http-client

5) logging

6) **add swagger**

7) Websocket - https://github.com/finagle/finagle-websocket

8) don't create jar for root