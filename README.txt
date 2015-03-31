Test program to use websockets for something a little more interesting than a chat application.

Web page contains a google map. a connected street view panorama and a chat comms channel.
So the lead can move around the map and panorama the follow side should automatically synchronise.
Chat channel allows discussion of what is being looked at.

Server side just receives websocket messages and broadcasts them.

inspiration taken from:
http://jansipke.nl/websocket-tutorial-with-java-server-jetty-and-javascript-client/
http://www.javacirecep.com/web/java-a-simple-websocket-example/
http://java.dzone.com/articles/creating-websocket-chat
https://developers.google.com/maps/documentation/javascript/examples/streetview-events
https://developers.google.com/maps/documentation/javascript/examples/streetview-simple

TODO:
currently using localhost because I do not have a public ip to run jetty on - convert when possible
sync initial state
handle websocket timeouts
handle panorama pov zoom
build from maven - pull jetty in from maven
build as war?
single html file that can be used as lead and follow (currently set in a global var)