# Pizza Factory : Conflation 'merge' example app

## The Pizza Factory

This is an example application that processes PriceChange events, for a virtual Pizza Factory company that would have to compute the prices of its Pizzas based on the ingredients prices change in real time.
There are 3 possible PriceChange events, for the ingredient of the Pizza :
 - Tomato
 - Mushroom
 - Flour
Each event also has a 'providerId' to represent its provider.

The application uses the Akka framework, 3 actors are involved in the virtual process :
 - SourceActor : virtually is the source of events and actual randomly generates them for a range of 'providerId's
 - ConflationActor : will implement the conflation logic
 - ProcessActor : virtually processes the events, and actually just log them
 
## Monitoring

Monitoring with Kamon is used to measure the efficiency of the Conflation in the ConflationActor.
A statsd + Graphite + Grafana stack can be used to visualize the data, a docker-compose is provided by Kamon at https://github.com/kamon-io/docker-grafana-graphite.
The following metrics are produced :
 - in (counter) : events received by the ConflationActor
 - out (counter) : events (merged) sent by the ConflationActor
 - merged (histogram) : size of the buffered events for a Provider, each time the 'merge' function is applied
 - buffer (histogram) : size of the conflation buffer, that contains an entry for each Provider, when it is checked for expired entries
 
## Branches

There are 2 steps to introduce the Conflation, and for each before and after steps to highlight the implementation in the ConflationActor.
 - master : Initial PizzaFactory application without conflation (the ConflationActor just fowards the events)
 - step-1-merge-conflation : We introduce the Merged event, and an interface with the methods used to implement the Conflation
 - solution-1-merge-conflation : Implementation done in the ConflationActor
 - step-2-monitoring : We introduce Kamon and the Kamon recorder for our Conflation implementation
 - solution-2-monitoring : The measures are collected in the ConflationActor
