package ufc.pds.locagames4all.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufc.pds.locagames4all.enums.StatusJogo;
import ufc.pds.locagames4all.enums.TipoJogo;
import ufc.pds.locagames4all.model.Jogo;

import java.util.List;

@Repository
public interface JogoRepositoryJPA extends JpaRepository<Jogo, Long> {
    List<Jogo> findByTipo(TipoJogo tipo);
    List<Jogo> findByStatus(StatusJogo status);
    @Query("select j from Jogo j where j.qtdMinJogadores <= ?1 and j.qtdMaxJogadores >= ?1")
    List<Jogo> findJogosByQtdJogadores(int quantidade);
    List<Jogo> findJogosByValorDiariaIsLessThanEqual(Double valorDiaria);
    List<Jogo> findByNomeContainsIgnoreCase(String texto);
}