package model;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.engine.*;
import sim.util.*;
import sim.field.continuous.*;
import sim.field.network.*;

import util.BriefFormatter;

/**
 * Simulation core: calculate output variables in one MC run
 */
public class Model extends SimState {

	public static final Logger log = Logger.getLogger(Model.class.getName());

	static final long serialVersionUID = 1L;

	static boolean INCLUDEZERO = true;
	static boolean INCLUDEONE = true;

	static String CONFIGFILENAME;

	int MC_RUN = -1;

	ModelParams params;
	
	public Continuous2D region = new Continuous2D (1.0, 
			ModelParams.REGION_WIDTH, ModelParams.REGION_HEIGHT);
	
	public Network contractFarming = new Network(false);
	
	// YIELD SENSITIVITY ANALYSIS (currently not studied)
	public static double yieldSA = ModelParams.YIELD_BEST_SCENARIO;
	public static double exportParity = ModelParams.EXPORT_BASE_SCENARIO;
	public static int supportPolicyA = 0;
	public static int supportPolicyB = 0;
	int[] supportContractorArray = new int[]{supportPolicyA, supportPolicyB};
	
	//LOAN (CURRENTLY SET FIXED AT 0.075) (currently not studied)
	public static double loanContractorA = 0.075;
	public static double loanContractorB = 0.075;
	double[] loanContractorArray = new double[]{loanContractorA, loanContractorB};
	
	// ENVIRONMENT VARIABLES ==================================================	
	double[] smPriceAvg = new double[ModelParams.MAX_STEPS];
	double[][] smPriceHarvestMonths = new double[ModelParams.MAX_STEPS][ModelParams.HARVESTING_LENGTH];
	
	int numSF = (int)(ModelParams.NO_FARMERS * ModelParams.SMALL_F_PROPORTION);
	int numMF = (int)(ModelParams.NO_FARMERS * ModelParams.MEDIUM_F_PROPORTION);
	int numLF = (int)(ModelParams.NO_FARMERS * ModelParams.LARGE_F_PROPORTION);
	
	// ########################################################################
	// 2. AGENTS' VARIABLES
	// ########################################################################

	Bag farmers;
	Bag contractors;
	
