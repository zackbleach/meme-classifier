package com.zackbleach.memetable.util;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageViewer extends JFrame {

	public ImageViewer(Image image) {
		showImage(image);
	}

	public void showImage(Image image) {
		JFrame frame = new JFrame("Glorious Meme");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
					
		ImageIcon imageIcon = new ImageIcon(image);
		frame.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());

		JLabel label1 = new JLabel(" ", imageIcon, JLabel.CENTER);
		frame.getContentPane().add(label1);
		
		frame.validate();
		frame.setVisible(true);
	}
	
}
