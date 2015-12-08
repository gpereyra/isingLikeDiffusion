package isingLikeDiffusion.network;

import isingLikeDiffusion.SocialAgent;

import org.apache.commons.collections15.BidiMap;

import repast.simphony.space.graph.Network;

public interface RewiringStrategy {

	public abstract void rewire(Network<SocialAgent> network, BidiMap<SocialAgent, Integer> map, double beta);

}