	int[][] contractedF = new int[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
	int[][] nonContractedF = new int[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
	
	int[][] noRefuseContract = new int[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	int[][] noRefuseContractSF = new int[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	int[][] noRefuseContractMF = new int[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	int[][] noRefuseContractLF = new int[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	
	int[][] noBreachF = new int[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
	int[][] noBreachTotalF = new int[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	int[][] noBreachC = new int[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];

	double[][] rateContractAccepted = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	double[][] rateContractSuccess = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	double[][] VolumeAchieved = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	
	double[][] sizeContractedF  = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
	double[][] sizeNonContractedF  = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
	double[][] payoffsContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
	double[][] payoffsNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
	double[] payoffsAllConF = new double[ModelParams.MAX_STEPS];
	
	double[][] payoffsConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	double[][] totalPayoffsCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	double[] payoffsWithoutCF = new double[ModelParams.MAX_STEPS];
	
	double[][] costConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	double[] costWithoutCF = new double[ModelParams.MAX_STEPS];
	
	double[][] profitMarginCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	double[] profitMarginWithoutCF = new double[ModelParams.MAX_STEPS];
	
	double[][] contractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	int[][] nonContractedFContractor = new int[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	
	double[][] trustFToContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	double[][] trustContractorToF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	
	double[][] demandStep = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	
	double[][] rateFBreach = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	
	// --------------------------- Get/Set methods ---------------------------//

	public static void setConfigFileName(String _configFileName) {
		CONFIGFILENAME = _configFileName;
	}
	public ModelParams getParametersObject() {
		return this.params;
	}

	public int[][] getContractedF() {
		return contractedF;
	}
	public int[][] getNonContractedF() {
		return nonContractedF;
	}

	public int[][] getNoBreachTotalF(){
		return noBreachTotalF;
	}
	public int[][] getNoBreachC(){
		return noBreachC;
	}
	
	public double[][] getContractedFContractor() {
		return contractedFContractor;
	}
	public int[][] getNonContractedFContractor() {
		return nonContractedFContractor;
	}

	public double[][] getVolumeAchieved() {
		return VolumeAchieved;
	}
	
	public double[][] getPayoffsConCF() {
		return payoffsConCF;
	}
	public double[][] getTotalPayoffsCF() {
		return totalPayoffsCF;
	}
	public double[] getPayoffsWithoutCF() {
		return payoffsWithoutCF;
	}
	
	public double[][] getProfitMarginCF(){
		return profitMarginCF;
	}
	public double[] getProfitMarginWithoutCF(){
		return profitMarginWithoutCF;
	}
	
	public double[][] getTrustFToContractor() {
		return trustFToContractor;
	}
	
	public double[][] getDemandStep(){
		return demandStep;
	}
	
	public double[][] getRateFBreach(){
		return rateFBreach;
	}

	// ########################################################################
	// Constructors
	// ########################################################################

	public Model(long seed){ super(seed);}
	
	/**
	 * Constructor with the Model Parameter object
	 */
	public Model(ModelParams _params) {

		super(_params.getSeed());

		try {

			// This block configure the logger with handler and formatter
			long millis = System.currentTimeMillis();
			FileHandler fh = new FileHandler("./logs/" + _params.getOutputFile() 
				+ "_" + millis + ".log");
			log.addHandler(fh);
			BriefFormatter formatter = new BriefFormatter();
			fh.setFormatter(formatter);

			log.setLevel(Level.FINE);

			log.log(Level.FINE, "Log file created at " + millis + " (System Time Millis)\n");

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		params = _params;
		
		// INITIALIZE ENVIRONMENT VARIABLES 
		// Price domestic wholesale from 2008 - 2017 collected from AGROINFO (mil/ton)
		// Average of 3 month
		double[] smPriceOrdinaryWinterAvg = new double[]{

			4.282, 4.055, 5.661, 5.419, 5.474,
			5.625, 5.385, 5.805, 5.462
		};
		
		double[] smPriceOrdinarySummerAvg = new double[]{

			3.564, 4.140, 6.717, 5.253, 4.660,
			5.354, 4.726, 4.789, 5.285
		};

		
		double[] smPriceJasmineWinterAvg = new double[] {

			5.682, 5.455, 7.061, 6.819, 6.874,
			7.025, 6.785, 7.205, 6.862
		};
		
		double[][] smPriceOrdinaryWinterHarvestMonths = new double[][]{
			
			{4.300, 4.294, 4.253},
			{4.229, 4.017, 3.920}, {5.515, 5.567, 5.900},
			{5.745, 5.300, 5.211}, {5.617, 5.473, 5.331},
			{5.891, 5.524, 5.460}, {5.176, 5.454, 5.527},
			{5.533, 5.870, 6.011}, {5.399, 5.514, 5.473}
		};
		
		double[][] smPriceOrdinarySummerHarvestMonths = new double[][]{
		
			{3.595, 3.482, 3.616},
			{3.485, 4.369, 4.565}, {7.148, 6.609, 6.395},
			{5.001, 5.498, 5.260}, {4.743, 4.690, 4.548},
			{5.255, 5.334, 5.475}, {4.690, 4.678, 4.810},
			{4.840, 4.790, 4.737}, {5.276, 5.190, 5.390}
		};
		
		double[][] smPriceJasmineWinterHarvestMonths = new double[][]{
			
			{5.701, 5.694, 5.653},
			{5.629, 5.417, 5.320}, {6.915, 6.967, 7.300},
			{7.145, 6.700, 6.611}, {7.017, 6.873, 6.731},
			{7.291, 6.924, 6.860}, {6.575, 6.854, 6.927},
			{6.933, 7.270, 7.411}, {6.799, 6.914, 6.873}
		};
		
		// Generate Past Spot Market Price based on types of rice and cropping seasons
		if (params.getOrdinaryRice() == 1) {
			for (int i = 0; i < ModelParams.MAX_STEPS; i++){
				if (i % 2 == 0){
					smPriceAvg[i] = smPriceOrdinaryWinterAvg[i/2];
					smPriceHarvestMonths[i] = smPriceOrdinaryWinterHarvestMonths[i/2];
				} else {
					smPriceAvg[i] = smPriceOrdinarySummerAvg[(i - 1)/2];
					smPriceHarvestMonths[i] = smPriceOrdinarySummerHarvestMonths[(i - 1)/2];
				}
			}
			
		} else if (params.getOrdinaryRice() == 0) {
			for (int i = 0; i < ModelParams.MAX_STEPS; i++){
				if (i % 2 == 0){
					smPriceAvg[i] = smPriceJasmineWinterAvg[i/2];
					smPriceHarvestMonths[i] = smPriceJasmineWinterHarvestMonths[i/2];
				}
				else {
					smPriceAvg[i] = 0;
					smPriceHarvestMonths[i] = new double[] {0,0,0};
				}
			}
		}
	}
	
	// --------------------------- SimState methods --------------------------//

	/**
	 * Sets up the simulation. The first method called when the simulation.
	 */
	public void start() {

		super.start();
		region.clear();
		contractFarming.clear();

		this.MC_RUN++;
		
		if ((numSF + numMF + numLF) != ModelParams.NO_FARMERS) {
			System.err.println("Error with the SF, MF, LF distribution. "
				+ "Check parameters " + "to be equal to the total number of agents \n");
		}

		// Initial scheduleCounter set to zero
		int scheduleCounter = 0;
		
		// INITIALIZE OUTPUT VARIABLES
		for (int i = 0; i < ModelParams.MAX_STEPS; i++) {
			
			for(int j = 0; j < ModelParams.F_TYPES; j++){
				contractedF[i][j] = 0;
				nonContractedF[i][j] = 0;
				
				noBreachF[i][j] = 0;
				
				sizeContractedF[i][j] = 0.;
				sizeNonContractedF[i][j] = 0.;
				payoffsContractedF[i][j] = 0.;
				payoffsNonContractedF[i][j] = 0.;
			}

			for(int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				
				noBreachC[i][j] = 0;
				
				noRefuseContract[i][j] = 0;
				noRefuseContractSF[i][j] = 0;
				noRefuseContractMF[i][j] = 0;
				noRefuseContractLF[i][j] = 0;
				noBreachTotalF[i][j] = 0;
				
				rateContractAccepted[i][j] = 0.;
				rateContractSuccess[i][j] = 0.;
				VolumeAchieved[i][j] = 0.;
				
				payoffsConCF[i][j] = 0.;
				totalPayoffsCF[i][j] = 0.;
				
				costConCF[i][j] = 0.;
				
				profitMarginCF[i][j] = 0.;
				
				contractedFContractor[i][j] = 0;
				nonContractedFContractor[i][j] = 0;
				
				trustFToContractor[i][j] = 0.;
				trustContractorToF[i][j] = 0.;
				
				demandStep[i][j] = 0.;
				
				rateFBreach[i][j] = 0.;
			}
			
			payoffsAllConF[i] = 0;
			
			costWithoutCF[i] = 0;
			payoffsWithoutCF[i] = 0;
			profitMarginWithoutCF[i] = 0.;
		}
		
		double[] cfContractRate = new double[]{
			params.cfPriceConA, params.cfPriceConB};
		
		int[] commitmentCon = new int[]{
				params.commitmentConA, params.commitmentConB};
		
		int[] huskingFacilityArray = new int[]{
				params.huskingFacilityConA, params.huskingFacilityConB};
		
		int[] millingFacilityArray = new int[]{
				params.millingFacilityConA, params.millingFacilityConB};
		
		// Generate parameter and contractor agents
		for (int i = ModelParams.NO_FARMERS; i < ModelParams.NO_FARMERS + ModelParams.NO_CONTRACTORS; i++) {
			
			int role = ModelParams.CONTRACTOR;
			
			int contracting = ModelParams.NON_OFFERED_C;
			
			int honoring = ModelParams.TRUSTWORTHY_C;
			
			double loan = loanContractorArray[i - ModelParams.NO_FARMERS];
			
			int supportPolicy = supportContractorArray[i - ModelParams.NO_FARMERS]; 
			
			int huskingFacility = huskingFacilityArray[i - ModelParams.NO_FARMERS];
			int millingFacility = millingFacilityArray[i - ModelParams.NO_FARMERS];
			
			double farmCoverage = ModelParams.C_COVERAGE;
			
			double weightUtility = ModelParams.WEIGHT_UTILITY_CONTRACTOR;
			
			int ordinaryRice = params.ordinaryRice;
			
			int commitment = commitmentCon[i - ModelParams.NO_FARMERS];
			
			double[] trustBaseContractor = new double[ModelParams.NO_FARMERS];
			for(int j = 0; j < ModelParams.NO_FARMERS; j++){
				trustBaseContractor[j] = ModelParams.TRUST_BASE_C;
			}
			
			double cfRate = cfContractRate[i - ModelParams.NO_FARMERS];
			
			CFAgent contractor = new ContractorAgent(i, role, contracting, honoring, 
					loan, farmCoverage, weightUtility, ordinaryRice,
					commitment, trustBaseContractor, cfRate, 
					supportPolicy, huskingFacility, millingFacility);

			if (i == ModelParams.NO_FARMERS){
				region.setObjectLocation(contractor, 
						new Double2D(ModelParams.REGION_WIDTH / 4, ModelParams.REGION_HEIGHT / 2));
			
			} else if (i == ModelParams.NO_FARMERS + 1){
				region.setObjectLocation(contractor, 
						new Double2D(ModelParams.REGION_WIDTH / 4 * 3, ModelParams.REGION_HEIGHT / 2));
			}
			
			contractFarming.addNode(contractor);
			schedule.scheduleRepeating(Schedule.EPOCH, scheduleCounter, contractor);
		}
		
		//TODO Generate parameters and farmer agents
		for (int i = 0; i < ModelParams.NO_FARMERS; i++) {
			
			int role = ModelParams.UNDEFINED_AGENT;
			
			int contracting = ModelParams.NON_CONTRACTED_F;
			
			int honoring = ModelParams.TRUSTWORTHY_F;
			
			double farmerCapacity = 0;
			
			double neighborRange;
			
			double autonomyPremium = ModelParams.AUTONOMY_FARMER_MIN + random.nextDouble()
				* (ModelParams.AUTONOMY_FARMER_MAX - ModelParams.AUTONOMY_FARMER_MIN);
			
			double futureTrade = triangularDistribution
					(ModelParams.FARMER_COMMITMENT - ModelParams.FARMER_COMMITMENT_VARIANCE, 
					ModelParams.FARMER_COMMITMENT + ModelParams.FARMER_COMMITMENT_VARIANCE, 
					ModelParams.FARMER_COMMITMENT);
			
			double[] trustBaseFarmer = new double[ModelParams.NO_CONTRACTORS];
			for(int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				trustBaseFarmer[j] = ModelParams.TRUST_BASE_FC_MIN + random.nextDouble()
					* (ModelParams.TRUST_BASE_FC_MAX - ModelParams.TRUST_BASE_FC_MIN);
			}
			
			if (i < numSF) {
				role = ModelParams.SMALL_F;
				
				farmerCapacity = ModelParams.SMALL_F_SIZE_MIN + random.nextDouble()
						* (ModelParams.SMALL_F_SIZE_THRESHOLD - ModelParams.SMALL_F_SIZE_MIN);
				
			} else if (i < numSF + numMF) {
				role = ModelParams.MEDIUM_F;
				
				farmerCapacity = ModelParams.SMALL_F_SIZE_THRESHOLD + random.nextDouble()
						* (ModelParams.MEDIUM_F_SIZE_THRESHOLD - ModelParams.SMALL_F_SIZE_THRESHOLD);
				
			} else if (i < numSF + numMF + numLF) {
				role = ModelParams.LARGE_F;
				
				farmerCapacity = ModelParams.MEDIUM_F_SIZE_THRESHOLD + random.nextDouble()
						* (ModelParams.LARGE_F_SIZE_MAX - ModelParams.MEDIUM_F_SIZE_THRESHOLD);
			}
			
			double weightUtility = ModelParams.WEIGHT_UTILITY_F_MIN + random.nextDouble()
			* (ModelParams.WEIGHT_UTILITY_F_MAX - ModelParams.WEIGHT_UTILITY_F_MIN);
			
			int ordinaryRice = params.ordinaryRice;
			
			neighborRange = ModelParams.F_NEIGHBOR;
			
			CFAgent farmer = new FarmerAgent(i, role, contracting, honoring, 
					farmerCapacity, neighborRange, autonomyPremium, weightUtility, 
					ordinaryRice, futureTrade, trustBaseFarmer);
			
			// Add random location to the each farmer agent
			region.setObjectLocation(farmer, 
					new Double2D(region.getWidth()*random.nextDouble(INCLUDEZERO, INCLUDEONE),
					region.getHeight()*random.nextDouble(INCLUDEZERO, INCLUDEONE)));

			contractFarming.addNode(farmer);
			schedule.scheduleRepeating(Schedule.EPOCH, scheduleCounter, farmer);
		}
		
		// Initialize Bag of contractors and farmers agents
		contractors = new Bag();
		farmers = new Bag();
		
		// Get Bag of all agents
		Bag cfAgents = contractFarming.getAllNodes();
		
		for (int i = 0; i < cfAgents.size(); i++){
			if(((CFAgent)cfAgents.get(i)).getAgentRole() == ModelParams.CONTRACTOR) {
				contractors.add(cfAgents.get(i));
			}
			else{ 
				farmers.add(cfAgents.get(i));
			}
		}
		
		// Create Edges among contractors and all their "neighbor" farmers
		for (int i = 0; i < contractors.size(); i++){
			Double2D contractorLoc;
			contractorLoc = region.getObjectLocation(contractors.get(i));
			
			for (int j = 0; j < farmers.size(); j++){
				
				Double2D farmerLoc;
				farmerLoc = region.getObjectLocation(farmers.get(j));
				
				double distance = contractorLoc.distance(farmerLoc);
				
				/*
				[0] - contractor contracting status
				[1] - farmer contracting status
				[2] - contractor profit under Contract Farming scheme (gain & loss)
				[3] - farmer profit under Contract Farming scheme (gain & loss)
				[4] - contractor trust
				[5] - farmer trust
				[6] - contractor score
				[7] - farmer score
				[8] - contractor deal
				[9] - farmer deal
				[10] - contractor honoring
				[11] - farmer honoring
				[12] - farmer refuse contract in stage 2
				[13] - not yet first contract (0 - no; 1 - yes)
				[14] - contractor cost
				 */

				double[] attributes = new double[15];
				attributes[0] = ModelParams.NON_OFFERED_C;
				attributes[1] = ModelParams.NON_CONTRACTED_F;
				attributes[8] = 0;
				attributes[9] = 0;
				attributes[10] = ModelParams.TRUSTWORTHY_C;
				attributes[11] = ModelParams.TRUSTWORTHY_F;
				attributes[12] = 0; // not yet refuse
				attributes[13] = 0; // first contract
				
				if(distance < ((ContractorAgent)(contractors.get(i))).getFarmCoverage()){
					
					contractFarming.addEdge(contractors.get(i), farmers.get(j), attributes);
				}
			}
		}
		
		// Create Edges among neighboring farmers
		for (int i = 0; i < farmers.size(); i++){
			
			Double2D farmerLoc;
			farmerLoc = region.getObjectLocation(farmers.get(i));
			Bag farmerNeighbors = new Bag();
			
			Bag allNeighbors = region.getNeighborsWithinDistance(farmerLoc, 
					((FarmerAgent)(farmers.get(i))).getNeighborRange());
			
			for(int j = 0; j < allNeighbors.size(); j++){
				CFAgent onlyFarmer = (CFAgent)allNeighbors.get(j);
				if(onlyFarmer.getAgentRole() != ModelParams.CONTRACTOR){
					farmerNeighbors.add(onlyFarmer);
				}
			}
			
			for(int j = 0; j < farmerNeighbors.size(); j++){
				
				double[] score = new double[2];
				contractFarming.addEdge(farmers.get(i), farmerNeighbors.get(j), score);
			}
		}
		
		setAnonymousAgentApriori(scheduleCounter);
		scheduleCounter++;
		setAnonymousAgentAposteriori(scheduleCounter);
	}

	/**
	 * Adds the anonymous agent to schedule (at the beginning of each step), 
	 * which calculates the statistics.
	 */
	private void setAnonymousAgentApriori(int scheduleCounter) {

		// Add to the schedule at the end of each step
		schedule.scheduleRepeating(Schedule.EPOCH, scheduleCounter, new Steppable() {
			private static final long serialVersionUID = -2837885990121299044L;

			public void step(SimState state) {

				Model model = (Model) state;
				
				for (int i = 0; i < contractors.size(); i++) {
					
					ContractorAgent contractor = (ContractorAgent) contractors.get(i);
					
					contractor.setContracting(ModelParams.NON_OFFERED_C);
					contractor.setHonoring(ModelParams.TRUSTWORTHY_C);
					
					Bag out = model.contractFarming.getEdges(contractor, null);

					for (int j = 0; j < out.size(); j++) {
						Edge e = (Edge) (out.get(j));
						double[] attributes;
						attributes = (double[]) (e.info);
						
						FarmerAgent farmer = (FarmerAgent) (e.getOtherNode(contractor));

						attributes[0] = ModelParams.NON_OFFERED_C;
						attributes[1] = ModelParams.NON_CONTRACTED_F;
						attributes[2] = 0;
						attributes[3] = 0;
						attributes[12] = 0; // Refuse contract status (0 = false; 1 = true)
						attributes[14] = 0;
						
						// Keep the history of untrustworthiness within the pairwise relationship
						contractFarming.updateEdge(e, contractor, farmer, attributes);
					}
				}
				
				for (int i = 0; i < farmers.size(); i++) {
					
					FarmerAgent farmer = (FarmerAgent) farmers.get(i);
					
					farmer.setContracting(ModelParams.NON_CONTRACTED_F);
					farmer.setHonoring(ModelParams.TRUSTWORTHY_F);
					farmer.setBeingBreached(false);
					farmer.setBreachingContractor(ModelParams.NO_FARMERS + ModelParams.NO_CONTRACTORS);
					farmer.setCFContractorID(0);
					farmer.setCFContractorRate(0);
				}
			}
		});
	}
	
	public double triangularDistribution(double a, double b, double c) {
		
	    double tri = (c - a) / (b - a);
	    double rand = random.nextDouble();
	    
	    if (rand < tri) {
	        return a + Math.sqrt(rand * (b - a) * (c - a));
	    } else {
	        return b - Math.sqrt((1 - rand) * (b - a) * (b - c));
	    }
	}

	/**
	 * Adds the anonymous agent to schedule (at the end of each step), which calculates the statistics.
	 */
	private void setAnonymousAgentAposteriori(int scheduleCounter) {

		// Add to the schedule at the end of each step
		schedule.scheduleRepeating(Schedule.EPOCH, scheduleCounter, new Steppable() {
			private static final long serialVersionUID = 3078492735754898981L;

			public void step(SimState state) {
				
				Model model = (Model) state;

				int currentStep = (int) schedule.getSteps();
				
				// ########################################################################
				// RESET ALL OUTPUT VARAIBLES
				// ########################################################################
				
				for (int i = 0; i < ModelParams.F_TYPES; i++){
					contractedF[currentStep][i] = 0;
					nonContractedF[currentStep][i] = 0;
					
					noBreachF[currentStep][i] = 0;
					
					sizeContractedF[currentStep][i] = 0.;
					sizeNonContractedF[currentStep][i] = 0.;
					payoffsContractedF[currentStep][i] = 0.;
					payoffsNonContractedF[currentStep][i] = 0.;
				}

				for (int i = 0; i < ModelParams.NO_CONTRACTORS; i++){
					
					noBreachC[currentStep][i] = 0;
					
					noRefuseContract[currentStep][i] = 0;
					noRefuseContractSF[currentStep][i] = 0;
					noRefuseContractMF[currentStep][i] = 0;
					noRefuseContractLF[currentStep][i] = 0;
					noBreachTotalF[currentStep][i] = 0;
					
					rateContractAccepted[currentStep][i] = 0.; 
					rateContractSuccess[currentStep][i] = 0.; 
					VolumeAchieved[currentStep][i] = 0.; 
					
					payoffsConCF[currentStep][i] = 0.;
					totalPayoffsCF[currentStep][i] = 0.;
					
					costConCF[currentStep][i] = 0.;
					
					profitMarginCF[currentStep][i] = 0.;
					
					contractedFContractor[currentStep][i] = 0;
					nonContractedFContractor[currentStep][i] = 0;
					
					trustFToContractor[currentStep][i] = 0.;
					trustContractorToF[currentStep][i] = 0.;
					
					demandStep[currentStep][i] = 0.;
					
					rateFBreach[currentStep][i] = 0.;
				}
				
				payoffsAllConF[currentStep] = 0;
				
				costWithoutCF[currentStep] = 0;
				payoffsWithoutCF[currentStep] = 0;
				profitMarginWithoutCF[currentStep] = 0.;
				
				// ########################################################################
				// TODO RUN ALL CONTRACT FARMING STAGES
				// ########################################################################
					
				for (int k = 0; k < ModelParams.NO_CONTRACT_OFFERS; k++){
					
					for (int j = 0; j < ModelParams.C_COVERAGE; j = j + ModelParams.DISTANCE_CONTRACT_REVIEW){
						
						for (int i = 0; i < contractors.size(); i++){
							((ContractorAgent) contractors.get(i)).
							contractorOfferContracts(state, j, j + ModelParams.DISTANCE_CONTRACT_REVIEW);
						}
						for (int i = 0; i < farmers.size(); i++){
							((FarmerAgent) farmers.get(i)).farmerAcceptContracts(state);
						}
					}
				}
				
				for (int i = 0; i < farmers.size(); i++){
					((FarmerAgent) farmers.get(i)).farmerHonourContract(state);
				}
				
				for (int i = 0; i < contractors.size(); i++){
					((ContractorAgent) contractors.get(i)).contractorHonourContract(state);
				}
				
				for (int i = 0; i < contractors.size(); i++){
					((ContractorAgent) contractors.get(i)).contractorTotalVolume(state);
				}
				
				for (int i = 0; i < farmers.size(); i++){
					((FarmerAgent) farmers.get(i)).farmerUpdateTrust(state);
				}
				
				// ########################################################################
				// CALCULATE ALL OUTPUT VARIABLES
				// ########################################################################
				
				for (int i = 0; i < farmers.size(); i++) {
					
					FarmerAgent farmer = (FarmerAgent) farmers.get(i);
					
					if (farmer.getAgentRole() == ModelParams.SMALL_F) {
						
						if (farmer.getHonoring() == ModelParams.UNTRUSTWORTHY_F) noBreachF[currentStep][0]++;
						if (farmer.getContracting() == 	ModelParams.CONTRACTED_F){
							contractedF[currentStep][0]++;
							payoffsAllConF[currentStep] += farmer.getProfitAgent();
							sizeContractedF[currentStep][0] += farmer.getFarmSize();
						}
						if (farmer.getContracting() == ModelParams.NON_CONTRACTED_F) {
							nonContractedF[currentStep][0]++;
							payoffsNonContractedF[currentStep][0] += farmer.getProfitAgent();
							sizeNonContractedF[currentStep][0] += farmer.getFarmSize();
						}
					}
					if (farmer.getAgentRole() == ModelParams.MEDIUM_F) {
						
						if (farmer.getHonoring() == ModelParams.UNTRUSTWORTHY_F) noBreachF[currentStep][1]++;
						if (farmer.getContracting() == 	ModelParams.CONTRACTED_F){
							contractedF[currentStep][1]++;
							payoffsAllConF[currentStep] += farmer.getProfitAgent();
							sizeContractedF[currentStep][1] += farmer.getFarmSize();
						}
						if (farmer.getContracting() == ModelParams.NON_CONTRACTED_F) {
							nonContractedF[currentStep][1]++;
							payoffsNonContractedF[currentStep][1] += farmer.getProfitAgent();
							sizeNonContractedF[currentStep][1] += farmer.getFarmSize();
						}
					}
					if (farmer.getAgentRole() == ModelParams.LARGE_F) {
						
						if (farmer.getHonoring() == ModelParams.UNTRUSTWORTHY_F) noBreachF[currentStep][2]++;
						if (farmer.getContracting() == 	ModelParams.CONTRACTED_F){
							contractedF[currentStep][2]++;
							payoffsAllConF[currentStep] += farmer.getProfitAgent();
							sizeContractedF[currentStep][2] += farmer.getFarmSize();
						}
						if (farmer.getContracting() == ModelParams.NON_CONTRACTED_F) {
							nonContractedF[currentStep][2]++;
							payoffsNonContractedF[currentStep][2] += farmer.getProfitAgent();
							sizeNonContractedF[currentStep][2] += farmer.getFarmSize();
						}
					}
				}
				
				for (int i = 0; i < contractors.size(); i++) {
					
					ContractorAgent contractor = (ContractorAgent)contractors.get(i);
					Bag out = model.contractFarming.getEdges(contractor, null);
					
					int noRefuse = 0;
					int noRefuseSmallF = 0;
					int noRefuseMediumF = 0;
					int noRefuseLargeF = 0;
					
					int noBreachSmallF = 0;
					int noBreachMediumF = 0;
					int noBreachLargeF = 0;
					int noBreachCon = 0;
					
					double noContractedF = 0;
					int noNonContractedF = 0;
					
					double volumeAccum = 0;
					
					double payoffsCF = 0;
					
					double costCF = 0;
					
					double trustFarmerToContractor = 0;
					
					double trustContractorToFarmer = 0;
					
					for (int j = 0; j < out.size(); j++) {
						
						Edge e = (Edge) (out.get(j));
						FarmerAgent farmer = (FarmerAgent)(e.getOtherNode(contractor));
						double[] attributes;
						attributes = (double[]) (e.info);
						double trustC = attributes[4];
						double trustF = attributes[5];
						
						if (attributes[1] == ModelParams.CONTRACTED_F){
							
							noContractedF++;
							// Profit: gain from trustworthy & loss from untrustworthy farmers
							payoffsCF += attributes[2];
							
							costCF += attributes[14];
							
							if (attributes[11] == ModelParams.UNTRUSTWORTHY_F){
								
								if (farmer.getAgentRole() == ModelParams.SMALL_F) noBreachSmallF++;
								else if (farmer.getAgentRole() == ModelParams.MEDIUM_F) noBreachMediumF++;
								else if (farmer.getAgentRole() == ModelParams.LARGE_F) noBreachLargeF++;
							}
							
							if (attributes[11] == ModelParams.TRUSTWORTHY_F){
								
								volumeAccum += farmer.getVolumeFarmer();
								
								if (attributes[10] == ModelParams.UNTRUSTWORTHY_C) {
									noBreachCon++;
								}
							}
						}
						
						if (attributes[1] == ModelParams.NON_CONTRACTED_F){
							noNonContractedF++;
						}

						trustFarmerToContractor += trustF;
						trustContractorToFarmer += trustC;
					}
					
					//Calculate No Refuse
					noRefuseContract[currentStep][i] = noRefuse;
					noRefuseContractSF[currentStep][i] = noRefuseSmallF;
					noRefuseContractMF[currentStep][i] = noRefuseMediumF;
					noRefuseContractLF[currentStep][i] = noRefuseLargeF;
					
					// Calculate no of breach in each contractor
					noBreachC[currentStep][i] = noBreachCon;
					
					// Calculate no of breach from types of farmers in each contractor
					noBreachTotalF[currentStep][i] = noBreachSmallF + noBreachMediumF + noBreachLargeF;
					
					// Calculate contract success at final stage
					rateContractSuccess[currentStep][i] = (noContractedF - noBreachTotalF[currentStep][i]) / noContractedF * 100;
					
					// Calculate total volume achieved at the post-harvesting stage
					VolumeAchieved[currentStep][i] = volumeAccum * contractor.getRiceConversion();
					if (VolumeAchieved[currentStep][i] > ModelParams.CONTRACTOR_INITIAL_DEMAND) 
						VolumeAchieved[currentStep][i] = ModelParams.CONTRACTOR_INITIAL_DEMAND;
					
					// Calculate payoffs of each contractor
					payoffsConCF[currentStep][i] = payoffsCF;
					
					// Calculate cost of each contractor
					costConCF[currentStep][i] = costCF;
					
					// Calculate profit margin of each contractor
					profitMarginCF[currentStep][i] = payoffsConCF[currentStep][i] / costConCF[currentStep][i] * 100;

						
					// Calculate no contracted vs. non-contracted farmer
					contractedFContractor[currentStep][i] = noContractedF;
					nonContractedFContractor[currentStep][i] = noNonContractedF;
					
					// Calculate trust from Contractor to Farmer and vice versa
					trustFToContractor[currentStep][i] = trustFarmerToContractor / (noContractedF + noNonContractedF);
					trustContractorToF[currentStep][i] = trustContractorToFarmer / (noContractedF + noNonContractedF);
					
					demandStep[currentStep][i] = contractor.getDemand(currentStep);
					
					// Calculate percentage of breaching farmer among contracted farmer
					rateFBreach[currentStep][i] = (noBreachSmallF + noBreachMediumF + noBreachLargeF) / noContractedF * 100;
				}
				
				ContractorAgent contractor = (ContractorAgent)contractors.get(0);
				costWithoutCF[currentStep] = ModelParams.CONTRACTOR_INITIAL_DEMAND * (smPriceAvg[currentStep] //+ contractor.getCostTrader() 
					+ contractor.getCostHusking() + contractor.getCostMillingPolishing() + ModelParams.EXPORTING_FIXED_COST);
				payoffsWithoutCF[currentStep] = contractor.getSMExportPrice() * ModelParams.CONTRACTOR_INITIAL_DEMAND
					- costWithoutCF[currentStep];
				
				profitMarginWithoutCF[currentStep] = payoffsWithoutCF[currentStep] / costWithoutCF[currentStep] * 100;
			}
		});
	}
}
