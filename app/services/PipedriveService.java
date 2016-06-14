package services;

import entities.Atividade;
import play.libs.ws.WSResponse;

public interface PipedriveService {

	WSResponse adicionarNovaAtividade(Atividade atividade) throws Exception;

	WSResponse obterDetalhesUmaAtividade(Long codigoAtividade) throws Exception;

}
