
```
sbt run
```

```
curl -H "correlationID: 12345678" localhost:9090/init
{"correlationID":"94da2874-956e-4f3a-acb6-850df37adbb9","message":"Hi, How can I help you?"}
```

coffee intent
-------------

```
curl -v -H "x-user: prayagupd" -H "x-client-version: 1.0" -d '{"correlationID": "9c327ed6-05ad-4df6-beab-875c33906aab", "message": "coffee near me"}' localhost:9090/chat
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

```
curl -H "x-user: prayagupd" -H "x-client-version: 1.0" -d '{"correlationID": "9c327ed6-05ad-4df6-beab-875c33906aab", "message": "can i know about renters insurance"}' localhost:9090/chat
{"correlationID":"9c327ed6-05ad-4df6-beab-875c33906aab","displayText":"Did not understand you"}
```


Perf
----

