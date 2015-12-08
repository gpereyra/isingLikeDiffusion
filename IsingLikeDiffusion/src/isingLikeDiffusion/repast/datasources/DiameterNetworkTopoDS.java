package isingLikeDiffusion.repast.datasources;

public class DiameterNetworkTopoDS extends NetworkTopoDS {

	@Override
	public Object get(Iterable<?> objs, int size) {
		return super.getDiameter();
	}

}
