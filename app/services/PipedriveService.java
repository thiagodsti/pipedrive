package services;

import java.util.concurrent.ExecutionException;

import entities.Atividade;
import play.libs.ws.WSResponse;

public interface PipedriveService {

	WSResponse adicionarNovaAtividade(Atividade atividade) throws Exception;

	WSResponse obterDetalhesUmaAtividade(Long codigoAtividade) throws Exception;

	WSResponse editarAtividade(Atividade atividade, Long codigoAtividade) throws Exception;

	WSResponse deletarAtividade(Long codigoAtividade) throws InterruptedException, ExecutionException;

}
