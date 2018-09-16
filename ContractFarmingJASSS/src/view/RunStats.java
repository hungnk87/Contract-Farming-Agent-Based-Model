package view;

import java.io.PrintWriter;
import org.apache.commons.math3.stat.descriptive.*;
import model.ModelParams;

/**
 * StatsRun for the Contract Farming Model
 * 
 * This class will store all the results for the MonteCarlo simulation. 
 * It will also update the stats metrics
 */

public class RunStats {

	private int numberRuns; 
	private String expName; 
	
	private int contractedF[][][]; 		
	private double avgContractedF[][];	
	private double stdContractedF[][];	
	private double minContractedF[][];
	private double maxContractedF[][];
	
	private int nonContractedF[][][]; 		
	private double avgNonContractedF[][];	
	private double stdNonContractedF[][];	
	private double minNonContractedF[][];
	private double maxNonContractedF[][];
	
	
	private int noRefuseContract[][][]; 		
	private double avgNoRefuseContract[][];	
	private double stdNoRefuseContract[][];	
	private double minNoRefuseContract[][];
	private double maxNoRefuseContract[][];
	
	private int noRefuseContractSF[][][]; 		
	private double avgNoRefuseContractSF[][];	
	private double stdNoRefuseContractSF[][];	
	private double minNoRefuseContractSF[][];
	private double maxNoRefuseContractSF[][];
	
	private int noRefuseContractMF[][][]; 		
	private double avgNoRefuseContractMF[][];	
	private double stdNoRefuseContractMF[][];	
	private double minNoRefuseContractMF[][];
	private double maxNoRefuseContractMF[][];
	
	private int noRefuseContractLF[][][]; 		
	private double avgNoRefuseContractLF[][];	
	private double stdNoRefuseContractLF[][];	
	private double minNoRefuseContractLF[][];
	private double maxNoRefuseContractLF[][];
	
	
	private int noBeachF[][][];
	private double avgNoBeachF[][];
	private double stdNoBeachF[][];
	private double minNoBeachF[][];
	private double maxNoBeachF[][];
	
	private int noBeachTotalF[][][];
	private double avgNoBeachTotalF[][];
	private double stdNoBeachTotalF[][];
	private double minNoBeachTotalF[][];
	private double maxNoBeachTotalF[][];
	
	private int noBeachC[][][];
	private double avgNoBeachC[][];
	private double stdNoBeachC[][];
	private double minNoBeachC[][];
	private double maxNoBeachC[][];
	
	
	private double rateContractAccepted[][][];
	private double avgRateContractAccepted[][];
	private double stdRateContractAccepted[][];
	private double minRateContractAccepted[][];
	private double maxRateContractAccepted[][];
	
	private double rateContractSuccess[][][];
	private double avgRateContractSuccess[][];
	private double stdRateContractSuccess[][];
	private double minRateContractSuccess[][];
	private double maxRateContractSuccess[][];
	
	private double volumeAchieved[][][];
	private double avgVolumeAchieved[][];
	private double stdVolumeAchieved[][];
	private double minVolumeAchieved[][];
	private double maxVolumeAchieved[][];
	
	
	private double payoffsConCF[][][];
	private double avgPayoffsConCF[][];
	private double stdPayoffsConCF[][];
	private double minPayoffsConCF[][];
	private double maxPayoffsConCF[][];
	
	private double totalPayoffsCF[][][];
	private double avgTotalPayoffsCF[][];
	private double stdTotalPayoffsCF[][];
	private double minTotalPayoffsCF[][];
	private double maxTotalPayoffsCF[][];
	
	private double costConCF[][][];
	private double avgCostConCF[][];
	private double stdCostConCF[][];
	private double minCostConCF[][];
	private double maxCostConCF[][];
	
	private double profitMarginCF[][][];
	private double avgProfitMarginCF[][];
	private double stdProfitMarginCF[][];
	private double minProfitMarginCF[][];
	private double maxProfitMarginCF[][];
	
	
	private double payoffsWithoutCF[][];
	private double avgPayoffsWithoutCF[];
	private double stdPayoffsWithoutCF[];
	private double minPayoffsWithoutCF[];
	private double maxPayoffsWithoutCF[];
	
	private double costWithoutCF[][];
	private double avgCostWithoutCF[];
	private double stdCostWithoutCF[];
	private double minCostWithoutCF[];
	private double maxCostWithoutCF[];
	
	private double profitMarginWithoutCF[][];
	private double avgProfitMarginWithoutCF[];
	private double stdProfitMarginWithoutCF[];
	private double minProfitMarginWithoutCF[];
	private double maxProfitMarginWithoutCF[];
	
	
	private double sizeContractedF[][][];
	private double avgSizeContractedF[][];
	private double stdSizeContractedF[][];
	private double minSizeContractedF[][];
	private double maxSizeContractedF[][];
	
	private double sizeNonContractedF[][][];
	private double avgSizeNonContractedF[][];
	private double stdSizeNonContractedF[][];
	private double minSizeNonContractedF[][];
	private double maxSizeNonContractedF[][];
	
