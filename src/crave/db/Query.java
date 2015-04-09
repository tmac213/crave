import java.util.HashMap;
import java.util.Queue;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.StringReader;

public class Query extends Object {
  
  private String query_;
  
  public Query(Queue<String> query, HashMap<String, String> args)
  {
    StringBuilder b = new StringBuilder();
    //b.append(query.poll());
    boolean addPercent = false;
    String needPercentCheck = null;
    
    Iterator<String> iterator = query.iterator();
    for(String line = iterator.next(); ; line = iterator.next())
    {
//      System.out.println("line: [" + line + "]");
      if(args.containsKey(line))
      {
//        System.out.println("\tFOUND ARGUMENT: [" + line + "]\tREPLACING WITH: " + args.get(line));
        needPercentCheck = args.get(line).toLowerCase();
        if(needPercentCheck.equals("is like") || needPercentCheck.equals("is not like")) { addPercent = true; }
        b.append(args.get(line));
      }
      else if(line.equals(" ") || line.equals(")")) { b.append(line); }
      else
      {
//        System.out.println("\tDID NOT FIND ARGUMENT");
        if(line.contains(".name"))
        {
          if(addPercent)
          {
            b.append(this.addPercentSigns(line));
            addPercent = false;
          }
          else { b.append(line); }
        }
        else { b.append("\n" + line); }
      }
      if(!iterator.hasNext())
      {
        this.query_ = b.toString().trim();
        this.scrub(this.toString());
        return;
      }
    }
  }
  
  @Override
  public String toString()
  {
    return this.query_;
  }
  
  public String getQuery()
  {
    return this.toString().replaceAll("\\s+", " ").replaceAll("\n", "").trim();
  }
  
  private String addPercentSigns(String toAddTo)
  {
    System.out.println("TO ADD TO: [" + toAddTo + "]");
    Matcher m = Pattern.compile("([a-zA-Z0-9|.]+)").matcher(toAddTo);
    if(m.find())
    {
      StringBuilder b = new StringBuilder();
      b.append(toAddTo.substring(0, m.start()));
      b.append("%" + m.group().trim() + "%");
      System.out.println("builder: " + b.toString());
      if(toAddTo.length() > m.end()) { b.append(toAddTo.substring(m.end(), toAddTo.length())); }
      return b.toString();
    }
    return toAddTo;
  }
  
  public void scrub(String query)
  {
    try
    {
      StringBuilder b = new StringBuilder();
      String[] brokenQuery = query.split("\n");
      for(int i = 0; i < brokenQuery.length; i++)
      {
        if(!brokenQuery[i].contains("*null*"))
        {
          b.append(brokenQuery[i]);
          if(i < brokenQuery.length - 1) { b.append("\n"); }
        }
      }
      this.query_ = b.toString();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      this.query_ = query;
    }
  }
}