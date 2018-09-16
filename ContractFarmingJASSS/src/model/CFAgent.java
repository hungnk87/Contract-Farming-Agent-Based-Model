package model;

import sim.engine.*;
import sim.portrayal.*;
import java.awt.*;

public class CFAgent extends SimplePortrayal2D implements Steppable {
	
	private static final long serialVersionUID = 1L;
	
	int currentStep;

	public int agentId;
	public int agentRole;
	public int contracting;
	public int honoring;
	public double weightUtility;
	public int ordinaryRice;
	public double smMedianPrice = 0;
	
	public CFAgent(int _agentId, int _agentRole, int _contracting, int _honoring,
			double _weightUtility, int _ordinaryRice){ 
		
		this.agentId = _agentId;
		this.agentRole = _agentRole;
		this.contracting = _contracting;
		this.honoring = _honoring;
		this.weightUtility = _weightUtility;
		this.ordinaryRice = _ordinaryRice;
	}
	
	// --------------------------- Get/Set methods ---------------------------//

	public int getAgentId() {
		return agentId;
	}

	public int getAgentRole() {
		return agentRole;
	}
	
	public int getContracting() {
		return contracting;
	}
	public void setContracting(int _contracting) {
		this.contracting = _contracting;
	}
	
	public int getHonoring(){
		return honoring;
	}
	public void setHonoring(int _honoring){
		this.honoring = _honoring;
	}
	
	public double getSMMedianPrice(){
		
		if (this.ordinaryRice == 1) {
			smMedianPrice = 5.457; 
		
		} else if (this.ordinaryRice == 0) {
		
			if (currentStep % 2 == 0) smMedianPrice = 6.873; 
			else smMedianPrice = 0; 
		}
		
		return smMedianPrice;
}
	
	// --------------------------- Steppable method --------------------------//

	 //@Override Step of the simulation.
	public void step(SimState state) {

		Model model = (Model) state;
		currentStep = (int) model.schedule.getSteps();
	}
	
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info){
		
		int diameter = 0; 
		if (agentRole == ModelParams.SMALL_F) diameter = ModelParams.SMALL_F_DIAMETER;
		if (agentRole == ModelParams.MEDIUM_F) diameter = ModelParams.MEDIUM_F_DIAMETER;
		if (agentRole == ModelParams.LARGE_F) diameter = ModelParams.LARGE_F_DIAMETER;
		if (agentRole == ModelParams.CONTRACTOR) diameter = ModelParams.C_DIAMETER;
	    
		final double width = info.draw.width * diameter;
	    final double height = info.draw.height * diameter;
	    
	    final int x = (int)(info.draw.x - width / 2.0);
	    final int y = (int)(info.draw.y - height / 2.0);
	    final int w = (int)(width);
	    final int h = (int)(height);
	
		// set the agent's color
		if (this.getAgentId() == ModelParams.NO_FARMERS) graphics.setColor(contractorColorA);
		if (this.getAgentId() == ModelParams.NO_FARMERS + 1) graphics.setColor(contractorColorB);
		
		if (contracting == ModelParams.NON_CONTRACTED_F) graphics.setColor(nonConFColor);
		else if (contracting == ModelParams.CONTRACTED_F){
			
			double contractorID = ((FarmerAgent)this).getCFContractorID();
			if (contractorID == ModelParams.NO_FARMERS) graphics.setColor(contractorColorA);
			else if (contractorID == ModelParams.NO_FARMERS + 1) graphics.setColor(contractorColorB);
		}
		graphics.fillOval(x,y,w,h);
    }
	
	protected Color contractorColorA = new Color(255,128,0);
	protected Color contractorColorB = new Color(0,128,255);
	
	protected Color nonConFColor = new Color(0,200,0);
}
