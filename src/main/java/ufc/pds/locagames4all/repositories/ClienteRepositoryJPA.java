package ufc.pds.locagames4all.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufc.pds.locagames4all.model.Cliente;

import java.util.Optional;

@Repository
public interface ClienteRepositoryJPA extends JpaRepository<Cliente, Long> {
    public Optional<Cliente> findByCpf(String cpf);
}
