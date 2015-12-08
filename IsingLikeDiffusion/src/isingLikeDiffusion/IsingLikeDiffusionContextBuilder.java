package isingLikeDiffusion;

import isingLikeDiffusion.network.MySmallWorldNetworkGenerator;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.Lattice1DGenerator;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.graph.Network;

public class IsingLikeDiffusionContextBuilder implements ContextBuilder<SocialAgent> {

	@Override
	public Context build(Context<SocialAgent> context) {
		long time = SocialLogger.debug(this, "Inicio creación de contexto.");
		context.setId("IsingLikeDiffusion");
		
		NetworkBuilder<SocialAgent> ntwkBuilder = new NetworkBuilder<SocialAgent>("social_network", context, false);
		ntwkBuilder.setGenerator(new Lattice1DGenerator<SocialAgent>(true, true));
		Network<SocialAgent> socialNetwork = ntwkBuilder.buildNetwork();
		Parameters parameters = RunEnvironment.getInstance().getParameters();
		
		int socialAgentsCount = parameters.getInteger(ParameterConstants.AGENT_COUNT);
		for (int i = 0; i < socialAgentsCount; i++) {
			SocialAgent socialAgent = new SocialAgent();
			context.add(socialAgent);
		}

		socialNetwork = new MySmallWorldNetworkGenerator(
				parameters.getDouble(ParameterConstants.REWIRING_PROBABILITY), 
				8)
				.createNetwork(socialNetwork);
		
		for (SocialAgent sa : context) {
			sa.setSocialNetwork(socialNetwork);
		}
		SocialLogger.debug(this, "Fin creación de contexto.", time);
		//NetworkTopoDS.calcNetworkAnalytics(socialNetwork);
		Register.getInstance().clear();
		return context;
	}

}
