package ufc.pds.locagames4all.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufc.pds.locagames4all.model.Locacao;

import java.util.List;


@Repository
public interface LocacaoRepositoryJPA extends JpaRepository<Locacao, Long> {
    List<Locacao> findByClienteId(long id);
    List<Locacao> findByJogoId(long id);
    List<Locacao> findByClienteIdAndDataDaDevolucaoIsNull(long id);
    List<Locacao> findByClienteIdAndJogoId(Long clienteId, Long jogoId);
}