public class MyEdge implements Comparable{
	public String src, dest;
	public int weight;
	
	public MyEdge(String src, String dest, int weight){
		this.src = src;
		this.dest = dest;
		this.weight = weight;
	}

	@Override
	public int compareTo(Object o) {
		MyEdge e = (MyEdge)o;
		if (this.weight > e.weight) return 1;
		else return -1;
		// TODO Auto-generated method stub
	}
}
