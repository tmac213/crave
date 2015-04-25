import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class QueryManager {
  
  private QueryTemplate template_;//HashSet<QueryTemplate> templates_;
  private HashSet<String> rules_;
  
  public QueryManager()
  {
    super();
    this.template_ = null;//new HashMap<Integer, QueryTemplate>();
    this.rules_ = new HashSet<String>();
  }
  
  private QueryTemplate getTemplate()
  {
    return this.template_;
  }
  
  public HashSet<String> getRules() { return this.rules_; }
  
  public boolean addTemplate(QueryTemplate template)
  {
    if(this.getTemplate() == null)
    {
      this.template_ = template;
      this.getRules().addAll(template.getArgs());
      return true;
    }
    return false;
  }
  
  public Query produceQuery(String arg)
  {
//    System.out.println("QUERYMANAGER\tTEMPLATE: " + this.getTemplate() + " rules: " + this.getRules());
    return this.getTemplate().makeQuery(this.tokenize(arg));
  }
  
  public Queue<QueryToken> tokenize(String arg)
  {
    Queue<QueryToken> result = new LinkedList<QueryToken>();
    
    String sentence[] = arg.split("\\s+");
    QueryToken token = null;
    
    StringBuilder b = new StringBuilder();
    for(int i = 0; i < sentence.length; i++)
    {
//      System.out.println("TOKEN: " + sentence[i]);
//      if((token == null && this.getRules().contains(sentence[i]))
//           || (token != null && this.getRules().contains(sentence[i])))
//      {
//        System.out.println("\tTOKEN IS IDENTIFIER");
      if(token == null && this.getRules().contains(sentence[i]))
      {
        token = new QueryToken(sentence[i]);
      }
      else if(token != null && this.getRules().contains(sentence[i]))
      {
//        System.out.println("\tBUILDER: " + b.toString());
        if(b.toString().length() > 0) { token.add(b.toString().trim()); }
        result.add(token);
//        System.out.println("\t\tTOKEN: " + token);
        token = new QueryToken(sentence[i]);
        b = new StringBuilder();
      }
      else if(token == null && !this.getRules().contains(sentence[i]))
      {
        System.err.println("EXPECTED IDENTIFIER BEFORE: " + sentence[i]);
        return null;
      }
      else if(i == sentence.length - 1)
      {
        b.append(" " + sentence[i]);
        if(token != null) { token.add(b.toString().trim()); }
        result.add(token);
      }
      else
      {
//        System.out.println("\tTOKEN IS VALUE");
        b.append(" " + sentence[i]);
//        token.add(sentence[i]);
//        result.add(token);
//        token = null;
      }
    }
    return result;
  }
  
  public void flush()
  {
    for(String arg : this.getTemplate().getMap().keySet())
    {
      this.getTemplate().getMap().put(arg, "*null*");
    }
  }
  
}
