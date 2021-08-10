/*
 * #%L
 * Core commands for SciJava applications.
 * %%
 * Copyright (C) 2010 - 2017 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.vcell.imagej.plugin;


import java.awt.Dimension;
import org.scijava.ItemIO;
import org.scijava.ItemVisibility;
import org.scijava.command.Command;
import org.scijava.command.ContextCommand;
import org.scijava.command.Previewable;
import org.scijava.io.RecentFileService;
import org.scijava.menu.MenuConstants;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Menu;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.DialogPrompt;
import org.scijava.ui.UIService;
import org.scijava.widget.Button;
import org.scijava.widget.NumberWidget;
//import org.vcell.util.BeanUtils;

import net.imagej.ImageJ;

/*import cbit.vcell.client.PopupGenerator;
import cbit.vcell.resource.PropertyLoader*/

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

/**
 * A test of text displays.
 * 
 * @author Curtis Rueden
 */

@Plugin(type = Command.class, label = "VCell-ImageJ Groovy-Scripts",  iconPath="left.gif",menu = {
		@Menu(label = MenuConstants.PLUGINS_LABEL, weight = MenuConstants.FILE_WEIGHT,
			mnemonic = MenuConstants.FILE_MNEMONIC),
		@Menu(label = "VCell Help",  weight = 10, mnemonic = 'H'),
		@Menu(label = "VCell-ImageJ Groovy-Scripts", weight = 11,
			mnemonic = 'g',accelerator = "^G") },  attrs = { @Attr(name = "no-legacy") })
public class Groovy_Reference implements Command, Previewable {
//"(c) Copyright 1998-2020 UConn Health"
	
	@Parameter
	private UIService uiService;

	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header1 = "<html><h3 style=\"font-family:'Times New Roman';color:green;font-size:20px;\">You can access various Groovy Scripts from here</h3><html>";

	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header2 = "<html><p style=\"font-family:'Optima';color:red;font-size:15px;\">To run a Groovy Script: </p><html>";
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header3 = "<html><ul> <li>Click on Macro, a editable interface appears</li>"
		  		+ "<li>Copy the Groovy script and paste there</li>"
		  		+ "<li>Under the 'Language' menu choose Groovy</li>"	
		  		+"<li>When you are done click run, you will get the desired result.</li></ul></html>";
	
	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header4 = "<html><h3 style=\"font-family:'Optima';color:red;font-size:15px;\">Access Groovy Scripts from Github</h2><html>";
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header05 = "<html><p style=\"font-family:'Optima'\">Groovy Script for displaying one variable at a time</p><html>";

	@Parameter(label="Display1Var1Time", callback = "display")
	private Button buttonDisplay;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header6 = "<html><p style=\"font-family:'Optima'\">Groovy Script for displaying Line and Time Plot </p><html>";

	@Parameter(label="Chart", callback = "chart")
	private Button buttonChart;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header7 = "<html><p style=\"font-family:'Optima'\">saidkerbai2.</p><html>";

	@Parameter(label="Said Kerbai2", callback = "saidkerbai2")
	private Button buttonSaidkerbai2;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header8 = "<html><p style=\"font-family:'Optima'\">combineFig</p><html>";

	@Parameter(label="CombineFig", callback = "combinefig")
	private Button buttonCombinefig;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header9 = "<html><p style=\"font-family:'Optima'\">complex</p><html>";

	@Parameter(label="Complex", callback = "complex")
	private Button buttoncomplex;

	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header10 = "<html><p style=\"font-family:'Optima'\">example</p><html>";
	
	@Parameter(label="Example", callback = "example")
	private Button buttonexample;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header11 = "<html><p style=\"font-family:'Optima'\">example2</p><html>";
	
	@Parameter(label="example2", callback = "example2")
	private Button buttonexample2;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header12 = "<html><p style=\"font-family:'Optima'\">kjp_stoch</p><html>";
	
	@Parameter(label="kjp_stoch", callback = "kjp_stoch")
	private Button buttonkjp_stoch;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header13 = "<html><p style=\"font-family:'Optima'\">open2D</p><html>";
	
	@Parameter(label="open2D", callback = "open2D")
	private Button buttonopen2D;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header14 = "<html><p style=\"font-family:'Optima'\">photoactivation</p><html>";
	
	@Parameter(label="Photoactivation", callback = "photoactivation")
	private Button buttonphotoactivation;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header15 = "<html><p style=\"font-family:'Optima'\">photoactivation_analyze</p><html>";
	
	@Parameter(label="photoactivation_analyze", callback = "photoactivation_analyze")
	private Button buttonphotoactivation_analyze;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header16 = "<html><p style=\"font-family:'Optima'\">photoactivation_load</p><html>";
	
	@Parameter(label="photoactivation_load", callback = "photoactivation_load")
	private Button buttonphotoactivation_load;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header17 = "<html><p style=\"font-family:'Optima'\">photoactivation_run</p><html>";
	
	@Parameter(label="photoactivation_run", callback = "photoactivation_run")
	private Button buttonphotoactivation_run;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header18 = "<html><p style=\"font-family:'Optima'\">solverData</p><html>";
	
	@Parameter(label="solverData", callback = "solverData")
	private Button buttonsolverData;

	
	public static void main(final String... args) throws Exception {
        // create the ImageJ application context with all available services
    	
    	
        final ImageJ ij = new ImageJ();
        ij.ui().showUI();
    }

	private JDialog progressDialog = null;
	private final Dimension dim = new Dimension(200,25);
	private final JProgressBar jProgressBar = new JProgressBar(0,100) {
		@Override
		public Dimension getPreferredSize() {
			// TODO Auto-generated method stub
			return dim;
		}
		@Override
		public Dimension getSize(Dimension rv) {
			// TODO Auto-generated method stub
			return dim;
		}
	};

	//for launching Display1Var1Time Groovy Script 	
	protected void display()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/Display1Var1Time.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
	}
	
	//for launching Chart Groovy Script
	protected void chart()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/chart.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching saidkerbai2 Groovy Script
	protected void saidkerbai2()
	{
		try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/Said_Kerbai2.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
	}
	
	//for launching combinefig 
	protected void combinefig()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/combineFig.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
	}
	
	//for launching complex Groovy Script
	protected void complex()
	{
		try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/complex.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
	}
	
	//for launching example Groovy Script
	protected void example()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/example.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching the SMOLDYN 
	protected void example2()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/example2.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching the kjp_stoch Groovy Script
	protected void kjp_stoch()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/kjp_stoch.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching open2D Groovy Script
	protected void open2D()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/open2D.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
    
	//for launching photoactivation Groovy Script
	protected void photoactivation()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/photoactivation.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching photoactivation_analyze Groovy Script
	protected void photoactivation_analyze()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/photoactivation_analyze.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching photoactivation_load Groovy Script
	protected void photoactivation_load()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/photoactivation_load.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching open2D Groovy Script
	protected void photoactivation_run()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/photoactivation_run.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching open2D Groovy Script
	protected void solverData()
	{
        try {
            
            String myurl = "https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/solverData.groovy";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	@Override
	public void preview() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
