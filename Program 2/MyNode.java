import java.util.Comparator;

class MyNode implements Comparable
{
    public int node;
    public int cost;
 
    public MyNode()
    {
    }
 
    public MyNode(int node, int cost)
    {
        this.node = node;
        this.cost = cost;
    }
 

    public int compare(MyNode node1, MyNode node2)
    {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
    }

	@Override
	public int compareTo(Object o) {
		MyNode node1 = (MyNode) o;
		if (node1.cost < this.cost) return 1;
		return -1;
	}
}
