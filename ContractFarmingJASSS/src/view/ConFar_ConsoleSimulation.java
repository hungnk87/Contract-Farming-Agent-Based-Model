package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
//import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import controller.Controller;
import model.Model;
import model.ModelParams;

//----------------------------- MAIN FUNCTION -----------------------------//

/**
 * Main function to run the simulation for Contract Farming model
 */

public class ConFar_ConsoleSimulation {		

	// constants to get different options for simple or SA runs
	public final static int NO_SA = 0;
	public final static int YES_SA = 1;
	
	// LOGGING
	private static final Logger log = Logger.getLogger( Model.class.getName() );
	
	/**
	 * Create an options class to store all the arguments of the command-line call of the program
	 * @param options the class containing the options, when returned. It has to be created before calling
	 */
	private static void createArguments (Options options) {
				
		options.addOption("paramsFile", true, "Pathfile with the parameters file");
		options.getOption("paramsFile").setRequired(true);
		
		options.addOption("outputFile", true, "File to store all the information about the simulation");
		options.getOption("outputFile").setRequired(true);
		
		options.addOption("cfPriceConA", true, "higher offering price from Contractor A");
		options.addOption("cfPriceConB", true, "higher offering price from Contractor B");
		options.addOption("commitmentConA", true, "commitment from Contractor A");
		options.addOption("commitmentConB", true, "commitment from Contractor B");
		options.addOption("huskingFacilityConA", true, "availability of husking facility from Contractor A");
		options.addOption("huskingFacilityConB", true, "availability of husking facility from Contractor B");
		options.addOption("millingFacilityConA", true, "availability of milling facility from Contractor A");
		options.addOption("millingFacilityConB", true, "availability of milling facility from Contractor B");
		options.addOption("ordinaryRice", true, "Ordinary Rice or Jasmine Rice");
		
		options.addOption("MC", true, "Number of MC simulations");
		options.addOption("seed", true, "Seed for running the MC simulations");
		
		// parameters for SA
		options.addOption("SA", false, "If running a Sensitivity Analysis");
	}
	
