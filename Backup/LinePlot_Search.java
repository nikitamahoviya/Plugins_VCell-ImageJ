/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package org.vcell.imagej.plugin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import org.jfree.data.Range;
import org.scijava.command.ContextCommand;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.DialogPrompt.MessageType;
import org.scijava.ui.UIService;
import org.vcell.imagej.helper.VCellHelper;
import org.vcell.imagej.helper.VCellHelper.BasicStackDimensions;
import org.vcell.imagej.helper.VCellHelper.IJDataList;
import org.vcell.imagej.helper.VCellHelper.IJVarInfos;
import org.vcell.imagej.helper.VCellHelper.VARTYPE_POSTPROC;
import org.vcell.imagej.helper.VCellHelper.VCellModelSearchResults;

import net.imagej.ImageJ;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.type.numeric.real.DoubleType;


/**
 * This example illustrates how to create an ImageJ {@link ContextCommand} plugin that uses VCellHelper.
 * <p>
 * You should replace the parameter fields with your own inputs and outputs,
 * and replace the {@link run} method implementation with your own logic.
 * </p>
 * <p>
 * To add VCellHelper to this project,
 * rt-click on topmost tree element
 * "imagej-plugin2"->Properties->Libraries tab->Add External Jars...->
 * File Dialog->{EclipseVCellWorkspaceRootDir}/vcell/vcell-imagej-helper/target/vcell-imagej-helper-0.0.1-SNAPSHOT.jar.
 * </p>
 * <p>
 * Once vcell-imagej-helper-0.0.1-SNAPSHOT.jar has been added to the Libraries tab open
 * the small arrow to the left and select "Source Attachment"->Add/Edit->External Location->
 * External File Dialog->{EclipseVCellWorkspaceRootDir}/vcell/vcell-imagej-helper/target/vcell-imagej-helper-0.0.1-SNAPSHOT-sources.jar.
 * </p>
 * <p>
 * When editing the original VCellHelper in another running Eclipse,
 * to make the changes show up in this project choose thisEclipse->Project->Clean...->clean.
 * </p>
  */
@Plugin(type = ContextCommand.class, menuPath = "Plugins>LinePlot_Search")
public class LinePlot_Search extends ContextCommand {
	
	
	
	public static class StyledComboBoxUI extends BasicComboBoxUI {
		  protected ComboPopup createPopup() {
		    BasicComboPopup popup = new BasicComboPopup(comboBox) {
		      @Override
		      protected Rectangle computePopupBounds(int px,int py,int pw,int ph) {
		        return super.computePopupBounds(
		            px,py,Math.max(comboBox.getPreferredSize().width,pw),ph
		        );
		      }
		    };
		    popup.getAccessibleContext().setAccessibleParent(comboBox);
		    return popup;
		  }
		}

	public static class StyledComboBox<E> extends JComboBox {
		  public StyledComboBox() {
		    setUI(new StyledComboBoxUI());
		  }
		  public StyledComboBox(E[] items) {
			  setUI(new StyledComboBoxUI());
			  setModel(new DefaultComboBoxModel<E>(items));
		  }
		public StyledComboBox(Object[] array) {
			// TODO Auto-generated constructor stub
		}
		}
	
	public static class VCellSelection {
		public String theCacheKey;
		public String userid;
		public String modelName;
		public String appName;
		public String simname;
		public int startIndex;
		public int endIndex;
		public String varName;
		public int timePointIndex;
		public Exception exception;
		public VCellSelection(String theCacheKey, String userid,String modelName, String appName, String simname,String varName,int timePointIndex, int startIndex, int endIndex) {
			super();
			this.theCacheKey = theCacheKey;
			this.userid=userid;
			this.modelName = modelName;
			this.appName = appName;
			this.simname = simname;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.varName = varName;
			this.timePointIndex = timePointIndex;
		}
		public VCellSelection(Exception exception) {
			this.exception = exception;
		}
	}
	
	@Plugin(type = PreprocessorPlugin.class)
	public static class MyPreProcessor extends AbstractPreprocessorPlugin {
		
		public static final String CANCELLED = "cancelled";

		@Parameter
		private UIService uiService;

