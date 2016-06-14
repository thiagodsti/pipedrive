package services;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import entities.Atividade;
import entities.Atividade.AtividadeTipoEnum;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

public class PipedriveServiceImpl implements PipedriveService {

	private static final String URL_PIPEDRIVE = "https://api.pipedrive.com/v1";
	private static final String URL_PIPEDRIVE_ATIVIDADES = URL_PIPEDRIVE + "/activities";
	@Inject
	private WSClient ws;

	public WSResponse adicionarNovaAtividade(Atividade atividade) throws Exception {
		if (atividade == null) {
			throw new Exception("Atividade está nula");
		}
		this.validarAtividade(atividade);
		JsonNode jsonNode = Json.toJson(atividade);
		WSRequest request = obterWsRequestAtividadesComToken();
		request.setContentType("application/json");
		CompletionStage<WSResponse> responsePromise = request.post(jsonNode);
		WSResponse wsResponse = responsePromise.toCompletableFuture().get();
		return wsResponse;
	}

	private void validarAtividade(Atividade atividade) throws Exception {
		validarTipoAtividade(atividade.getTipoAtividade());
		validarNegocio(atividade.getIdNegocio());
		validarAssunto(atividade.getAssunto());
	}

	private void validarAssunto(String assunto) throws Exception {
		if (StringUtils.isEmpty(assunto)) {
			throw new Exception("Assunto deve ser preenchido");
		}
	}

	private void validarNegocio(Long idNegocio) throws Exception {
		if (idNegocio == null) {
			throw new Exception("Atividade deve estar atrelada a algum Negócio.");
		}
	}

	private void validarTipoAtividade(AtividadeTipoEnum tipoAtividade) throws Exception {
		if (tipoAtividade == null) {
			throw new Exception("Atividade deve possuir um tipo.");
		}
	}

	@Override
	public WSResponse obterDetalhesUmaAtividade(Long codigoAtividade) throws Exception {
		if (codigoAtividade == null) {
			return null;
		}
		WSRequest wsRequest = obterWsRequestAtividadesComToken();
		return wsRequest.setQueryParameter("id", codigoAtividade.toString()).get().toCompletableFuture().get();
	}

	private WSRequest obterWsRequestAtividadesComToken() {
		return ws.url(URL_PIPEDRIVE_ATIVIDADES).setQueryParameter("api_token",
				"539e9bff4e3cc6b4a0ecf984f8d9f80039dd5667");
	}

}
