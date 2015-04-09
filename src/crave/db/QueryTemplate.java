package crave.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryTemplate extends Object {
  
  private HashMap<String, String> args_;
  private Queue<String> query_;
  
  public QueryTemplate(String fileName) throws FileNotFoundException, IOException, Exception
  {
    super();
    this.args_ = new HashMap<String, String>();
    this.query_ = new LinkedList<String>();
    
    String p = "[a-zA-Z0-9]*";
    Pattern pattern = Pattern.compile("(<" + p + ">" + p + "</" + p +">)+");
    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    Matcher m = null;
    boolean matched = true;
    String arg = null;
    for(String line = reader.readLine(); line != null; line = reader.readLine())
    {
//      System.out.println("line: " + line + " length: " + line.length());
      matched = true;
      while(matched)
      {
        m = pattern.matcher(line);
        if(m.find())
        {
          arg = m.group().trim().replaceAll("<" + p + ">", "").replaceAll("</" + p + ">", "");
          if(arg == null || arg.equals(""))
          {
            throw new Exception("UNPREDICTED SYMBOL IN INPUT");
          }
          this.args_.put(arg, "*null*");
          this.query_.add(line.substring(0, m.start()));
          this.query_.add(arg);
//          System.out.println("\tFOUND " + arg + " IN LINE AT START: " + m.start() + " END: " + m.end());
          if(line.length() >= m.end() + 1) { line = line.substring(m.end(), line.length()); }
          else
          { 
            m.replaceFirst(arg);
            matched = false;
          }
//          System.out.println("\tNEW LINE: " + line);
        }
        else
        { 
          matched = false;
          this.query_.add(line);
        }
      }
    }
  }
  
  public Set<String> getArgs() { return this.args_.keySet(); }
  
  public HashMap<String, String> getMap() { return this.args_; }
  
  public Queue<String> getQuery() { return this.query_; }

  public Query makeQuery(Queue<QueryToken> tokens)
  {
//    System.out.println("ARG MAP: " + this.getMap() + "\nquery: " + this.getQuery() + "\ntokens: " + tokens);
    for(QueryToken token : tokens)
    {
//      System.out.println("TOKEN: " + token);
      this.getMap().put(token.getID(), token.getValue());
    }
    return new Query(this.getQuery(), this.getMap());
  }
  
  @Override
  public String toString()
  {
    return "[args: " + this.getArgs() + ", queryQueue: " + this.getQuery() + "]";
  }
  
}