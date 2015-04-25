package crave.db;

public class QueryToken extends Object {
  
  private String id_, value_;
  
  public QueryToken(String id, String value)
  {
    super();
    this.id_ = id;
    this.value_ = value;
  }
  
  public QueryToken(String id)
  {
    super();
    this.id_ = id;
    this.value_ = null;
  }
  
  public String getID() { return this.id_; }
  
  public String getValue() { return this.value_; }
  
  public boolean add(String value)
  {
    if(this.getValue() != null) { return false; }
    this.value_ = value;
    return true;
  }
  
  @Override
  public int hashCode() 
  {
    if(this.getValue() == null) { return this.getID().hashCode(); }
    return this.getID().hashCode() * this.getID().hashCode()
      + this.getValue().hashCode();
  }
  
  @Override
  public String toString()
  {
    return "(" + this.getID() + ", " + this.getValue() + ")";
  }
  
}
