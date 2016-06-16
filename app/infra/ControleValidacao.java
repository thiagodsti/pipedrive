package infra;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

//Melhorar, fazer um jeito de injetar e sempre retornar novo e fazer um item de exceção,
//nao utilizar o Exception.
public class ControleValidacao {

	private List<Exception> validacoes;

	public ControleValidacao() {
		validacoes = new ArrayList<>();
	}

	public void checkNotNull(Object obj, String msg) {
		if (obj == null) {
			addValidacao(msg);
		}
	}

	public void addValidacao(Exception ex) {
		if (ex != null) {
			validacoes.add(ex);
		}
	}

	public void addValidacao(String msg) {
		if (StringUtils.isNotBlank(msg)) {
			validacoes.add(new Exception(msg));
		}
	}

	public void validar() {
		if (validacoes.isEmpty()) {
			return;
		}
		StringBuilder stringBuilder = new StringBuilder();
		validacoes.forEach(validacao -> {
			// Mudar para o commons de alguma lib o "\n" não lembro qual usar.
			stringBuilder.append(validacao.getMessage());
		});

		throw new RuntimeException(stringBuilder.toString());
	}

}
