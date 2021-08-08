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
import org.scijava.widget.Button;
import org.scijava.widget.NumberWidget;

import net.imagej.ImageJ;

import java.awt.GridBagLayout;

//import org.vcell.util.BeanUtils;

/*import cbit.vcell.client.PopupGenerator;
import cbit.vcell.resource.PropertyLoader*/

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * A test of text displays.
 * 
 * @author Curtis Rueden
 */

//For setting the location where the this plugin will be available 
@Plugin(type = Command.class, label = "VCell-ImageJ Template",  iconPath="editor_properties.png",menu = {
		@Menu(label = MenuConstants.PLUGINS_LABEL, weight = MenuConstants.FILE_WEIGHT,
			mnemonic = MenuConstants.FILE_MNEMONIC),
		@Menu(label = "VCell Plugins",  weight = 4, mnemonic = 'V'),
		@Menu(label = "VCell-ImageJ Template", weight = 30,
			mnemonic = 't',accelerator = "^T") },  attrs = { @Attr(name = "no-legacy") })

public class Template implements Command, Previewable {
	
	private void handleException(Throwable exception) {
		System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		exception.printStackTrace(System.out);
	}
	
	//putting a header or a display message
	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header1 = "<html><h2 style=\"font-family:'Times New Roman';color:green;\">Hello " + System.getProperty("user.name") + " !  Welcome to VCell Help</h2><html>";

	//Writing a simple message in the GUI
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header2 = "<html><p style=\"font-family:'Optima'; color:blue\">For writing a message or any content inside a GUI"
			+ "<br>one should be accoustmed of writing basic HTML code.</p><html>";
	
	//Browsing a file
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	private final String header3 = "<html><p style=\"font-family:'Optima'; color:red\">For browsing a file</p><html>";
	
	@Parameter 
	private File file;
	
	//Adding a button and adding functionality to it
	//changing the font style and font size of text
	@Parameter (visibility = ItemVisibility.MESSAGE) 
	private final String header4 = "<html><p style=\"font-family:'Times New Roman'; font-size:20px\">For browsing websites</p><html>";
	
	@Parameter(label="VCell Webiste", callback = "website")
	private Button buttonWebsite;
	
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

	public void show() {
		// TODO Auto-generated method stub
		
	}
	
}
