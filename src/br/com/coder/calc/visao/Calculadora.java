package br.com.coder.calc.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Calculadora extends JFrame{
	public Calculadora() {		
		setTitle("Calculator");
		ImageIcon image = new ImageIcon("images.png");
		setIconImage(image.getImage());
		organizarLayout();
		setSize(240, 322);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	private void organizarLayout() {
		setLayout(new BorderLayout());
		
		Display display = new Display(); 
		display.setPreferredSize(new Dimension(233,60));
		add(display, BorderLayout.NORTH);
		
		Teclado teclado = new Teclado(); 
		add(teclado, BorderLayout.CENTER);
	}
	public static void main(String[] args) {
		new Calculadora();
	}
}