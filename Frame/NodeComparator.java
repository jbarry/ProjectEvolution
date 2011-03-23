package Frame;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
	@Override 
	public int compare(Node fst, Node sec) {
		if (fst.getPriority() > sec.getPriority())
			return 1;
		else if(fst.getPriority() == sec.getPriority())
			return 0;
		else
			return -1;
	}
}
