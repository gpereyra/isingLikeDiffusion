package isingLikeDiffusion.repast.viz;

import isingLikeDiffusion.SocialAgent;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class SocialAgentViz extends DefaultStyleOGL2D {
	@Override
	public Color getColor(Object agent) {
		SocialAgent sa = (SocialAgent) agent;
		return sa.getHasAdopted() ? Color.RED : sa.getIsRewired() ? Color.BLACK : Color.GRAY;
	}

}
