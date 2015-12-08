package isingLikeDiffusion.network;

import isingLikeDiffusion.SocialAgent;
import repast.simphony.space.graph.RepastEdge;

public class SocialEdge extends RepastEdge<SocialAgent> {
	private Boolean isRewired;

	public SocialEdge(SocialAgent source, SocialAgent target,
			boolean isDirected, double weight) {
		super(source,target,isDirected,weight);
	}

	public Boolean getIsRewired() {
		return isRewired;
	}

	public void setIsRewired(Boolean isRewired) {
		this.isRewired = isRewired;
	}

}
