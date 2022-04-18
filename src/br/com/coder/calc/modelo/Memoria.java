package br.com.coder.calc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Memoria {
	private enum TipoComando {
		ZERAR, NUMERO, DIV, MULT, SOMA, SUB, IGUAL, VIRGULA, MUDARSINAL, PORCENT, PORSOMA, PORSUB
	}

	private static final Memoria istancia = new Memoria();
	private final List<MemoriaObservador> observadores = new ArrayList<>();
	private TipoComando ultimaOperacao = null;
	private boolean substituir = false;
	private String textoAtual = "";
	private String textoBuffer = "";

	private Memoria() {
	}
	//insacia
	public static Memoria getIstancia() {
		return istancia;
	}

	public String getTextoAtual() {
		return textoAtual.isEmpty() ? "0" : textoAtual;
	}

	public void adicionarObservador(MemoriaObservador observador) {
		observadores.add(observador);
	}

	public void processarComando(String texto) {
		TipoComando Comando = detectarTipoComando(texto);
		if (Comando == null) {
			return;
		} else if (Comando == TipoComando.ZERAR) {
			textoAtual = "";
			textoBuffer = "";
			substituir = false;
			ultimaOperacao = null;
		} else if (Comando == TipoComando.NUMERO) {
			textoAtual = substituir ? texto : textoAtual + texto;
			substituir = false;
		} else if (Comando == TipoComando.VIRGULA) {
			if (textoAtual == "") {
				textoAtual += "0";
			}
			textoAtual = substituir ? "0" + texto : textoAtual + texto;
			substituir = false;
		} else if (Comando == TipoComando.MUDARSINAL && textoAtual.contains("-")) {
			textoAtual = textoAtual.substring(1);
		} else if (Comando == TipoComando.MUDARSINAL && !textoAtual.contains("-")) {
			if (textoAtual != "") {
				textoAtual = "-" + textoAtual;
			}
		} else if (Comando == TipoComando.PORCENT) {
			if (ultimaOperacao == TipoComando.SOMA) {
				ultimaOperacao = TipoComando.PORSOMA;
				Comando = TipoComando.PORSOMA;
			} else if (ultimaOperacao == TipoComando.SUB) {
				ultimaOperacao = TipoComando.PORSUB;
				Comando = TipoComando.PORSUB;
			}
		} else {
			substituir = true;
			textoAtual = obterResultadoOperacao();
			textoBuffer = textoAtual;
			ultimaOperacao = Comando;
		}
		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
	}

	private String obterResultadoOperacao() {
		if (ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) {
			return textoAtual;
		}
		double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));
		double resultado = 0;

		if (ultimaOperacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		} else if (ultimaOperacao == TipoComando.SUB) {
			resultado = numeroBuffer - numeroAtual;
		} else if (ultimaOperacao == TipoComando.MULT) {
			resultado = numeroBuffer * numeroAtual;
		} else if (ultimaOperacao == TipoComando.DIV) {
			resultado = numeroBuffer / numeroAtual;
		} else if (ultimaOperacao == TipoComando.PORSOMA) {
			resultado = numeroBuffer * (1 + numeroAtual / 100);
		} else if (ultimaOperacao == TipoComando.PORSUB) {
			resultado = numeroBuffer * (1 - numeroAtual / 100);
		}
		String resultadoOperacao = String.format("%.2f", resultado);
		boolean inteiro = resultadoOperacao.endsWith(",00");
		return inteiro ? resultadoOperacao.replace(",00", "") : resultadoOperacao;
	}

	private TipoComando detectarTipoComando(String texto) {
		if (textoAtual.isEmpty() && texto.equals("0")) {
			return null;
		}
		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			if (texto.equals("AC")) {
				return TipoComando.ZERAR;
			} else if (texto.equals("/")) {
				return TipoComando.DIV;
			} else if (texto.equals("*")) {
				return TipoComando.MULT;
			} else if (texto.equals("+")) {
				return TipoComando.SOMA;
			} else if (texto.equals("-")) {
				return TipoComando.SUB;
			} else if (texto.equals("=")) {
				return TipoComando.IGUAL;
			} else if (texto.equals("±")) {
				return TipoComando.MUDARSINAL;
			} else if (texto.equals("%")) {
				return TipoComando.PORCENT;
			} else if (texto.equals(",") && (!textoAtual.contains(",") || textoAtual == textoBuffer)) {
				return TipoComando.VIRGULA;
			}
		}
		return null;
	}
}