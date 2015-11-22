import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class BaconNumber {
  /** The center from which the bacon number is calculated */
  private String center;
  /** HashMap of actor names and their corresponding vertices */
  private HashMap<String, Vertex> actors;
  /** HashMap of movie titles and their corresponding vertices */
  private HashMap<String, Vertex> movies;
  
  /**
   * Constructs a Kevin Bacon game.
   * 
   * @param url
   *            the database of actors and movies.
   * @param name
   *            the name that is the center of the game.
   * @throws IOException
   */
  public BaconNumber(String url, String name) throws IOException {
    this.center = name;
    this.actors = new HashMap<String, Vertex>();
    this.movies = new HashMap<String, Vertex>();
    
    Scanner read = new Scanner(new URL(url).openStream());
    
    while (read.hasNextLine()) { // while next line exists
      String[] AandM = read.nextLine().split("\\|");
