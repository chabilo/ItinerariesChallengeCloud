package org.challenge.routesservice.model;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code Node}
 */
public class Node<T> {

    public T data;
    public Integer dist;
    public String route = "";
    public Map<Node<T>, Integer> edges = new HashMap<>();

    public T getData() {
        return data;
    }

    public Node(T d) {
        data = d;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Map<Node<T>, Integer> getEdges() {
        return edges;
    }

    public void setEdges(Map<Node<T>, Integer> edges) {
        this.edges = edges;
    }

}
