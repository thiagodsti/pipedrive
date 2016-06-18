package services;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import entities.Atividade;
import entities.Atividade.AtividadeTipoEnum;
import infra.ControleValidacao;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

public class PipedriveServiceImpl implements PipedriveService {

	private static final String URL_PIPEDRIVE = "https://api.pipedrive.com/v1";
	private static final String URL_PIPEDRIVE_ATIVIDADES = URL_PIPEDRIVE + "/activities";
	private static final String URL_PIPEDRIVE_ATIVIDADES_COM_CODIGO = URL_PIPEDRIVE_ATIVIDADES + "/%s";

	@Inject
	private WSClient ws;

	public WSResponse adicionarNovaAtividade(Atividade atividade) throws Exception {
		this.validarAtividade(atividade);
		JsonNode jsonNode = Json.toJson(atividade);
		WSRequest request = obterWsRequestAtividadesComToken(URL_PIPEDRIVE_ATIVIDADES);
		request.setContentType("application/json");
		CompletionStage<WSResponse> responsePromise = request.post(jsonNode);
		WSResponse wsResponse = responsePromise.toCompletableFuture().get();
		return wsResponse;
	}

	@Override
	public WSResponse obterDetalhesUmaAtividade(Long codigoAtividade) throws Exception {
		this.validarCodigoAtividade(codigoAtividade);
		String url = String.format(URL_PIPEDRIVE_ATIVIDADES_COM_CODIGO, codigoAtividade);
		WSRequest request = obterWsRequestAtividadesComToken(url);
		return request.get().toCompletableFuture().get();

	}

	@Override
	public WSResponse editarAtividade(Atividade atividade, Long codigoAtividade) throws Exception {
		this.validarAtividade(atividade);
		JsonNode jsonNode = Json.toJson(atividade);
		String url = String.format(URL_PIPEDRIVE_ATIVIDADES_COM_CODIGO, codigoAtividade);
		WSRequest request = obterWsRequestAtividadesComToken(url);
		request.setContentType("application/json");
		CompletionStage<WSResponse> responsePromise = request.put(jsonNode);
		WSResponse wsResponse = responsePromise.toCompletableFuture().get();
		return wsResponse;
	}

	@Override
	public WSResponse deletarAtividade(Long codigoAtividade) throws Exception {
		this.validarCodigoAtividade(codigoAtividade);
		String url = String.format(URL_PIPEDRIVE_ATIVIDADES_COM_CODIGO, codigoAtividade);
		WSRequest request = obterWsRequestAtividadesComToken(url);
		return request.delete().toCompletableFuture().get();
	}

	private WSRequest obterWsRequestAtividadesComToken(String URL) {
		return ws.url(URL).setQueryParameter("api_token", "539e9bff4e3cc6b4a0ecf984f8d9f80039dd5667");
	}

	private void validarAtividade(Atividade atividade) throws Exception {
		ControleValidacao controleValidacao = new ControleValidacao();
		validarAtividadeNaoNula(controleValidacao, atividade);
		validarTipoAtividade(controleValidacao, atividade.getTipoAtividade());
		validarNegocio(controleValidacao, atividade.getIdNegocio());
		validarAssunto(controleValidacao, atividade.getAssunto());
		controleValidacao.validar();
	}

	private void validarCodigoAtividade(Long codigoAtividade) {
		ControleValidacao controleValidacao = new ControleValidacao();
		controleValidacao.checkNotNull(codigoAtividade, "Deve ser informado um código de atividade");
		controleValidacao.validar();
	}

	private void validarAtividadeNaoNula(ControleValidacao controleValidacao, Atividade atividade) {
		controleValidacao.checkNotNull(atividade, "Atividade não foi informada");
	}

	private void validarAssunto(ControleValidacao controleValidacao, String assunto) {
		if (StringUtils.isEmpty(assunto)) {
			controleValidacao.addValidacao("Assunto deve ser preenchido");
		}
	}

	private void validarNegocio(ControleValidacao controleValidacao, Long idNegocio) {
		controleValidacao.checkNotNull(idNegocio, "Atividade deve estar atrelada a algum Negócio.");
	}

	private void validarTipoAtividade(ControleValidacao controleValidacao, AtividadeTipoEnum tipoAtividade) {
		controleValidacao.checkNotNull(tipoAtividade, "Atividade deve possuir um tipo.");
	}

}
