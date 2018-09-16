package model;

import sim.engine.*;
import sim.util.*;
import sim.field.network.*;
import java.util.*;

/**
 * Class of the farmer agents behaviors
 */

public class FarmerAgent extends CFAgent implements Steppable {

	private static final long serialVersionUID = 1L;

	double farmSize;
	double neighborRange;
	double autonomyPremium;
	double farmerCommit;
	double[] trustBaseFarmer;


	boolean beingBreached;
	int breachingContractor;
	double cfContractorID;
	double cfContractorRate;
	int randomHarvestMonth = 0;
	
	double yieldSF = 0;
	double yieldMF = 0;
	double yieldLF = 0;
	double costSF = 0;
	double costMF = 0;
	double costLF = 0;
	
	double profitAgent; 		
	double volumeFarmer;		
	double diffSecondBest;
	
	// Another option: go for ordinary product with (assumption) traditional method
	double[] volumeF = new double[ModelParams.NO_CONTRACTORS + 1];
	double[] benefitF = new double[ModelParams.NO_CONTRACTORS + 1];
	double[] utilityF = new double[ModelParams.NO_CONTRACTORS + 1];
	double[] trustF = new double[ModelParams.NO_CONTRACTORS];
	double[] scoreF = new double[ModelParams.NO_CONTRACTORS + 1];
	
	// Declare Output Variables 
	double payoffsNonContractedF[];
	double payoffsContractedF[];
	
	double distanceNonContractedF[]; 
	double distanceContractedF[];

	double payoffsSpotMarket[];
	
	double trustSFToContractor[];
	double trustMFToContractor[];
	double trustLFToContractor[];

	/**
	 *TODO CONSTRUCTOR a new instance of the Farmer agent class
	 */
	public FarmerAgent(int _agentId, int _agentRole, int _contracting, int _honoring, 
			double _farmSize, double _neighborRange, double _autonomyPremium,  double _weightUtility, 
			int _ordinaryRice, double _farmerCommit, double[] _trustBaseFarmer) {

		super(_agentId, _agentRole, _contracting, _honoring, _weightUtility, _ordinaryRice);
		
		this.farmSize = _farmSize;
		this.neighborRange = _neighborRange;
		this.autonomyPremium = _autonomyPremium;
		this.farmerCommit = _farmerCommit;
		this.trustBaseFarmer = _trustBaseFarmer;
		
		this.distanceContractedF = new double[ModelParams.MAX_STEPS];
		this.distanceNonContractedF = new double[ModelParams.MAX_STEPS];
		
		this.payoffsContractedF = new double[ModelParams.MAX_STEPS];
		this.payoffsNonContractedF = new double[ModelParams.MAX_STEPS];

		this.payoffsSpotMarket = new double[ModelParams.MAX_STEPS];
		
		this.trustSFToContractor = new double[ModelParams.MAX_STEPS];
		this.trustMFToContractor = new double[ModelParams.MAX_STEPS];
		this.trustLFToContractor = new double[ModelParams.MAX_STEPS];
		
		for (int i = 0; i < ModelParams.MAX_STEPS; i++) {
			this.distanceContractedF[i] = 0;
			this.distanceNonContractedF[i] = 0;
			
			this.payoffsContractedF[i] = 0;
			this.payoffsNonContractedF[i] = 0;
			
			this.payoffsSpotMarket = new double[ModelParams.MAX_STEPS];
			
			this.trustSFToContractor[i] = 0;
			this.trustMFToContractor[i] = 0;
			this.trustLFToContractor[i] = 0;
		}
		
		this.profitAgent = 0; 		
		this.volumeFarmer = 0;
		this.beingBreached = false;	
		this.breachingContractor = ModelParams.NO_FARMERS + ModelParams.NO_CONTRACTORS;
		this.cfContractorID = 0;
		this.cfContractorRate = 0;
		this.diffSecondBest = 0;
	}
	// --------------------------- Get/Set methods ---------------------------//

	public double getFarmSize(){
		return this.farmSize;
	}
	
	public double getNeighborRange(){
		return this.neighborRange;
	}
	
	public double getAutonomyPremium(){
		return this.autonomyPremium;
	}
	
