package isingLikeDiffusion.repast.viz;

import java.awt.Color;

import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;

public class SocialEdgeViz implements EdgeStyleOGL2D {
	
	@Override
	public int getLineWidth(RepastEdge<?> edge) {
		return 1;
	}

	@Override
	public Color getColor(RepastEdge<?> edge) {
		return Color.GREEN;//((SocialEdge)edge).getIsRewired() ? Color.GREEN : Color.GRAY;
	}

}
