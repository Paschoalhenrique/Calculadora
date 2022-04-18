package br.com.coder.calc.modelo;

@FunctionalInterface
public interface MemoriaObservador {
	public void valorAlterado(String novoValor);

}