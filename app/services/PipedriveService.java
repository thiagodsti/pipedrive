package services;

import entities.Atividade;
import play.libs.ws.WSResponse;

public interface PipedriveService {

	/**
	 * Adicionar uma nova {@link Atividade}
	 * 
	 * @param atividade
	 *            - dados da {@link Atividade} a ser adicionada.
	 * @return ID do elemento criado no Header do Response.
	 * @throws Exception
	 *             possíveis erros lançados.
	 */
	WSResponse adicionarNovaAtividade(Atividade atividade) throws Exception;

	/**
	 * Obter detalhes de uma {@link Atividade} pelo seu código.
	 * 
	 * @param codigoAtividade
	 *            - código da atividade.
	 * @return Dados da {@link Atividade}.
	 * @throws Exception
	 *             possíveis erros lançados.
	 */
	WSResponse obterDetalhesUmaAtividade(Long codigoAtividade) throws Exception;

	/**
	 * Editar uma {@link Atividade}
	 * 
	 * @param atividade
	 *            - os novos dados da {@link Atividade}.
	 * @param codigoAtividade
	 *            - código da {@link Atividade} a ser editada.
	 * @return ID do elemento criado no Header do Response.
	 * @throws Exception
	 *             possíveis erros lançados.
	 */
	WSResponse editarAtividade(Atividade atividade, Long codigoAtividade) throws Exception;

	/**
	 * Deletar uma {@link Atividade}
	 * 
	 * @param codigoAtividade
	 *            - código da {@link Atividade} a ser deletada.
	 * @return
	 * @throws Exception
	 *             possíveis erros lançados.
	 */
	WSResponse deletarAtividade(Long codigoAtividade) throws Exception;

}
