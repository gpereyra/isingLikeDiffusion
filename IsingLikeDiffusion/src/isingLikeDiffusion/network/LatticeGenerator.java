package isingLikeDiffusion.network;

import isingLikeDiffusion.SocialAgent;

import java.util.List;

import repast.simphony.space.graph.Network;

public interface LatticeGenerator {
	public Network<SocialAgent> createLattice(List<SocialAgent> set);
}
