package isingLikeDiffusion.network;

import isingLikeDiffusion.SocialAgent;
import repast.simphony.space.graph.EdgeCreator;

public class SocialEdgeCreator implements EdgeCreator<SocialEdge, SocialAgent> {

	@Override
	public Class<SocialEdge> getEdgeType() {
		return SocialEdge.class;
	}

	@Override
	public SocialEdge createEdge(SocialAgent source, SocialAgent target,
			boolean isDirected, double weight) {
		return new SocialEdge(source, target, isDirected, weight);
	}

}
