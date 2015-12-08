package isingLikeDiffusion.repast.datasources;

public class ClusteringCoefficientNetworkTopoDS extends NetworkTopoDS {

	@Override
	public Object get(Iterable<?> objs, int size) {
		return super.getClusteringCoefficient();
	}

}