		@Parameter(required = true)
		private VCellHelper vcellHelper;

		
		@Override
		public void process(Module module) {
			final ModuleItem<VCellSelection> vcellModelsInput = getvcellModelsInput(module);
			if (vcellModelsInput == null) {
				return;
			}
			Comparator<String> comp = new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.toLowerCase().compareTo(o2.toLowerCase());
				}};
			TreeSet<String> useridSet = new TreeSet<String>(comp);
			Hashtable<String,TreeSet<String>> mapUseridToModelNameTime  = new Hashtable<String, TreeSet<String>>();
			Hashtable<String,String> mapModelNameTimeToActualModelname  = new Hashtable<String, String>();
			Hashtable<String,TreeSet<String>> mapModelToApps = new Hashtable<String, TreeSet<String>>();
			Hashtable<String,TreeSet<String>> mapAppsToSims = new Hashtable<String, TreeSet<String>>();
	        Hashtable<Integer, TreeSet<String>> mapSimsToSI = new Hashtable<Integer,TreeSet<String>>(1279);
	        Hashtable<Integer, TreeSet<String>> mapSIToEI = new Hashtable<Integer, TreeSet<String>>(1321);
			//	Hashtable<int,TreeSet<int>> mapSimsToStartIndex = new Hashtable<String, TreeSet<int>>();
//			VCellHelper.VCellModelSearch vcms = new VCellHelper.VCellModelSearch(VCellHelper.ModelType.bm,"tutorial","Tutorial_MultiApp","3D pde","Simulation4",null,null);
			displayProgressBar(true, "Searching Database...", "VCell Model Loader", 25,uiService);
			VCellHelper.VCellModelSearch vcms = new VCellHelper.VCellModelSearch(VCellHelper.ModelType.bm,null,null,null,null,null,null);
			displayProgressBar(true, "Creating GUI...", "VCell Model Loader", 100,uiService);
			try {
				final DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
				ArrayList<VCellModelSearchResults> vcmsr = vcellHelper.getSearchedModelSimCacheKey(false,vcms,null);
				final Iterator<VCellModelSearchResults> iterator = vcmsr.iterator();
				while(iterator.hasNext()) {
					final VCellModelSearchResults next = iterator.next();
					String userid = next.getUserId();
					useridSet.add(userid);
					final String modelName = next.getModelName();
					final String modelNameTime = modelName+" - "+(next.getDate()==null?"unsaved":dateTimeInstance.format(new Date(next.getDate())));
					TreeSet<String> modelsForUserid = mapUseridToModelNameTime.get(userid);
					if(modelsForUserid == null) {
						modelsForUserid = new TreeSet<String>();
						mapUseridToModelNameTime.put(userid, modelsForUserid);
					}
					modelsForUserid.add(modelNameTime);
					mapModelNameTimeToActualModelname.put(modelNameTime, modelName);
					//System.out.println(modelName+" "+next.getApplicationName()+" "+next.getSimulationName());
					TreeSet<String> appsForModel = mapModelToApps.get(modelNameTime);
					if(appsForModel == null) {
						appsForModel = new TreeSet<String>();
						mapModelToApps.put(modelNameTime, appsForModel);
					}
					appsForModel.add(next.getApplicationName());
					String modelNameTimeApp = modelNameTime+" "+next.getApplicationName();
					TreeSet<String> simsForApp = mapAppsToSims.get(modelNameTimeApp);
					if(simsForApp == null) {
						simsForApp = new TreeSet<String>();
						mapAppsToSims.put(modelNameTimeApp, simsForApp);
					}
					simsForApp.add(next.getSimulationName());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				displayProgressBar(false, "Creating GUI...", "VCell Model Loader", 100,uiService);
				vcellModelsInput.setValue(module, new VCellSelection(e));//return empty VCellSelection
				module.resolveInput(vcellModelsInput.getName());
				return;
			}finally {
				displayProgressBar(false, "Creating GUI...", "VCell Model Loader", 100,uiService);
			}
			
			JFrame applicationFrame = (JFrame)uiService.getDefaultUI().getApplicationFrame();
			final Dimension dim = new Dimension(300,120);
			JPanel jp = new JPanel() {
				@Override
				public Dimension getPreferredSize() {
					return dim;
				}
			};
			jp.setLayout(new GridLayout(7,2));
			
			jp.add(new JLabel("VCell Userid"));
			JComboBox<String> jcbUserid = new StyledComboBox<String>(useridSet.toArray(new String[0]));
			jp.add(jcbUserid);

			jp.add(new JLabel("Model Name"));
			JComboBox<String> jcbModelNames = new StyledComboBox<String>(mapUseridToModelNameTime.get(jcbUserid.getSelectedItem()).toArray(new String[0]));
			jcbUserid.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jcbModelNames.removeAllItems();
					if(jcbUserid.getSelectedItem() != null && mapUseridToModelNameTime.get(jcbUserid.getSelectedItem()) != null) {
						jcbModelNames.setModel(new DefaultComboBoxModel<String>(mapUseridToModelNameTime.get(jcbUserid.getSelectedItem()).toArray(new String[0])));
						jcbModelNames.setSelectedIndex(0);
					}
				}});
			jp.add(jcbModelNames);

			
			jp.add(new JLabel("App Name"));
			JComboBox<String> jcbAppNames = new StyledComboBox<String>(mapModelToApps.get(jcbModelNames.getSelectedItem()).toArray(new String[0]));
			jcbModelNames.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jcbAppNames.removeAllItems();
					if(jcbModelNames.getSelectedItem() != null && mapModelToApps.get(jcbModelNames.getSelectedItem()) != null) {
						jcbAppNames.setModel(new DefaultComboBoxModel<String>(mapModelToApps.get(jcbModelNames.getSelectedItem()).toArray(new String[0])));
						jcbAppNames.setSelectedIndex(0);
					}
				}});
			jp.add(jcbAppNames);

			final JComboBox<String> jcbVars = new StyledComboBox<String>();
			jcbVars.setEnabled(false);

			final JComboBox<String> jcbTimes = new StyledComboBox<String>();
			jcbTimes.setEnabled(false);

			jp.add(new JLabel("Sim Name"));
			JComboBox<String> jcbSimNames = new StyledComboBox<String>(mapAppsToSims.get(jcbModelNames.getSelectedItem()+" "+jcbAppNames.getSelectedItem()).toArray(new String[0]));
			jcbAppNames.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jcbVars.removeAllItems();
					jcbTimes.removeAllItems();
					jcbSimNames.removeAllItems();
					if(jcbAppNames.getSelectedItem() != null && mapAppsToSims.get(jcbModelNames.getSelectedItem()+" "+jcbAppNames.getSelectedItem()) != null) {
						jcbSimNames.setModel(new DefaultComboBoxModel<String>(mapAppsToSims.get(jcbModelNames.getSelectedItem()+" "+jcbAppNames.getSelectedItem()).toArray(new String[0])));
						jcbSimNames.setSelectedIndex(0);
					}
				}});
			jp.add(jcbSimNames);
			


			/*jp.add(new JLabel("Start Index"));
			@SuppressWarnings("unchecked")

			JComboBox<String> jcbStartIndex = new StyledComboBox<String>(mapModelToApps.get(jcbModelNames.getSelectedItem()).toArray(new String[0]));
			jcbStartIndex.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jcbStartIndex.removeAllItems();
					if(jcbModelNames.getSelectedItem() != null && mapSimsToSI.get(jcbModelNames.getSelectedItem()) != null) {
						jcbStartIndex.setModel(new DefaultComboBoxModel<String>(mapSimsToSI.get(jcbModelNames.getSelectedItem()).toArray(new String[0])));
						jcbStartIndex.setSelectedIndex(1279);
					}
				}});
			jp.add(jcbStartIndex);*/

		/*	jp.add(new JLabel("End Index"));
			@SuppressWarnings("unchecked")
			JComboBox<String> jcbEndIndex = new StyledComboBox<String>(mapModelToApps.get(jcbModelNames.getSelectedItem()).toArray(new String[0]));
			jcbStartIndex.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jcbEndIndex.removeAllItems();
					if(jcbModelNames.getSelectedItem() != null && mapSIToEI.get(jcbModelNames.getSelectedItem()) != null) {
						jcbEndIndex.setModel(new DefaultComboBoxModel<String>(mapSIToEI.get(jcbModelNames.getSelectedItem()).toArray(new String[0])));
						jcbEndIndex.setSelectedIndex(1321);
					}
				}});
			jp.add(jcbEndIndex);
			*/
			final String[] cacheKeyHolder = new String[1];
			final IJVarInfos[] ijVarInfosHolder = new IJVarInfos[1];
			
			jp.add(new JLabel("Load Vars and Times"));
			JButton loadVarsAndTimesBtn = new JButton("Load...");
			loadVarsAndTimesBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							displayProgressBar(true, "Getting Vars and Times...", "VCell Model Loader", 25,uiService);
							String userid = jcbUserid.getSelectedItem().toString();
							String modelName = (jcbModelNames.getSelectedItem()==null?null:mapModelNameTimeToActualModelname.get(jcbModelNames.getSelectedItem()).toString());
							String appName = (jcbModelNames.getSelectedItem()==null?null:jcbAppNames.getSelectedItem().toString());
							String simName = (jcbModelNames.getSelectedItem()==null?null:jcbSimNames.getSelectedItem().toString());
							VCellHelper.VCellModelSearch vcms = new VCellHelper.VCellModelSearch(VCellHelper.ModelType.bm,userid,modelName,appName,simName,null,null);
						      try {
						  		ArrayList<VCellModelSearchResults> vcmsr = vcellHelper.getSearchedModelSimCacheKey(false,vcms,null);
						  		if(vcmsr.size() == 0) {
						  			throw new Exception("No Results for search found");
						  		}
						  		cacheKeyHolder[0] = vcmsr.get(0).getCacheKey();
						  		System.out.println("theCacheKey="+cacheKeyHolder[0]);
						  		
						  		SwingUtilities.invokeAndWait(new Runnable() {
									@Override
									public void run() {
								  		try {
								  			displayProgressBar(true, "Setting Var Times GUI...", "VCell Model Loader", 100,uiService);
											jcbVars.setEnabled(true);
											ijVarInfosHolder[0] = vcellHelper.getVarInfos(cacheKeyHolder[0]);
											for(int i=0;i<ijVarInfosHolder[0].getIjVarInfo().size();i++) {
												jcbVars.insertItemAt(""+ijVarInfosHolder[0].getIjVarInfo().get(i).getName()+":"+ijVarInfosHolder[0].getIjVarInfo().get(i).getDomain()+" ("+ijVarInfosHolder[0].getIjVarInfo().get(i).getVariableType()+")", i);
											}

											jcbTimes.setEnabled(true);
											for(int i=0;i<ijVarInfosHolder[0].getTimes().length;i++) {
												jcbTimes.insertItemAt(""+ijVarInfosHolder[0].getTimes()[i], i);
											}
											
											if(vcellModelsInput.getDefaultValue() != null) {//If user provided an inital value for VCellSelection var in VCellPlugin
												final VCellSelection defaultValue = vcellModelsInput.getDefaultValue();
												jcbVars.setSelectedIndex(0);
												for(int i=0;i<jcbVars.getModel().getSize();i++) {
													if(jcbVars.getModel().getElementAt(i).startsWith(defaultValue.varName+":")) {
														jcbVars.setSelectedIndex(i);
														break;
													}
												}
												
												jcbTimes.setSelectedIndex(0);
												if(defaultValue.timePointIndex < jcbTimes.getModel().getSize()) {
													jcbTimes.setSelectedIndex(defaultValue.timePointIndex);
												}
											}
										} catch (Exception e) {
											//e.printStackTrace();
									  		uiService.showDialog("VCellHelper.ModelType.bm,\"colreeze\",\""+modelName+"\",\""+appName+"\",\""+simName+"\",null,null\n"+e.getMessage(), "GUI setupfailed", MessageType.ERROR_MESSAGE);

										}finally {
											displayProgressBar(false, "Getting Vars and Times...", "VCell Model Loader", 25,uiService);
										}
										
									}});
						  		
						  		
						  	} catch (Exception e2) {
						  		displayProgressBar(false, "Getting Vars and Times...", "VCell Model Loader", 25,uiService);
						  		//e.printStackTrace();
						  		uiService.showDialog("VCellHelper.ModelType.bm,\"colreeze\",\""+modelName+"\",\""+appName+"\",\""+simName+"\",null,null\n"+e2.getMessage(), "Search failed", MessageType.ERROR_MESSAGE);
						  	}
						}}).start();
				}
				});
			jp.add(loadVarsAndTimesBtn);
			
	  		jp.add(new JLabel("Variables"));
			jp.add(jcbVars);
			jp.add(new JLabel("Times"));
			jp.add(jcbTimes);
			
	    	   IJDataList timePointData = null;
	     	  int[] timePointIndexes = new int[1]; // there is 1 timePoint
	     	  int theTimePointIndex = 0;
	     	  timePointIndexes[theTimePointIndex] = 500;
	     	  try {
	 			timePointData = vcellHelper.getTimePointData(theCacheKey, variable, VARTYPE_POSTPROC.NotPostProcess, timePointIndexes, 0);
	 		} catch (Exception e) {
				uiService.showDialog("VCellHelper.ModelType.bm,\"colreeze\",\""+modelName+"\",\""+appName+"\",\""+simName+"\",null,null\n"+e.getMessage(), "GUI setupfailed", MessageType.ERROR_MESSAGE);
	 			return;
	 		}
	     	 double[] theTimePointData = timePointData.ijData[theTimePointIndex].getDoubleData();
	     	Range xAxisRange = null;
			int startIndex = 1279;
			int endIndex = 1321;
	    	  int[] dataIndexes = new int[endIndex-startIndex+1];
	 		double[] dataIndexesDouble = new double[dataIndexes.length];// This is just for JFreeChart
	    	  for(int i=startIndex;i<=endIndex;i++) {
	    		  dataIndexes[i-startIndex] = i;
	    		dataIndexesDouble[i-startIndex] = i;
	    		xAxisRange = Range.expandToInclude(xAxisRange, dataIndexesDouble[i-startIndex]);
	    	  }

	    	  Range yAxisRange = null;
	     	double[] chartTheseDataPoints = new double[dataIndexes.length];
	 		for(int i=0;i<dataIndexes.length;i++){
	 			chartTheseDataPoints[i] = theTimePointData[dataIndexes[i]];
	 			yAxisRange = Range.expandToInclude(yAxisRange,chartTheseDataPoints[i]);
	 				//ijTimeSeriesJobResults.data[0/*index of "r"*/][i+1/*data, skip 0 because it holds copy of times*/][0/*0 always because we had only 1 timePoint=22.0*/]
	 		}
			
			jp.add(new JLabel("Line Plot by Time"));
			JButton lineplotbytimeBtn = new JButton("Show...");
			lineplotbytimeBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							   	try {
			//LINE plot of data at 1 timePoint along defined indexes
			//Create JFreechart x,y axis arrays for plotting x=data indexes, y=dataPoint values
			double[][] data = new double[][] {dataIndexesDouble,chartTheseDataPoints};



			String title = "LINE Plot at time="+timePoint;
			String xAxisLabel = "distance";
			String yAxisLabel = "val";

			DefaultXYDataset xyDataset = new DefaultXYDataset();
			xyDataset.addSeries( "data1", data);


			JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, xyDataset, PlotOrientation.VERTICAL, false, false, false);
			chart.getXYPlot().getDomainAxis().setRange(xAxisRange);//Y
			chart.getXYPlot().getRangeAxis().setRange(yAxisRange);//X
			ChartPanel chartPanel = new ChartPanel(chart);

			JFrame frame = new JFrame("Chart");
			        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(chartPanel);
			        //Display the window.
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			uiService.showDialog("LINE PlotGet Data failed\n"+e.getMessage(), MessageType.ERROR_MESSAGE);
			return;
		}
			jp.add(new JLabel("Time Plot at Data Index"));
			JButton timeplotatdataindexBtn = new JButton("Show...");
			timeplotatdataindexBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
								   	IJTimeSeriesJobResults timeSeries = null;
	   	try {
			IJVarInfos simulationInfo = vcellHelper.getSimulationInfo(theCacheKey);
			double[] times = simulationInfo.getTimes();
			
			timeSeries = vcellHelper.getTimeSeries(new String[] {variable}, new int[] {dataIndexes[0]}, times[0], 1, times[times.length-1], false, false, 0, Integer.parseInt(theCacheKey));
		} catch (Exception e) {
			uiService.showDialog("TIME Plot Get Data failed\n"+e.getMessage(), MessageType.ERROR_MESSAGE);
			return;
		}
	   	
		try {
			//TIME plot of data at 1 timePoint along defined indexes
			//Create JFreechart x,y axis arrays for plotting x=data indexes, y=dataPoint values
			double[][] data = new double[][] {timeSeries.data[0][0],timeSeries.data[0][1]};



			String title = "TIME Plot at dataIndex="+dataIndexes[0];
			String xAxisLabel = "time";
			String yAxisLabel = "val";

			DefaultXYDataset xyDataset = new DefaultXYDataset();
			xyDataset.addSeries( "data1", data);


			JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, xyDataset, PlotOrientation.VERTICAL, false, false, false);
