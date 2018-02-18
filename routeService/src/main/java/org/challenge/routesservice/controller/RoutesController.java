package org.challenge.routesservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.challenge.routesservice.model.Node;
import org.challenge.routesservice.model.Response;
import org.challenge.routesservice.model.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RefreshScope
@RequestMapping(path = "/routes", produces = {APPLICATION_JSON_UTF8_VALUE})
public class RoutesController {

    private static final Logger log = LoggerFactory.getLogger(RoutesController.class);
    
    @Value("${service.name}")
    private String name;
    @Value("${port}")
    private String port;
    @Value("${server}")
    private String server;

    /**
     * Search routes with the given origin city.
     * <p>
     * This method is idempotent.
     *
     * @param city The city to look for the different routes.
     * @return HTTP 200 if the are routes or HTTP 404 otherwise.
     */
//	@PreAuthorize("#oauth2.hasAnyScope('read','write','read-write')")
    @RequestMapping(method = GET, value = "/{city}")
    public List<Response> getRoutes(@PathVariable @NotNull String city) {
        List<Response> response = new ArrayList<Response>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            JsonNode itineraries = restTemplate.getForObject(this.server + ":" + this.port + "/" + this.name, JsonNode.class);
            if (itineraries != null && itineraries.size() > 0) {

                ObjectMapper mapper = new ObjectMapper();
                List<Route> itinerariesList = null;

                itinerariesList = mapper.readValue(
                        mapper.treeAsTokens(itineraries),
                        new TypeReference<List<Route>>() {
                });

                Map<String, Node<String>> nodes = makeGraph(itinerariesList);

                if (nodes.get(city) != null) {

                    dijkstra(nodes.get(city));

                    response = nodes.values().stream().filter(p -> !city.equalsIgnoreCase(p.getData()))
                            .map(p -> new Response(p.getData(), p.getRoute(), p.getDist()))
                            .collect(Collectors.toList());

                    return response;
                }
            }
        } catch (IOException e) {
            log.error("Error in databinding", e);
        }
        return null;
    }

    private Map<String, Node<String>> makeGraph(List<Route> routes) {
        Map<String, Node<String>> graph = new LinkedHashMap<>();
        routes.forEach((edge) -> {
            Node<String> a = graph.computeIfAbsent(edge.getOriginCity(), (k) -> new Node<>(edge.getOriginCity())), b = graph.computeIfAbsent(edge.getDestinyCity(), (k) -> new Node<>(edge.getDestinyCity()));
            b.edges.compute(a, (ak, av) -> a.edges.compute(b, (bk, bv) -> edge.getTime()));
        });
        return graph;
    }

    private <T> void dijkstra(Node<T> start) {
        UnaryOperator<Integer> nulInf = (value) -> value == null ? Integer.MAX_VALUE : value;
        PriorityQueue<Node<T>> priorityQueue = new PriorityQueue<>((cityA, cityB) -> nulInf.apply(cityA.dist).compareTo(nulInf.apply(cityB.dist)));
        start.dist = 0;
        priorityQueue.offer(start);
        while (priorityQueue.size() > 0) {
            Node<T> node = priorityQueue.poll();
            node.edges.forEach((neighbour, distance) -> {
                if (node.dist + distance < nulInf.apply(neighbour.dist)) {
                    neighbour.dist = node.dist + distance;
                    neighbour.route = node.route + " -> " + neighbour.data;
                    priorityQueue.add(neighbour);
                }
            });
        }
    }
    
    @RequestMapping(path="/service", method = RequestMethod.GET, 
      produces = MediaType.TEXT_PLAIN_VALUE)
    public String getMessage() {

        return this.server + ":" + this.port + "/" + this.name;
    }
}
