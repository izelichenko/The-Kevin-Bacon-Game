import java.util.ArrayList;

/**
 * Vertex.java Implementation of a class called "Vertex" which contains data and
 * an ArrayList of neighboring vertices according to a database
 * 
 * @author Isabella Zelichenko and Alan Wang
 * 
 */
 
public class Vertex {
  /**
   * contains the name of the actor or movie of this vertex
   */
  private String data;

  /**
   * contains a list of neighboring vertexes (Actor = Movie Vertex Neighbors; Movie = Actor Vertex Neighbors)
   */
  private ArrayList<Vertex> neighbors;

  /** The distance of this Vertex to the center of the Kevin Bacon game. */
  public int distance;

  /** Whether this Vertex has been visited in the Kevin Bacon game or not. */
  public boolean isVisited = false;

  /** The Vertex that lead to this one */
  public Vertex previous;

  /**
   * Constructor which takes in the data type of this vertex (actor name or
   * movie title) and set it to this.data. Also initiliazes new ArrayList
   * 
   * @param data
   */
  public Vertex(String data) {
    this.data = data;
    this.neighbors = new ArrayList<Vertex>();
  }

  /**
   * Returns a list of all of the neighbors of the Vertex.
   * 
   * @return List of neighbors.
   */
  public ArrayList<Vertex> Neighbors() {
    return this.neighbors;
  }
  
  /**
   * The name of the Vertex.
   * 
   * @return movie or actor name.
   */
  public String Name() {
    return this.data;
  }

  /**
   * Adds neighbor movie or actor to list neighbors
   * 
   * @param movie
   */
  public void add(Vertex neighbor) {
    this.neighbors.add(neighbor);
  }

  /**
   * Returns the name of the Vertex.
   * 
   * @return movie or actor name.
   */
  public String toString() {
    return this.data;
  }
}
