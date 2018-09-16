package model;

import java.io.PrintWriter;
import util.*;

/**
 * Parameters of the agent-based simulation model
 */
public class ModelParams {
	
	int runsMC;
	long seed;

	ConfigFileReader config;
	String outputFile;
	
	// ########################################################################
	// 1. GENERAL PARAMETERS
	// ########################################################################
	
	// CONFIGURATION PARAMETERS
	double cfPriceConA;
	double cfPriceConAMin;
	double cfPriceConAMax;
	double cfPriceConB;
	
	int commitmentConA;
	int commitmentConAMin;
	int commitmentConAMax;
	int commitmentConB;
	
	int huskingFacilityConA;
	int huskingFacilityConB;
	
	int millingFacilityConA;
	int millingFacilityConB;
	
	int ordinaryRice;
	
	// SIMULATION STEPS
	public final static int MAX_STEPS = 18;
	
	// NUMBER OF AGENTS
	public final static int NO_FARMERS = 3000;
	public final static int NO_CONTRACTORS = 2;
	
	// FARMER AGENT TYPES
	public final static int F_TYPES = 3;
	
	// AGENTS' ROLES
	public final static int UNDEFINED_AGENT = 4;
	public final static int CONTRACTOR = 3;
	public final static int SMALL_F = 0;
	public final static int MEDIUM_F = 1;
	public final static int LARGE_F = 2;
	
	// AGENTS' CONTRACTING STATUS AT PRE-HARVESTING STAGE
	public final static int NON_OFFERED_C = 0;
	public final static int OFFERED_C = 1;
	
	public final static int NON_CONTRACTED_F = 2;
	public final static int CONTRACTED_F = 3;
	
	// AGENTS' TRUST-WORTHINESS AT POST-HARVESTING STAGE
	public final static int TRUSTWORTHY_C = 1;
	public final static int UNTRUSTWORTHY_C = 2;
	public final static int TRUSTWORTHY_F = 3;
	public final static int UNTRUSTWORTHY_F = 4;
	
	// YIELD SENSITIVITY ANALYSIS
	public final static double YIELD_BEST_SCENARIO = 1;

	// EXPORT PARITY SENSITIVITY ANALYSIS 
	public final static double EXPORT_BASE_SCENARIO = 1;

	// ########################################################################
	// 2. FARMER AGENTS' PARAMETERS  
	// ########################################################################
	
	// FAMR SIZE TYPOLOGY (ha)
	public final static double SMALL_F_PROPORTION = 0.544;
	public final static double MEDIUM_F_PROPORTION = 0.32;
	public final static double LARGE_F_PROPORTION = 0.136;
	
	public final static double SMALL_F_SIZE_MIN = 0.5;
	public final static double SMALL_F_SIZE_THRESHOLD = 1;
	public final static double MEDIUM_F_SIZE_THRESHOLD = 2;
	public final static double LARGE_F_SIZE_MAX = 3;
	
	// PRODUCTION STAGE - CROP YIELD (tons/ha)
	public final static double YIELD_SMALL_ORDINARY_TRA_WINTER = 6.08;
	public final static double YIELD_SMALL_ORDINARY_TRA_SUMMER = 4.68;
	public final static double YIELD_MEDIUM_ORDINARY_TRA_WINTER = 6.75;
	public final static double YIELD_MEDIUM_ORDINARY_TRA_SUMMER = 5.2;
	public final static double YIELD_LARGE_ORDINARY_IMPR_WINTER = 7.40;
	public final static double YIELD_LARGE_ORDINARY_IMPR_SUMMER = 5.75;
	
	public final static double YIELD_SMALL_JASMINE_TRA_WINTER = 6.38;
	public final static double YIELD_MEDIUM_JASMINE_TRA_WINTER = 7.09;
	public final static double YIELD_LARGE_JASMINE_IMPR_WINTER = 7.77;
	
	// PRODUCTION COST (1milVND/ha)
	public final static double COST_SMALL_ORDINARY_TRA_WINTER = 21.777;
	public final static double COST_SMALL_ORDINARY_TRA_SUMMER = 16.522;
	public final static double COST_MEDIUM_ORDINARY_TRA_WINTER = 20.536;
	public final static double COST_MEDIUM_ORDINARY_TRA_SUMMER = 15.461;
	public final static double COST_LARGE_ORDINARY_IMPR_WINTER = 18.532;
	public final static double COST_LARGE_ORDINARY_IMPR_SUMMER = 13.653;
	
	public final static double COST_SMALL_JASMINE_TRA_WINTER = 23.257;
	public final static double COST_MEDIUM_JASMINE_TRA_WINTER = 22.336;
	public final static double COST_LARGE_JASMINE_IMPR_WINTER = 19.083;
	
