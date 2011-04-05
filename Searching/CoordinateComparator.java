package Searching;

import java.util.Comparator;

import Frame.Coordinate;

public class CoordinateComparator implements Comparator<Coordinate> {

	@Override
	public int compare(Coordinate fst, Coordinate sec) {
		if (fst.getPriority() > sec.getPriority())
			return 1;
		else if(fst.getPriority() == sec.getPriority())
			return 0;
		else
			return -1;
	}
}
