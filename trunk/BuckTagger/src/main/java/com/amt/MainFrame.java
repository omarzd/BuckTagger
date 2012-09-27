/* 
 *  Copyright 2012 Omar Alzuhaibi and Ahmed Aman
 */

/* 
 *  This file is part of BuckTagger.
 *  
 *  BuckTagger is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *  
 *  BuckTagger is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with BuckTagger.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

 //==================\
 //  Drools' License |
 //==================/ 
 /* 
    Copyright 2001-2005 (C) The Werken Company. All Rights Reserved.
     
    Redistribution and use of this software and associated documentation
    ("Software"), with or without modification, are permitted provided
    that the following conditions are met:
    
     1. Redistributions of source code must retain copyright
        statements and notices.  Redistributions must also contain a
        copy of this document.
     
     2. Redistributions in binary form must reproduce the
        above copyright notice, this list of conditions and the
        following disclaimer in the documentation and/or other
        materials provided with the distribution.
     
     3. The name "drools" must not be used to endorse or promote
        products derived from this Software without prior written
        permission of The Werken Company.  For written permission,
        please contact bob@werken.com.
     
     4. Products derived from this Software may not be called "drools"
        nor may "drools" appear in their names without prior written
        permission of The Werken Company. "drools" is a trademark of 
        The Werken Company.
     
     5. Due credit should be given to The Werken Company.
        (http://werken.com/)
     
    THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
    ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
    NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
    FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
    THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
    INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
    (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
    HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
    STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
    OF THE POSSIBILITY OF SUCH DAMAGE.
    
 */
 
 
