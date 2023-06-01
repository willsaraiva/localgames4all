package ufc.pds.locagames4all.service;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.pds.locagames4all.dto.JogoDTO;
import ufc.pds.locagames4all.enums.StatusJogo;
import ufc.pds.locagames4all.enums.TipoJogo;
import ufc.pds.locagames4all.model.Jogo;
import ufc.pds.locagames4all.repositories.JogoRepositoryJPA;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class JogoService {

    @Autowired
    private JogoRepositoryJPA jogoRepository;

    private static final String MSG_ENTITY_NOT_FOUND = "Jogo não encontrado.";
    private static final String MSG_JOGOS_NAO_ENCONTRADOS = "jogos não encontrados";

    public Jogo cadastrarJogo(JogoDTO jogoDTO) {
        Jogo jogo = jogoDTO.toModel();
        jogo.setExcluido(Boolean.FALSE);
        jogo.setStatus(StatusJogo.DISPONIVEL);
        return jogoRepository.save(jogo);
    }

    public List<Jogo> buscarTodosJogos(){
        return  jogoRepository.findAll();
    }

    public Jogo buscarJogoPorId(Long id){
        return jogoRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(MSG_ENTITY_NOT_FOUND));
    }

    public List<Jogo> buscarJogosPorTipo(TipoJogo tipo){
        List<Jogo> jogos = jogoRepository.findByTipo(tipo);
        if(jogos.isEmpty()){
            throw new EntityNotFoundException(MSG_JOGOS_NAO_ENCONTRADOS);
        } else
            return jogos;
    }

    public List<Jogo> buscarJogosPorStatus(StatusJogo status){
        List<Jogo> jogos =  jogoRepository.findByStatus(status);
        if(jogos.isEmpty()){
            throw new EntityNotFoundException(MSG_JOGOS_NAO_ENCONTRADOS);
        } else
            return jogos;
    }

    public List<Jogo> buscarJogosPorQtdJogadores(int quantidade){
        List<Jogo> jogos = jogoRepository.
                findJogosByQtdJogadores(quantidade);
        if(jogos.isEmpty()){
            throw new EntityNotFoundException(MSG_JOGOS_NAO_ENCONTRADOS);
        } else
            return jogos;
    }

    public List<Jogo> buscarJogosPorValorDiaria(double valorDiaria){
        List<Jogo> jogos = jogoRepository.
                findJogosByValorDiariaIsLessThanEqual(valorDiaria);
        if(jogos.isEmpty()){
            throw new EntityNotFoundException(MSG_JOGOS_NAO_ENCONTRADOS);
        } else
            return jogos;
    }

    public List<Jogo> buscarJogosPorNome(String texto) {
        List<Jogo> jogos = jogoRepository.findByNomeContainsIgnoreCase(texto);
        if(jogos.isEmpty()){
            throw new EntityNotFoundException(MSG_JOGOS_NAO_ENCONTRADOS);
        } else
            return jogos;
    }

    public Jogo atualizarJogo(JogoDTO jogoDTOAtualizado){
        Jogo jogoPersistido = buscarJogoPorId(jogoDTOAtualizado.getId());
        Jogo jogoAtualizado = jogoDTOAtualizado.toModel();
        jogoAtualizado.setId(jogoPersistido.getId());
        jogoAtualizado.setExcluido(jogoPersistido.getExcluido());
        jogoAtualizado.setStatus(jogoPersistido.getStatus());
        return jogoRepository.save(jogoAtualizado);
    }

    public Jogo excluirJogo(Long id){
        Jogo jogo = jogoRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(MSG_ENTITY_NOT_FOUND));
        if(BooleanUtils.isFalse(jogo.getExcluido())) {
            jogo.setExcluido(true);
            jogo.setStatus(StatusJogo.INDISPONIVEL);
            return jogoRepository.save(jogo);
        } else {
            throw new UnsupportedOperationException("Não foi possível concluir operação, o jogo já estava excluído.");
        }
    }
}