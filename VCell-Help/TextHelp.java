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

@Plugin(type = Command.class, label = "VCell-ImageJ Help",  iconPath="help.gif",menu = {
		@Menu(label = MenuConstants.PLUGINS_LABEL, weight = MenuConstants.FILE_WEIGHT,
			mnemonic = MenuConstants.FILE_MNEMONIC),
		@Menu(label = "VCell Help",  weight = 4, mnemonic = 'V'),
		@Menu(label = "VCell-ImageJ Help", weight = 9,
			mnemonic = 'h',accelerator = "^H") },  attrs = { @Attr(name = "no-legacy") })
public class TextHelp implements Command, Previewable {
//"(c) Copyright 1998-2020 UConn Health"
	
	@Parameter
	private UIService uiService;

	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header1 = "<html><h2 style=\"font-family:'Times New Roman';color:green;\">Hello " + System.getProperty("user.name") + " !  Welcome to VCell Help</h2><html>";

	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header2 = "<html><h3 style=\"font-family:'Optima';color:red;\">About your machine </h2><html>";

	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header3 = "<html><pstyle=\"font-family:'Optima'\">Java Version: "+" " + System.getProperty("java.version") +"<br> Operating System: "+" " +System.getProperty("os.name")+ " " + System.getProperty("os.arch")+"<br>(c) Copyright 1998-2020 UConn Health</p><html>";
	
	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header4 = "<html><h3 style=\"font-family:'Optima';color:red;\">Pre-Requirements for running VCell Plugins </h2><html>";

	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header5 = "<html><p style=\"font-family:'Optima'\">Virtual Cell is an advanced software platform for modeling and simulating reaction <br> "
		  		+ " kinetics,membrane transport and diffusion in the complex geometries</p><html>";
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header6 = "<html><p style=\"font-family:'Optima'\">To run various VCell Plugins there are certain pre requirements which are listed below <p> "
		  		+ "<ul> <li>VCell Client ImageJ service should be activated.</li>"
		  		+"<li>Check your connectivity and login to VCell with your userId or as a guest.</li></ul></html>";
	
	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header7 = "<html><h3 style=\"font-family:'Optima';color:red;\">Online Resources</h2><html>";
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header08 = "<html><p style=\"font-family:'Optima'\">To know more about VCell, visit our website. </p><html>";

	@Parameter(label="VCell Webiste", callback = "website")
	private Button buttonWebsite;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header09 = "<html><p style=\"font-family:'Optima'\">Having doubts or want to discuss with the community, visit the discussion forum</p><html>";

	@Parameter(label="Disucssion Forum", callback = "forum")
	private Button buttonForum;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header010 = "<html><p style=\"font-family:'Optima'\">Want to learn about changing permission for your model, checkout the permission changing page </p><html>";

	@Parameter(label="Change Permission", callback = "permission")
	private Button buttonPermission;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header011 = "<html><p style=\"font-family:'Optima'\">Want to make your model public, checkout the publishing model guidelines</p><html>";

	@Parameter(label="Publish your Model", callback = "publish")
	private Button buttonModel;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header012 = "<html><p style=\"font-family:'Optima'\">Visit VCell Support for your encountered problems</p><html>";

	@Parameter(label="Contact Support", callback = "support")
	private Button buttonSupport;

	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header8 = "<html><p style=\"font-family:'Optima'\">Featuring COPASI prameter estimation technology </p><html>";
	
	@Parameter(label="Visit COPASI", callback = "copasi")
	private Button buttonCopasi;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header9 = "<html><p style=\"font-family:'Optima'\">Featuring spatial stochastic simulations powered by SMOLDYN </p><html>";
	
	@Parameter(label="Visit SMOLYDYN", callback = "smoldyn")
	private Button buttonSmoldyn;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header10 = "<html><p style=\"font-family:'Optima'\">Featuring rule-based simulations powered by BIONETGEN </p><html>";
	
	@Parameter(label="Visit BIONETGEN", callback = "bionetgen", description = "help.gif")
	private Button buttonBionetgen;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header11 = "<html><p style=\"font-family:'Optima'\">Featuring network-free stochastic simulations powered by NFsim </p><html>";
	
	@Parameter(label="Visit NFsim", callback = "nfsim")
	private Button buttonNfsim;
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header12 = "<html><p style=\"font-family:'Optima';color: blue;\">Virtual Cell is supported by NIH Grant R24 GM137787 from the National Institute <br> "
		  		+ " for General Medical Sciences</p><html>";
	
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

	//for launching the VCell Website 	
	protected void website()
	{
        try {
            
            String myurl = "https://vcell.org/";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
	}
	
	//for launching the discussion forum
	protected void forum()
	{
        try {
            
            String myurl = "https://groups.google.com/g/vcell-discuss";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching the changing permission dialog
	protected void permission()
	{
		try {
            
            String myurl = "https://vcell.org/webstart/VCell_Tutorials/VCell_Help/topics/ch_1/Introduction/Permissions.html";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
	}
	
	//for publising your model
	protected void publish()
	{
        try {
            
            String myurl = "https://vcell.org/publish-a-vcell-model";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
	}
	
	//for launching the support
	protected void support()
	{
		try {
            
            String myurl = "https://vcell.org/support";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
	}
	
	//for launching the COPASI 
	protected void copasi()
	{
        try {
            
            String myurl = "http://copasi.org/";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching the SMOLDYN 
	protected void smoldyn()
	{
        try {
            
            String myurl = "http://www.smoldyn.org/";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching the BIONETEGEN
	protected void bionetgen()
	{
        try {
            
            String myurl = "http://bionetgen.org/";
           
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
           
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }	
	}
	
	//for launching the BIONETEGEN
	protected void nfsim()
	{
        try {
            
            String myurl = "http://michaelsneddon.net/nfsim/";
           
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
