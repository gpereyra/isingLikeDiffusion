package isingLikeDiffusion.repast.datasources;


public class AverageDistanceNetworkTopoDS extends NetworkTopoDS {

	@Override
	public Object get(Iterable<?> objs, int size) {
		return super.getAverageDistance();
	}

}
