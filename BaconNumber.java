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
      String[] AandM = read.nextLine().split("\\|"); // split the line at the pipe
      String nextActor = AandM[0]; // set index 0 of the array (actor) to variable
      String nextMovie = AandM[1]; // set index 1 of the array (movie) to variable
      if (this.actors.containsKey(nextActor) == false) { // if actor is not in our hashmap yet
        Vertex actorV = new Vertex(nextActor); // make new vertex object with this actor
        this.actors.put(nextActor, actorV); // put the actor in the hashmap with its vertex object as its value
        if (this.movies.containsKey(nextMove)) { // if movie is in our hashmap
          actorV.add(this.movies.get(nextMovie)); // add the movie to the neighbors of this actor vertex
          this.movies.get(nextMovie).add(actorV); // add the actor to the neighbors of the movie vertex as well
        } else { // if movie is not in hashmap
          Vertex movieV = new Vertex(nextMovie); // make new vertex object for this movie
          this.movies.put(nextMovie, movieV); // put into hashmap
          movieV.add(actorV); // add actor vertex as neighbor
          actorV.add(movieV); // add movie vertex as neighbor
        }
      } else if (this.actors.containsKey(nextActor)) { // same thing but for case when actor is already in hashmap
        if (this.movies.containsKey(nextMovie)) {
          this.actors.get(nextActor).add(this.movies.get(nextMovie));
          this.movies.get(nextMovie).add(this.actors.get(nextActor));
        } else {
          Vertex movieV = new Vertex(nextMovie);
          this.movies.put(nextMovie, movieV);
          movieV.add(this.actors.get(nextActor));
          this.actors.get(nextActor).add(movieV);
        }
      }
    }
    read.close();
  }
  
  /**
     * Returns the path of actors to Kevin Bacon.
     * 
     * @param name
     *            Name of the actor being searched for.
     * @return path to Kevin Bacon.
     */
  public String find (String name) {
    StringBuilder sb = new StringBuilder();
    this.actors.get(name).distance = 0;
    this.actors.get(name).isVisited = true;
    Queue<Vertex> nextNeighbor = new ArrayDeque<Vertex>();
    if (name.equals(this.center)) {
      sb.append(this.center);
      restoreIsVisited();
      return sb.toString();
    } else {
      nextNeighbor.add(this.actors.get(name));
      while (nextNeighbor.isEmpty() == false) {
        Vertex next = nextNeighbor.poll();
        ArrayList<Vertex> neighbors;
        if (this.actors.containsValue(next)) {
          neighbors = this.actors.get(next.Name()).Neighbors();
        } else {
          neighbors = this.movies.get(next.Name()).Neighbors();
        }
        for (int n = 0; n < neighbors.size(); n++) {
          if (neighbors.get(n).isVisited == false) {
            neighbors.get(n).previous = next;
            neighbors.get(n).distance = next.distance + 1;
            if (neighbors.get(n).Name().equals(this.center)) {
              Stack<String> previouses = new Stack<String>();
              Vertex previous = neighbors.get(n);
              for (int d = 0; d < neighbors.get(n).distance; d++) {
                previouses.add(previous.previous.Name());
                previous = previous.previous;
              }
              while (previouses.isEmpty() == false) {
                sb.append(previouses.pop());
                sb.append(" --> ");
              }
              sb.append(this.center);
              restoreIsVisited();
              return sb.toString();
            }
            neighbors.get(n).isVisited = true;
            nextNeighbor.add(neighbors.get(n));
          }
        }
      }
    }
    return name + " has a Bacon Number of infinity";
  }
  
  /**
     * Calculates the Kevin Bacon number for the actor.
     * 
     * @param name
     *            Name of actor that Bacon number is being calculated for.
     * @return Bacon number
     */
  public int BNumber (String name) {
    if (this.actors.containsKey(name) == false){
      return -2;
    }
    this.actors.get(name).distance = 0;
    this.actors.get(name).isVisited = true;
    Queue<Vertex> nextNeighbor = new ArrayDeque<Vertex>();
    if (name.equals(this.center)) {
      restoreIsVisited();
      return 0;
    } else {
      nextNeighbor.add(this.actors.get(name));
      while (nextNeighbor.isEmpty() == false) {
        Vertex next = nextNeighbor.poll();
        ArrayList<Vertex> neighbors;
        if (this.actors.containsValue(next)) {
          neighbors = this.actors.get(next.Name()).Neighbors();
        } else {
          neighbors = this.movies.get(next.Name()).Neighbors();
        }
        for (int n = 0; n < neighbors.size(); n++) {
          if (neighbors.get(n).isVisited == false) {
            neighbors.get(n).distance = next.distance + 1;
            if (neighbors.get(n).Name().equals(this.center)) {
              restoreIsVisited();
              return neighbors.get(n).distance / 2;
            }
            neighbors.get(n).isVisited = true;
            nextNeighbor.add(neighbors.get(n));
          }
        }
      }
      restoreIsVisited();
      return -1;
  }
  
  /**
    * Goes through graph and changes all marked visited spots back to false.
    */
  public void restoreIsVisited() {
    Collection<Vertex> actors = this.actors.values();
    Iterator<Vertex> actorsITR = actors.iterator();
    while (actorsITR.hasNext()) {
      actorsITR.next().isVisited = false;
    }

    Collection<Vertex> movies = this.movies.values();
    Iterator<Vertex> moviesITR = movies.iterator();
    while (moviesITR.hasNext()) {
      moviesITR.next().isVisited = false;
    }
  }
    
  /**
   * Changes the center of the game.
   * 
   * @param name
   *            New center of game.
   */
  public void recenter(String name) {
    this.center = name;
  }
  
  public double avgdist() {
    int totalReach = 0;
    int totalBacon = 0;
    int unReachable = 0;
    
    for (String x : this.actors.keySet()) {
      int bnumber = BNumber(x);
      if (bnumber == -1) {
        unReachable++;
      } else {
        totalReach++;
        totalBacon += bnumber;
      }
    }
    
    double avg = (double) totalBacon / totalReach;
    System.out.println(avg + "\t" + this.center + " (" + totalReach + "," + unReachable + ")");
    return avg;
  }
  
  /**
    * Prints a table of the counts of bacon numbers for the given center from 0
    * up to the longest.
    */
  public void table() {
    LinkedList<Integer> list = new LinkedList<Integer>();
    for (String x : this.actors.keySet()) {
      int bnumber = BNumber(x);
      if (list.size() == 0) {
        list.add(bnumber);
      } else {
        list.add(0, bnumber);
      }
    }
    
    int max = 0;
    
    for (int i : list) {
      if (i > max) {
        max = i;
      }
    }
    
    int[] bnList = new int[max + 2];
    
    for (int j : list) {
      if (j == -1) {
        bnList[bnList.length - 1]++;
      }
      bnList[j]++;
    }
    
    System.out.println("Table of distance for " + this.center);
    for (int a = 0; a < bnList.length - 1; a++) {
      System.out.println("Number\t" + a + ":" + "\t\t" + bnList[a]);
    }
    System.out.println("Unreachable:\t\t" + bnList[bnList.length - 1]);
  }
  
  /**
    * Print the n best centers (less avg bacon number is better)
    * 
    * @param n
    *            Number of centers being printed
    */
  public void topcenter(int n) {
    String curCenter = this.center;
    ArrayList<Double> doubles = new ArrayList<Double>();
    HashMap<Double, String> names = new HashMap<Double, String>();
    
    for (String x : this.actors.keySet()) {
      this.center = this.actors.get(x).Name();
      double thisavrg;
      
      int totalReach = 0;
      int totalBacon = 0;
      
      for (String y : this.actors.keySet()) {
        int bnumber = BNumber(y);
        if (bnumber != -1) {
          totalReach++;
          totalBacon += bnumber;
        }
      }
      
      thisavrg = (double) totalBacon / totalReach;
      
      if (doubles.isEmpty()) {
        doubles.add(thisavrg);
        names.put(thisavrg, this.center);
      } else {
        for (int s = 0; s < doubles.size(); s++) {
          if (thisavrg <= doubles.get(s)) {
            doubles.add(s, thisavrg);
            names.put(thisavrg, this.center);
            break;
          }
        }
      }
    }
    for (int i = 0; i < n; i++) {
      System.out.println(doubles.get(i) + "\t" + names.get(doubles.get(i)));
    }
    this.center = curCenter;
  }
  
  /**
    * Finds paths to the center for all actors and actresses in the database.
    */
  public void findall() {
    for (String x : this.actors.keySet()) {
      System.out.print(this.actors.get(x).Name() + ": " + find(this.actors.get(x).Name()));
      System.out.print(" (" + BNumber(this.actors.get(x).Name()) + ")");
      System.out.println(" ");
      System.out.println(" ");
    }
  }
  
  /**
   * List the actor with the most film credits.
   * 
   * @return ArrayList of Vertexes
   */
  public ArrayList<Vertex> most() {
    Vertex actor = null;
    for (String x : this.actors.keySet()) {
      if (actor == null) {
        actor = this.actors.get(x);
      } else {
        if (this.actors.get(x).Neighbors().size() > actor.Neighbors().size()) {
          actor = this.actors.get(x);
        }
      }
    }
    System.out.println("Name: " + actor.Name());
    System.out.println("Movie credits: " + actor.Neighbors().size());
    
    return actor.Neighbors();
  }
  
  /**
   * Print the longest path to the center.
   */
  public void longest() {
    String actor = null;
    for (String x : this.actors.keySet()) {
      if (actor == null) {
        actor = this.actors.get(x).Name();
      } else {
        if (BNumber(this.actors.get(x).Name()) > BNumber(actor)) {
          actor = this.actors.get(x).Name();
        }
      }
    }
    System.out.println(" ");
    System.out.println("Name: " + actor);
    System.out.println(find(actor) + " (" + BNumber(actor) + ")");
  }
  
  /**
   * List all of the movies that the actor has been in.
   * 
   * @param name
   *            actor being searched for.
   */
  public void movies(String name) {
    System.out.println(" ");
    System.out.println("Name: " + name);
    for (int i = 0; i < this.actors.get(name).Neighbors().size(); i++) {
      System.out.println(this.actors.get(name).Neighbors().get(i));
    }
  }
  
  @SuppressWarnings("resource")
  public static void main(String[] args) throws IOException {
    // URLS//
    String small = "http://cs.oberlin.edu/~gr151/imdb/imdb.small.txt";
    String top250 = "http://cs.oberlin.edu/~gr151/imdb/imdb.top250.txt";
    String pre1950 = "http://cs.oberlin.edu/~gr151/imdb/imdb.pre1950.txt";
    String post1950 = "http://cs.oberlin.edu/~gr151/imdb/imdb.post1950.txt";
    String onlytv = "http://cs.oberlin.edu/~gr151/imdb/imdb.only-tv-v.txt";
    String notv = "http://cs.oberlin.edu/~gr151/imdb/imdb.only-tv-v.txt";
    String full = "http://cs.oberlin.edu/~gr151/imdb/imdb.full.txt";

    // CHOOSING A FILE//
    System.out.println("Welcome to the Kevin Bacon Game!");
    System.out.println(" ");
    System.out.println("Please choose a database: ");
    System.out.println(" ");
    System.out.println("small -- a 1817 line file with just a handful of performers (161)");
    System.out.println("top250 -- a 14339 line file listing just the top 250 movies on IMDB");
    System.out.println("pre1950 -- a 966338 line file with movies made before 1950");
    System.out.println("post1950 -- a 6848516 line file with the movies made after 1950");
    System.out.println("only tv -- a 2021636 line file with only made for TV and direct to video movies");
    System.out.println("no tv -- a 5793218 line file without the made for TV and direct to video movies");
    System.out.println("full -- all 7814854 lines of IMDB for you to search through");
    boolean noSuchFile = true;
    String url = null;
    while (noSuchFile) {
      System.out.println(" ");
      System.out.print("Enter database name: ");
      Scanner link = new Scanner(System.in);
      String urlInput = link.nextLine();

      if (urlInput.equals("small")) {
        url = small;
        noSuchFile = false;
      } else if (urlInput.equals("top250")) {
        url = top250;
        noSuchFile = false;
      } else if (urlInput.equals("pre1950")) {
        url = pre1950;
        noSuchFile = false;
      } else if (urlInput.equals("post1950")) {
        url = post1950;
        noSuchFile = false;
      } else if (urlInput.equals("only tv")) {
        url = onlytv;
        noSuchFile = false;
      } else if (urlInput.equals("no tv")) {
        url = notv;
        noSuchFile = false;
      } else if (urlInput.equals("full")) {
        url = full;
        noSuchFile = false;
      } else {
        System.out.println("Sorry, this database does not exist.");
      }
    }
    
    // CREATING CLASS INSTANCE//
    BaconNumber bn;
    if (url == pre1950) { // Chooses a random actor as center since Kevin Bacon is not in database
      bn = new BaconNumber(url, "none");
      Random rand = new Random();
      int randint = rand.nextInt((bn.actors.size()));
      Set<String> actors = bn.actors.keySet();
      Iterator<String> actorsITR = actors.iterator();

      String randCenter = null;
      for (int i = 0; i < randint; i++) {
        randCenter = actorsITR.next();
      }
      bn.center = randCenter;
    } else {
      bn = new BaconNumber(url, "Kevin Bacon (I)"); // For all other files, Kevin Bacon is the default center
    }
    
    boolean keepRun = true; // boolean to keep the game running
    while (keepRun) {
      // CHOOSING A COMMAND//
      System.out.println(" ");
      System.out.println("Your center is currently: " + bn.center);
      System.out.println("Here are your command options: ");
      System.out.println(" ");
      System.out.println("1. bacon -- calculate the bacon number of an actor, the average distance for "
                            + "current center, and the table of distance for current center");
      System.out.println("2. recenter -- change the center of the game");
      System.out.println("4. topcenter -- find the top n centers by average distance");
      System.out.println("5. findall -- find the bacon number of every actor in the database");
      System.out.println("6. most -- find the actor with the most film credits");
      System.out.println("7. longest -- prints out one of the longest paths to the center");
      System.out.println("8. movies -- list all the movies a given actor has played in");
      System.out.println("9. exit -- exit the game");
      
      System.out.println(" ");
      System.out.print("Enter command: ");
      Scanner input = new Scanner(System.in);
      String command = input.nextLine();
      
      // POSSIBLE COMMAND OUTPUTS//
      if (command.equals("recenter")) {
        System.out.print("New center: ");
        Scanner input2 = new Scanner(System.in);
        String center = input2.nextLine();
        bn.recenter(center);
      } else if (command.equals("findall")) {
        System.out.println(" ");
        bn.findall();
      } else if (command.equals("most")) {
        System.out.println(" ");
        ArrayList<Vertex> movies = bn.most();
        System.out.println(" ");
        System.out.print("Would you like to print all the movies this actor has been in? (yes/no) ");
        Scanner yes = new Scanner(System.in);
        if (yes.nextLine().equals("yes")) {
          System.out.println(movies);
        }
      } else if (command.equals("longest")) {
        System.out.println(" ");
        bn.longest();
      } else if (command.equals("movies")) {
        System.out.println(" ");
        System.out.print("Enter actor name: ");
        Scanner name = new Scanner(System.in);
        String nameactor = name.nextLine();
        if (bn.BNumber(nameactor) == -2) {
          System.out.println("This actor doesn't exist in the database!");
      } else {
        bn.movies(nameactor);
      }
    } else if (command.equals("topcenter")) {
      System.out.print("Calculate this many topcenters: ");
      Scanner input4 = new Scanner(System.in);
      int topn = input4.nextInt();
      bn.topcenter(topn);
}
