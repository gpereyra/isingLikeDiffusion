package isingLikeDiffusion.network;

import isingLikeDiffusion.SocialAgent;
import repast.simphony.space.graph.Network;

public class SocialNetworkUtils {
	public static void addSocialEdge(Network<SocialAgent> network, SocialAgent source,
			SocialAgent target, Boolean isRewired) {
		SocialEdge edge = new SocialEdgeCreator().createEdge(
				(SocialAgent) source, (SocialAgent) target, false, 0);
		edge.setIsRewired(isRewired);
		network.addEdge(edge);
	}
}
