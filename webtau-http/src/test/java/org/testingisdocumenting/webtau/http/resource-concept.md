if same url as last time and same response then don't print the steps output information.
as one of the ideas


```
games = http.resource("/game/:gameId");
games = http.resource("/game", "/game/:gameId");

games = http.restResource("/games");

games = http.resource("/game/:gameId", http.header(...))


http.resource("/game/elden-ring").validate((header, body) -> {
});

http.resource("/game/elden-ring").withParams().body.get("title").waitTo(equal())

gamePrice = http.resource("/game/:gameId").body.get("price")
gamePrice.of("elden-ring").waitToBe(lessThan(60))
```

* mirror concept to DB as well

```

eldenRingTitle = http.resource("/game/:gameId").withParams("gameId", "elden-ring").body.get("title")


http.resource("/game/elden-ring").validate((header, body) -> {
    body.get("title").waitTo(equal("title"))
});


games.fetch("elden-ring").get("title").should(equal("Elden Ring Best Game In The World"))
eldenRing = games.withParam("gameId", "eldenRing")
eldenRingResponse = eldenRing.query();

eldenRingResponse.body.get("title").waitTo(equal("blah"))

eldenRingResponse.validate((header, body) -> {
    body.get("title").should(equal("blah"));
});

eldenRing.get("title").should(equal("Elden Ring Best Game In The World"))

updateResponse = eldenRing.update(payload)


games.create(payload)
```
