# hotelsearch


RATE LIMIT SERVICE FOR HOTEL SEARCH SERVICE

This is a Spring boot application.

For running this application run : mvn spring-boot:run

Server will listen at port no 3000 as configured in src/main/resources/application.properties

API Access:
curl -i -H "api-key:6527565b573e682c6233553d325b7036497b2824645658287b793a2a79" "http://localhost:3000/search?city=Bangkok&sort=DESC"

New keys can be added to APIKeys.store and there corresponding window size and request threshold can be added to keys.properties.
