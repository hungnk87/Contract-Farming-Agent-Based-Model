package model;

import sim.engine.*;
import sim.util.*;
import sim.field.network.*;
import java.util.*;

/**
 * Class of the contractor agents behaviors
 */

public class ContractorAgent extends CFAgent implements Steppable {

	private static final long serialVersionUID = 1L;
	
	double loan;
	double[] demand = new double[ModelParams.MAX_STEPS];
	double farmCoverage;
	double contractorCommit;
	int supportPolicy;
	int huskingFacility;
	int millingFacility;
	double[] trustBaseContractor;
	double cfContractRate;
	
	double exportPriceCF = 0;
	double exportPriceSM = 0;
	double yieldLargeFarm = 0;
	double costLargeFarm = 0;
	double riceConversion = 0;
	
	double costHusking = 0;
	double costMillingPolishing = 0;
	double rateCFHusking = 0;
	double rateCFMillingPolishing = 0;
	
	double profitAgent; 		
	double volumeAchieved;
	double[] totalFinalVolume = new double[ModelParams.MAX_STEPS];
	boolean[] rateFinalVolume = new boolean[ModelParams.MAX_STEPS];
	
	int noBreachC[];
	double rateContractAccepted[];
	double rateContractSuccess[];
	double payoffsContractors[];

	double[] volumeC = new double[ModelParams.NO_FARMERS];
	double[] benefitC = new double[ModelParams.NO_FARMERS];
	double[] utilityC = new double[ModelParams.NO_FARMERS];
	double[] trustC = new double[ModelParams.NO_FARMERS];
	double[] scoreC = new double[ModelParams.NO_FARMERS];
	
	/**
	 *TODO CONSTRUCTOR a new instance of the Farmer agent class
	 */
	public ContractorAgent(int _agentId, int _agentRole, int _contracting, int _honoring, 
			double _loan, double _farmCoverage, double _weightUtility, int _ordinaryRice,
			double _conCommit, double[] _trustBaseCon, double _cfContractRate, 
			int _supportPolicy, int _huskingFacility, int _millingFacility) {

		super(_agentId, _agentRole, _contracting, _honoring, _weightUtility, _ordinaryRice);
		
		this.loan = _loan;
		this.farmCoverage = _farmCoverage;
		this.contractorCommit = _conCommit;
		this.trustBaseContractor = _trustBaseCon;
		this.cfContractRate = _cfContractRate;
		this.supportPolicy = _supportPolicy;
		this.huskingFacility = _huskingFacility;
		this.millingFacility = _millingFacility;
		
		this.noBreachC = new int[ModelParams.MAX_STEPS];
		this.rateContractAccepted = new double[ModelParams.MAX_STEPS];
		this.rateContractSuccess = new double[ModelParams.MAX_STEPS];
		this.payoffsContractors = new double[ModelParams.MAX_STEPS];

		for (int i = 0; i < ModelParams.MAX_STEPS; i++) {
			
			this.noBreachC[i] = 0;
			this.rateContractAccepted[i] = 0;
			this.rateContractSuccess[i] = 0;
			this.payoffsContractors[i] = 0;
		}
		
		this.profitAgent = 0; 		
		this.volumeAchieved = 0;
	}
	// --------------------------- Get/Set methods ---------------------------//

	public double getLoan(){
		return this.loan;
	}
	
	public double getCFContractRate(){
		return this.cfContractRate;
	}
	
	public int getSupportPolicy(){
		return this.supportPolicy;
	}
	
	public int getHuskingFacility(){
		return this.huskingFacility;
	}
	
	public int getMillingFacility(){
		return this.millingFacility;
	}
	
	public double getDemand(int step){
		return this.demand[step];
	}
	public void setDemand(int step, double _demand){
		this.demand[step] = _demand;
	}
	
	public double getFarmCoverage(){
		return this.farmCoverage;
	}
	
	public void setTrustBaseContractor(int _index, double _trustBaseContractor){
		this.trustBaseContractor[_index] = _trustBaseContractor;
	}

	public void setProfitAgent(double _profitAgent){
		this.profitAgent = _profitAgent;
	}
	