//		chart.getXYPlot().getDomainAxis().setRange(yAxisRange);//Y
//		chart.getXYPlot().getRangeAxis().setRange(xAxisRange);//X
			ChartPanel chartPanel = new ChartPanel(chart);

			JFrame frame = new JFrame("Chart");
			        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(chartPanel);
			        //Display the window.
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			uiService.showDialog("TIME Plot show chart failed\n"+e.getMessage(), MessageType.ERROR_MESSAGE);
			return;
		}		
	}
						
			jp.add(new JLabel("Both the Plots"));
			JButton boththeplotsBtn = new JButton("Show...");
			boththeplotsBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
								   	try {
			//LINE plot of data at 1 timePoint along defined indexes
			//Create JFreechart x,y axis arrays for plotting x=data indexes, y=dataPoint values
			double[][] data = new double[][] {dataIndexesDouble,chartTheseDataPoints};



			String title = "LINE Plot at time="+timePoint;
			String xAxisLabel = "distance";
			String yAxisLabel = "val";

			DefaultXYDataset xyDataset = new DefaultXYDataset();
			xyDataset.addSeries( "data1", data);


			JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, xyDataset, PlotOrientation.VERTICAL, false, false, false);
			chart.getXYPlot().getDomainAxis().setRange(xAxisRange);//Y
			chart.getXYPlot().getRangeAxis().setRange(yAxisRange);//X
			ChartPanel chartPanel = new ChartPanel(chart);

			JFrame frame = new JFrame("Chart");
			        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(chartPanel);
			        //Display the window.
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			uiService.showDialog("LINE PlotGet Data failed\n"+e.getMessage(), MessageType.ERROR_MESSAGE);
			return;
		}

	   	  
	   	  
	   	IJTimeSeriesJobResults timeSeries = null;
	   	try {
			IJVarInfos simulationInfo = vcellHelper.getSimulationInfo(theCacheKey);
			double[] times = simulationInfo.getTimes();
			
			timeSeries = vcellHelper.getTimeSeries(new String[] {variable}, new int[] {dataIndexes[0]}, times[0], 1, times[times.length-1], false, false, 0, Integer.parseInt(theCacheKey));
		} catch (Exception e) {
			uiService.showDialog("TIME Plot Get Data failed\n"+e.getMessage(), MessageType.ERROR_MESSAGE);
			return;
		}
	   	
		try {
			//TIME plot of data at 1 timePoint along defined indexes
			//Create JFreechart x,y axis arrays for plotting x=data indexes, y=dataPoint values
			double[][] data = new double[][] {timeSeries.data[0][0],timeSeries.data[0][1]};



			String title = "TIME Plot at dataIndex="+dataIndexes[0];
			String xAxisLabel = "time";
			String yAxisLabel = "val";

			DefaultXYDataset xyDataset = new DefaultXYDataset();
			xyDataset.addSeries( "data1", data);


			JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, xyDataset, PlotOrientation.VERTICAL, false, false, false);