	// AUTONOMY PREMIUM
	public final static double AUTONOMY_FARMER_MIN = 0.1;
	public final static double AUTONOMY_FARMER_MAX = 1.0;
	
	// FARMER' NEIGHBORHOOD COVERAGE
	public final static double F_NEIGHBOR = 50;
	
	// FARMERS' COMMITMENT
	public final static double FARMER_COMMITMENT = 2;
	public final static double FARMER_COMMITMENT_VARIANCE = 2;
	
	// ########################################################################
	// 3. CONTRACTORS' STATE VARIABLES, ATTRIBUTES, AND PARAMETERS  
	// ########################################################################
	
	// CONTRACTOR DEMAND VOLUME
	public final static int CONTRACTOR_INITIAL_DEMAND = 4000;
	public final static double CONTRACTOR_INCREASE_DEMAND_RATE = 0.1;
	
	// CONTRACT FARMING DISTANCE & NUMBER OF OFFERS 
	public final static double C_COVERAGE = 500;
	public final static int DISTANCE_CONTRACT_REVIEW = 100;
	public final static int NO_CONTRACT_OFFERS = 2;
	
	// CONTRACT FARMING COST
	public final static double CONTRACTING_COST_BASELINE = 0.408; //(PER HA)
	
	// CONTRACT FARMING VS. TRADITIONAL STRUCTURE COST (VALUE CHAIN PERSPECTIVE)
	public final static double ORDINARY_SM_HUSKING_FIXED_COST = 1.225;
	public final static double ORDINARY_SM_MILLING_POLISHING_FIXED_COST = 1.066;		
	public final static double ORDINARY_SM_EXPORT_PRICE = 8.761;
	public final static double ORDINARY_CF_EXPORT_PRICE = 9.596;		//WorldBank assumption
	
//	public final static double JASMINE_SM_TRADER_FIXED_COST = 0.968;
	public final static double JASMINE_SM_HUSKING_FIXED_COST = 1.737;
	public final static double JASMINE_SM_MILLING_POLISHING_FIXED_COST = 1.899;
	public final static double JASMINE_SM_EXPORT_PRICE = 12.933;
	public final static double JASMINE_CF_EXPORT_PRICE = 13.350;		//WorldBank assumption
	
	public final static double CF_ORDINARY_HUSKING_COST_RATE = 0.6;
	public final static double CF_ORDINARY_MILLING_POLISHING_COST_RATE = 0.68;
	public final static double CF_JASMINE_HUSKING_COST_RATE = 0.54;
	public final static double CF_JASMINE_MILLING_POLISHING_COST_RATE = 0.61;
	public final static double EXPORTING_FIXED_COST = 0.52;
	
	// CONTRACTOR SUPPORT POLICY 
	public final static int SUPPOT_HARVESTING_MONTH = 1;
	
	public final static int HARVESTING_LENGTH = 3;
	public final static double LOAN_CONTRACTOR_DEFAULT = 0.075; //seed only
	
	// CONTRACTORS' COMMITMENT
	public final static int CONTRACTOR_COMMITED = 0;
	public final static int CONTRACTOR_NOT_COMMITTED_05 = 105;
	public final static int CONTRACTOR_NOT_COMMITTED_10 = 110;
	public final static int CONTRACTOR_NOT_COMMITTED_15 = 115;
	public final static int CONTRACTOR_NOT_COMMITTED_20 = 120;
	public final static int CONTRACTOR_NOT_COMMITTED_25 = 125;
	public final static int CONTRACTOR_NOT_COMMITTED_30 = 130;
	public final static int CONTRACTOR_NOT_COMMITTED_50 = 150;
	
	// ########################################################################
	// 4. TRUST ISSUE 
	// ########################################################################
	public final static double WEIGHT_UTILITY_CONTRACTOR = 0.9;
	public final static double WEIGHT_UTILITY_F_MIN = 0.1;
	public final static double WEIGHT_UTILITY_F_MAX = 0.9;
	
	public final static double TRUST_BASE_C = 0.5;
	public final static double TRUST_BASE_FC_MIN = 0.1;
	public final static double TRUST_BASE_FC_MAX = 0.5;
	public final static double TRUST_VALUE_TRADER = 0.9;
	
	public final static double TRUST_FACTOR = 0.5;
	public final static double TRUST_BASE_BREACH = 0.0;
	public final static double TRUST_LEVEL_MIN = 0.1;

	// ########################################################################
	// 5. SPOT MARKET AND OTHERS  
	// ########################################################################
	
	public final static double LOAN_TRADER = 0.15;
	
