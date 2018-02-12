package com.jcg.rca.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

public class DCFrame extends Shell {
	private Text PillarHeightTxt;
	private Text PillarWidthTxt;
	private Text PeriodTxt;
	private Text DutyCycleTxt;
	private Text DutyCyclePercentTxt;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			DCFrame shell = new DCFrame(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public DCFrame(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(0, 0, 295, 221);
		
		Group grpResults_1 = new Group(composite, SWT.NONE);
		grpResults_1.setText("Results");
		grpResults_1.setBounds(10, 10, 268, 185);
		
		Label lblPillarHeight = new Label(grpResults_1, SWT.NONE);
		lblPillarHeight.setBounds(10, 29, 111, 15);
		lblPillarHeight.setText("Pillar Height: ");
		
		PillarHeightTxt = new Text(grpResults_1, SWT.BORDER);
		PillarHeightTxt.setEditable(false);
		PillarHeightTxt.setBounds(138, 26, 111, 21);
		
		PillarWidthTxt = new Text(grpResults_1, SWT.BORDER);
		PillarWidthTxt.setEditable(false);
		PillarWidthTxt.setBounds(138, 53, 111, 21);
		
		PeriodTxt = new Text(grpResults_1, SWT.BORDER);
		PeriodTxt.setEditable(false);
		PeriodTxt.setBounds(138, 80, 111, 21);
		
		DutyCycleTxt = new Text(grpResults_1, SWT.BORDER);
		DutyCycleTxt.setEditable(false);
		DutyCycleTxt.setBounds(138, 107, 111, 21);
		
		Label lblPillarWidth = new Label(grpResults_1, SWT.NONE);
		lblPillarWidth.setBounds(10, 56, 100, 15);
		lblPillarWidth.setText("Pillar Width: ");
		
		Label lblDutyCycle = new Label(grpResults_1, SWT.NONE);
		lblDutyCycle.setBounds(10, 110, 100, 15);
		lblDutyCycle.setText("Duty Cycle: ");
		
		Label lblPeriod = new Label(grpResults_1, SWT.NONE);
		lblPeriod.setBounds(10, 83, 81, 15);
		lblPeriod.setText("Period:");
		
		Label lblDc = new Label(grpResults_1, SWT.NONE);
		lblDc.setBounds(10, 137, 55, 15);
		lblDc.setText("DC%");
		
		DutyCyclePercentTxt = new Text(grpResults_1, SWT.BORDER);
		DutyCyclePercentTxt.setEditable(false);
		DutyCyclePercentTxt.setBounds(138, 134, 111, 21);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Duty Cycle Results");
		setSize(302, 252);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	//Set the data into the Results window 
	protected void setData() {
		//TODO: convert data from pixels to mm
		DutyCycle dc = new DutyCycle();
		
		PillarHeightTxt.setText(dc.getPillarHeight());
		
		PillarWidthTxt.setText(dc.getPillarWidth());
		
		PeriodTxt.setText(dc.getPeriod());
		
		DutyCycleTxt.setText(dc.getDutyCycle());
		
		DutyCyclePercentTxt.setText(dc.getDcPercent());
	}
}