package com.amt;
import java.awt.EventQueue;
import java.awt.ComponentOrientation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.EmptyBorder;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.DefaultListModel;
//import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
//import javax.swing.JList;
//import javax.swing.border.LineBorder;
//import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.util.ArrayList;
import java.util.Iterator;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	protected static VocStem stem; // only one stem will be processed at a time.
	protected static JTextField txtPrimaryTag;
	protected static JTextArea txtSecondaryTags;
	protected static JTextField txtStem;
	protected static JTextField txtInputFile;
	protected static JButton submitButton;
	protected static JCheckBox chkBeforeLastHamza;
	protected static JCheckBox chkBeforeLastDblOrVowel;
	protected static JCheckBox chkFirstHamza;
	protected static JCheckBox chkImpYa;
	protected static JCheckBox chkPassive;
	protected static JCheckBox chkTransitive;
	/*
	//this is used for tristate checkboxes
	private static void refreshAllCheckBoxes() {
	
		chkBeforeLastHamza.refresh();
		chkBeforeLastDblOrVowel.refresh();
		chkFirstHamza.refresh();
		chkImpYa.refresh();
		chkPassive.refresh();
		chkTransitive.refresh();
	}
	*/
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
				    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
				    frame.setLocation( (screensize.width - frame.getWidth())/2,
				    		(screensize.height - frame.getHeight())/2 );
				    MainFrame.setDefaultLookAndFeelDecorated(false);
					frame.setVisible(true);

					// After setting the frame, read the rules from the rules table
					Core.readRules();
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("واسم");
		setResizable(false);
		this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 388, 402);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setEnabled(false);
		tabbedPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		tabbedPane.setBounds(10, 11, 362, 352);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("يدوي", null, panel, null);
		panel.setLayout(null);
		
		txtSecondaryTags = new JTextArea();
		txtSecondaryTags.setLineWrap(true);
		txtSecondaryTags.setEditable(false);
		txtSecondaryTags.setDisabledTextColor(Color.BLACK);
		txtSecondaryTags.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		txtSecondaryTags.setColumns(10);
		txtSecondaryTags.setBackground(Color.WHITE);
		//txtSecondaryTags.setBounds(35, 254, 219, 55);
		JScrollPane areaScrollPane = new JScrollPane(txtSecondaryTags);
		areaScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		areaScrollPane.setBounds(35, 254, 219, 55);
		areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panel.add(areaScrollPane );
		
		JLabel label = new JLabel("أوسمة فرعية");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(258, 257, 76, 14);
		panel.add(label);
		
		JLabel label_1 = new JLabel("وسم رئيس");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(259, 234, 76, 14);
		panel.add(label_1);
		
		txtPrimaryTag = new JTextField();
		txtPrimaryTag.setEditable(false);
		txtPrimaryTag.setDisabledTextColor(Color.BLACK);
		txtPrimaryTag.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		txtPrimaryTag.setColumns(10);
		txtPrimaryTag.setBackground(Color.WHITE);
		txtPrimaryTag.setBounds(35, 231, 219, 20);
		panel.add(txtPrimaryTag);
		
		submitButton = new JButton("إدخال");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.submitButton.requestFocusInWindow();
				try {
		        	
		        	if(stem!=null){
		        		// clear output fields
		        		txtPrimaryTag.setText("");
		        		txtSecondaryTags.setText("");
		        		// Get input from user
			    		String in = txtStem.getText(); 
			    		
			    		// first call to rules
			            System.out.println("=======1ST CALL=======");
			    		Core.doMagic(stem);
			    		System.out.println("Primary Tag: "+stem.getPrimaryTag());
			    		
			    		// second call to rules, to get secondary stems now that we know the primary tag.
			            System.out.println("=======2ND CALL=======");
			    		Core.doMagic(stem);
			    		// The user does not have to see a complete Buckwalter stem entry format like in the file "dictStems".
			    		// some post-processing: remove Imperfective Letter "ya/yu"--and the likes--to match Buckwalter's stems.
			    		// remove it from the primary tag's stem
			    		String inppp = Core.removeImp(in);
			    		// remove it from the list of secondary tag stems
			    		ArrayList<TagStem> ins = stem.getSecondaryTagStems();
			    		ArrayList<TagStem> inspp = new ArrayList<TagStem> () ;
			    		Iterator<TagStem> it = ins.iterator();
			    		while(it.hasNext()) {
			    			TagStem ts = it.next();
			    			//txtSecondaryTags.append(new TagStem(ts.getSecondaryTag(), Core.removeImp(ts.getSecondaryStem()),Core.removeImp(ts.getDiacritizedStem())).toString() + "\n");
			    			inspp.add(new TagStem(ts.getSecondaryTag(), Core.removeImp(ts.getSecondaryStem()),Core.removeImp(ts.getDiacritizedStem())));
			    		}
			    		// show user final result
				        System.out.println("=======RESULTS========");
			    		System.out.println("Primary Tag:\n"+stem.getPrimaryTag()+":\t  "+inppp);
			    		System.out.println("Secondary Tags: \n"+inspp);
			    		txtPrimaryTag.setText(stem.getPrimaryTag()+":    "+inppp);
			    		txtSecondaryTags.setText(inspp.toString());
		        	}else{
		        		JOptionPane.showMessageDialog(null, "أدخِل كلمةً رجاء");
		        	}
		        } catch (Throwable t) {
		            t.printStackTrace();
		        }
			}
			
		});
		submitButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		submitButton.setBounds(35, 201, 89, 23);
		panel.add(submitButton);
		
		chkBeforeLastHamza = new JCheckBox("قبل آخره همزة");
		chkBeforeLastHamza.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		chkBeforeLastHamza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox chkbox = (JCheckBox)arg0.getSource();
				if(stem!=null)
					stem.setBeforeLastHamza(chkbox.isSelected());
			}
		});
		chkBeforeLastHamza.setBounds(62, 171, 97, 23);
		panel.add(chkBeforeLastHamza);
		
		chkBeforeLastDblOrVowel = new JCheckBox("قبل آخره مضعف أو حرف علة");
		chkBeforeLastDblOrVowel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		chkBeforeLastDblOrVowel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox dblvow = (JCheckBox)arg0.getSource();
				if(stem!=null)
					stem.setBeforeLastDblOrVowel(dblvow.isSelected());
			}
		});
		chkBeforeLastDblOrVowel.setBounds(161, 171, 155, 23);
		panel.add(chkBeforeLastDblOrVowel);
		
		
		chkFirstHamza = new JCheckBox("فاؤه همزة");
		chkFirstHamza.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		chkFirstHamza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox chk = (JCheckBox)arg0.getSource();
				if(stem!=null)
					stem.setFirstHamza(chk.isSelected());
			}
		});
		chkFirstHamza.setBounds(21, 145, 138, 23);
		panel.add(chkFirstHamza);
		
		chkImpYa = new JCheckBox("حرف المضارعة مفتوح");
		chkImpYa.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		chkImpYa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox chk = (JCheckBox)arg0.getSource();
				if(stem!=null)
					stem.setImpYa(chk.isSelected());
			}
		});
		chkImpYa.setBounds(21, 119, 138, 23);
		panel.add(chkImpYa);
		
		chkPassive = new JCheckBox("مبني للمجهول");
		chkPassive.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		chkPassive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox passive = (JCheckBox)arg0.getSource();
				if(stem!=null){
					stem.setPassive(passive.isSelected());
					chkImpYa.setSelected(false);
					stem.setImpYa(chkImpYa.isSelected());
				}
			}
		});
		chkPassive.setBounds(219, 145, 97, 23);
		panel.add(chkPassive);
		
		
		chkTransitive = new JCheckBox("متعدي");
		chkTransitive.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		chkTransitive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox trans = (JCheckBox)arg0.getSource();
				if(stem!=null)
					stem.setTransitivity(trans.isSelected()?1:0);
			}
		});
		chkTransitive.setBounds(219, 119, 97, 23);
		panel.add(chkTransitive);
		
		JLabel label_2 = new JLabel("تصنيف فرعي");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(251, 98, 65, 14);
		panel.add(label_2);
		
		ListCellRenderer renderer = new DefaultListCellRenderer();
		( (JLabel) renderer ).setHorizontalAlignment( SwingConstants.RIGHT );
		
		JComboBox comboBox = new JComboBox();
		comboBox.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		comboBox.setRenderer(renderer);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"\u0645\u0636\u0627\u0631\u0639"}));		
		comboBox.setBounds(169, 95, 72, 20);
		panel.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setRenderer(renderer);
		comboBox_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"\u0641\u0639\u0644"}));
		comboBox_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		comboBox_1.setBounds(169, 67, 72, 20);
		panel.add(comboBox_1);
		
		JLabel label_3 = new JLabel("نوع الكلمة");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(258, 70, 58, 14);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("ملاحظة: يجب أن تكون الكلمة مجردة من الزوائد ومشكلة.");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label_4.setBounds(35, 42, 281, 14);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("أدخل كلمة");
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		label_5.setBounds(270, 14, 64, 14);
		panel.add(label_5);
		
		txtStem = new JTextField();
		txtStem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		txtStem.setColumns(10);
		txtStem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtStem.transferFocus();
			}
		});
		txtStem.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				final JTextField text = (JTextField)arg0.getSource();
				if(!text.getText().isEmpty()){
					stem = new VocStem(text.getText());
					chkBeforeLastDblOrVowel.setSelected(stem.isBeforeLastDblOrVowel());
					chkBeforeLastHamza.setSelected(stem.isBeforeLastHamza());
					chkPassive.setSelected(stem.isPassive());
					chkImpYa.setSelected(stem.isImpYa());
					chkFirstHamza.setSelected(stem.isFirstHamza());
					chkTransitive.setSelected(stem.getTransitivity()>0);
					//refreshAllCheckBoxes(); // used for tristate checkboxes
				}
			}
		});
		txtStem.setBounds(46, 11, 221, 20);
		panel.add(txtStem);
		
		
		// auto-entry tab		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("تلقائي", null, panel_1, null);
		