//		chart.getXYPlot().getDomainAxis().setRange(yAxisRange);//Y
//		chart.getXYPlot().getRangeAxis().setRange(xAxisRange);//X
			ChartPanel chartPanel = new ChartPanel(chart);

			JFrame frame = new JFrame("Chart");
			        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(chartPanel);
			        //Display the window.
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			uiService.showDialog("TIME Plot show chart failed\n"+e.getMessage(), MessageType.ERROR_MESSAGE);
			return;
		}		
	}
			
			if(vcellModelsInput.getDefaultValue() != null) {//If user provided an inital value for VCellSelection var in VCellPlugin
				final VCellSelection defaultValue = vcellModelsInput.getDefaultValue();
				jcbUserid.setSelectedItem(defaultValue.userid);
				System.out.println(jcbUserid.getSelectedItem());
				for(int i=0;i<jcbModelNames.getModel().getSize();i++) {
					//Do this because the model names are annotated with their date in this combobox
					if(jcbModelNames.getModel().getElementAt(i).toString().startsWith(defaultValue.modelName)) {
						jcbModelNames.setSelectedIndex(i);
						break;
					}
				}				
				System.out.println(jcbModelNames.getSelectedItem());
				jcbAppNames.setSelectedItem(defaultValue.appName);
				System.out.println(jcbAppNames.getSelectedItem());
				jcbSimNames.setSelectedItem(defaultValue.simname);
				System.out.println(jcbSimNames.getSelectedItem());
			}

			displayProgressBar(false, "Creating GUI...", "VCell Model Loader", 100,uiService);
			int response = JOptionPane.showConfirmDialog(applicationFrame, jp,"Select User Model App Sim",JOptionPane.OK_CANCEL_OPTION);
			if(response != JOptionPane.OK_OPTION) {
				vcellModelsInput.setValue(module, new VCellSelection(new Exception(CANCELLED)));//return VCellSelection with 'cancel' exception
				module.resolveInput(vcellModelsInput.getName());
				return;
			}
			
			String userid = jcbUserid.getSelectedItem().toString();
			String modelName = (jcbModelNames.getSelectedItem()==null?null:mapModelNameTimeToActualModelname.get(jcbModelNames.getSelectedItem()).toString());
			String appName = (jcbModelNames.getSelectedItem()==null?null:jcbAppNames.getSelectedItem().toString());
			String simName = (jcbModelNames.getSelectedItem()==null?null:jcbSimNames.getSelectedItem().toString());

		    VCellSelection result = new VCellSelection(cacheKeyHolder[0], userid,modelName, appName, simName,ijVarInfosHolder[0].getIjVarInfo().get(jcbVars.getSelectedIndex()).getName(),jcbTimes.getSelectedIndex());
			vcellModelsInput.setValue(module, result);
			module.resolveInput(vcellModelsInput.getName());