	public boolean getBeingBreached(){
		return beingBreached;
	}
	public void setBeingBreached(boolean _beingBreached){
		this.beingBreached = _beingBreached;
	}
	
	public int getBreachingContractor(){
		return breachingContractor;
	}
	public void setBreachingContractor(int _breachingContractor){
		this.breachingContractor = _breachingContractor;
	}
	
	public double getCFContractorRate(){
		return cfContractorRate;
	}
	public void setCFContractorRate(double _cfContractorRate){
		this.cfContractorRate = _cfContractorRate;
	}
	
	public int getRandomHarvestMonth(){
		return randomHarvestMonth;
	}
	public void setRandomHarvestMonth(int _randomHarvestMonth){
		this.randomHarvestMonth = _randomHarvestMonth;
	}
	
	public double getProfitAgent(){
		return profitAgent; 
	}
	public void setProfitAgent(double _profitAgent){
		this.profitAgent = _profitAgent;
	}
	
	public double getVolumeFarmer(){
		return volumeFarmer; 
	}
	public void setVolumeFarmer(double _volumeFarmer){
		this.volumeFarmer = _volumeFarmer;
	}
	
	public double getDiffSecondBest(){
		return diffSecondBest;
	}
	public void setDiffSecondBest(double _diffSecondBest){
		this.diffSecondBest = _diffSecondBest;
	}
	
