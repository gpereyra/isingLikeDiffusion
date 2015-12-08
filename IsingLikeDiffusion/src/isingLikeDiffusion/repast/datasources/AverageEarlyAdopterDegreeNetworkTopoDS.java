package isingLikeDiffusion.repast.datasources;

public class AverageEarlyAdopterDegreeNetworkTopoDS extends NetworkTopoDS {

	@Override
	public Object get(Iterable<?> objs, int size) {
		return super.getAverageEarlyAdopterDegree();
	}

}