	private double payoffsNonContractedF[][][];
	private double avgPayoffsNonContractedF[][];
	private double stdPayoffsNonContractedF[][];
	private double minPayoffsNonContractedF[][];
	private double maxPayoffsNonContractedF[][];
	
	private double payoffsAllConF[][];
	private double avgPayoffsAllConF[];
	private double stdPayoffsAllConF[];
	private double minPayoffsAllConF[];
	private double maxPayoffsAllConF[];
	
	
	private double contractedFContractor[][][];
	private double avgContractedFContractor[][];
	private double stdContractedFContractor[][];
	private double minContractedFContractor[][];
	private double maxContractedFContractor[][];
	
	private int nonContractedFContractor[][][];
	private double avgNonContractedFContractor[][];
	private double stdNonContractedFContractor[][];
	private double minNonContractedFContractor[][];
	private double maxNonContractedFContractor[][];
	
	
	private double trustFToContractor[][][];
	private double avgTrustFToContractor[][];
	private double stdTrustFToContractor[][];
	private double minTrustFToContractor[][];
	private double maxTrustFToContractor[][];
	
	private double trustContractorToF[][][];
	private double avgTrustContractorToF[][];
	private double stdTrustContractorToF[][];
	private double minTrustContractorToF[][];
	private double maxTrustContractorToF[][];
	
	private double demandStep[][][];
	private double avgDemandStep[][];
	private double stdDemandStep[][];
	private double minDemandStep[][];
	private double maxDemandStep[][];
	
	private double rateFBreach[][][];
	private double avgRateFBreach[][];
	private double stdRateFBreach[][];
	private double minRateFBreach[][];
	private double maxRateFBreach[][];
	
	// ########################################################################
	// Methods/Functions
	// ########################################################################

	// --------------------------- Getters and setters ---------------------------//

	public void setExpName(String expName) {
		this.expName = expName;
	}
	