	public double getYieldSmallFarmer(){
		
		if (this.ordinaryRice == 1) {
			if (currentStep % 2 == 0) yieldSF = ModelParams.YIELD_SMALL_ORDINARY_TRA_WINTER;
			else yieldSF = ModelParams.YIELD_SMALL_ORDINARY_TRA_SUMMER;
			
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) yieldSF = ModelParams.YIELD_SMALL_JASMINE_TRA_WINTER;
			else yieldSF = 0;
		}
		return yieldSF;
	}
	
	public double getYieldMediumFarmer(){
		
		if (this.ordinaryRice == 1) {
			if (currentStep % 2 == 0) yieldMF = ModelParams.YIELD_MEDIUM_ORDINARY_TRA_WINTER;
			else yieldMF = ModelParams.YIELD_MEDIUM_ORDINARY_TRA_SUMMER;
			
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) yieldMF = ModelParams.YIELD_MEDIUM_JASMINE_TRA_WINTER;
			else yieldMF = 0;
		}
		return yieldMF;
	}
	
	public double getYieldLargeFarmer(){
		
		if (this.ordinaryRice == 1) {
			if (currentStep % 2 == 0) yieldLF = ModelParams.YIELD_LARGE_ORDINARY_IMPR_WINTER * Model.yieldSA;
			else yieldLF = ModelParams.YIELD_LARGE_ORDINARY_IMPR_SUMMER * Model.yieldSA;
			
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) yieldLF = ModelParams.YIELD_LARGE_JASMINE_IMPR_WINTER * Model.yieldSA;
			else yieldLF = 0;
		}
		return yieldLF;
	}
	
	public double getCostSmallFarmer(){
		
		if (this.ordinaryRice == 1) {
			if (currentStep % 2 == 0) costSF = ModelParams.COST_SMALL_ORDINARY_TRA_WINTER;
			else costSF = ModelParams.COST_SMALL_ORDINARY_TRA_SUMMER;
			
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) costSF = ModelParams.COST_SMALL_JASMINE_TRA_WINTER;
			else costSF = 0;
		}
		return costSF;
	}
	
	public double getCostMediumFarmer(){
		
		if (this.ordinaryRice == 1) {
			if (currentStep % 2 == 0) costMF = ModelParams.COST_MEDIUM_ORDINARY_TRA_WINTER;
			else costMF = ModelParams.COST_MEDIUM_ORDINARY_TRA_SUMMER;
			
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) costMF = ModelParams.COST_MEDIUM_JASMINE_TRA_WINTER;
			else costMF = 0;
		}
		return costMF;
	}
	
	public double getCostLargeFarmer(){
		
		if (this.ordinaryRice == 1) {
			if (currentStep % 2 == 0) costLF = ModelParams.COST_LARGE_ORDINARY_IMPR_WINTER;
			else costLF = ModelParams.COST_LARGE_ORDINARY_IMPR_SUMMER;
			
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) costLF = ModelParams.COST_LARGE_JASMINE_IMPR_WINTER;
			else costLF = 0;
		}
		return costLF;
	}
	
	public double getFarmerCommit(){
		return farmerCommit;
	}
	
	public double getCFContractorID(){
		return cfContractorID;
	}
	
	public double getTrustBaseFarmer(int _index){
		return trustBaseFarmer[_index]; 
	}
	public void setTrustBaseFarmer(int _index, double _trustBaseFarmer){
		this.trustBaseFarmer[_index] = _trustBaseFarmer;
	}
	
	public void setCFContractorID(double _cfContractorID){
		this.cfContractorID = _cfContractorID;
	}
	
	/*
	 * TODO SECOND STAGE OF CONTRACT FARMING PROCESS
	 */
	public void farmerAcceptContracts(SimState state) {
		
		Model model = (Model) state;
		Bag out = model.contractFarming.getEdges(this, null);
		
		// 1. THE FARMER GIVE SCORE FOR HIMSELF WITH TRADITION (IF NON-CONTRACTED)
		
		// Initialize Array to sort options with highest score including scoreF and ID
		List<double[]> sortScore = new ArrayList<>();
		
		// Average price of past cropping; at time 0 & 1 past price is seen as the current price
		double smPastPrice = 0;
		if (this.ordinaryRice == 1) {
			if (currentStep == 0 || currentStep == 1) smPastPrice = this.getSMMedianPrice();
			else smPastPrice = (model.smPriceAvg[currentStep-1] + model.smPriceAvg[currentStep-2])/2; 
		} else if (this.ordinaryRice == 0) {
			if (currentStep == 0 || currentStep == 2) smPastPrice = this.getSMMedianPrice();
			if (currentStep >= 4) smPastPrice = (model.smPriceAvg[currentStep-2] + model.smPriceAvg[currentStep-4])/2; 
		}
		
		double yieldTrader;
		double costTrader;
		if (this.getAgentRole() == ModelParams.SMALL_F){
			yieldTrader = this.getYieldSmallFarmer();
			costTrader = this.getCostSmallFarmer();
			
		} else {
			yieldTrader = this.getYieldMediumFarmer();
			costTrader = this.getCostMediumFarmer();
		}
		
		double preBenefitMinFTrader = (smPastPrice 
				* yieldTrader * (1 + ModelParams.LOAN_TRADER) - costTrader) * this.getFarmSize();
		double preBenefitMinFContract = ((smPastPrice 
				* (1 + Math.min(model.getParametersObject().getCFPriceConA(), 
						model.getParametersObject().getCFPriceConB()))
				* this.getYieldLargeFarmer() - this.getCostLargeFarmer()) * this.getFarmSize())
				/ (1 + this.getAutonomyPremium())
				+ this.getCostLargeFarmer() * this.getFarmSize() * (1 + ModelParams.LOAN_CONTRACTOR_DEFAULT);
		double preBenefitMinF = Math.min(preBenefitMinFTrader, preBenefitMinFContract);
		double preBenefitMaxF = ((smPastPrice 
				* (1 + Math.max(model.getParametersObject().getCFPriceConA(), 
						model.getParametersObject().getCFPriceConB()))
				* this.getYieldLargeFarmer() - this.getCostLargeFarmer()) * this.getFarmSize())
				/ (1 + this.getAutonomyPremium())
				+ this.getCostLargeFarmer() * this.getFarmSize() * (1 + ModelParams.LOAN_CONTRACTOR_DEFAULT);

		double riskF;
		riskF = preBenefitMaxF;
		
		// CALCULATE OPTION WITH TRADER
		if ((this.ordinaryRice == 0) && currentStep % 2 != 0){
			
			volumeF[volumeF.length - 1] = 0;
			benefitF[benefitF.length - 1] = 0;
			utilityF[utilityF.length - 1] = 0;
			scoreF[scoreF.length - 1] = 0;
			
		} else {
			
			volumeF[volumeF.length - 1] = yieldTrader * this.getFarmSize();
			// Trader offers to pay 15-20% of produce value in advance for every farmer  
			benefitF[benefitF.length - 1] = (smPastPrice * yieldTrader * (1 + ModelParams.LOAN_TRADER)
					- costTrader) * this.getFarmSize();

			utilityF[utilityF.length - 1] = (1 - Math.exp(-(benefitF[benefitF.length - 1] - preBenefitMinF) / riskF))
					/ (1 - Math.exp(-(preBenefitMaxF - preBenefitMinF) / riskF));
			
			scoreF[scoreF.length - 1] = Math.pow(utilityF[utilityF.length - 1], weightUtility)
					* Math.pow(ModelParams.TRUST_VALUE_TRADER, (1 - weightUtility));
		}
		
		if ((this.ordinaryRice == 0) && currentStep % 2 == 0 && Double.isNaN(scoreF[scoreF.length - 1])){
			System.out.println("NaN detected SECOND FARMER-2 !!!!!!!!!!!");
			System.out.println("farmerID: " + this.getAgentId() + " step: " + currentStep
			+ " benefit: " + String.format("%.2f", benefitF[benefitF.length - 1])
			+ " preBenefitMin: " + String.format("%.2f", preBenefitMinF)
			+ " preBenefitMax: " + String.format("%.2f", preBenefitMaxF)
			+ " utility: " + String.format("%.2f", utilityF[utilityF.length - 1]) 
			+ " score: " + String.format("%.2f", scoreF[scoreF.length - 1]));
		}

		sortScore.add(new double[] { scoreF[scoreF.length - 1], 0, benefitF[benefitF.length - 1],
				volumeF[volumeF.length - 1] });

		// 2. FAMRER CALCULATES SCORES FOR CONTRACT-OFFERED CONTRACTORS
		for (int i = 0; i < out.size(); i++) {
			
			Edge e = (Edge) (out.get(i));
			CFAgent cfAgent = (CFAgent)(e.getOtherNode(this));
			
			if (cfAgent.getAgentRole() == ModelParams.CONTRACTOR){
				
				ContractorAgent contractor = (ContractorAgent) cfAgent;
				
				double[] attributes;
				attributes = (double[]) (e.info);

				int contractorID = contractor.getAgentId();
				int contractorIDIndex = contractorID - ModelParams.NO_FARMERS;
				
				double numDeal = attributes[9];

				if (attributes[0] == ModelParams.NON_OFFERED_C || 		//Either contractor does not offer contract
						attributes[10] == ModelParams.UNTRUSTWORTHY_C) {//Either contractor used to be un-trustworthy
					
					trustF[contractorIDIndex] = this.trustBaseFarmer[contractorIDIndex]
							+ (1 - this.trustBaseFarmer[contractorIDIndex])
							* (1 - 1 / (ModelParams.TRUST_FACTOR * numDeal + 1 - ModelParams.TRUST_FACTOR));
					if (trustF[contractorIDIndex] < ModelParams.TRUST_LEVEL_MIN)
						trustF[contractorIDIndex] = ModelParams.TRUST_LEVEL_MIN;
					attributes[5] = trustF[contractorIDIndex];
				}

				if (this.getContracting() == ModelParams.NON_CONTRACTED_F &&
						//Farmer is not contracted with other contractor
						attributes[0] == ModelParams.OFFERED_C &&
						//Contractor offers contract
						attributes[10] == ModelParams.TRUSTWORTHY_C) {
						//Contractor is trustworthy during past trades

					volumeF[contractorIDIndex] = this.getYieldLargeFarmer() * this.getFarmSize();
					double pCFContractor = smPastPrice * (1 + contractor.getCFContractRate());
					
					benefitF[contractorIDIndex] = ((pCFContractor * this.getYieldLargeFarmer()
							- this.getCostLargeFarmer()) * this.getFarmSize())
							/ (1 + this.getAutonomyPremium())
							+ this.getCostLargeFarmer() * this.getFarmSize()
							* (1 + ModelParams.LOAN_CONTRACTOR_DEFAULT);
					
					utilityF[contractorIDIndex] = (1 - Math.exp(-(benefitF[contractorIDIndex]
							- preBenefitMinF) / riskF))
							/ (1 - Math.exp(-(preBenefitMaxF - preBenefitMinF) / riskF));
					
					trustF[contractorIDIndex] = this.trustBaseFarmer[contractorIDIndex]
							+ (1 - this.trustBaseFarmer[contractorIDIndex])
							* (1 - 1 / (ModelParams.TRUST_FACTOR * numDeal + 1 - ModelParams.TRUST_FACTOR));
					if (trustF[contractorIDIndex] < ModelParams.TRUST_LEVEL_MIN)
						trustF[contractorIDIndex] = ModelParams.TRUST_LEVEL_MIN;

					scoreF[contractorIDIndex] = Math.pow(utilityF[contractorIDIndex], weightUtility)
							* Math.pow(trustF[contractorIDIndex], (1 - weightUtility));
					
					if ((this.ordinaryRice == 0) && currentStep % 2 == 0 && Double.isNaN(scoreF[contractorIDIndex])){
						System.out.println("NaN detected THIRD FARMER-2 !!!!!!!!!!!");
						System.out.println("farmerID:" + this.getAgentId() + " step: " + currentStep
							+ " with contractor " + contractorID 
							+ " benefit: " + String.format("%.2f", benefitF[contractorIDIndex]) 
							+ " preBenefitMin: " + String.format("%.2f", preBenefitMinF)
							+ " preBenefitMax: " + String.format("%.2f", preBenefitMaxF)
							+ " utility: " + String.format("%.2f", utilityF[contractorIDIndex]) 
							+ " trust: " + String.format("%.2f", trustF[contractorIDIndex]) 
							+ " score: " + String.format("%.2f", scoreF[contractorIDIndex])
							+ " deal: " + numDeal); 
					}
					
					sortScore.add(new double[] { scoreF[contractorIDIndex], contractorID, benefitF[contractorIDIndex],
							volumeF[contractorIDIndex] });

					attributes[3] = benefitF[contractorIDIndex];
					attributes[5] = trustF[contractorIDIndex];
					attributes[7] = scoreF[contractorIDIndex];
					
					model.contractFarming.updateEdge(e, contractor, this, attributes);
				}
			}
		}

		double[][] sortedScore = sortScore.toArray(new double[sortScore.size()][]);
		Arrays.sort(sortedScore, (v1, v2) -> Double.compare(v2[0], v1[0]));
		
		// 3. PROCEED WITH HIGHEST SCORE IN SORTEDSCORE & UPDATE
		
		if (this.getContracting() == ModelParams.NON_CONTRACTED_F){
			
			for (int i = 0; i < out.size(); i++) {
				Edge e = (Edge) (out.get(i));

				// Get neighbor from the other end
				CFAgent contractor = (CFAgent) (e.getOtherNode(this));
				
				if (contractor.getAgentRole() == ModelParams.CONTRACTOR){
					
					double[] attributes;
					attributes = (double[]) (e.info);
					
					if (attributes[0] == ModelParams.OFFERED_C){
						
						if (contractor.getAgentId() == sortedScore[0][1]) {
							
							// CALCULATE THE DIFF BETWEEN THE BEST AND THE SECOND BEST (for CONTRACTED farmer)
							double diff =  sortedScore[0][2] - benefitF[benefitF.length - 1];//sortedScore[1][2];
							this.setDiffSecondBest(diff);

							this.setContracting(ModelParams.CONTRACTED_F);
							attributes[1] = ModelParams.CONTRACTED_F;
							this.setCFContractorID(sortedScore[0][1]);
							this.setVolumeFarmer(sortedScore[0][3]);
							this.setCFContractorRate(((ContractorAgent)contractor).getCFContractRate());
							
							model.contractFarming.updateEdge(e, contractor, this, attributes);
						
						} else {
							// Update the status of turn down offered contract since farmer chose another contractor
							attributes[12] = 1;
						}
					}
				} 
			}
		}
	}
	
	//TODO THIRD STAGE OF CONTRACT FARMING PROCESS
	
	public void farmerHonourContract(SimState state) {
		
		Model model = (Model) state;
		
		// Average price of past cropping; at time 0 & 1 past price is seen as the current price
		double smPastPrice = 0;
		if (this.ordinaryRice == 1) {
			if (currentStep == 0 || currentStep == 1) smPastPrice = this.getSMMedianPrice();
			//model.smPriceAvg[currentStep];
			else smPastPrice = (model.smPriceAvg[currentStep-1] + model.smPriceAvg[currentStep-2])/2; 
		} else if (this.ordinaryRice == 0) {
			if (currentStep == 0 || currentStep == 2) smPastPrice = this.getSMMedianPrice();
			//model.smPriceAvg[currentStep];
			if (currentStep >= 4) smPastPrice = (model.smPriceAvg[currentStep-2] + model.smPriceAvg[currentStep-4])/2; 
		}
		
		//Random number of month in harvesting time
		this.setRandomHarvestMonth(model.random.nextInt(ModelParams.HARVESTING_LENGTH)); 
		double smCurrentPriceTrader = model.smPriceHarvestMonths[currentStep][this.getRandomHarvestMonth()];
		
		double yieldTrader;
		double costTrader;
		if (this.getAgentRole() == ModelParams.SMALL_F){
			yieldTrader = this.getYieldSmallFarmer();
			costTrader = this.getCostSmallFarmer();
		} else {
			yieldTrader = this.getYieldMediumFarmer();
			costTrader = this.getCostMediumFarmer();
		}
		
		//Update Volume Changes (IN CASE YIELD CHANGE)
		double volumeChanges = this.getVolumeFarmer();
		this.setVolumeFarmer(volumeChanges);
		
		//1. CALCULATE VOLUME, PROFIT FOR ALL NON_CONTRACTED_F ==> PROCEED WITHOUT ANY BREACH/HONOUR OPTIONS
		if (this.getContracting() == ModelParams.NON_CONTRACTED_F){
			
			benefitF[benefitF.length - 1] = ((smCurrentPriceTrader 
					* yieldTrader - costTrader) * this.getFarmSize());
			
			this.setProfitAgent(benefitF[benefitF.length - 1]);
		}
		
		//2. CONTRACTED FARMER, UPDATE ALL RELEVANT PROPERTIES & EDGES 
		if (this.getContracting() == ModelParams.CONTRACTED_F){
			
			double postBenefitFHonour = ((smPastPrice * (1 + this.getCFContractorRate()) 
					* this.getYieldLargeFarmer() - this.getCostLargeFarmer()) * this.getFarmSize())
					/ (1 + this.getAutonomyPremium());
			double postBenefitFBreach = ((smCurrentPriceTrader
					* this.getYieldLargeFarmer() - this.getCostLargeFarmer()) * this.getFarmSize())
					/ (1 + this.getAutonomyPremium())
					+ this.getCostLargeFarmer() * this.getFarmSize() * (1 + ModelParams.LOAN_CONTRACTOR_DEFAULT);
			double postBenefitMinF = Math.min(postBenefitFHonour, postBenefitFBreach);
			double postBenefitMaxF = Math.max(postBenefitFHonour, postBenefitFBreach);
			double riskF;
			riskF = postBenefitMaxF;
			
			Bag out = model.contractFarming.getEdges(this, null);
			for (int i = 0; i < out.size(); i++) {
				
				Edge e = (Edge) (out.get(i));
				double[] attributes;
				attributes = (double[])(e.info);
				
				CFAgent cfAgent = (CFAgent)(e.getOtherNode(this));
				int farmerID = this.getAgentId();
				int contractorID = cfAgent.getAgentId();
				int contractorIDIndex = contractorID - ModelParams.NO_FARMERS;
				
				if (this.getCFContractorID() == contractorID && cfAgent.getAgentRole() == ModelParams.CONTRACTOR){
					
					ContractorAgent contractor = (ContractorAgent) cfAgent;
					
					volumeF[contractorIDIndex] = volumeF[volumeF.length - 1];
					
					//OPTION 1: Break the contract and sell all produce in the Spot Market (NOT RETURN LOAN)
					benefitF[benefitF.length - 1] = ((smCurrentPriceTrader 
							* this.getYieldLargeFarmer() - this.getCostLargeFarmer()) * this.getFarmSize())
							/ (1 + this.getAutonomyPremium())
							+ this.getCostLargeFarmer() * this.getFarmSize()
							* (1 + ModelParams.LOAN_CONTRACTOR_DEFAULT);
					
					double postBenefitBreach = benefitF[benefitF.length - 1]
							- this.getDiffSecondBest() * this.getFarmerCommit();
					if (postBenefitBreach < postBenefitMinF) postBenefitBreach = postBenefitMinF;
					
					utilityF[utilityF.length-1] = (1 - Math.exp(-(postBenefitBreach - postBenefitMinF) / riskF))
							/ (1 - Math.exp(-(postBenefitMaxF - postBenefitMinF) / riskF));
					
					scoreF[scoreF.length - 1] = Math.pow(utilityF[utilityF.length - 1], weightUtility)
							* Math.pow(ModelParams.TRUST_VALUE_TRADER, (1 - weightUtility));
					if((this.ordinaryRice == 0) && currentStep % 2 == 0 && Double.isNaN(scoreF[scoreF.length -1]))
						System.out.println("NaN detected FOURTH FARMER-3 !!!!!!!!!!!");
					
					//OPTION 2: Stick with the contract (RETURN LOAN)
					double purchasingPrice = smPastPrice * (1 + contractor.getCFContractRate());
					if (contractor.getSupportPolicy() == ModelParams.SUPPOT_HARVESTING_MONTH){
						if (purchasingPrice < smCurrentPriceTrader) purchasingPrice = smCurrentPriceTrader;
					}
					
					benefitF[contractorIDIndex] = ((purchasingPrice * this.getYieldLargeFarmer()
							- this.getCostLargeFarmer()) * this.getFarmSize())
							/ (1 + this.getAutonomyPremium());
					
					utilityF[contractorIDIndex] = (1 - Math.exp(-(benefitF[contractorIDIndex] - postBenefitMinF) / riskF))
							/ (1 - Math.exp(-(postBenefitMaxF - postBenefitMinF) / riskF));
					
					trustF[contractorIDIndex] = this.trustBaseFarmer[contractorIDIndex]
							+ (1 - this.trustBaseFarmer[contractorIDIndex])
							* (1 - 1 / (ModelParams.TRUST_FACTOR * attributes[9] + 1 - ModelParams.TRUST_FACTOR));
					
					if (trustF[contractorIDIndex] < ModelParams.TRUST_LEVEL_MIN)
						trustF[contractorIDIndex] = ModelParams.TRUST_LEVEL_MIN;
					
					scoreF[contractorIDIndex] = Math.pow(utilityF[contractorIDIndex], weightUtility)
							* Math.pow(trustF[contractorIDIndex], (1-weightUtility));
					
					if((this.ordinaryRice == 0) && currentStep % 2 == 0 && Double.isNaN(scoreF[contractorIDIndex]))
						System.out.println("NaN detected FIFITH - FARMER-3 !!!!!!!!!!!");
					
					//3. CONTRACTED FARMER MAKE DECISION WHETHER TO BREACH OR HONOUR CONTRACT
					if (scoreF[scoreF.length - 1] > scoreF[contractorIDIndex]){
						
						// Update Farmer
						this.setContracting(ModelParams.CONTRACTED_F);
						this.setProfitAgent(benefitF[benefitF.length - 1]);
						this.setHonoring(ModelParams.UNTRUSTWORTHY_F);
						
						attributes[1] = ModelParams.CONTRACTED_F;
						attributes[3] = benefitF[benefitF.length - 1];
						attributes[5] = trustF[contractorIDIndex];
						attributes[11] = ModelParams.UNTRUSTWORTHY_F;
						
						// Update contractor
						// in that contract, the contractor loss the contracting cost & loan
						contractor.benefitC[farmerID] = - this.getFarmSize() * (ModelParams.CONTRACTING_COST_BASELINE + 
								this.getCostLargeFarmer() * contractor.getLoan());
						contractor.setProfitAgent(contractor.benefitC[farmerID]);
						attributes[2] = contractor.benefitC[farmerID];
						attributes[14] = contractor.benefitC[farmerID];
						
						// Set number of deal to 1
						attributes[8] = 1; 
						// Set trust base to trust base minimum
						contractor.setTrustBaseContractor(farmerID, ModelParams.TRUST_BASE_BREACH);
						contractor.trustC[farmerID] = contractor.trustBaseContractor[farmerID] 
								+ (1 - contractor.trustBaseContractor[farmerID])
								* (1 - 1 / (ModelParams.TRUST_FACTOR * attributes[8] + 1 - ModelParams.TRUST_FACTOR));
						if (contractor.trustC[farmerID] < ModelParams.TRUST_LEVEL_MIN)
							contractor.trustC[farmerID] = ModelParams.TRUST_LEVEL_MIN;
						attributes[4] = contractor.trustC[farmerID];
						attributes[13] = 1; // already have first contract
						
						model.contractFarming.updateEdge(e, contractor, this, attributes);
					}
				}
			}
		}
	}
	
	//TODO UPDATE TRUST AMONG FARMERS IN THEIR SPATIAL NETWORK 
	public void farmerUpdateTrust(SimState state) {
		
		Model model = (Model) state;
		
		if (this.getContracting() == ModelParams.NON_CONTRACTED_F || this.getContracting() == ModelParams.CONTRACTED_F){
			
			Bag out = model.contractFarming.getEdges(this, null);
			int totalNeighbours = out.size();
			
			int[] totalContracted = new int[ModelParams.NO_CONTRACTORS];
			
			for (int j = ModelParams.NO_FARMERS; j < ModelParams.NO_FARMERS + ModelParams.NO_CONTRACTORS; j++){
				
				int contractorIndex = j - ModelParams.NO_FARMERS;
				
				for (int i = 0; i < totalNeighbours; i++) {
					
					Edge e = (Edge) (out.get(i));
					CFAgent otherFarmer = (CFAgent)(e.getOtherNode(this));
					
					if (otherFarmer.getAgentRole() != ModelParams.CONTRACTOR){
				
						if(((FarmerAgent)(otherFarmer)).getCFContractorID() == j &&
								//Contracted farmer with that SPECIFIC contractor
								(otherFarmer).getHonoring() == ModelParams.TRUSTWORTHY_F){
								//Trustworthy Farmer
							
							totalContracted[contractorIndex] += 1;
						}
					}
				}
				
				double currentTrustBase = this.getTrustBaseFarmer(contractorIndex);
				
				//INCREASE trust with contract farming
				currentTrustBase += currentTrustBase * totalContracted[contractorIndex]/totalNeighbours;
				if (currentTrustBase > 0.9) currentTrustBase = 0.9;
				
				this.setTrustBaseFarmer(contractorIndex, currentTrustBase);
			}
		}
		
		if (! this.getBeingBreached()){
			
			Bag out = model.contractFarming.getEdges(this, null);
			
			int[] totalBeingBreached = new int[ModelParams.NO_CONTRACTORS];
			int[] totalNeighbours = new int[ModelParams.NO_CONTRACTORS];
			
			for (int j = ModelParams.NO_FARMERS; j < ModelParams.NO_FARMERS + ModelParams.NO_CONTRACTORS; j++){
				
				int contractorIndex = j - ModelParams.NO_FARMERS;
				
				for (int i = 0; i < out.size(); i++) {
					
					Edge e = (Edge) (out.get(i));
					CFAgent otherFarmer = (CFAgent)(e.getOtherNode(this));
					
					if (otherFarmer.getAgentRole() != ModelParams.CONTRACTOR){
						
						if (((FarmerAgent)(otherFarmer)).getCFContractorID() == j){
							totalNeighbours[contractorIndex] += 1;
							
							if (((FarmerAgent)(otherFarmer)).getBreachingContractor() == j)
								totalBeingBreached[contractorIndex] += 1;
						}
					}
				}
				
				double currentTrustBase = this.getTrustBaseFarmer(contractorIndex);
				
				//DECREASE trust with BREACHED contractor
				if (totalNeighbours[contractorIndex] == 0){continue;}
				else currentTrustBase -= currentTrustBase * totalBeingBreached[contractorIndex]
						/ totalNeighbours[contractorIndex];
				if (currentTrustBase < 0) currentTrustBase = 0;
				
				this.setTrustBaseFarmer(contractorIndex, currentTrustBase);
			}
		}
	}
}