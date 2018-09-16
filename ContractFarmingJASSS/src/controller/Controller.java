package controller;

import view.RunStats;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

/**
 * This class is the controller to call the model, run it to get all the
 * steps and return a list of simulated values
 */
public class Controller {

	// LOGGING
	private static final Logger log = Logger.getLogger(Model.class.getName());

	// MODEL
	protected Model model;

	/**
	 * Constructor load Configuration File, and the Params object
	 */
	public Controller(ModelParams _params, String _paramsConfigFile) {

		Model.setConfigFileName(_paramsConfigFile);
		this.model = new Model(_params);
	}

	/**
	 * Run the model one time
	 * @return the statistics after running the model
	 */
	public RunStats runModel() {

		// Store results into the stats object
		RunStats stats = new RunStats(model.getParametersObject().getRunsMC());

		// LOGGING
		log.log(Level.FINE, "\n**Starting MC sim\n");
		log.log(Level.FINE, "\n" + model.getParametersObject().exportGeneral() + "\n");

		for (int i = 0; i < model.getParametersObject().getRunsMC(); i++) {

			// for each MC run we start the model and run it
			model.start();

			do {
				if (!model.schedule.step(model)) break;

			} while (model.schedule.getSteps() < ModelParams.MAX_STEPS);

			model.finish();

			// store to the stats object
			stats.setContractedF(i, model.getContractedF());
			stats.setNonContractedF(i, model.getNonContractedF());
			
			stats.setContractedFContractor(i, model.getContractedFContractor());
			stats.setNonContractedFContractor(i, model.getNonContractedFContractor());
			
			stats.setNoBreachTotalF(i, model.getNoBreachTotalF());
			stats.setNoBreachC(i, model.getNoBreachC());
			
			stats.setVolumeAchieved(i, model.getVolumeAchieved());
			
			stats.setPayoffsConCF(i, model.getPayoffsConCF());
			stats.setTotalPayoffsCF(i, model.getTotalPayoffsCF());
			stats.setPayoffsWithoutCF(i, model.getPayoffsWithoutCF());

			stats.setProfitMarginCF(i, model.getProfitMarginCF());
			stats.setProfitMarginWithoutCF(i, model.getProfitMarginWithoutCF());
			
			stats.setTrustFToContractor(i, model.getTrustFToContractor());

			stats.setDemandStep(i, model.getDemandStep());
			
			stats.setRateFBreach(i, model.getRateFBreach());
			
			// to show some logs about activity of the agents
			log.log(Level.FINE, "MC-" + (i + 1) + "/"
					+ model.getParametersObject().getRunsMC() + " ended\n\n");
			System.out.println("MC-" + (i + 1) + "/"
					+ model.getParametersObject().getRunsMC() + " ended!");
		}
		return stats;
	}
}