//			String userid = jcbUserid.getSelectedItem().toString();
//			String modelName = (jcbModelNames.getSelectedItem()==null?null:mapModelNameTimeToActualModelname.get(jcbModelNames.getSelectedItem()).toString());
//			String appName = (jcbModelNames.getSelectedItem()==null?null:jcbAppNames.getSelectedItem().toString());
//			String simName = (jcbModelNames.getSelectedItem()==null?null:jcbSimNames.getSelectedItem().toString());
//			vcms = new VCellHelper.VCellModelSearch(VCellHelper.ModelType.bm,userid,modelName,appName,simName,null,null);
//			String theCacheKey = null;
//		      try {
//		  		ArrayList<VCellModelSearchResults> vcmsr = vcellHelper.getSearchedModelSimCacheKey(false,vcms,null);
//		  		if(vcmsr.size() == 0) {
//		  			throw new Exception("No Results for search found");
//		  		}
//		  		theCacheKey = vcmsr.get(0).getCacheKey();
//		  		System.out.println("theCacheKey="+theCacheKey);
//		  		
//		  		jcbTimes.setEnabled(true);
//		  		IJVarInfos ijVarInfos = vcellHelper.getVarInfos(theCacheKey);
//				for(int i=0;i<ijVarInfos.getTimes().length;i++) {
//					jcbTimes.insertItemAt(""+ijVarInfos.getTimes()[i], i);
//				}
//
//		  		//displayProgressBar(false, null, null);
//		  	} catch (Exception e) {
//		  		//e.printStackTrace();
//		  		uiService.showDialog("VCellHelper.ModelType.bm,\"tutorial\",\""+modelName+"\",\""+appName+"\",\""+simName+"\",null,null\n"+e.getMessage(), "Search failed", MessageType.ERROR_MESSAGE);
//		  		//displayProgressBar(false, null, null);
//		  	}
//
//		    VCellSelection result = new VCellSelection(theCacheKey, userid,modelName, appName, simName);
//			vcellModelsInput.setValue(module, result);
//			module.resolveInput(vcellModelsInput.getName());