	public void setContractedF(int _numberOfRun, int _contractedF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.F_TYPES; j++){
				this.contractedF[_numberOfRun][i][j] = _contractedF[i][j];
			}
		}
	}
	
	public void setNonContractedF(int _numberOfRun, int _nonContractedF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.F_TYPES; j++){
				this.nonContractedF[_numberOfRun][i][j] = _nonContractedF[i][j];
			}
		}
	}
	
	public void setNoRefuseContract(int _numberOfRun, int _noRefuseContract[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.noRefuseContract[_numberOfRun][i][j] = _noRefuseContract[i][j];
			}
		}
	}
	
	public void setNoRefuseContractSF(int _numberOfRun, int _noRefuseContractSF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.noRefuseContractSF[_numberOfRun][i][j] = _noRefuseContractSF[i][j];
			}
		}
	}
	
	public void setNoRefuseContractMF(int _numberOfRun, int _noRefuseContractMF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.noRefuseContractMF[_numberOfRun][i][j] = _noRefuseContractMF[i][j];
			}
		}
	}
	
	public void setNoRefuseContractLF(int _numberOfRun, int _noRefuseContractLF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.noRefuseContractLF[_numberOfRun][i][j] = _noRefuseContractLF[i][j];
			}
		}
	}
	
	public void setNoBreachF(int _numberOfRun, int _noBeachF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.F_TYPES; j++){
				this.noBeachF[_numberOfRun][i][j] = _noBeachF[i][j];
			}
		}
	}
	
	public void setNoBreachTotalF(int _numberOfRun, int _noBeachTotalF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.noBeachTotalF[_numberOfRun][i][j] = _noBeachTotalF[i][j];
			}
		}
	}
	
	public void setNoBreachC(int _numberOfRun, int _noBeachC[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.noBeachC[_numberOfRun][i][j] = _noBeachC[i][j];
			}
		}
	}
	
	public void setContractedFContractor(int _numberOfRun, double _contractedFContractor[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.contractedFContractor[_numberOfRun][i][j] = _contractedFContractor[i][j];
			}
		}
	}
	
	public void setNonContractedFContractor(int _numberOfRun, int _nonContractedFContractor[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.nonContractedFContractor[_numberOfRun][i][j] = _nonContractedFContractor[i][j];
			}
		}
	}
	
	public void setRateContractAccepted(int _numberOfRun, double _rateContractAccepted[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.rateContractAccepted[_numberOfRun][i][j] = _rateContractAccepted[i][j];
			}
		}
	}
	
	public void setRateContractSuccess(int _numberOfRun, double _rateContractSuccess[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.rateContractSuccess[_numberOfRun][i][j] = _rateContractSuccess[i][j];
			}
		}
	}
	
	public void setVolumeAchieved(int _numberOfRun, double _volumeAchieved[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.volumeAchieved[_numberOfRun][i][j] = _volumeAchieved[i][j];
			}
		}
	}

	public void setPayoffsConCF(int _numberOfRun, double _payoffsConCF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.payoffsConCF[_numberOfRun][i][j] = _payoffsConCF[i][j];
			}
		}
	}
	
	public void setTotalPayoffsCF(int _numberOfRun, double _totalPayoffsCF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.totalPayoffsCF[_numberOfRun][i][j] = _totalPayoffsCF[i][j];
			}
		}
	}
	
	public void setCostConCF(int _numberOfRun, double _costConCF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.costConCF[_numberOfRun][i][j] = _costConCF[i][j];
			}
		}
	}
	
	public void setCostWithoutConSM(int _numberOfRun, double _costWithoutConSM[]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			this.costWithoutCF[_numberOfRun][i] = _costWithoutConSM[i];
		}
	}
	
	public void setPayoffsWithoutCF(int _numberOfRun, double _payoffsWithoutCF[]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			this.payoffsWithoutCF[_numberOfRun][i] = _payoffsWithoutCF[i];
		}
	}
	
	public void setProfitMarginCF(int _numberOfRun, double _profitMarginCF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.profitMarginCF[_numberOfRun][i][j] = _profitMarginCF[i][j];
			}
		}
	}
	
	public void setProfitMarginWithoutCF(int _numberOfRun, double _profitMarginWithoutCF[]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			this.profitMarginWithoutCF[_numberOfRun][i] = _profitMarginWithoutCF[i];
		}
	}
	
	public void setTrustFToContractor(int _numberOfRun, double _trustFToAContractor[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.trustFToContractor[_numberOfRun][i][j] = _trustFToAContractor[i][j];
			}
		}
	}
	
	public void setDemandStep(int _numberOfRun, double _demandStep[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.demandStep[_numberOfRun][i][j] = _demandStep[i][j];
			}
		}
	}
	
	public void setRateFBreach(int _numberOfRun, double _rateFBreach[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.rateFBreach[_numberOfRun][i][j] = _rateFBreach[i][j];
			}
		}
	}

	public void setTrustContractorToF(int _numberOfRun, double _trustContractorToF[][]){
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.NO_CONTRACTORS; j++){
				this.trustContractorToF[_numberOfRun][i][j] = _trustContractorToF[i][j];
			}
		}
	}
	
	public void setSizeContractedF(int _numberOfRun, double _sizeContractedF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.F_TYPES; j++){
				this.sizeContractedF[_numberOfRun][i][j] = _sizeContractedF[i][j];
			}
		}
	}
	
	public void setSizeNonContractedF(int _numberOfRun, double _sizeNonContractedF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.F_TYPES; j++){
				this.sizeNonContractedF[_numberOfRun][i][j] = _sizeNonContractedF[i][j];
			}
		}
	}

	public void setPayoffsNonContractedF(int _numberOfRun, double _payoffsNonContractedF[][]) {
		for (int i = 0; i < ModelParams.MAX_STEPS; i++){
			for (int j = 0; j < ModelParams.F_TYPES; j++){
				this.payoffsNonContractedF[_numberOfRun][i][j] = _payoffsNonContractedF[i][j];
			}
		}
	}
	
	public void setPayoffsAllConF(int _numberOfRun, double _payoffsAllConF[]){
		for (int i = 0; i < ModelParams.MAX_STEPS; i++)
			this.payoffsAllConF[_numberOfRun][i] = _payoffsAllConF[i]; 
	}
	
	// --------------------------- Constructor ---------------------------//
	/**
	 * constructor of Stats
	 * 
	 * @param _nRuns
	 */
	public RunStats(int _nRuns) {

		numberRuns = _nRuns;

		// allocating space for metrics
		this.contractedF = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.nonContractedF = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		
		this.noRefuseContract = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.noRefuseContractSF = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.noRefuseContractMF = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.noRefuseContractLF = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.noBeachF = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.noBeachTotalF = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.noBeachC = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.rateContractAccepted = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.rateContractSuccess = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.volumeAchieved = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.payoffsConCF = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.totalPayoffsCF = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.costConCF = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.profitMarginCF = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.payoffsWithoutCF = new double[_nRuns][ModelParams.MAX_STEPS];
		this.costWithoutCF = new double[_nRuns][ModelParams.MAX_STEPS];
		this.profitMarginWithoutCF = new double[_nRuns][ModelParams.MAX_STEPS];
		
		this.sizeContractedF = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.sizeNonContractedF = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.payoffsNonContractedF = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.payoffsAllConF = new double[_nRuns][ModelParams.MAX_STEPS];
		
		this.contractedFContractor = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.nonContractedFContractor = new int[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.trustFToContractor = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.trustContractorToF = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.demandStep = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.rateFBreach = new double[_nRuns][ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.stdContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.minContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.maxContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		
		this.avgNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.stdNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.minNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.maxNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		
		
		this.avgNoRefuseContract = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdNoRefuseContract = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minNoRefuseContract = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxNoRefuseContract = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgNoRefuseContractSF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdNoRefuseContractSF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minNoRefuseContractSF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxNoRefuseContractSF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgNoRefuseContractMF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdNoRefuseContractMF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minNoRefuseContractMF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxNoRefuseContractMF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgNoRefuseContractLF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdNoRefuseContractLF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minNoRefuseContractLF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxNoRefuseContractLF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		
		this.avgNoBeachF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.stdNoBeachF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.minNoBeachF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.maxNoBeachF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		
		this.avgNoBeachTotalF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdNoBeachTotalF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minNoBeachTotalF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxNoBeachTotalF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgNoBeachC = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdNoBeachC = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minNoBeachC = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxNoBeachC = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		

		this.avgRateContractAccepted = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdRateContractAccepted = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minRateContractAccepted = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxRateContractAccepted = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgRateContractSuccess = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdRateContractSuccess = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minRateContractSuccess = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxRateContractSuccess = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgVolumeAchieved = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdVolumeAchieved = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minVolumeAchieved = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxVolumeAchieved = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		
		this.avgPayoffsConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdPayoffsConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minPayoffsConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxPayoffsConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgTotalPayoffsCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdTotalPayoffsCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minTotalPayoffsCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxTotalPayoffsCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgCostConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdCostConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minCostConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxCostConCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgProfitMarginCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdProfitMarginCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minProfitMarginCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxProfitMarginCF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		
		this.avgPayoffsWithoutCF = new double[ModelParams.MAX_STEPS];
		this.stdPayoffsWithoutCF = new double[ModelParams.MAX_STEPS];
		this.minPayoffsWithoutCF = new double[ModelParams.MAX_STEPS];
		this.maxPayoffsWithoutCF = new double[ModelParams.MAX_STEPS];
		
		this.avgCostWithoutCF = new double[ModelParams.MAX_STEPS];
		this.stdCostWithoutCF = new double[ModelParams.MAX_STEPS];
		this.minCostWithoutCF = new double[ModelParams.MAX_STEPS];
		this.maxCostWithoutCF = new double[ModelParams.MAX_STEPS];
		
		this.avgProfitMarginWithoutCF = new double[ModelParams.MAX_STEPS];
		this.stdProfitMarginWithoutCF = new double[ModelParams.MAX_STEPS];
		this.minProfitMarginWithoutCF = new double[ModelParams.MAX_STEPS];
		this.maxProfitMarginWithoutCF = new double[ModelParams.MAX_STEPS];
		
		
		this.avgSizeContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.stdSizeContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.minSizeContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.maxSizeContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		
		this.avgSizeNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.stdSizeNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.minSizeNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.maxSizeNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		
		this.avgPayoffsNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.stdPayoffsNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.minPayoffsNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		this.maxPayoffsNonContractedF = new double[ModelParams.MAX_STEPS][ModelParams.F_TYPES];
		
		this.avgPayoffsAllConF = new double[ModelParams.MAX_STEPS];
		this.stdPayoffsAllConF = new double[ModelParams.MAX_STEPS];
		this.minPayoffsAllConF = new double[ModelParams.MAX_STEPS];
		this.maxPayoffsAllConF = new double[ModelParams.MAX_STEPS];
		
		
		this.avgContractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdContractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minContractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxContractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgNonContractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdNonContractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minNonContractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxNonContractedFContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		
		this.avgTrustFToContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdTrustFToContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minTrustFToContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxTrustFToContractor = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgTrustContractorToF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdTrustContractorToF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minTrustContractorToF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxTrustContractorToF = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		

		this.avgDemandStep = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdDemandStep = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minDemandStep = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxDemandStep = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		
		this.avgRateFBreach = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.stdRateFBreach = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.minRateFBreach = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
		this.maxRateFBreach = new double[ModelParams.MAX_STEPS][ModelParams.NO_CONTRACTORS];
	}
	
	/**
	 * TODO This method prints avg of MC runs 
	 */
	public void printAllSteps(PrintWriter writer, boolean append) {

		for (int i = 0; i < ModelParams.MAX_STEPS; i++) {

			String toPrint = "Exp;" + this.expName
					
					+ ";Step;" + i 
					
					+ ";ConSF;" + String.format("%.2f", this.avgContractedF[i][0])
					+ ";ConMF;" + String.format("%.2f", this.avgContractedF[i][1]) 
					+ ";ConLF;" + String.format("%.2f", this.avgContractedF[i][2])
					+ ";NonConSF;" + String.format("%.2f", this.avgNonContractedF[i][0])
					+ ";NonConMF;" + String.format("%.2f", this.avgNonContractedF[i][1]) 
					+ ";NonConLF;" + String.format("%.2f", this.avgNonContractedF[i][2]) 
					
					+ ";ConFC_A;" + String.format("%.2f", this.avgContractedFContractor[i][0])
					+ ";ConFC_B;" + String.format("%.2f", this.avgContractedFContractor[i][1]) 
					
					+ ";BreachTotalFC_A;" + String.format("%.2f", this.avgNoBeachTotalF[i][0])
					+ ";BreachTotalFC_B;" + String.format("%.2f", this.avgNoBeachTotalF[i][1])

					+ ";BreachC_A;" + String.format("%.2f", this.avgNoBeachC[i][0])
					+ ";BreachC_B;" + String.format("%.2f", this.avgNoBeachC[i][1])

					+ ";VolC_A;" + String.format("%.2f", this.avgVolumeAchieved[i][0])
					+ ";VolC_A_Min;" + String.format("%.2f", this.minVolumeAchieved[i][0])
					+ ";VolC_A_Max;" + String.format("%.2f", this.maxVolumeAchieved[i][0])
					+ ";VolC_A_Std;" + String.format("%.2f", this.stdVolumeAchieved[i][0])
					+ ";VolC_B;" + String.format("%.2f", this.avgVolumeAchieved[i][1])
					+ ";VolC_B_Min;" + String.format("%.2f", this.minVolumeAchieved[i][1])
					+ ";VolC_B_Max;" + String.format("%.2f", this.maxVolumeAchieved[i][1])
					+ ";VolC_B_Std;" + String.format("%.2f", this.stdVolumeAchieved[i][1])
					
					+ ";PayoffsC_A;" + String.format("%.2f", this.avgPayoffsConCF[i][0])
					+ ";PayoffsC_A_Min;" + String.format("%.2f", this.minPayoffsConCF[i][0])
					+ ";PayoffsC_A_Max;" + String.format("%.2f", this.maxPayoffsConCF[i][0])
					+ ";PayoffsC_A_Std;" + String.format("%.2f", this.stdPayoffsConCF[i][0])
					+ ";PayoffsC_B;" + String.format("%.2f", this.avgPayoffsConCF[i][1])
					+ ";PayoffsC_B_Min;" + String.format("%.2f", this.minPayoffsConCF[i][1])
					+ ";PayoffsC_B_Max;" + String.format("%.2f", this.maxPayoffsConCF[i][1])
					+ ";PayoffsC_B_Std;" + String.format("%.2f", this.stdPayoffsConCF[i][1])
					
					+ ";PayoffsWithoutCF;" + String.format("%.2f", this.avgPayoffsWithoutCF[i])
					
					+ ";ProfitMarginC_A;" + String.format("%.2f", this.avgProfitMarginCF[i][0])
					+ ";ProfitMargin_B;" + String.format("%.2f", this.avgProfitMarginCF[i][1])

					+ ";ProfitMarginWithoutCF;" + String.format("%.2f", this.avgProfitMarginWithoutCF[i])
					
					+ ";TrustFToC_A;" + String.format("%.3f", this.avgTrustFToContractor[i][0])
					+ ";TrustFToC_A_Min;" + String.format("%.3f", this.minTrustFToContractor[i][0])
					+ ";TrustFToC_A_Max;" + String.format("%.3f", this.maxTrustFToContractor[i][0])
					+ ";TrustFToC_A_Std;" + String.format("%.3f", this.stdTrustFToContractor[i][0])
					+ ";TrustFToC_B;" + String.format("%.3f", this.avgTrustFToContractor[i][1])
					+ ";TrustFToC_B_Min;" + String.format("%.3f", this.minTrustFToContractor[i][1])
					+ ";TrustFToC_B_Max;" + String.format("%.3f", this.maxTrustFToContractor[i][1])
					+ ";TrustFToC_B_Std;" + String.format("%.3f", this.stdTrustFToContractor[i][1])

					+ ";DemandC_A;" + String.format("%.2f", this.avgDemandStep[i][0])
					+ ";DemandC_B;" + String.format("%.2f", this.avgDemandStep[i][1])
					
					+ ";RateFBreachC_A;" + String.format("%.2f", this.avgRateFBreach[i][0])
					+ ";RateFBreachC_A_Min;" + String.format("%.2f", this.minRateFBreach[i][0])
					+ ";RateFBreachC_A_Max;" + String.format("%.2f", this.maxRateFBreach[i][0])
					+ ";RateFBreachC_A_Std;" + String.format("%.2f", this.stdRateFBreach[i][0])
					+ ";RateFBreachC_B;" + String.format("%.2f", this.avgRateFBreach[i][1])
					+ ";RateFBreachC_B_Min;" + String.format("%.2f", this.minRateFBreach[i][1])
					+ ";RateFBreachC_B_Max;" + String.format("%.2f", this.maxRateFBreach[i][1])
					+ ";RateFBreachC_B_Std;" + String.format("%.2f", this.stdRateFBreach[i][1])

					+ "\n";
			
			if (append)
				writer.append(toPrint);
			else
				writer.print(toPrint);
		}
	}

	public void appendAllSteps(PrintWriter writer) {
		printAllSteps(writer, true);
	}

	/**
	 * TODO This method is for calculating all the statistical information for the runs of the metrics
	 */
	public void calcAllStats() {

		for (int j = 0; j < ModelParams.MAX_STEPS; j++) {

			for (int k = 0; k < ModelParams.F_TYPES; k++) {
				// Get a DescriptiveStatistics instance
				DescriptiveStatistics statsContractedF = new DescriptiveStatistics();
				DescriptiveStatistics statsNonContractedF = new DescriptiveStatistics();
				
				DescriptiveStatistics statsNoBeachF = new DescriptiveStatistics();

				DescriptiveStatistics statsSizeContractedF = new DescriptiveStatistics();
				DescriptiveStatistics statsSizeNonContractedF = new DescriptiveStatistics();
				DescriptiveStatistics statsPayoffsNonContractedF = new DescriptiveStatistics();
				
				for (int i = 0; i < this.numberRuns; i++) {
					statsContractedF.addValue(this.contractedF[i][j][k]);
					statsNonContractedF.addValue(this.nonContractedF[i][j][k]);
					
					statsNoBeachF.addValue(this.noBeachF[i][j][k]);

					statsSizeContractedF.addValue(this.sizeContractedF[i][j][k]);
					statsSizeNonContractedF.addValue(this.sizeNonContractedF[i][j][k]);
					statsPayoffsNonContractedF.addValue(this.payoffsNonContractedF[i][j][k]);
				}

				this.avgContractedF[j][k] = statsContractedF.getMean();
				this.stdContractedF[j][k] = statsContractedF.getStandardDeviation();
				this.minContractedF[j][k] = statsContractedF.getMin();
				this.maxContractedF[j][k] = statsContractedF.getMax();

				this.avgNonContractedF[j][k] = statsNonContractedF.getMean();
				this.stdNonContractedF[j][k] = statsNonContractedF.getStandardDeviation();
				this.minNonContractedF[j][k] = statsNonContractedF.getMin();
				this.maxNonContractedF[j][k] = statsNonContractedF.getMax();
				
				this.avgNoBeachF[j][k] = statsNoBeachF.getMean();
				this.stdNoBeachF[j][k] = statsNoBeachF.getStandardDeviation();
				this.minNoBeachF[j][k] = statsNoBeachF.getMin();
				this.maxNoBeachF[j][k] = statsNoBeachF.getMax();

				
				this.avgSizeContractedF[j][k] = statsSizeContractedF.getMean();
				this.stdSizeContractedF[j][k] = statsSizeContractedF.getStandardDeviation();
				this.minSizeContractedF[j][k] = statsSizeContractedF.getMin();
				this.maxSizeContractedF[j][k] = statsSizeContractedF.getMax();

				this.avgSizeNonContractedF[j][k] = statsSizeNonContractedF.getMean();
				this.stdSizeNonContractedF[j][k] = statsSizeNonContractedF.getStandardDeviation();
				this.minSizeNonContractedF[j][k] = statsSizeNonContractedF.getMin();
				this.maxSizeNonContractedF[j][k] = statsSizeNonContractedF.getMax();
				
				this.avgPayoffsNonContractedF[j][k] = statsPayoffsNonContractedF.getMean();
				this.stdPayoffsNonContractedF[j][k] = statsPayoffsNonContractedF.getStandardDeviation();
				this.minPayoffsNonContractedF[j][k] = statsPayoffsNonContractedF.getMin();
				this.maxPayoffsNonContractedF[j][k] = statsPayoffsNonContractedF.getMax();
			}

			for (int k = 0; k < ModelParams.NO_CONTRACTORS; k++) {
				
				DescriptiveStatistics statsNoRefuseContract = new DescriptiveStatistics();
				DescriptiveStatistics statsNoRefuseContractSF = new DescriptiveStatistics();
				DescriptiveStatistics statsNoRefuseContractMF = new DescriptiveStatistics();
				DescriptiveStatistics statsNoRefuseContractLF = new DescriptiveStatistics();
				
				DescriptiveStatistics statsNoBeachSF = new DescriptiveStatistics();
				DescriptiveStatistics statsNoBeachC = new DescriptiveStatistics();
				
				DescriptiveStatistics statsRateContractAccepted = new DescriptiveStatistics();
				DescriptiveStatistics statsRateContractSuccess = new DescriptiveStatistics();
				DescriptiveStatistics statsVolumeAchieved = new DescriptiveStatistics();

				DescriptiveStatistics statsPayoffsConCF = new DescriptiveStatistics();
				DescriptiveStatistics statsTotalPayoffsCF = new DescriptiveStatistics();
				DescriptiveStatistics statsCostConCF = new DescriptiveStatistics();
				DescriptiveStatistics statsProfitMarginCF = new DescriptiveStatistics();
				
				DescriptiveStatistics statsContractedFContractor = new DescriptiveStatistics();
				DescriptiveStatistics statsNonContractedFContractor = new DescriptiveStatistics();
				
				DescriptiveStatistics statsTrustFToContractor = new DescriptiveStatistics();
				DescriptiveStatistics statsTrustContractorToF = new DescriptiveStatistics();
				
				DescriptiveStatistics statsDemandStep = new DescriptiveStatistics();
				
				DescriptiveStatistics statsRateFBreach = new DescriptiveStatistics();

				for (int i = 0; i < this.numberRuns; i++) {
					
					statsNoRefuseContract.addValue(this.noRefuseContract[i][j][k]);
					statsNoRefuseContractSF.addValue(this.noRefuseContractSF[i][j][k]);
					statsNoRefuseContractMF.addValue(this.noRefuseContractMF[i][j][k]);
					statsNoRefuseContractLF.addValue(this.noRefuseContractLF[i][j][k]);
					
					statsNoBeachSF.addValue(this.noBeachTotalF[i][j][k]);
					statsNoBeachC.addValue(this.noBeachC[i][j][k]);
					
					statsRateContractAccepted.addValue(this.rateContractAccepted[i][j][k]);
					statsRateContractSuccess.addValue(this.rateContractSuccess[i][j][k]);
					statsVolumeAchieved.addValue(this.volumeAchieved[i][j][k]);

					statsPayoffsConCF.addValue(this.payoffsConCF[i][j][k]);
					statsTotalPayoffsCF.addValue(this.totalPayoffsCF[i][j][k]);
					statsCostConCF.addValue(this.costConCF[i][j][k]);
					statsProfitMarginCF.addValue(this.profitMarginCF[i][j][k]);
					
					statsContractedFContractor.addValue(this.contractedFContractor[i][j][k]);
					statsNonContractedFContractor.addValue(this.nonContractedFContractor[i][j][k]);
					
					statsTrustFToContractor.addValue(this.trustFToContractor[i][j][k]);
					statsTrustContractorToF.addValue(this.trustContractorToF[i][j][k]);
					
					statsDemandStep.addValue(this.demandStep[i][j][k]);
					
					statsRateFBreach.addValue(this.rateFBreach[i][j][k]);
				}
				
				this.avgNoBeachTotalF[j][k] = statsNoBeachSF.getMean();
				this.stdNoBeachTotalF[j][k] = statsNoBeachSF.getStandardDeviation();
				this.minNoBeachTotalF[j][k] = statsNoBeachSF.getMin();
				this.maxNoBeachTotalF[j][k] = statsNoBeachSF.getMax();
				
				this.avgNoBeachC[j][k] = statsNoBeachC.getMean();
				this.stdNoBeachC[j][k] = statsNoBeachC.getStandardDeviation();
				this.minNoBeachC[j][k] = statsNoBeachC.getMin();
				this.maxNoBeachC[j][k] = statsNoBeachC.getMax();
				
				this.avgNoRefuseContract[j][k] = statsNoRefuseContract.getMean();
				this.stdNoRefuseContract[j][k] = statsNoRefuseContract.getStandardDeviation();
				this.minNoRefuseContract[j][k] = statsNoRefuseContract.getMin();
				this.maxNoRefuseContract[j][k] = statsNoRefuseContract.getMax();
				
				this.avgNoRefuseContractSF[j][k] = statsNoRefuseContractSF.getMean();
				this.stdNoRefuseContractSF[j][k] = statsNoRefuseContractSF.getStandardDeviation();
				this.minNoRefuseContractSF[j][k] = statsNoRefuseContractSF.getMin();
				this.maxNoRefuseContractSF[j][k] = statsNoRefuseContractSF.getMax();
				
				this.avgNoRefuseContractMF[j][k] = statsNoRefuseContractMF.getMean();
				this.stdNoRefuseContractMF[j][k] = statsNoRefuseContractMF.getStandardDeviation();
				this.minNoRefuseContractMF[j][k] = statsNoRefuseContractMF.getMin();
				this.maxNoRefuseContractMF[j][k] = statsNoRefuseContractMF.getMax();
				
				this.avgNoRefuseContractLF[j][k] = statsNoRefuseContractLF.getMean();
				this.stdNoRefuseContractLF[j][k] = statsNoRefuseContractLF.getStandardDeviation();
				this.minNoRefuseContractLF[j][k] = statsNoRefuseContractLF.getMin();
				this.maxNoRefuseContractLF[j][k] = statsNoRefuseContractLF.getMax();

				this.avgRateContractAccepted[j][k] = statsRateContractAccepted.getMean();
				this.stdRateContractAccepted[j][k] = statsRateContractAccepted.getStandardDeviation();
				this.minRateContractAccepted[j][k] = statsRateContractAccepted.getMin();
				this.maxRateContractAccepted[j][k] = statsRateContractAccepted.getMax();
				
				this.avgRateContractSuccess[j][k] = statsRateContractSuccess.getMean();
				this.stdRateContractSuccess[j][k] = statsRateContractSuccess.getStandardDeviation();
				this.minRateContractSuccess[j][k] = statsRateContractSuccess.getMin();
				this.maxRateContractSuccess[j][k] = statsRateContractSuccess.getMax();
				
				this.avgVolumeAchieved[j][k] = statsVolumeAchieved.getMean();
				this.stdVolumeAchieved[j][k] = statsVolumeAchieved.getStandardDeviation();
				this.minVolumeAchieved[j][k] = statsVolumeAchieved.getMin();
				this.maxVolumeAchieved[j][k] = statsVolumeAchieved.getMax();
				

				this.avgPayoffsConCF[j][k] = statsPayoffsConCF.getMean();
				this.stdPayoffsConCF[j][k] = statsPayoffsConCF.getStandardDeviation();
				this.minPayoffsConCF[j][k] = statsPayoffsConCF.getMin();
				this.maxPayoffsConCF[j][k] = statsPayoffsConCF.getMax();
				
				this.avgTotalPayoffsCF[j][k] = statsTotalPayoffsCF.getMean();
				this.stdTotalPayoffsCF[j][k] = statsTotalPayoffsCF.getStandardDeviation();
				this.minTotalPayoffsCF[j][k] = statsTotalPayoffsCF.getMin();
				this.maxTotalPayoffsCF[j][k] = statsTotalPayoffsCF.getMax();
				
				this.avgCostConCF[j][k] = statsCostConCF.getMean();
				this.stdCostConCF[j][k] = statsCostConCF.getStandardDeviation();
				this.minCostConCF[j][k] = statsCostConCF.getMin();
				this.maxCostConCF[j][k] = statsCostConCF.getMax();
				
				this.avgProfitMarginCF[j][k] = statsProfitMarginCF.getMean();
				this.stdProfitMarginCF[j][k] = statsProfitMarginCF.getStandardDeviation();
				this.minProfitMarginCF[j][k] = statsProfitMarginCF.getMin();
				this.maxProfitMarginCF[j][k] = statsProfitMarginCF.getMax();
				
				
				this.avgContractedFContractor[j][k] = statsContractedFContractor.getMean();
				this.stdContractedFContractor[j][k] = statsContractedFContractor.getStandardDeviation();
				this.minContractedFContractor[j][k] = statsContractedFContractor.getMin();
				this.maxContractedFContractor[j][k] = statsContractedFContractor.getMax();

				this.avgNonContractedFContractor[j][k] = statsNonContractedFContractor.getMean();
				this.stdNonContractedFContractor[j][k] = statsNonContractedFContractor.getStandardDeviation();
				this.minNonContractedFContractor[j][k] = statsNonContractedFContractor.getMin();
				this.maxNonContractedFContractor[j][k] = statsNonContractedFContractor.getMax();
				

				this.avgTrustFToContractor[j][k] = statsTrustFToContractor.getMean();
				this.stdTrustFToContractor[j][k] = statsTrustFToContractor.getStandardDeviation();
				this.minTrustFToContractor[j][k] = statsTrustFToContractor.getMin();
				this.maxTrustFToContractor[j][k] = statsTrustFToContractor.getMax();
				
				this.avgTrustContractorToF[j][k] = statsTrustContractorToF.getMean();
				this.stdTrustContractorToF[j][k] = statsTrustContractorToF.getStandardDeviation();
				this.minTrustContractorToF[j][k] = statsTrustContractorToF.getMin();
				this.maxTrustContractorToF[j][k] = statsTrustContractorToF.getMax();
				
				this.avgDemandStep[j][k] = statsDemandStep.getMean();
				this.stdDemandStep[j][k] = statsDemandStep.getStandardDeviation();
				this.minDemandStep[j][k] = statsDemandStep.getMin();
				this.maxDemandStep[j][k] = statsDemandStep.getMax();
				
				this.avgRateFBreach[j][k] = statsRateFBreach.getMean();
				this.stdRateFBreach[j][k] = statsRateFBreach.getStandardDeviation();
				this.minRateFBreach[j][k] = statsRateFBreach.getMin();
				this.maxRateFBreach[j][k] = statsRateFBreach.getMax();
			}

			DescriptiveStatistics statsPayoffsAllConF = new DescriptiveStatistics();
			DescriptiveStatistics statsPayoffsWithoutCF = new DescriptiveStatistics();
			DescriptiveStatistics statsCostWithoutCF = new DescriptiveStatistics();
			DescriptiveStatistics statsProfitMarginWithoutCF = new DescriptiveStatistics();
			
			for (int i = 0; i < this.numberRuns; i++) {
				
				statsPayoffsAllConF.addValue(this.payoffsAllConF[i][j]);
				statsPayoffsWithoutCF.addValue(this.payoffsWithoutCF[i][j]);
				statsCostWithoutCF.addValue(this.costWithoutCF[i][j]);
				statsProfitMarginWithoutCF.addValue(this.profitMarginWithoutCF[i][j]);
			}
			
			this.avgPayoffsWithoutCF[j] = statsPayoffsWithoutCF.getMean();
			this.stdPayoffsWithoutCF[j] = statsPayoffsWithoutCF.getStandardDeviation();
			this.minPayoffsWithoutCF[j] = statsPayoffsWithoutCF.getMin();
			this.maxPayoffsWithoutCF[j] = statsPayoffsWithoutCF.getMax();
			
			this.avgCostWithoutCF[j] = statsCostWithoutCF.getMean();
			this.stdCostWithoutCF[j] = statsCostWithoutCF.getStandardDeviation();
			this.minCostWithoutCF[j] = statsCostWithoutCF.getMin();
			this.maxCostWithoutCF[j] = statsCostWithoutCF.getMax();
			
			this.avgProfitMarginWithoutCF[j] = statsProfitMarginWithoutCF.getMean();
			this.stdProfitMarginWithoutCF[j] = statsProfitMarginWithoutCF.getStandardDeviation();
			this.minProfitMarginWithoutCF[j] = statsProfitMarginWithoutCF.getMin();
			this.maxProfitMarginWithoutCF[j] = statsProfitMarginWithoutCF.getMax();
			
			this.avgPayoffsAllConF[j] = statsPayoffsAllConF.getMean();
			this.stdPayoffsAllConF[j] = statsPayoffsAllConF.getStandardDeviation();
			this.minPayoffsAllConF[j] = statsPayoffsAllConF.getMin();
			this.maxPayoffsAllConF[j] = statsPayoffsAllConF.getMax();
		}
	}
}
