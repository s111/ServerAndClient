package com.github.groupa.client;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
 
public class ImageViewer {
	
    public static boolean RIGHT_TO_LEFT = false;
     
    public static void addComponentsToPane(Container pane) {
         
    	
    	
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
         
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }
         
        // Top Panel
        JPanel TopPanel = new JPanel();
        JLabel TopLabel = new JLabel("Top Toolbar");
        pane.add(TopPanel, BorderLayout.PAGE_START);
        pane.add(TopLabel, BorderLayout.PAGE_START);
         
        // Picture Panel
        JPanel PicturePanel = new JPanel();
        PicturePanel.setPreferredSize(new Dimension(640, 640));
        PicturePanel.setBackground(Color.GREEN);
        
        pane.add(PicturePanel, BorderLayout.CENTER);
         
        // Left Panel
        JPanel LeftPanel = new JPanel();
        JLabel LeftLabel = new JLabel("Left Toolbar");
        LeftPanel.setPreferredSize(new Dimension(100, 0));
        pane.add(LeftPanel, BorderLayout.LINE_START);
        pane.add(LeftLabel, BorderLayout.LINE_START);
         
        // Bottom Panel
        JPanel BottomPanel = new JPanel();
        JLabel BottomLabel = new JLabel("Bottom Toolbar");
        pane.add(BottomPanel, BorderLayout.PAGE_END);
        pane.add(BottomLabel, BorderLayout.PAGE_END);
        
        // Right Panel
        JPanel RightPanel = new JPanel();
        JLabel RightLabel = new JLabel("Right Toolbar");
        RightPanel.setPreferredSize(new Dimension(100, 0));
        pane.add(RightPanel, BorderLayout.LINE_END);
        pane.add(RightLabel, BorderLayout.LINE_END);
    }
     

    private static void createAndShowGUI() {
         
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {

         createAndShowGUI();
            
     }
}
