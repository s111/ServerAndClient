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
        TopPanel.setPreferredSize(new Dimension(0, 100));
        TopPanel.setBackground(Color.RED);
        pane.add(TopPanel, BorderLayout.PAGE_START);
         
        // Picture Panel
        JPanel PicturePanel = new JPanel();
        PicturePanel.setPreferredSize(new Dimension(640, 640));
        PicturePanel.setBackground(Color.BLACK);
        pane.add(PicturePanel, BorderLayout.CENTER);
         
        // Left Panel
        JPanel LeftPanel = new JPanel();
        LeftPanel.setPreferredSize(new Dimension(100, 0));
        LeftPanel.setBackground(Color.WHITE);
        pane.add(LeftPanel, BorderLayout.LINE_START);
         
        // Bottom Panel
        JPanel BottomPanel = new JPanel();
        BottomPanel.setPreferredSize(new Dimension(0, 100));
        BottomPanel.setBackground(Color.RED);
        pane.add(BottomPanel, BorderLayout.PAGE_END);
        
        // Right Panel
        JPanel RightPanel = new JPanel();
        RightPanel.setPreferredSize(new Dimension(100, 0));
        RightPanel.setBackground(Color.WHITE);
        pane.add(RightPanel, BorderLayout.LINE_END);
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
