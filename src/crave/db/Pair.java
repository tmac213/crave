public class Pair<T,V> extends Object {
	
	private T val1_;
	private V val2_;
	
	public Pair(T val1, V val2)
	{
		this.val1_ = val1;
		this.val2_ = val2;
	}
	
	public T getVal1() { return this.val1_; }
	
	public void setVal1(T val1) { this.val1_ = val1; }
	
	public V getVal2() { return this.val2_; }
	
	public void setVal2(V val2) { this.val2_ = val2; }
	
	@Override
	public String toString()
	{
		return "(" + this.getVal1() + ", " + this.getVal2() + ")";
	}
	
}