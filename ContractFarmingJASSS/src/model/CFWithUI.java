package model;

import sim.portrayal.network.*;
import sim.portrayal.continuous.*;
import sim.engine.*;
import sim.display.*;
import javax.swing.*;

import java.awt.Color;

public class CFWithUI extends GUIState {
	
	public Display2D display;
	public JFrame displayFrame;
	
	ContinuousPortrayal2D regionPortrayal = new ContinuousPortrayal2D();
	NetworkPortrayal2D cfPortrayal = new NetworkPortrayal2D();

	public static void main(String[] args) {
		
		new CFWithUI().createController();
	}
	
	public CFWithUI(){
		super(new Model(System.currentTimeMillis()));
	}

	public static String getName(){
		return "Contract Farming Region";
	}
	
	public void start() {
		super.start();
		setupPortrayals();
	}
	
	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}
	
	public void setupPortrayals() {
		Model model = (Model) state;

		// tell the portrayals what to portray and how to portray them
		regionPortrayal.setField(model.region);
		//regionPortrayal.setPortrayalForAll(new OvalPortrayal2D());
		
		cfPortrayal.setField( new SpatialNetwork2D( model.region, model.contractFarming ) );
	    cfPortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());

		// reschedule the displayer
		display.reset();
		display.setBackdrop(Color.black);

		// redraw the display display.repaint();
		display.repaint();
	}
	
	public void init(Controller c) {

		super.init(c);
		display = new Display2D(ModelParams.REGION_WIDTH * 0.6, ModelParams.REGION_HEIGHT * 0.6, this);
		display.setClipping(false);

		displayFrame = display.createFrame();
		displayFrame.setTitle("Contract Farming Display");

		c.registerFrame(displayFrame); // so the frame appears in the "Display" list
		displayFrame.setVisible(true);
//		display.attach( cfPortrayal, "Contract Farming" );
		display.attach(regionPortrayal, "Region");
	}

	public void quit() {
		super.quit();

		if (displayFrame != null) displayFrame.dispose();

		displayFrame = null;
		display = null;
	}

}
