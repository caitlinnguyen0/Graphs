import java.util.*;

public class Graph {
    private int vertices;
    private int edges;
    //private boolean isParametersUpperBounds;
    private ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        //example of graph: {{v1, v2, edgeWeight}, {v1, v3, edgeWeight2}, {v4, v5}}
    private ArrayList<ArrayList<Integer>> connectedVertices = new ArrayList<>();

    public Graph() {}

    public Graph(int vertices, int edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    private int getRandomVertex(int v) {
        return (int) (1 + Math.random() * v);
    }

    private boolean toContinue() {
        double random = Math.random();
        return !(random < 0.5);
    }

    //generates a random weight for an edge between 1 and 20
    private int generateEdgeWeight() {
        return (int) (1 + Math.random() * 20);
    }

    /*
    Function generateConnectedGraph() creates a random connected graph using vertice and edge information if available.
    We start at a random vertex and draw an edge to another random vertex.
    The program then randomly decides whether to continue drawing edges stemming from this vertex or to move on to a different vertex.
    If it decides to stay, then a new edge is drawn stemming from the same vertex.
    If it decides to continue, our new vertex of focus will be the most recent vertex we drew an edge to.
    This cycle of drawing random edges and moving on/staying repeats for the specified # edges.
     */
    public void generateConnectedGraph() {
        int v = vertices;
        int e = edges;
        int curr_v;
        int target_v;
        int currEdgeWeight;
        //usedVertices are relevant only to the specific curr_v we are stationed on. Will be reset every time we move to the next v.
        ArrayList<Integer> usedVertices = new ArrayList<>();

        ArrayList<Integer> completedVertices = new ArrayList<>();

        //Minimum number of edges in a connected graph is # vertices - 1.
        if (vertices - 1 > edges) {
            System.out.println("making a connected graph with these parameters is not possible.");
            return;
        }

        //if vertices and edges aren't already set by the user, generate random v (between 1 and 15) and e values
        //else, set v and e to predetermined vertices and edges
        if (vertices + edges == 0) {
            //v set to be in-between 3 and 23
            v = 3 + (int) (Math.random() * 20);

            //calculates the maximum number of edges a graph with v vertices can have
            int maxEdges = 1;
            for (int j = v - 1; j >= 1; j--) {
                maxEdges = maxEdges + j;
            }

            //e set to be in between a minimum connected graph and a complete graph
            e = v - 1 + (int) (Math.random() * (maxEdges - (v-1)));
            System.out.println("Random v = " + v + " and e = " + e + " generated.");
        }

        //start at a random vertex and immediately add that vertex to used vertex list
        curr_v = getRandomVertex(v);
        usedVertices.add(curr_v);
        //repeat adding edges for as many e's we have set
        for (int i = 1; i <= e; i++) {
            System.out.println("Finding edge " + i + ".");
            //This while loop draws an edge connecting a vertex to another random vertex, ensuring that no repeats occur along the way
            while(true) {
                //randomly choose a vertex to connect curr_v to, making sure that there doesn't already exist an edge between the two
                target_v = getRandomVertex(v);
                System.out.println("New target vertex generated.");
                while (usedVertices.contains(target_v)) {
                    target_v = getRandomVertex(v);
                    System.out.println("Target vertex changed due to repetition. We are on e: " + i);
                    System.out.println("New target vertex chosen: " + target_v);
                }
                usedVertices.add(target_v);
                System.out.println("Used target vertices: " + usedVertices);

                //creates a list for adjacent vertex data {v1, v2, edge_weight} and checks if these connected vertices already exists in the graph, modifying if need be.
                currEdgeWeight = generateEdgeWeight();
                ArrayList<Integer> newAdjacentVertex = new ArrayList<>();
                newAdjacentVertex.add(curr_v);
                newAdjacentVertex.add(target_v);
                //If edge doesn't exist yet, add both instances of these connected vertices (ex. 3,2 and 2,3) to the connectedVertices bank list.
                //Then add the connected vertices plus their edge weight to the graph.
                if (!connectedVertices.contains(newAdjacentVertex)) {
                    connectedVertices.add(newAdjacentVertex);
                    ArrayList<Integer> reversedNewAdjacentVertex = new ArrayList<>();
                    reversedNewAdjacentVertex.add(target_v);
                    reversedNewAdjacentVertex.add(curr_v);
                    connectedVertices.add(reversedNewAdjacentVertex);
                    System.out.println("New adjacent vertex: " + newAdjacentVertex);
                    System.out.println("Current Connected Vertices: " + connectedVertices);
                    System.out.println("New adjacent vertex successfully added.\n");
                    ArrayList<Integer> newSubgraph = new ArrayList<>();
                    newSubgraph.add(curr_v);
                    newSubgraph.add(target_v);
                    newSubgraph.add(currEdgeWeight);
                    graph.add(newSubgraph);
                    break;
                }
                //Catches edge case where the only other vertex that curr_v can go to is already taken.
                //The code then randomly chooses another vertex to focus on.
                if (connectedVertices.contains(newAdjacentVertex) && usedVertices.size() == v) {
                    completedVertices.add(curr_v);
                    curr_v = getRandomVertex(v);
                    while (completedVertices.contains(curr_v)) {
                        curr_v = getRandomVertex(v);
                        System.out.println("These are the completed vertices: " + completedVertices+ "Jumping to new vertex: " + curr_v + ". Total e: " + e + ". Currently on e: " + e + ". Total v: " + v);
                    }
                    System.out.println("These are the completed vertices: " + completedVertices+ "Jumping to new vertex: " + curr_v);
                    usedVertices.clear();
                    usedVertices.add(curr_v);
                }

                System.out.println("Exact edge already in graph. New edge must be generated.\n");
            }

            //We now randomly decide to continue drawing edges from this vertex or move on to the next vertex.
            //If we continue, set our current vertex to the most recent one we connected an edge to and clear usedVertices.
            //If we don't continue, everything remains the same.
            if (toContinue() || usedVertices.size() == v) {
                if (usedVertices.size() == v)
                    System.out.println("Vertex " + curr_v + " connects to all other vertices!");
                curr_v = target_v;
                usedVertices.clear();
                System.out.println("unusedVertices cleared. \n");
                usedVertices.add(curr_v);
            }
        }
    System.out.println("Connected graph generated.");
        System.out.println("Graph contains " + v + " vertices and " + e + " edges.\n");
    }

    public void printGraph() {
        int totalVertices = 3;
        for (ArrayList<Integer> adjacentVertex : graph) {
            System.out.println(adjacentVertex.get(0) + " connected to " + adjacentVertex.get(1) + ". Edge weight is " + adjacentVertex.get(2));
            //checks if value of current vertex is greater than totalVertices and updates totalVertices if true.
            //We end up with the highest vertice value, which is the total number of vertices.
            if (adjacentVertex.get(0) > totalVertices) {totalVertices = adjacentVertex.get(0);}
            if (adjacentVertex.get(1) > totalVertices) {totalVertices = adjacentVertex.get(1);}
        }
        System.out.println("Summary: Graph has " + totalVertices + " vertices and " + graph.size() + " edges.");
    }


}
