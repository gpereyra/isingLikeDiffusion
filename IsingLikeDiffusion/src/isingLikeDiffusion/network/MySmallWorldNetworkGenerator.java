package isingLikeDiffusion.network;

import isingLikeDiffusion.Configuration;
import isingLikeDiffusion.ParameterConstants;
import isingLikeDiffusion.SocialAgent;
import isingLikeDiffusion.SocialLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.BidiMap;

import cern.jet.random.Normal;
import repast.simphony.context.space.graph.AbstractGenerator;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import edu.uci.ics.jung.algorithms.util.Indexer;

/**
 * MySmallWorldNetworkGenerator is a specific graph generator based on Jung
 * Project's WattsBetaSmallWorldGenerator.
 * 
 * @author Gustavo Pereyra
 * @see "Small Worlds:The Dynamics of Networks between Order and Randomness by D.J. Watts"
 *      <p/>
 *      This is based on the Jung Project code.
 */
public class MySmallWorldNetworkGenerator extends
		AbstractGenerator<SocialAgent> {

	
	private double beta;
	private int degree;

	/**
	 * Constructs the small world graph generator.
	 * 
	 * @param beta
	 *            the probability of an edge being rewired randomly; the
	 *            proportion of randomly rewired edges in a graph. Must be
	 *            between 0 and 1.
	 * @param degree
	 *            the number of edges connected to each vertex around the local
	 *            neighborhood. This is the local ngh size. Must be an even
	 *            number.
	 * @param symmetrical
	 *            whether or not the generated edges will be symmetrical. This
	 *            has no effect on a non-directed network.
	 */
	public MySmallWorldNetworkGenerator(double beta, int degree) {
		if (degree % 2 != 0) {
			msg.error("Error creating MySmallWorldNetworkGenerator",
					new IllegalArgumentException(
							"All nodes must have an even degree."));
		}
		if (beta > 1.0 || beta < 0.0) {
			msg.error("Error creating MySmallWorldNetworkGenerator",
					new IllegalArgumentException(
							"Beta must be between 0 and 1."));
		}

		this.beta = beta;
		this.degree = degree;
	}

	/**
	 * Generates a beta-network from a 1-lattice according to the parameters
	 * given.
	 * 
	 * @return a beta-network model that is potentially a small-world
	 */
	public Network<SocialAgent> createNetwork(Network<SocialAgent> network) {
		long time = SocialLogger.debug(this, "Inicio creación de red.");

		List<SocialAgent> set = new ArrayList<SocialAgent>();
		for (SocialAgent node : network.getNodes()) {
			set.add(node);
		}
		Collections.shuffle(set);
		BidiMap<SocialAgent, Integer> map = Indexer.create(set);

		createLattice(network, map);
		Long time2 = SocialLogger.debug(this, "Inicio sembrado");
		seedInitialAdopters(network, map);
		SocialLogger.debug(this, "Fin sembrado", time2);

		AbstractRewiringStrategy.getRewirer().rewire(network, map, beta);
		SocialLogger.debug(this, "Fin creación de red.", time);
		return network;
	}

	private void seedInitialAdopters(Network<SocialAgent> network,
			BidiMap<SocialAgent, Integer> map) {
		double initialAdopterShare = Configuration.getInstance().getDouble("model.initial_adopter_share");
		int earlyAdopterCount = Double.valueOf(network.size() * initialAdopterShare)
				.intValue();
		Set<Integer> doneWith = new HashSet<Integer>();
		//FIXME desviacionCluster???? AbstractDistribution distribucion = new cern.jet.random.Normal(0.0, desviacionCluster, randomEngine);
		/*cern.jet.random.Normal dist = new cern.jet.random.Normal(0.0, network.size(),//(double)((double)network.size() / 20f),
				new cern.jet.random.engine.MersenneTwister(
						(Integer) RunEnvironment.getInstance()
						.getParameters().getValue(ParameterConstants.PARAM_RANDOM_SEED)));
		boolean useRandom = (Integer) RunEnvironment.getInstance()
				.getParameters().getValue(ParameterConstants.PARAM_USE_RANDOM) == 1;*/
		for (int i = 0; i < earlyAdopterCount; i++) {
			Integer rndIndex = null;
			do {
				//if (useRandom) {
					rndIndex = RandomHelper.nextIntFromTo(0, network.size());
				/*} else {
					rndIndex = getSantiRandom(network.size(), dist);
				}*/
			} while (doneWith.contains(rndIndex) || map.getKey(rndIndex) == null);
			SocialAgent initialAdopter = map.getKey(rndIndex);
			initialAdopter.setHasAdopted(Boolean.TRUE);
			doneWith.add(rndIndex);
		}
	}

	private int getSantiRandom(Integer tamagno, Normal dist) {
		int x;
		do {
			x = dist.nextInt()
					+ (tamagno / 2);
		} while ((x < 0) || (x >= tamagno));

		return x;
	}

	private void createLattice(Network<SocialAgent> network,
			BidiMap<SocialAgent, Integer> map) {

		network.removeEdges();
		boolean is1D = false;
		if (is1D) {
			int numKNeighbors = degree / 2;
			// create the lattice
			for (int i = 0; i < network.size(); i++) {
				for (int s = 1; s <= numKNeighbors; s++) {
					SocialAgent source = map.getKey(i);
					int upI = upIndex1D(i, s, network.size());
					SocialAgent target = map.getKey(upI);
					SocialNetworkUtils.addSocialEdge(network, source, target,
							Boolean.FALSE);
				}
			}
		} else {
			int latticeSize = (int) Math.floor(Math.sqrt(network.size()));

			int currentLatticeRow = 0, currentLatticeColumn = 0;
			int northIndex = 0, southIndex = 0, westIndex = 0, eastIndex = 0, northWestIndex = 0, southWestIndex = 0, northEastIndex = 0, southEastIndex = 0;

			int numNodes = network.size();

			for (int i = 0; i < numNodes; i++) {
				currentLatticeRow = i / latticeSize;
				currentLatticeColumn = i % latticeSize;

				northIndex = northIndex(currentLatticeRow,
						currentLatticeColumn, latticeSize);
				northWestIndex = northWestIndex(currentLatticeRow,
						currentLatticeColumn, latticeSize);
				southWestIndex = southWestIndex(currentLatticeRow,
						currentLatticeColumn, latticeSize);
				northEastIndex = northEastIndex(currentLatticeRow,
						currentLatticeColumn, latticeSize);
				southEastIndex = southEastIndex(currentLatticeRow,
						currentLatticeColumn, latticeSize);
				westIndex = westIndex(currentLatticeRow, currentLatticeColumn,
						latticeSize);
				southIndex = southIndex(currentLatticeRow,
						currentLatticeColumn, latticeSize);
				eastIndex = eastIndex(currentLatticeRow, currentLatticeColumn,
						latticeSize);

				SocialAgent source = map.getKey(i);
				SocialAgent north = map.getKey(northIndex);
				if (!network.isAdjacent(source, north)) {
					SocialNetworkUtils.addSocialEdge(network, source, north,
							Boolean.FALSE);
				}
				SocialAgent northWest = map.getKey(northWestIndex);
				if (!network.isAdjacent(source, northWest)) {
					SocialNetworkUtils.addSocialEdge(network, source,
							northWest, Boolean.FALSE);
				}
				SocialAgent northEast = map.getKey(northEastIndex);
				if (!network.isAdjacent(source, northEast)) {
					SocialNetworkUtils.addSocialEdge(network, source,
							northEast, Boolean.FALSE);
				}
				SocialAgent southWest = map.getKey(southWestIndex);
				if (!network.isAdjacent(source, southWest)) {
					SocialNetworkUtils.addSocialEdge(network, source,
							southWest, Boolean.FALSE);
				}
				SocialAgent southEast = map.getKey(southEastIndex);
				if (!network.isAdjacent(source, southEast)) {
					SocialNetworkUtils.addSocialEdge(network, source,
							southEast, Boolean.FALSE);
				}
				SocialAgent west = map.getKey(westIndex);
				if (!network.isAdjacent(source, west)) {
					SocialNetworkUtils.addSocialEdge(network, source, west,
							Boolean.FALSE);
				}
				SocialAgent south = map.getKey(southIndex);
				if (!network.isAdjacent(source, south)) {
					SocialNetworkUtils.addSocialEdge(network, source, south,
							Boolean.FALSE);
				}
				SocialAgent east = map.getKey(eastIndex);
				if (!network.isAdjacent(source, east)) {
					SocialNetworkUtils.addSocialEdge(network, source, east,
							Boolean.FALSE);
				}
			}
		}
	}

	/**
	 * Determines the index of the neighbor ksteps above
	 * 
	 * @param numSteps
	 *            is the number of steps away from the current index that is
	 *            being considered.
	 * @param currentIndex
	 *            the index of the selected vertex.
	 * @param numNodes
	 */
	private int upIndex1D(int currentIndex, int numSteps, int numNodes) {

		int value = currentIndex + numSteps;
		if (value > numNodes - 1)
			return value % numNodes;
		return value;
	}

	protected int northIndex(int currentLatticeRow, int currentLatticeColumn,
			int latticeSize) {
		if (currentLatticeRow == 0) {
			return latticeSize * (latticeSize - 1) + currentLatticeColumn;
		} else {
			return (currentLatticeRow - 1) * latticeSize + currentLatticeColumn;
		}
	}

	protected int northWestIndex(int currentLatticeRow,
			int currentLatticeColumn, int latticeSize) {
		int n = northIndex(currentLatticeRow, currentLatticeColumn, latticeSize);
		return westIndex(n / latticeSize, n % latticeSize, latticeSize);
	}

	protected int northEastIndex(int currentLatticeRow,
			int currentLatticeColumn, int latticeSize) {
		int n = northIndex(currentLatticeRow, currentLatticeColumn, latticeSize);
		return eastIndex(n / latticeSize, n % latticeSize, latticeSize);
	}

	protected int southWestIndex(int currentLatticeRow,
			int currentLatticeColumn, int latticeSize) {
		int n = southIndex(currentLatticeRow, currentLatticeColumn, latticeSize);
		return westIndex(n / latticeSize, n % latticeSize, latticeSize);
	}

	protected int southEastIndex(int currentLatticeRow,
			int currentLatticeColumn, int latticeSize) {
		int n = southIndex(currentLatticeRow, currentLatticeColumn, latticeSize);
		return eastIndex(n / latticeSize, n % latticeSize, latticeSize);
	}

	protected int southIndex(int currentLatticeRow, int currentLatticeColumn,
			int latticeSize) {
		if (currentLatticeRow == latticeSize - 1) {
			return currentLatticeColumn;
		} else {
			return (currentLatticeRow + 1) * latticeSize + currentLatticeColumn;
		}
	}

	protected int westIndex(int currentLatticeRow, int currentLatticeColumn,
			int latticeSize) {
		if (currentLatticeColumn == 0) {
			return currentLatticeRow * latticeSize + latticeSize - 1;
		} else {
			return currentLatticeRow * latticeSize + currentLatticeColumn - 1;
		}
	}

	protected int eastIndex(int currentLatticeRow, int currentLatticeColumn,
			int latticeSize) {
		if (currentLatticeColumn == latticeSize - 1) {
			return currentLatticeRow * latticeSize;
		} else {
			return currentLatticeRow * latticeSize + currentLatticeColumn + 1;
		}
	}
}
