package isingLikeDiffusion.repast.datasources;

import isingLikeDiffusion.SocialAgent;
import isingLikeDiffusion.SocialLogger;

import java.util.Map;

import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.space.graph.Network;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;

public abstract class NetworkTopoDS implements AggregateDataSource {
	private static Double averageDistance = 0d;
	private static Double diameter = 0d;
	private static Double clusteringCoefficient = 0d;
	private static Double averageEarlyAdopterDegree = 0d;

	
	public static void calcNetworkAnalytics(Network<SocialAgent> network) {
		long time = SocialLogger.debug(NetworkTopoDS.class, "Inicio cálculo de datos red.");
		int count = 0;
	    double totalClusteringCoef = 0;
	    Graph graph = ((ContextJungNetwork) network).getGraph();
	    Map<SocialAgent, Double> clusteringCoefficients = Metrics.clusteringCoefficients(graph);
		/*
	    int eaCount = 0;
	    double totalEarlyAdopterDegree = 0;
	    double totalDistance = 0;
		Transformer<SocialAgent, Double> averageDistances = DistanceStatistics.averageDistances(graph);
	    double diameter = DistanceStatistics.diameter(graph);*/
	    for (SocialAgent node : network.getNodes()) {
	    	count++;
			//totalDistance += averageDistances.transform(node);
			totalClusteringCoef += clusteringCoefficients.get(node);
			/*if (node.getHasAdopted()){
				eaCount++;
				totalEarlyAdopterDegree += network.getDegree(node);
			}*/
		}
	    /*NetworkTopoDS.averageDistance = totalDistance/count;
	    NetworkTopoDS.diameter = diameter;*/
	    NetworkTopoDS.clusteringCoefficient = totalClusteringCoef/count;
	    //NetworkTopoDS.averageEarlyAdopterDegree = totalEarlyAdopterDegree/eaCount;
	    SocialLogger.debug(NetworkTopoDS.class, "Fin cálculo de datos red.", time);
	}

	public Double getDiameter() {
		return diameter;
	}

	public Double getClusteringCoefficient() {
		return clusteringCoefficient;
	}

	public Double getAverageEarlyAdopterDegree() {
		return averageEarlyAdopterDegree;
	}
	
	public static Double getAverageDistance() {
		return averageDistance;
	}

	@Override
	public Class<?> getDataType() {
		return Double.class;
	}

	@Override
	public String getId() {
		return this.getClass().getSimpleName();
	}

	@Override
	public Class<?> getSourceType() {
		return void.class;
	}

	@Override
	public void reset() {
		/*NetworkTopoDS.averageDistance = 0d;
	    NetworkTopoDS.diameter = 0d;
	    NetworkTopoDS.clusteringCoefficient = 0d;
	    NetworkTopoDS.averageEarlyAdopterDegree = 0d;*/
	}
}
