package crawling;

import java.util.Comparator;

public class NetworkTimeComparator implements Comparator<Network>{

	@Override
	public int compare(Network o1, Network o2) {
		if(o1.getTimestamp().after(o2.getTimestamp()))
			return 1;
		else if (o2.getTimestamp().after(o1.getTimestamp()))
			return -1;
		else
			return 0;
	}


}
