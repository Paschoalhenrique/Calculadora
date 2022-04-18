package br.com.coder.calc.visao;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.coder.calc.modelo.Memoria;
import br.com.coder.calc.modelo.MemoriaObservador;

@SuppressWarnings("serial")
public class Display extends JPanel implements MemoriaObservador{
	
	private final JLabel label;
	public Display() {
		Memoria.getIstancia().adicionarObservador(this);
		setBackground(new Color(46,49,50));
		label = new JLabel(Memoria.getIstancia().getTextoAtual());
		label.setForeground(Color.WHITE);
		label.setFont(new Font("courier",Font.PLAIN,30));
		
		setLayout(new FlowLayout(FlowLayout.RIGHT,8,12));
		add(label);
	}
	@Override
	public void valorAlterado(String novoValor) {
		label.setText(novoValor);
	}
}