	public final static double PADDY_TO_RICE_CONV_WINTER = 0.5;
	public final static double PADDY_TO_RICE_CONV_SUMMER = 0.48;
	
	// VISUALIZATION
	public final static int REGION_WIDTH = 1001;
	public final static int REGION_HEIGHT = 1001;

	public final static int SMALL_F_DIAMETER = 3;
	public final static int MEDIUM_F_DIAMETER = 6;
	public final static int LARGE_F_DIAMETER = 9;
	public final static int C_DIAMETER = 30;
	
	// --------------------------- Get/Set methods ---------------------------//
	
	public int getRunsMC() {
		return runsMC;
	}
	public void setRunsMC(int _runsMC) {
		this.runsMC = _runsMC;
	}
	
	public long getSeed() {
		return seed;
	}
	public void setSeed(long _seed) {
		this.seed = _seed;
	}
	
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String _outputFile) {
		this.outputFile = _outputFile;
	}
	
	public int getNumSF() {
		return (int)(ModelParams.NO_FARMERS * ModelParams.SMALL_F_PROPORTION);
	}
	public int getNumMF() {
		return (int)(ModelParams.NO_FARMERS * ModelParams.MEDIUM_F_PROPORTION);
	}
	public int getNumLF() {
		return (int)(ModelParams.NO_FARMERS * ModelParams.LARGE_F_PROPORTION);
	}
	
	public double getCFPriceConA(){
		return cfPriceConA;
	}
	public void setCFPriceConA(double _cfPriceConA){
		this.cfPriceConA = _cfPriceConA;
	}
	
	public double getCFPriceConAMin(){
		return cfPriceConAMin;
	}
	public void setCFPriceConAMin(double _cfPriceConAMin){
		this.cfPriceConAMin = _cfPriceConAMin;
	}
	
	public double getCFPriceConAMax(){
		return cfPriceConAMax;
	}
	public void setCFPriceConAMax(double _cfPriceConAMax){
		this.cfPriceConAMax = _cfPriceConAMax;
	}
	
	public double getCFPriceConB(){
		return cfPriceConB;
	}
	public void setCFPriceConB(double _cfPriceConB){
		this.cfPriceConB = _cfPriceConB;
	}
	
	public void setCommitmentConA(int _commitmentConA){
		this.commitmentConA = _commitmentConA;
	}
	public void setCommitmentConAMin(int _commitmentConAMin){
		this.commitmentConAMin = _commitmentConAMin;
	}
	public void setCommitmentConAMax(int _commitmentConAMax){
		this.commitmentConAMax = _commitmentConAMax;
	}
	
	public void setCommitmentConB(int _commitmentConB){
		this.commitmentConB = _commitmentConB;
	}

	public void setHuskingFacilityConA(int _huskingFacilityConA){
		this.huskingFacilityConA = _huskingFacilityConA;
	}
	public void setHuskingFacilityConB(int _huskingFacilityConB){
		this.huskingFacilityConB = _huskingFacilityConB;
	}
	
	public void setMillingFacilityConA(int _millingFacilityConA){
		this.millingFacilityConA = _millingFacilityConA;
	}
	public void setMillingFacilityConB(int _millingFacilityConB){
		this.millingFacilityConB = _millingFacilityConB;
	}
	
	public int getOrdinaryRice(){
		return ordinaryRice;
	}
	public void setOrdinaryRice(int _ordinaryRice){
		this.ordinaryRice = _ordinaryRice;
	}
	
	/**
	 * Constructor of ModelParameters class
	 */
	public ModelParams() {}

	public String exportGeneral() {

		String result = "\n";

		result += "MC_runs = " + this.runsMC + "\n";
		result += "seed = " + this.seed + "\n" + "\n";
		
		result += "nrFAgents = " + ModelParams.NO_FARMERS + "\n";
		result += "numC = " + ModelParams.NO_CONTRACTORS + "\n";
		result += "numSF = " + this.getNumSF() + "\n";
		result += "numMF = " + this.getNumMF() + "\n";
		result += "numLF = " + this.getNumLF() + "\n" + "\n";
		
		result += "CFPriceConA = " + this.cfPriceConA + "\n";
		result += "CFPriceConB = " + this.cfPriceConB + "\n";
		result += "\n";
		
		if (this.commitmentConA == ModelParams.CONTRACTOR_COMMITED)
			result += "commitmentConA = COMMITTED\n";
		else if (this.commitmentConA == ModelParams.CONTRACTOR_NOT_COMMITTED_05)
			result += "commitmentConA = NOT_COMMITTED 5 PERCENT\n";
		else if (this.commitmentConA == ModelParams.CONTRACTOR_NOT_COMMITTED_10)
			result += "commitmentConA = NOT_COMMITTED 10 PERCENT\n";
		else if (this.commitmentConA == ModelParams.CONTRACTOR_NOT_COMMITTED_15)
			result += "commitmentConA = NOT_COMMITTED 15 PERCENT\n";
		else if (this.commitmentConA == ModelParams.CONTRACTOR_NOT_COMMITTED_20)
			result += "commitmentConA = NOT_COMMITTED 20 PERCENT\n";
		else if (this.commitmentConA == ModelParams.CONTRACTOR_NOT_COMMITTED_25)
			result += "commitmentConA = NOT_COMMITTED 25 PERCENT\n";
		else if (this.commitmentConA == ModelParams.CONTRACTOR_NOT_COMMITTED_30)
			result += "commitmentConA = NOT_COMMITTED 30 PERCENT\n";
		
		if (this.commitmentConB == ModelParams.CONTRACTOR_COMMITED)
			result += "commitmentConB = COMMITTED\n";
		else if (this.commitmentConB == ModelParams.CONTRACTOR_NOT_COMMITTED_05)
			result += "commitmentConB = NOT_COMMITTED 5 PERCENT\n";
		else if (this.commitmentConB == ModelParams.CONTRACTOR_NOT_COMMITTED_10)
			result += "commitmentConB = NOT_COMMITTED 10 PERCENT\n";
		else if (this.commitmentConB == ModelParams.CONTRACTOR_NOT_COMMITTED_15)
			result += "commitmentConB = NOT_COMMITTED 15 PERCENT\n";
		else if (this.commitmentConB == ModelParams.CONTRACTOR_NOT_COMMITTED_20)
			result += "commitmentConB = NOT_COMMITTED 20 PERCENT\n";
		else if (this.commitmentConB == ModelParams.CONTRACTOR_NOT_COMMITTED_25)
			result += "commitmentConB = NOT_COMMITTED 25 PERCENT\n";
		else if (this.commitmentConB == ModelParams.CONTRACTOR_NOT_COMMITTED_30)
			result += "commitmentConB = NOT_COMMITTED 30 PERCENT\n";
		result += "\n";
		
		if (this.huskingFacilityConA == 1) result += "huskingFacilityConA = true\n";
		else if (this.huskingFacilityConA == 0) result += "huskingFacilityConA = false\n";
		
		if (this.huskingFacilityConB == 1) result += "huskingFacilityConB = true\n";
		else if (this.huskingFacilityConB == 0) result += "huskingFacilityConB = false\n";
		result += "\n";
		
		if (this.millingFacilityConA == 1) result += "huskingFacilityConA = true\n";
		else if (this.millingFacilityConA == 0) result += "huskingFacilityConA = false\n";
		
		if (this.millingFacilityConB == 1) result += "huskingFacilityConB = true\n";
		else if (this.millingFacilityConB == 0) result += "huskingFacilityConB = false\n";
		result += "\n";
		
		if (ordinaryRice == 1) result += "ORDINARY RICE TYPE\n";
		else if (ordinaryRice == 0) result += "JASMINE RICE TYPE\n";
		
		return result;
	}
	
	/**
	 * Reads parameters from the configuration file
	 */
	public void readParameters(String CONFIGFILENAME) {
		
		// Read parameters from the file
		config = new ConfigFileReader(CONFIGFILENAME);

		config.readConfigFile();
		
		// Get global parameters
		this.runsMC = config.getParameterInteger("MCRuns");
		this.seed = config.getParameterInteger("seed");
		
		this.cfPriceConA = config.getParameterDouble("cfPriceConA");
		this.cfPriceConAMin = config.getParameterDouble("cfPriceConAMin");
		this.cfPriceConAMax = config.getParameterDouble("cfPriceConAMax");
		this.cfPriceConB = config.getParameterDouble("cfPriceConB");
		
		this.commitmentConA = config.getParameterInteger("commitmentConA");
		this.commitmentConAMin = config.getParameterInteger("commitmentConAMin");
		this.commitmentConAMax = config.getParameterInteger("commitmentConAMax");
		this.commitmentConB = config.getParameterInteger("commitmentConB");
		
		this.huskingFacilityConA = config.getParameterInteger("huskingFacilityConA");
		this.huskingFacilityConB = config.getParameterInteger("huskingFacilityConB");
		
		this.millingFacilityConA = config.getParameterInteger("millingFacilityConA");
		this.millingFacilityConB = config.getParameterInteger("millingFacilityConB");
		
		ordinaryRice = config.getParameterInteger("ordinaryRice");
	}

	/**
	 * Prints simple statistics evolution during the time.
	 */
	public void printParameters(PrintWriter writer) { 
		
		writer.println(this.exportGeneral());	}
}
