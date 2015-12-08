package isingLikeDiffusion;

import java.math.BigDecimal;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;

public class SocialAgent {
	private Network<SocialAgent> socialNetwork;
	private Boolean hasAdopted;
	private Boolean hasDecidedAdoption;
	private Boolean isRewired;
	

	public Boolean getIsRewired() {
		if (this.isRewired == null){
			this.isRewired = Boolean.FALSE;
		}
		return isRewired;
	}
	public void setIsRewired(Boolean isRewired) {
		this.isRewired = isRewired;
	}
	public SocialAgent() {
		super();
		hasAdopted = Boolean.FALSE;
	}
	public SocialAgent(Network<SocialAgent> socialNetwork, Grid<SocialAgent> neighbourhood) {
		this();
		this.socialNetwork = socialNetwork;
		
	}
	public Network<SocialAgent> getSocialNetwork() {
		return socialNetwork;
	}
	public void setSocialNetwork(Network<SocialAgent> socialNetwork) {
		this.socialNetwork = socialNetwork;
	}
	public Boolean getHasAdopted() {
		return hasAdopted;
	}
	
	public int getAdoptedAsInt(){
		return getHasAdopted() ? 1 : 0;
	}

	public void setHasAdopted(Boolean hasAdopted) {
		this.hasAdopted = hasAdopted;
	}

	@ScheduledMethod ( start = 1, interval = 1 )
	public void decideAdoption() {
		long time = System.currentTimeMillis();
		boolean allowDisadoption = Configuration.getInstance().getBoolean("model.allow_disadoption");
		Integer tieDecision = Configuration.getInstance().getInteger("model.tie_decision");
		boolean decideTiesStocastically = tieDecision == 1;
		boolean decideTiesAdopting = tieDecision == 2;
		float beta = 10000000f;
		float alfa = 0.5f;
		double mv = 0;
		int adoptionCount = 0;
		int neighbourCount = 0;
		for (SocialAgent n : getNeighbours()) {
			if (n.getHasAdopted()){
				adoptionCount++;
				mv += 1;
			} else {
				mv -= 1;
			}
			neighbourCount++;
		}
		/*if ((Integer)RunEnvironment.getInstance()
				.getParameters().getValue(ParameterConstants.PARAM_USE_RANDOM) == 1){
			mv /= neighbourCount;
			double m = alfa * mv + (1.0f - alfa) * Configuration.getInstance().getDouble(ParameterConstants.UTILITY);
			
			Double pAdoptar = null;
			if (beta < Integer.MAX_VALUE) {
				pAdoptar = 1.0f / (1.0f + Math.exp(-2.0f * m * beta));
			} else {
				double signum = Math.signum(m);
				if (signum == 1.0) {
					pAdoptar = 1.0;
				} else if (signum == 0.0) {				
					pAdoptar = 0.5;
				} else {
					pAdoptar = 0.0;
				}
			}
			double r = RandomHelper.nextDouble();
			if (pAdoptar >= r) {
				 // Adopta siempre
				if (!getHasAdopted()){
					decideAdopt();
				}
			} else {
				if (allowDisadoption && getHasAdopted()){
					decideDisadopt();
				}
			}
		} else {*/
			//vi+ >	1/2 * Niv * (1 - delta ui)
			double adoptionThreshold = 0.5 * neighbourCount * ( 1 - Configuration.getInstance().getDouble(ParameterConstants.UTILITY));
			if (adoptionCount > adoptionThreshold ){
				if (!getHasAdopted()){
					decideAdopt();
				}
			} else if (adoptionCount == adoptionThreshold ){
				if (decideTiesStocastically){
					if (RandomHelper.nextDoubleFromTo(0d, 0.99d) < 0.5){
						if (allowDisadoption && getHasAdopted()){
							decideDisadopt();
						}
					} else {
						if (!getHasAdopted()){
							decideAdopt();
						}
					}
				} else {
					if (decideTiesAdopting){
						if (!getHasAdopted()){
							decideAdopt();
						}
					}
				}
			} else if (allowDisadoption) {
				if (getHasAdopted()){
					decideDisadopt();
				}
			}
		//}
		
		SocialTimer.time(System.currentTimeMillis() - time);
	}
	
	@ScheduledMethod(pick = 1L, start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
	public void endIfStable() {
		Iterable<SocialAgent> sas = this.getSocialNetwork().getNodes();
		int adopted = 0;
		for (SocialAgent sa : sas) {
			sa.decide();
			if (sa.getHasAdopted()){
				adopted++;
			}
		}
		BigDecimal adoptedBD = BigDecimal.valueOf(adopted);
		Register.getInstance().takeReading(ModelAnalytic.ADOPTION_COUNT, adoptedBD);
		if (Register.getInstance().getIsStable(ModelAnalytic.ADOPTION_COUNT, adoptedBD)){
			RunEnvironment.getInstance().endRun();
		}
	}

	private Iterable<SocialAgent> getNeighbours() {
		return socialNetwork.getAdjacent(this);
	}
	
	
	public Boolean getHasDecidedAdoption() {
		return hasDecidedAdoption;
	}
	public void setHasDecidedAdoption(Boolean hasDecidedAdoption) {
		this.hasDecidedAdoption = hasDecidedAdoption;
	}
	
	private void decideDisadopt() {
		setHasDecidedAdoption(Boolean.FALSE);
		
	}
	
	private void decideAdopt() {
		setHasDecidedAdoption(Boolean.TRUE);
	}
	
	private void decide() {
		if (getHasDecidedAdoption() != null){
			if (getHasDecidedAdoption()){
				adopt();
			} else {
				disadopt();
			}
		}
	}
	
	private void disadopt() {
		setHasDecidedAdoption(null);
		setHasAdopted(Boolean.FALSE);
		
	}
	private void adopt() {
		setHasDecidedAdoption(null);
		setHasAdopted(Boolean.TRUE);
	}
	
}