//			System.out.println(module);
//			final Map<String, Object> inputs = module.getInputs();
//			for(String key:inputs.keySet()) {
//				System.out.println("     "+key+" "+inputs.get(key));
//			}
		}
		
		private ModuleItem<VCellSelection> getvcellModelsInput(final Module module) {
			ModuleItem<VCellSelection> result = null;
			for (final ModuleItem<?> input : module.getInfo().inputs()) {
				if (module.isInputResolved(input.getName())) continue;
				final Class<?> type = input.getType();
				if (!VCellSelection.class.isAssignableFrom(type)) {
					// not a VCellSelection parameter; abort
					return null;
				}
				if (result != null) {
					// second VCellSelection parameter; abort
					return null;
				}
				@SuppressWarnings("unchecked")
				final ModuleItem<VCellSelection> vcellSelect = (ModuleItem<VCellSelection>) input;
				result = vcellSelect;
			}
			return result;
		}
		
		private JPanel getLabeledJComboBox(String labelName,String[] items) {
			JPanel jp = new JPanel(new FlowLayout());
			jp.add(new JLabel(labelName));
			JComboBox<String> jcbModelNames = new JComboBox<String>(items);
			jp.add(jcbModelNames);
			return jp;
		}

	}	
	
	@Parameter
	private UIService uiService;

  	@Parameter
	private VCellHelper vcellHelper;
  	
  	@Parameter
  	private VCellSelection vcellSelection = new VCellSelection("-1", "colreeze","Monkeyflower_pigmentation_v2", "Pattern_formation", "WT","A",50);
  	//private VCellSelection vcellSelection;
	

    /**
     * This main function serves for development purposes.
     * It allows you to run the plugin immediately out of
     * your integrated development environment (IDE).
     *
     * @param args whatever, it's ignored
     * @throws Exception
     */
    public static void main(final String... args) throws Exception {
        // create the ImageJ application context with all available services
        final ImageJ ij = new ImageJ();
        ij.ui().showUI();
    }

	private static JDialog progressDialog = null;
	private static final Dimension dim = new Dimension(200,30);
	private static final JProgressBar jProgressBar = new JProgressBar(0,100) {
		@Override
		public Dimension getPreferredSize() {
			return dim;
		}
		@Override
		public Dimension getSize(Dimension rv) {
			return dim;
		}
	};
    private static void displayProgressBar(boolean bShow,String message,String title,int progress,UIService uiService) {
    	if(progressDialog == null) {
			JFrame applicationFrame = (JFrame)uiService.getDefaultUI().getApplicationFrame();
			progressDialog = new JDialog(applicationFrame,"Checking for VCell Client",false);
			progressDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					super.windowClosing(e);
					progressDialog.dispose();
					progressDialog = null;
				}});
			progressDialog.getContentPane().add(jProgressBar);
			jProgressBar.setStringPainted(true);
			jProgressBar.setString("setting up...");
			progressDialog.pack();
			
    	}
    	
    	if(SwingUtilities.isEventDispatchThread()) {
	    	if(progressDialog ==null) {
				return;
			}
			if(!bShow) {
				progressDialog.dispose();
				progressDialog = null;
				return;
			}
			progressDialog.setVisible(true);
			jProgressBar.setValue(progress);
			progressDialog.setTitle(title);
			jProgressBar.setString(message);
			jProgressBar.invalidate();
			progressDialog.revalidate();
			
    	}else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if(progressDialog ==null) {
						return;
					}
					if(!bShow) {
						progressDialog.dispose();
						progressDialog = null;
						return;
					}
					progressDialog.setVisible(true);
					jProgressBar.setValue(progress);
					progressDialog.setTitle(title);
					jProgressBar.setString(message);
					jProgressBar.invalidate();
					progressDialog.revalidate();
				}
			});
    	}
    }