	public double getVolumeAchieved(){
		return volumeAchieved;
	}
	public void setVolumeAchieved(double _volumeAchieved){
		this.volumeAchieved = _volumeAchieved;
	}
	
	public double getTotalFinalVolume(int step){
		return totalFinalVolume[step];
	}
	public void setTotalFinalVolume (int step, double _totalFinalVolume){
		this.totalFinalVolume[step] = _totalFinalVolume;
	}
	
	public boolean getRateFinalVolume(int step){
		return rateFinalVolume[step];
	}
	public void setRateFinalVolume (int step, boolean _rateFinalVolume){
		this.rateFinalVolume[step] = _rateFinalVolume;
	}
	
	public double getYieldLargeFarm(){
		if (this.ordinaryRice == 1) {
			if (currentStep % 2 == 0) yieldLargeFarm = ModelParams.YIELD_LARGE_ORDINARY_IMPR_WINTER * Model.yieldSA;
			else yieldLargeFarm = ModelParams.YIELD_LARGE_ORDINARY_IMPR_SUMMER * Model.yieldSA;
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) yieldLargeFarm = ModelParams.YIELD_LARGE_JASMINE_IMPR_WINTER * Model.yieldSA;
			else yieldLargeFarm = 0;
		}
		return yieldLargeFarm;
	}
	
	public double getCostLargeFarm(){
		if (this.ordinaryRice == 1) {
			if (currentStep % 2 == 0) costLargeFarm = ModelParams.COST_LARGE_ORDINARY_IMPR_WINTER;
			else costLargeFarm = ModelParams.COST_LARGE_ORDINARY_IMPR_SUMMER;
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) costLargeFarm = ModelParams.COST_LARGE_JASMINE_IMPR_WINTER;
			else costLargeFarm = 0;
		}
		return costLargeFarm;
	}
	
	public double getRiceConversion(){
		if (currentStep % 2 == 0) riceConversion = ModelParams.PADDY_TO_RICE_CONV_WINTER;
		else riceConversion = ModelParams.PADDY_TO_RICE_CONV_SUMMER;
		return riceConversion;
	}
	
	public double getCFExportPrice(){
		if (this.ordinaryRice == 1)
			exportPriceCF = ModelParams.ORDINARY_CF_EXPORT_PRICE * Model.exportParity;
		if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) 
				exportPriceCF = ModelParams.JASMINE_CF_EXPORT_PRICE * Model.exportParity;
			else exportPriceCF = 0; 
		}
		return exportPriceCF;
	}
	
	public double getSMExportPrice(){
		if (this.ordinaryRice == 1)
			exportPriceSM = ModelParams.ORDINARY_SM_EXPORT_PRICE;
		if (this.ordinaryRice == 0) {
			if (currentStep % 2 == 0) 
				exportPriceSM = ModelParams.JASMINE_SM_EXPORT_PRICE;
			else exportPriceSM = 0; 
		}
		return exportPriceSM;
	}

	public double getCostHusking(){
		if (this.ordinaryRice == 1) 
			this.costHusking = ModelParams.ORDINARY_SM_HUSKING_FIXED_COST;
		else if (this.ordinaryRice == 0) 
			this.costHusking = ModelParams.JASMINE_SM_HUSKING_FIXED_COST;
		return costHusking;
	}
	
	public double getCostMillingPolishing(){
		if (this.ordinaryRice == 1)
			this.costMillingPolishing = ModelParams.ORDINARY_SM_MILLING_POLISHING_FIXED_COST;
		else if (this.ordinaryRice == 0) 
			this.costMillingPolishing = ModelParams.JASMINE_SM_MILLING_POLISHING_FIXED_COST;
		return costMillingPolishing;
	}
	
	public double getRateCFHusking(){
		if (this.ordinaryRice == 1) {
			if (this.getHuskingFacility() == 1) 
				this.rateCFHusking = ModelParams.CF_ORDINARY_HUSKING_COST_RATE;
			else if (this.getHuskingFacility() == 0) 
				this.rateCFHusking = 1;
		}
		if (this.ordinaryRice == 0) {
			if (this.getHuskingFacility() == 1) 
				this.rateCFHusking = ModelParams.CF_JASMINE_HUSKING_COST_RATE;
			else if (this.getHuskingFacility() == 0) 
				this.rateCFHusking = 1;
		}
		return rateCFHusking;
	}
	
	public double getRateCFMillingPolishing(){
		if (this.ordinaryRice == 1) {
			if (this.getHuskingFacility() == 1) 
				this.rateCFMillingPolishing = ModelParams.CF_ORDINARY_MILLING_POLISHING_COST_RATE;
			else if (this.getHuskingFacility() == 0) 
				this.rateCFMillingPolishing = 1;
		}
		if (this.ordinaryRice == 0) {
			if (this.getHuskingFacility() == 1) 
				this.rateCFMillingPolishing = ModelParams.CF_JASMINE_MILLING_POLISHING_COST_RATE;
			else if (this.getHuskingFacility() == 0)
				this.rateCFMillingPolishing = 1;
		}
		return rateCFMillingPolishing;
	}

	/*
	 * TODO FIRST STAGE OF CONTRACT FARMING PROCESS
	 */
	public void contractorOfferContracts(SimState state, double distanceThreMin, double distanceThreMax) {
		
		Model model = (Model) state;
		Bag out = model.contractFarming.getEdges(this, null);
		
		// 0. SET THE DEMAND FOR THIS CURRENT CROPPING
		if (this.ordinaryRice == 1) {
			if (currentStep == 0 || currentStep == 1)
				this.setDemand(currentStep, ModelParams.CONTRACTOR_INITIAL_DEMAND);
			else {
				double pastVolume = (this.getTotalFinalVolume(currentStep - 1) +
						this.getTotalFinalVolume(currentStep - 2)) / 2;
				this.setDemand(currentStep, pastVolume);

				if (this.getRateFinalVolume(currentStep - 1) && this.getRateFinalVolume(currentStep - 2)){
					pastVolume = pastVolume * (1 + ModelParams.CONTRACTOR_INCREASE_DEMAND_RATE);
					if (pastVolume > ModelParams.CONTRACTOR_INITIAL_DEMAND)
						pastVolume = ModelParams.CONTRACTOR_INITIAL_DEMAND;
					this.setDemand(currentStep, pastVolume * (1 + ModelParams.CONTRACTOR_INCREASE_DEMAND_RATE));
				}
			}
		} else if (this.ordinaryRice == 0) {
			if (currentStep % 2 != 0) this.setDemand(currentStep, 0);
			else {
				if (currentStep == 0 || currentStep == 2)
					this.setDemand(currentStep, ModelParams.CONTRACTOR_INITIAL_DEMAND);
				if (currentStep >= 4) {
					double pastVolume = (this.getTotalFinalVolume(currentStep - 2) +
							this.getTotalFinalVolume(currentStep - 4)) / 2;
					this.setDemand(currentStep, pastVolume);
					
					if (this.getRateFinalVolume(currentStep - 2) && this.getRateFinalVolume(currentStep - 4)){
						pastVolume = pastVolume * (1 + ModelParams.CONTRACTOR_INCREASE_DEMAND_RATE);
						if (pastVolume > ModelParams.CONTRACTOR_INITIAL_DEMAND)
							pastVolume = ModelParams.CONTRACTOR_INITIAL_DEMAND;
						this.setDemand(currentStep, pastVolume * (1 + ModelParams.CONTRACTOR_INCREASE_DEMAND_RATE));
					}
				}
			}
		}
		
		// 1. CONTRACTOR CALCULATE SCORES FOR ALL NEIGHBORING FARMERS
		// Average price of past cropping; at time 0 & 1 past price is seen as the current price
		double smPastPrice = 0;
		if (this.ordinaryRice == 1) {
			if (currentStep == 0 || currentStep == 1) smPastPrice = this.getSMMedianPrice();
			else smPastPrice = (model.smPriceAvg[currentStep-1] + model.smPriceAvg[currentStep-2])/2; 
		} else if (this.ordinaryRice == 0) {
			if (currentStep == 0 || currentStep == 2) smPastPrice = this.getSMMedianPrice();
			if (currentStep >= 4) smPastPrice = (model.smPriceAvg[currentStep-2] + model.smPriceAvg[currentStep-4])/2; 
		}
		
		// Contract Price  
		double pCF = (1 + this.getCFContractRate()) * smPastPrice;
		
		double conCFFixedCost = this.getCostHusking() * this.getRateCFHusking()
				+ this.getCostMillingPolishing() * this.getRateCFMillingPolishing()
				+ ModelParams.EXPORTING_FIXED_COST;
		
		//In PreHarvesting stage, contractor is assumed to have no intention to break contract
		//min Value is assumed to be 0 (not contract with farmer with losses) minus the FIRST_CONTRACT_COST & LOAN
		double preBenefitMinC = //0;
				- ModelParams.LARGE_F_SIZE_MAX * this.getCostLargeFarm() * this.getLoan()
				- ModelParams.LARGE_F_SIZE_MAX * ModelParams.CONTRACTING_COST_BASELINE;
		double preBenefitMaxCLarge = (this.getCFExportPrice()  
				- pCF - conCFFixedCost) * this.getYieldLargeFarm() 
				* ModelParams.LARGE_F_SIZE_MAX * this.getRiceConversion()
				- ModelParams.SMALL_F_SIZE_MIN * this.getCostLargeFarm() * this.getLoan()
				- ModelParams.SMALL_F_SIZE_MIN * ModelParams.CONTRACTING_COST_BASELINE;
		double preBenefitMaxCSmall = (this.getCFExportPrice()  
				- pCF - conCFFixedCost) * this.getYieldLargeFarm() 
				* ModelParams.SMALL_F_SIZE_MIN * this.getRiceConversion()
				- ModelParams.SMALL_F_SIZE_MIN * this.getCostLargeFarm() * this.getLoan()
				- ModelParams.SMALL_F_SIZE_MIN * ModelParams.CONTRACTING_COST_BASELINE;
		double preBenefitMaxC = Math.max(preBenefitMaxCSmall, preBenefitMaxCLarge);
		
		double riskC;
		riskC = preBenefitMaxC;
		
		List<double[]> sortScore = new ArrayList<>();

		for (int i = 0; i < out.size(); i++) {

			Edge e = (Edge) (out.get(i));
			double[] attributes;
			attributes = (double[]) (e.info);
			
			FarmerAgent farmer = (FarmerAgent) (e.getOtherNode(this));
			int farmerID = farmer.getAgentId();
			
			Double2D me = model.region.getObjectLocation(this);
			double distance = me.distance(model.region.getObjectLocation(farmer));
			
			if (distance >= distanceThreMin && distance < distanceThreMax){
				
				if (attributes[1] == ModelParams.NON_CONTRACTED_F 					// Non_Contracted_F (attributes)
						&& farmer.getContracting() == ModelParams.NON_CONTRACTED_F	// Non_Contracted_F (not with other contractors 
						&& attributes[12] == 0 										// Non_Refuse 
						&& attributes[11] == ModelParams.TRUSTWORTHY_F){			// Trustworthy Contractor (history)
						
					volumeC[farmerID] = farmer.getFarmSize() * this.getYieldLargeFarm()
							* this.getRiceConversion();
					benefitC[farmerID] = (this.getCFExportPrice() - pCF - conCFFixedCost) * volumeC[farmerID]
							- farmer.getFarmSize() * this.getCostLargeFarm() * this.getLoan()
							- farmer.getFarmSize() * ModelParams.CONTRACTING_COST_BASELINE;
					if (benefitC[farmerID] < preBenefitMinC) continue;
					
					double numDeal = attributes[8];
					utilityC[farmerID] = (1 - Math.exp(-(benefitC[farmerID] - preBenefitMinC) / riskC))
							/ (1 - Math.exp(-(preBenefitMaxC - preBenefitMinC) / riskC));
					trustC[farmerID] = trustBaseContractor[farmerID] + (1 - trustBaseContractor[farmerID])
							* (1 - 1 / (ModelParams.TRUST_FACTOR * numDeal + 1 - ModelParams.TRUST_FACTOR));
					if (trustC[farmerID] < ModelParams.TRUST_LEVEL_MIN)
						trustC[farmerID] = ModelParams.TRUST_LEVEL_MIN;
					scoreC[farmerID] = Math.pow(utilityC[farmerID], weightUtility)
							* Math.pow(trustC[farmerID], (1 - weightUtility));

					if (Double.isNaN(scoreC[farmerID])){
						System.out.println("NaN detected FIRST CONTRACTOR-1 !!!!!!!!!!!");
						System.out.println("contractorID: " + this.getAgentId()
						+ "; farmerID: " + farmerID
						+ "; CScore: " +  String.format("%.2f", scoreC[farmerID])
						+ "; benefitC: " + String.format("%.2f", benefitC[farmerID])
						+ "; utilityC: " + String.format("%.2f", utilityC[farmerID])
						+ "; preBenefitMinC: " + String.format("%.2f", preBenefitMinC)
						+ "; preBenefitMaxC: " + String.format("%.2f", preBenefitMaxC));
					}

					sortScore.add(new double[] { scoreC[farmerID], farmerID, volumeC[farmerID] });
					
					attributes[4] = trustC[farmerID];
					attributes[6] = scoreC[farmerID];
					model.contractFarming.updateEdge(e, this, farmer, attributes);
				}
			}
		}
			
		// 1.5 CALCULATE CURRENT VOLUME ACHIEVED FROM THOSE PREVIOUS CONTRACT OFFERS
		double totalAchieved = 0;
		
		for (int i = 0; i < out.size(); i++) {

			Edge e = (Edge) (out.get(i));
			FarmerAgent farmer = (FarmerAgent) (e.getOtherNode(this));
			double[] attributes;
			attributes = (double[]) (e.info);
			
			if (attributes[1] == ModelParams.CONTRACTED_F){
				totalAchieved += farmer.getVolumeFarmer() * this.getRiceConversion();
			}
		}
		this.setVolumeAchieved(totalAchieved);

		// 2. CONTRACTOR SENDS CONTRACTS TO THOSE FARMERS WITH HIGHEST SCORES
		// Sort with highest score (reversed order in 2d Array with Java 8)
		double[][] sortedScore = sortScore.toArray(new double[sortScore.size()][]);
		Arrays.sort(sortedScore, (v1, v2) -> Double.compare(v2[0], v1[0]));

		// Initialize ArrayList of neighboring farmer who are offered contracts and who are not
		List<Double> farmersOfferedID = new ArrayList<>();
		double demandTotal = 0;

		for (int i = 0; i < sortedScore.length; i++) {

			demandTotal += sortedScore[i][2];
			farmersOfferedID.add(sortedScore[i][1]);

			// If accumulation larger or equal contractor's demand -> stop sending contracts
			if (demandTotal >= this.getDemand(currentStep) - this.getVolumeAchieved())
				break;
		}
		
		// 3. UPDATE THE STATUS OF CONTRACTING BETWEEN THE CONTRACTORS AND NEIGHBORING FARMERS
		for (int i = 0; i < out.size(); i++) {

			Edge e = (Edge) (out.get(i));
			FarmerAgent farmer = (FarmerAgent) (e.getOtherNode(this));
			double[] attributes;
			attributes = (double[]) (e.info);

			for (int j = 0; j < farmersOfferedID.size(); j++) {
				
				if (farmer.getAgentId() == farmersOfferedID.get(j)) {
					attributes[0] = ModelParams.OFFERED_C;
				}
			}
			model.contractFarming.updateEdge(e, this, farmer, attributes);
		}
	}
	
	/*
	 * TODO FOURTH STAGE OF CONTRACT FARMING PROCESS
	 */
	public void contractorHonourContract(SimState state) {
		
		Model model = (Model) state;
		Bag out = model.contractFarming.getEdges(this, null);
		
		int contractorID = this.getAgentId();
		int contractorIDIndex = contractorID - ModelParams.NO_FARMERS;
		
		// Average price of past cropping; at time 0 & 1 past price is seen as the current price
		double smPastPrice = 0;
		if (this.ordinaryRice == 1) {
			if (currentStep == 0 || currentStep == 1) smPastPrice = this.getSMMedianPrice();
			else smPastPrice = (model.smPriceAvg[currentStep-1] + model.smPriceAvg[currentStep-2])/2; 
		} else if (this.ordinaryRice == 0) {
			if (currentStep == 0 || currentStep == 2) smPastPrice = this.getSMMedianPrice();
			if (currentStep >= 4) smPastPrice = (model.smPriceAvg[currentStep-2] + model.smPriceAvg[currentStep-4])/2; 
		}

		// Contract Price  
		double pCF = (1 + this.getCFContractRate()) * smPastPrice; 
		double pBreachCF = model.smPriceAvg[currentStep] * (1 + this.getCFContractRate());
		double conFixedCost = this.getCostHusking() * this.getRateCFHusking()
				+ this.getCostMillingPolishing() * this.getRateCFMillingPolishing()
				+ ModelParams.EXPORTING_FIXED_COST;
		
		double postBenefitMinC =
				- ModelParams.LARGE_F_SIZE_MAX * this.getCostLargeFarm() * this.getLoan()
				- ModelParams.LARGE_F_SIZE_MAX * ModelParams.CONTRACTING_COST_BASELINE;
		
		double[] smCurrentPriceTraderArray = model.smPriceHarvestMonths[currentStep];
		Arrays.sort(smCurrentPriceTraderArray);
		double postBenefitMaxCBreach = (this.getCFExportPrice() - conFixedCost
				- smCurrentPriceTraderArray[0] * (1 + this.getCFContractRate())) 
				* this.getYieldLargeFarm() * ModelParams.LARGE_F_SIZE_MAX * this.getRiceConversion()
				- ModelParams.SMALL_F_SIZE_MIN * ModelParams.CONTRACTING_COST_BASELINE;
		double postBenefitMaxCHonour = (this.getCFExportPrice() - conFixedCost
				- pCF)
				* this.getYieldLargeFarm() * ModelParams.LARGE_F_SIZE_MAX * this.getRiceConversion()
				- ModelParams.SMALL_F_SIZE_MIN * ModelParams.CONTRACTING_COST_BASELINE;
		double postBenefitMaxC = Math.max(postBenefitMaxCBreach, postBenefitMaxCHonour);
		
		double riskC;
		riskC = postBenefitMaxC;
		
		for (int i = 0; i < out.size(); i++) {

			Edge e = (Edge) (out.get(i));
			double[] attributes;
			attributes = (double[]) (e.info);
			
			FarmerAgent farmer = (FarmerAgent) (e.getOtherNode(this));
			int farmerID = farmer.getAgentId();
			
			double volumeRice = farmer.getVolumeFarmer() * this.getRiceConversion();
			
			if (attributes[1] == ModelParams.CONTRACTED_F 
					&& attributes[11] == ModelParams.TRUSTWORTHY_F){ //not yet breach the contract in same cropping
				
				//OPTION 1: Break the contract and force farmer to buy at a lower contract price
				double postCostBreak = 0;
				double postBenefitBreak = 0;
				double scoreBreak = 0;
				
				if (pBreachCF < pCF){
					
					postCostBreak = (pBreachCF + conFixedCost) * volumeRice
							+ farmer.getFarmSize() * ModelParams.CONTRACTING_COST_BASELINE;
					postBenefitBreak = this.getCFExportPrice() * volumeRice - postCostBreak;
					double utilityBreak = (1 - Math.exp(-(postBenefitBreak - postBenefitMinC) / riskC))
							/ (1 - Math.exp(-(postBenefitMaxC - postBenefitMinC) / riskC));
					
					scoreBreak = Math.pow(utilityBreak, weightUtility)
							* Math.pow(ModelParams.TRUST_VALUE_TRADER, (1 - weightUtility));
					
					if (Double.isNaN(scoreBreak)){
						System.out.println("NaN detected SIXTH CONTRACTOR-4 !!!!!!!!!!!");
						System.out.println("benefitBreak " + String.format("%.2f", postBenefitBreak)  
								+ " benefitMinC " + String.format("%.2f", postBenefitMinC) 
								+ " benefitMaxC " + String.format("%.2f", postBenefitMaxC)
								+ " utilityBreak " + String.format("%.2f", utilityBreak)
								+ " scoreBreak" + String.format("%.2f", scoreBreak));
					}
				}

				//OPTION 2: Honor the contract and stay with farmer 
				// Get the trading price in the specific harvesting time when that farmer harvest and trade
				double smCurrentPriceTrader = model.smPriceHarvestMonths[currentStep][farmer.getRandomHarvestMonth()];
				
				double purchasingPrice = pCF;
				if (this.getSupportPolicy() == ModelParams.SUPPOT_HARVESTING_MONTH){
					if (pCF < smCurrentPriceTrader) purchasingPrice = smCurrentPriceTrader;
				}
				
				double postCostHonour = (purchasingPrice + conFixedCost) * volumeRice
						+ farmer.getFarmSize() * ModelParams.CONTRACTING_COST_BASELINE;
				benefitC[farmerID] = this.getCFExportPrice() * volumeRice - postCostHonour;
				utilityC[farmerID] = (1 - Math.exp(-(benefitC[farmerID] - postBenefitMinC) / riskC))
						/ (1 - Math.exp(-(postBenefitMaxC - postBenefitMinC) / riskC));

				// Farmer honor the contract & deliver the produce to contractor
				trustC[farmerID] = 1;

				scoreC[farmerID] = Math.pow(utilityC[farmerID], weightUtility)
						* Math.pow(trustC[farmerID], (1 - weightUtility));
				
				if (Double.isNaN(scoreC[farmerID])){
					System.out.println("NaN detected SEVENTH (CONTRACTOR-4 !!!!!!!!!!!");
					System.out.println("benefitHonor " + String.format("%.2f", benefitC[farmerID]) 
							+ " benefitMinC " + String.format("%.2f", postBenefitMinC) 
							+ " benefitMaxC " + String.format("%.2f", postBenefitMaxC)
							+ " utilityHonor " + String.format("%.2f", utilityC[farmerID])
							+ " scoreHonor " + String.format("%.2f", scoreC[farmerID]));
				}
				
				trustC[farmerID] = trustBaseContractor[farmerID] + (1 - trustBaseContractor[farmerID])
						* (1 - 1 / (ModelParams.TRUST_FACTOR * attributes[8] + 1 - ModelParams.TRUST_FACTOR));
				if (trustC[farmerID] < ModelParams.TRUST_LEVEL_MIN)
					trustC[farmerID] = ModelParams.TRUST_LEVEL_MIN;
				attributes[4] = trustC[farmerID];
				
				// MASON manual: 0.0 may be drawn but 1.0 will never be drawn.
				double r = model.random.nextDouble(Model.INCLUDEZERO, Model.INCLUDEONE);
				
				double prob = 1; 
				if (this.contractorCommit == ModelParams.CONTRACTOR_NOT_COMMITTED_05) prob = 0.05;
				if (this.contractorCommit == ModelParams.CONTRACTOR_NOT_COMMITTED_10) prob = 0.1;
				if (this.contractorCommit == ModelParams.CONTRACTOR_NOT_COMMITTED_15) prob = 0.15;
				if (this.contractorCommit == ModelParams.CONTRACTOR_NOT_COMMITTED_20) prob = 0.2;
				if (this.contractorCommit == ModelParams.CONTRACTOR_NOT_COMMITTED_25) prob = 0.25;
				if (this.contractorCommit == ModelParams.CONTRACTOR_NOT_COMMITTED_30) prob = 0.3;
				if (this.contractorCommit == ModelParams.CONTRACTOR_NOT_COMMITTED_50) prob = 0.5;
				
				// If not committed and ...
				if (this.contractorCommit != ModelParams.CONTRACTOR_COMMITED && 	
						scoreBreak > scoreC[farmerID] && r < prob){
					
					//Update contractor
					this.setProfitAgent(postBenefitBreak);
					
					this.setHonoring(ModelParams.UNTRUSTWORTHY_C);
					
					attributes[2] = postBenefitBreak;
					attributes[10] = ModelParams.UNTRUSTWORTHY_C;
					attributes[13] = 1; // already have first contract
					attributes[14] = postCostBreak;
					
					//Update farmer
					farmer.benefitF[contractorIDIndex] = ((pBreachCF 
							* farmer.getYieldLargeFarmer() - farmer.getCostLargeFarmer()) * farmer.getFarmSize())
							/ (1 + farmer.getAutonomyPremium())
							+ farmer.getCostLargeFarmer() * farmer.getFarmSize() * (1 + this.getLoan());
					
					farmer.setProfitAgent(farmer.benefitF[contractorIDIndex]);
					
					attributes[3] = farmer.benefitF[contractorIDIndex];
					// Set number of deal to 1 (reset)
					attributes[9] = 1;
					
					// Set beingBreached to true
					farmer.setBeingBreached(true);
					farmer.setBreachingContractor(contractorID);
					
					// Set trust base to trust base minimum & calculate trust again
					farmer.setTrustBaseFarmer(contractorIDIndex, ModelParams.TRUST_BASE_BREACH);
					farmer.trustF[contractorIDIndex] = farmer.trustBaseFarmer[contractorIDIndex] 
							+ (1 - farmer.trustBaseFarmer[contractorIDIndex])
							* (1 - 1 / (ModelParams.TRUST_FACTOR * attributes[9] + 
									1 - ModelParams.TRUST_FACTOR));
					
					if (farmer.trustF[contractorIDIndex] < ModelParams.TRUST_LEVEL_MIN)
						farmer.trustF[contractorIDIndex] = ModelParams.TRUST_LEVEL_MIN;

					attributes[5] = farmer.trustF[contractorIDIndex];
					
					model.contractFarming.updateEdge(e, this, farmer, attributes);
				}
				
				// Either (NOT committed && NOT ...)
				if (this.contractorCommit == ModelParams.CONTRACTOR_COMMITED ||
						(this.contractorCommit != ModelParams.CONTRACTOR_COMMITED &&
								!(scoreBreak > scoreC[farmerID] && r < prob))) {
					
					// Update contractor
					this.setProfitAgent(benefitC[farmerID]);
					
					this.setHonoring(ModelParams.TRUSTWORTHY_C);
					
					attributes[2] = benefitC[farmerID];
					attributes[8]++; 
					attributes[10] = ModelParams.TRUSTWORTHY_C;
					// trust calculation has already updated above with attributes[4]
					attributes[13] = 1; // already have first contract
					attributes[14] = postCostHonour;
					
					// Update farmer
					farmer.benefitF[contractorIDIndex] = ((purchasingPrice * this.getYieldLargeFarm() - this.getCostLargeFarm()) * farmer.getFarmSize())
							/(1 + farmer.getAutonomyPremium());
					
					farmer.setProfitAgent(farmer.benefitF[contractorIDIndex]);
					
					farmer.setHonoring(ModelParams.TRUSTWORTHY_F);
					
					attributes[3] = farmer.benefitF[contractorIDIndex];
					attributes[9] ++;
					attributes[11] = ModelParams.TRUSTWORTHY_F;
					// trust with attributes[5] remain the same
					
					model.contractFarming.updateEdge(e, this, farmer, attributes);
				}
			}
		}
	}
	
	/*
	 * TODO CALCULATE THE TOTAL FINAL RICE VOLUME ACHIEVED
	 */
	public void contractorTotalVolume(SimState state) {
		
		Model model = (Model) state;
		Bag out = model.contractFarming.getEdges(this, null);
		
		double totalVolume = 0;
		
		for (int i = 0; i < out.size(); i++) {

			Edge e = (Edge) (out.get(i));
			FarmerAgent farmer = (FarmerAgent) (e.getOtherNode(this));
			
			double[] attributes;
			attributes = (double[]) (e.info);
			
			if (attributes[1] == ModelParams.CONTRACTED_F && 
					attributes[11] == ModelParams.TRUSTWORTHY_F){
			
				totalVolume += farmer.getVolumeFarmer() * this.getRiceConversion();
			}
		}
		if (totalVolume > this.getDemand(currentStep)){
			totalVolume = this.getDemand(currentStep);
			this.setRateFinalVolume(currentStep, true);
		}
		// Set final volume achieved
		this.setTotalFinalVolume(currentStep, totalVolume);
	}
}