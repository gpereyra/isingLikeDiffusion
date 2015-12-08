package isingLikeDiffusion.network;

import isingLikeDiffusion.SocialAgent;

import org.apache.commons.collections15.BidiMap;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

public class NodeBasedRewiringStrategy extends AbstractRewiringStrategy {

	@Override
	public void rewire(Network<SocialAgent> network,
			BidiMap<SocialAgent, Integer> map, double beta) {
		for (SocialAgent sa : network.getNodes()) {
			for (SocialAgent neighbour : network.getAdjacent(sa)){
				if (beta > RandomHelper.nextDouble()) {
					int rndIndex = RandomHelper.nextIntFromTo(0, map.size() - 1);
					SocialAgent randomNode = map.getKey(rndIndex);
					if (!sa.equals(randomNode)
							&& !network.isPredecessor(sa, randomNode)) {
						sa.setIsRewired(Boolean.TRUE);
						RepastEdge<SocialAgent> removedEdge = network.getEdge(sa, neighbour);
						network.removeEdge(removedEdge);
						SocialNetworkUtils.addSocialEdge(network, sa, randomNode, Boolean.TRUE);
					}
				}
			}
		}

	}

}
