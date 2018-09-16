package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.Controller;
import model.Model;
import model.ModelParams;


/**
 * SA class to run and save results when running the MC model 
 * with different parameter configuration from a config base
 */

public class SensitivityAnalysis {		
		
	
	// LOGGING
	private static final Logger log = Logger.getLogger( Model.class.getName() );
	
	public static void runSA (ModelParams _params, String _paramsFile, File fileAllSteps) {

		// create output files in case they exist as we will append the simulation contents
		
		if (fileAllSteps.exists() && !fileAllSteps.isDirectory()) {
			fileAllSteps.delete();
		}
		
		// create the arrays with the R2 values
		ArrayList<Double> valuesRatio = new ArrayList<Double>();
		
		// save the values to the array of ratios
		double v = _params.getCFPriceConAMax();
		double step = 0.05;
		int k = 0;
		while (v >= _params.getCFPriceConAMin()){	
				    	
			valuesRatio.add(k, v);
			v = v - step;
			k ++;
		}

	    Controller controller = new Controller (_params, _paramsFile);

	    for (int i = 0; i < valuesRatio.size(); i++ ) {
		    		
    		// set the given R value to the params (the only changing parameter)  	
	    	double valueSA = valuesRatio.get(i);
	    	
	    	_params.setCFPriceConA(valueSA);

	        System.out.println("-> parameter valueSA of " + _params.getCFPriceConA() 
	        		+ " (with fixed valueSAMax = " + _params.getCFPriceConAMax() 
	        		+ ", valueSAMin = " + _params.getCFPriceConAMin() + ")" );

			log.log(Level.FINE, "\n****** Running Monte-Carlo simulation for Sensitivity Analysis: parameter valueSA of " 
					+ _params.getCFPriceConA()
					+ " (with fixed valueSAMax = " + _params.getCFPriceConAMax() 
					+ ", valueSAMin = " + _params.getCFPriceConAMin() + ")" + " \n");
		
			RunStats stats;

			long time1 = System.currentTimeMillis ();
			
	 		// running the model with the MC simulations
	 		stats = controller.runModel();		

	 		long  time2  = System.currentTimeMillis( );
	 		System.out.println("\n****** " + (double)(time2 - time1)/1000 + "s spent during the simulation");

	 		// calculate the stats for this run and set the name of this experiment
	 		stats.setExpName("" + _params.getCFPriceConA());		    	
	 		stats.calcAllStats();

			PrintWriter out = new PrintWriter(System.out, true);
			_params.printParameters(out);
			 
			// print the stats into a file
			System.out.println("\n****** Stats of Sensitivity Analysis are saved into a file ******\n");
			
			PrintWriter printWriter;
	         
	 		try {
	 			
	 			// print all the runs in a file 			
	 			printWriter = new PrintWriter (new FileOutputStream(fileAllSteps, true));
	 			
	 			if ( fileAllSteps.exists() && !fileAllSteps.isDirectory() )
	 		        stats.appendAllSteps(printWriter);		 		        
	 		    else
		 			stats.printAllSteps(printWriter, false);		 		  
	 		    
	 	        printWriter.close ();       	
	 	        
	 		} catch (FileNotFoundException e) {
	 			
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 			
	 		    log.log( Level.SEVERE, e.toString(), e );
	 		} 
    	}
    	
    }				
}
