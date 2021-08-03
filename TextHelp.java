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

import java.awt.Font;
import java.awt.event.ActionEvent;

/**
 * A test of text displays.
 * 
 * @author Curtis Rueden
 */

@Plugin(type = Command.class, label = "VCell-ImageJ Help",  iconPath="help.gif",menu = {
		@Menu(label = MenuConstants.PLUGINS_LABEL, weight = MenuConstants.FILE_WEIGHT,
			mnemonic = MenuConstants.FILE_MNEMONIC),
		@Menu(label = "VCell Plugins",  weight = 4, mnemonic = 'V'),
		@Menu(label = "VCell-ImageJ Help", weight = 1,
			mnemonic = 'h',accelerator = "^H") },  attrs = { @Attr(name = "no-legacy") })
public class TextHelp implements Command, Previewable {


	@Parameter(visibility = ItemVisibility.MESSAGE)
	private final String header = "<html><h2 style=\"font-family:'Times New Roman'\">Hello " + System.getProperty("user.name") + " !  Welcome to VCell Help</h2><html>";
	//<p>
  //  We’re a <a href="/about/about_team.htm">team</a> of professionals working
   // hard to provide free learning content.
 //</p>
	//System.getProperty("java.version") + " " + System.getProperty("os.name")+ " " + System.getProperty("os.arch");
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header1= "<html><pstyle=\"font-family:'Optima'\">Version: "+" " + System.getProperty("java.version");
	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header2= "<html><pstyle=\"font-family:'Optima'\">Operating System: "+" " +System.getProperty("os.name")+ " " + System.getProperty("os.arch");

	
	@Parameter(visibility = ItemVisibility.MESSAGE) 
	  private final String header3= "<html><p style=\"font-family:'Optima'\">Virtual Cell is an advanced software platform for modeling  <br> "
		  		+ "and simulating reaction kinetics,membrane transport <br>"
		  		+ "and diffusion in the complex geometries</p><html>";
	

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