/*
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("اختر ملفًا نصيًا :");
		lblNewLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		lblNewLabel.setBounds(254, 11, 93, 14);
		panel_1.add(lblNewLabel);
		
		txtInputFile = new JTextField();
		lblNewLabel.setLabelFor(txtInputFile);
		txtInputFile.setEditable(false);
		txtInputFile.setBounds(120, 8, 142, 20);
		panel_1.add(txtInputFile);
		txtInputFile.setColumns(10);
		
		final JList lstWords = new JList();
		lstWords.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lstWords.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		lstWords.setBorder(new LineBorder(new Color(0, 0, 0)));
		lstWords.setBounds(21, 39, 241, 214);
		panel_1.add(lstWords);
		
		JButton btnBrowse = new JButton("استعراض ...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
				fc.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
				int returnVal = fc.showOpenDialog(contentPane);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtInputFile.setText(fc.getSelectedFile().getAbsolutePath());
					try{
						  FileInputStream fstream = new FileInputStream(fc.getSelectedFile().getAbsolutePath());
						  DataInputStream in = new DataInputStream(fstream);

						  BufferedReader br = new BufferedReader(new InputStreamReader(in));
						  DefaultListModel model = new DefaultListModel();
						  String line;
						  
						  while (( line = br.readLine()) != null)   {
							  	model.addElement(line);
							  
						  }
						  
						  lstWords.setModel(model);
						  
						  //Close the input stream
						  in.close();
					}	catch (Exception e){//Catch exception if any
						  System.err.println("Error: " + e.getMessage());
					}
				}
			}
		});
		btnBrowse.setBounds(21, 7, 89, 23);
		panel_1.add(btnBrowse);
		
		JLabel lblNewLabel_1 = new JLabel("قائمة الكلمات:");
		lblNewLabel_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		lblNewLabel_1.setBounds(264, 40, 83, 14);
		panel_1.add(lblNewLabel_1);
		
		final JButton btnStart = new JButton("بدء");
		final String outFileName = "output.txt";
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					btnStart.setEnabled(false);
					PrintWriter out = new PrintWriter(outFileName);
					int size = lstWords.getModel().getSize(); 
					for (int i=0; i<size; i++) {
					    String word = (String)lstWords.getModel().getElementAt(i);
					    VocStem autostem = new VocStem(word);
					    System.out.println(word);
					    
					    // TODO: here, must find automatically isTransitivite and isPassive and all other attributes 
					    // Two calls to rules
			    		ArrayList<TagStem> result = doMagic(autostem ); // this object does not have to be used at this point.
			    		System.out.println("Primary Tag: "+autostem .getPrimaryTag());
			    		ArrayList<TagStem> result2 = doMagic(autostem );
					    
					    String inppp = removeImp(word);
					    
					    out.println(VocStem.removeDiacritics(inppp)+'\t'+inppp+'\t'+autostem.getPrimaryTag()+'\t'+VocStem.getMeaning(word));
					    ArrayList<TagStem> ins = autostem.getSecondaryTagStems();
					    Iterator<TagStem> it = ins.iterator();
					    while(it.hasNext()) {
					    	TagStem ts = it.next();
					    	//inspp.add(new TagStem(ts.getSecondaryTag(), removeImp(ts.getSecondaryStem())));
					    	out.println(new TagStem(ts.getSecondaryTag(), removeImp(ts.getSecondaryStem()),removeImp(ts.getDiacritizedStem())));
					    }
					}
					out.close();
					JOptionPane.showMessageDialog(null, "انتهت العملية بنجاح ، جد مخرجات البرنامج في الملف "+outFileName);
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				btnStart.setEnabled(true);
                
			
			}
		});
		btnStart.setBounds(21, 260, 89, 23);
		panel_1.add(btnStart);
*/	
	}
	
}
