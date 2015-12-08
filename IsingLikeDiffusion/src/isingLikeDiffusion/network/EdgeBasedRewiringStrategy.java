package isingLikeDiffusion.network;

import isingLikeDiffusion.SocialAgent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.BidiMap;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

public class EdgeBasedRewiringStrategy extends AbstractRewiringStrategy {
	@Override
	public void rewire(Network<SocialAgent> network,
			BidiMap<SocialAgent, Integer> map, double beta) {
		List<RepastEdge<SocialAgent>> edges = new ArrayList<RepastEdge<SocialAgent>>();
		for (RepastEdge<SocialAgent> edge : network.getEdges()) {
			edges.add(edge);
		}

		Set<RepastEdge<SocialAgent>> removedEdges = new HashSet<RepastEdge<SocialAgent>>();

		for (RepastEdge<SocialAgent> edge : edges) {
			if (beta > RandomHelper.nextDouble()) {
				int rndIndex = RandomHelper.nextIntFromTo(0, map.size() - 1);
				SocialAgent randomNode = map.getKey(rndIndex);
				SocialAgent source = edge.getSource();
				if (!source.equals(randomNode)
						&& !network.isPredecessor(source, randomNode)) {
					SocialAgent sa = (SocialAgent) source;
					sa.setIsRewired(Boolean.TRUE);
					network.removeEdge(edge);
					removedEdges.add(edge);
					SocialNetworkUtils.addSocialEdge(network, source, randomNode, Boolean.TRUE);
				}
			}
		}
	}
}
