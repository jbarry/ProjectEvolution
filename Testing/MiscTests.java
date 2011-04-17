package Testing;

import java.util.LinkedList;
import java.util.List;

public class MiscTests {
	public static void main(String[] args) {
		List<Integer> intList = new LinkedList<Integer>();
		System.out.println(intList.size());
		((LinkedList<Integer>) intList).removeFirst();
	}
}