//    private Hashtable<String,Thread> threadHash = new Hashtable<String,Thread>();
//    private void startJProgressThread0(String lastName,String newName) {
//    	if(lastName != null && threadHash.get(lastName) != null) {
//	    	threadHash.get(lastName).interrupt();
//	    	while(threadHash.get(lastName) != null) {
//	    		try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					break;
//				}
//	    	}
//    	}
//    	if(newName == null) {
//    		return;
//    	}
//    	final Thread progressThread = new Thread(new Runnable(){
//			@Override
//			public void run() {
//				final int[] progress = new int[] {1};
//				while(progressDialog.isVisible()) {
//					if(Thread.currentThread().isInterrupted()) {
//						break;
//					}
//					SwingUtilities.invokeLater(new Runnable() {
//						@Override
//						public void run() {
//							jProgressBar.setValue(progress[0]);
//						}});
//					progress[0]++;
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						break;
//					}
//				}
//				threadHash.remove(Thread.currentThread().getName());
//			}});
//    	threadHash.put(newName, progressThread);
//		progressThread.setName(newName);
//		progressThread.setDaemon(true);//So not block JVM exit
//		progressThread.start();
//    }
    
	@Override
	public void run() {
		try {
			if(vcellSelection != null && vcellSelection.exception != null) {
				if(!vcellSelection.exception.getMessage().equals(MyPreProcessor.CANCELLED)) {
					uiService.showDialog("Model search failed\n"+vcellSelection.exception.getClass().getName()+"\n"+vcellSelection.exception.getMessage(), MessageType.ERROR_MESSAGE);
				}
				return;
			}
			if(vcellSelection == null || vcellSelection.theCacheKey==null) {
				return;
			}
			String var = vcellSelection.varName;
			int[] time = new int[] {vcellSelection.timePointIndex};
			displayProgressBar(true, "loading Image...", "VCell Model Loader", 50,uiService);
			IJDataList tpd = vcellHelper.getTimePointData(vcellSelection.theCacheKey,var,VCellHelper.VARTYPE_POSTPROC.NotPostProcess,time,0);
			displayProgressBar(true, "displaying Image...", "VCell Model Loader", 100,uiService);
			double[] data = tpd.ijData[0].getDoubleData();
			BasicStackDimensions bsd = tpd.ijData[0].stackInfo;
			System.out.println(bsd.xsize+" "+bsd.ysize);
			ArrayImg<DoubleType, DoubleArray> testimg = ArrayImgs.doubles( data, bsd.xsize,bsd.ysize,bsd.zsize);
			uiService.show(testimg);
		} catch (Exception e) {
			displayProgressBar(false, "displaying Image...", "VCell Model Loader", 100,uiService);
			uiService.showDialog("theCacheKey,var,VCellHelper.VARTYPE_POSTPROC.NotPostProcess,time,0\n"+e.getMessage(), "getTimePoint(...) failed", MessageType.ERROR_MESSAGE);
		}finally {
			displayProgressBar(false, "displaying Image...", "VCell Model Loader", 100,uiService);
		}
	}
			}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