	/**
	 * MAIN CONSOLE-BASED FUNCTION TO RUN A SIMPLE RUN OR A SENSITIVITY ANALYSIS OF THE MODEL PARAMETERS
	 */
	public static void main (String[] args) {
		
		int SA = YES_SA;
		
		String paramsFile = "";
		String outputFile = "";

		ModelParams params = null;
		
		// parsing the arguments
		Options options = new Options();
		
		createArguments (options);		

		// create the parser
	    CommandLineParser parser = new DefaultParser();
	    
	    try {
	        // parse the command line arguments for the given options
	        CommandLine line = parser.parse( options, args );

			// get parameters
			params = new ModelParams();	
			
	        // retrieve the arguments
	        		    
		    if( line.hasOption( "paramsFile" ) )		    
		    	paramsFile = line.getOptionValue("paramsFile");
		    else System.err.println( "A parameters file is needed");

		    if( line.hasOption( "outputFile" ) ) 			    
		    	outputFile = line.getOptionValue("outputFile");

		    // read parameters from file
			params.readParameters(paramsFile);

			// set the outputfile
			params.setOutputFile(outputFile);
			
		    if( line.hasOption( "MC" ) )
		    	params.setRunsMC(Integer.parseInt(line.getOptionValue("MC")));
		    if( line.hasOption( "seed" ) )
		    	params.setSeed(Long.parseLong(line.getOptionValue("seed")));
		    if( line.hasOption("cfPriceConA"))
		    	params.setCFPriceConA(Double.parseDouble(line.getOptionValue("cfPriceConA")));
		    if( line.hasOption("cfPriceConAMin"))
		    	params.setCFPriceConAMin(Double.parseDouble(line.getOptionValue("cfPriceConAMin")));
		    if( line.hasOption("cfPriceConAMax"))
		    	params.setCFPriceConAMax(Double.parseDouble(line.getOptionValue("cfPriceConAMax")));
		    if( line.hasOption("cfPriceConB"))
		    	params.setCFPriceConB(Double.parseDouble(line.getOptionValue("cfPriceConB")));
		    if( line.hasOption( "commitmentConA" ) )
		    	params.setCommitmentConA(Integer.parseInt(line.getOptionValue("commitmentConA")));
		    if( line.hasOption( "commitmentConAMin" ) )
		    	params.setCommitmentConAMin(Integer.parseInt(line.getOptionValue("commitmentConAMin")));
		    if( line.hasOption( "commitmentConAMax" ) )
		    	params.setCommitmentConAMax(Integer.parseInt(line.getOptionValue("commitmentConAMax")));
		    if( line.hasOption( "commitmentConB" ) )
		    	params.setCommitmentConB(Integer.parseInt(line.getOptionValue("commitmentConB")));
		    if( line.hasOption( "huskingFacilityConA" ) )
		    	params.setHuskingFacilityConA(Integer.parseInt(line.getOptionValue("huskingFacilityConA")));
		    if( line.hasOption( "huskingFacilityConB" ) )
		    	params.setHuskingFacilityConB(Integer.parseInt(line.getOptionValue("huskingFacilityConB")));
		    if( line.hasOption( "millingFacilityConA" ) )
		    	params.setMillingFacilityConA(Integer.parseInt(line.getOptionValue("millingFacilityConA")));
		    if( line.hasOption( "millingFacilityConB" ) )
		    	params.setMillingFacilityConB(Integer.parseInt(line.getOptionValue("millingFacilityConB")));
		    if( line.hasOption( "ordinaryRice" ) )
		    	params.setOrdinaryRice(Integer.parseInt(line.getOptionValue("ordinaryRice")));
		    
		    // SENSITIVITY ANALYSIS
		    if( line.hasOption( "YES_SA" ) ) {
		    	
		    	// we have to run a SA 
		    	SA = YES_SA;
		    	
		    }
	    }
	    
	    catch (ParseException exp ) {
	    	
	        // oops, something went wrong
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );

			log.log(Level.SEVERE, "Parsing failed.  Reason: " + exp.toString(), exp);
	    }
	    	
        System.out.println("\n****** STARTING THE RUN OF THE ABM CONTRACT FARMING MODEL ******\n");

        Date date = new Date();
        System.out.println("****** " + date.toString() + "******\n");

        File fileAllSteps = new File ("./logs/" + "ConFar_AllSteps_" +  params.getOutputFile() + ".txt");
       
        // the SA check    	    			    	
	    if (SA == NO_SA) {
	    	
	    	// no SA, simple run
	    	RunStats stats;
	    	
	    	// print parameters for double-checking
	    	System.out.println("****** Parameters values ******");
		    PrintWriter out = new PrintWriter(System.out, true);
	        params.printParameters(out);
	        
	        log.log(Level.FINE, "\n*** Parameters values of this model:\n" + params.exportGeneral());
	        
			// run the MC simulations
			long time1 = System.currentTimeMillis ();
			
		    Controller controller = new Controller (params, paramsFile);

	 		// running the model with the MC simulations
	 		stats = controller.runModel();		
	 		
	 		stats.setExpName(params.getOutputFile());
	 		
	 		long  time2  = System.currentTimeMillis( );
	 		System.out.println("\n****** " + (double)(time2 - time1)/1000 + "s spent during the simulation");
	 		
	 		stats.calcAllStats();

	 		// print the stats into a file
	        System.out.println("\n****** Stats also saved into a file ******\n");
	         
	        PrintWriter printWriter;
	         
	 		try {
	 			
	 	        printWriter = new PrintWriter (fileAllSteps);
	 			stats.printAllSteps (printWriter, false);
	 	        printWriter.close (); 
	 	        
	 		} catch (FileNotFoundException e) {
	 			
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 			
	 		    log.log( Level.SEVERE, e.toString(), e );
	 		} 
	 	
	    } else {
	    	
	    	if ( SA == YES_SA) {
	    		
	    		SensitivityAnalysis.runSA (params, paramsFile, fileAllSteps);
	    		
	    	}
	    }
	}
}
