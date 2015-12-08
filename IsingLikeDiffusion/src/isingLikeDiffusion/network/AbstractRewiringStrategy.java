package isingLikeDiffusion.network;

import isingLikeDiffusion.Configuration;
import isingLikeDiffusion.SocialAgent;

import org.apache.commons.collections15.BidiMap;

import repast.simphony.space.graph.Network;

public abstract class AbstractRewiringStrategy implements RewiringStrategy {

	public abstract void rewire(Network<SocialAgent> network, BidiMap<SocialAgent, Integer> map, double beta);

	public static AbstractRewiringStrategy getRewirer() {
		try {
			return (AbstractRewiringStrategy) Class.forName(Configuration.getInstance().getString("rewiring.strategy")).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
