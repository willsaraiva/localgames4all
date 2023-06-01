package ufc.pds.locagames4all.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.pds.locagames4all.dto.LocacaoDTO;
import ufc.pds.locagames4all.enums.StatusJogo;
import ufc.pds.locagames4all.model.Cliente;
import ufc.pds.locagames4all.model.Jogo;
import ufc.pds.locagames4all.model.Locacao;
import ufc.pds.locagames4all.repositories.LocacaoRepositoryJPA;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class LocacaoService {

    private static final String MSG_LOCACAO_NAO_ENCONTRADA = "Locação não encontrada.";
    private static final String MSG_LOCACOES_NAO_ENCONTRADOS = "Locações não encontradas";
    private static final String MSG_LOCACAO_JA_CONCLUIDA = "A locação já estava concluída";
    private static final String MSG_JOGO_NAO_DISPONIVEL = "Este jogo não está com situação disponível.";
    private static final String MSG_CLIENTE_INATIVO = "Este cliente está com cadastro inativo.";

    @Autowired
    private LocacaoRepositoryJPA locacaoRepositoryJPA;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JogoService jogoService;

    public Locacao cadastrarLocacao(LocacaoDTO locacaoDTO) {
        Cliente cliente = clienteService.buscarClientePorCpf(locacaoDTO.getClienteCpf());
        if (cliente.getExcluido().equals(true)) {
            throw new UnsupportedOperationException(MSG_CLIENTE_INATIVO);
        }
        Jogo jogo = jogoService.buscarJogoPorId(locacaoDTO.getJogoId());
        if (!jogo.getStatus().equals(StatusJogo.DISPONIVEL)) {
            throw new UnsupportedOperationException(MSG_JOGO_NAO_DISPONIVEL);
        }
        jogo.setStatus(StatusJogo.ALUGADO);
        return locacaoRepositoryJPA.save(locacaoDTO.toModel(cliente, jogo));
    }

    public List<Locacao> buscarTodasLocacoes() {
        return locacaoRepositoryJPA.findAll();
    }

    public Locacao buscarLocacoesPorId(Long id) {
        return locacaoRepositoryJPA.findById(id).orElseThrow(() -> new EntityNotFoundException(MSG_LOCACAO_NAO_ENCONTRADA));
    }

    public List<Locacao> buscarHistoricoDeLocacoesPorCPF(String cpf) {
        Cliente cliente = clienteService.buscarClientePorCpf(cpf);
        List<Locacao> locacoes = locacaoRepositoryJPA.findByClienteId(cliente.getId());
        if (locacoes.isEmpty()) {
            throw new EntityNotFoundException(MSG_LOCACOES_NAO_ENCONTRADOS);
        } else {
            return locacoes;
        }
    }

    public List<Locacao> buscarLocacoesAtivasPorCPF(String cpf) {
        Cliente cliente = clienteService.buscarClientePorCpf(cpf);
        List<Locacao> locacoes = locacaoRepositoryJPA.findByClienteIdAndDataDaDevolucaoIsNull(cliente.getId());
        if (locacoes.isEmpty()) {
            throw new EntityNotFoundException(MSG_LOCACOES_NAO_ENCONTRADOS);
        } else {
            return locacoes;
        }
    }

    public List<Locacao> buscarHistoricoDeLocacoesPorJogoId(Long jogoId) {
        List<Locacao> locacoes = locacaoRepositoryJPA.findByJogoId(jogoId);
        if (locacoes.isEmpty()) {
            throw new EntityNotFoundException(MSG_LOCACOES_NAO_ENCONTRADOS);
        } else {
            return locacoes;
        }
    }

    public List<Locacao> buscarLocacoesPorCPFeJogoId(String cpf, Long jogoId) {
        Cliente cliente = clienteService.buscarClientePorCpf(cpf);
        List<Locacao> locacoes = locacaoRepositoryJPA.findByClienteIdAndJogoId(cliente.getId(), jogoId);
        if (locacoes.isEmpty()) {
            throw new EntityNotFoundException(MSG_LOCACOES_NAO_ENCONTRADOS);
        } else {
            return locacoes;
        }
    }

    public Locacao consultarLocacaoParaDevolucao(Long id) {
        Locacao locacao = buscarLocacoesPorId(id);
        if (locacao.getDataDaDevolucao() != null) {
            throw new UnsupportedOperationException(MSG_LOCACAO_JA_CONCLUIDA);
        }
        Long diferencaEmDias = ChronoUnit.DAYS.between(LocalDate.now(), locacao.getDataPrevistaDevolucao());
        locacao.setQtdDiasLocados(diferencaEmDias);
        locacao.setSaldo(diferencaEmDias * locacao.getValorDaDiariaNaLocacao());
        return locacao;
    }

    public Locacao devolverLocacao(Long id) {
        Locacao locacao = consultarLocacaoParaDevolucao(id);
        locacao.setDataDaDevolucao(LocalDate.now());
        locacao.getJogo().setStatus(StatusJogo.DISPONIVEL);
        return locacaoRepositoryJPA.save(locacao);
    }

    public void excluirLocacao(Long id) {
        Locacao locacao = buscarLocacoesPorId(id);
        if(locacao.getDataDaDevolucao() != null) {
            locacao.getJogo().setStatus(StatusJogo.DISPONIVEL);
        }
        locacaoRepositoryJPA.delete(locacao);
    }
}