package ufc.pds.locagames4all.service;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.pds.locagames4all.dto.ClienteDTO;
import ufc.pds.locagames4all.model.Cliente;
import ufc.pds.locagames4all.model.Jogo;
import ufc.pds.locagames4all.repositories.ClienteRepositoryJPA;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepositoryJPA clienteRepository;

    @Autowired
    private JogoService jogoService;

    private static final String MSG_ENTITY_NOT_FOUND = "Cliente não encontrado.";
    private static final String MSG_CLIENTE_JA_CADASTRADO = "Cliente já encontrado.";

    public Cliente cadastrarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = clienteDTO.toModel();
        cliente.setExcluido(Boolean.FALSE);
        cliente.setJogosFavoritos(new ArrayList<>());
        Optional<Cliente> clienteJaCadastrado = clienteRepository.findByCpf(cliente.getCpf());
        if (clienteJaCadastrado.isPresent()) {
            if (BooleanUtils.isTrue(clienteJaCadastrado.get().getExcluido())) {
                cliente.setId(clienteJaCadastrado.get().getId());
                return clienteRepository.save(cliente);
            } else {
                throw new UnsupportedOperationException(MSG_CLIENTE_JA_CADASTRADO);
            }
        } else {
            return clienteRepository.save(cliente);
        }
    }

    public List<Cliente> buscarTodosCLientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).orElseThrow(() -> new EntityNotFoundException(MSG_ENTITY_NOT_FOUND));
    }

    public Cliente atualizaCliente(ClienteDTO clienteDTOAtualizado) {
        Cliente clienteAtualizado = clienteDTOAtualizado.toModel();
        Cliente clientePersistido = buscarClientePorCpf(clienteDTOAtualizado.getCpf());
        clienteAtualizado.setId(clientePersistido.getId());
        clienteAtualizado.setExcluido(clientePersistido.getExcluido());
        return clienteRepository.save(clienteAtualizado);
    }

    public Cliente desativaCliente(String cpf) {
        Cliente clientePersistido = clienteRepository.findByCpf(cpf).orElseThrow(() -> new EntityNotFoundException(MSG_ENTITY_NOT_FOUND));
        if (BooleanUtils.isFalse(clientePersistido.getExcluido())) {
            clientePersistido.setExcluido(true);
            clientePersistido.setNome(null);
            clientePersistido.setEmail(null);
            clientePersistido.setTelefone(null);
            clientePersistido.setEndereco(null);
            return clienteRepository.save(clientePersistido);
        } else {
            throw new UnsupportedOperationException("O cadastro deste cliente já estava inativo.");
        }
    }

    public Cliente favoritarJogo(String cpf, Long jogoId) {
        Cliente cliente = buscarClientePorCpf(cpf);
        Jogo jogo = jogoService.buscarJogoPorId(jogoId);
        List<Jogo> favoritos = cliente.getJogosFavoritos();
        if (favoritos.contains(jogo)) {
            throw new UnsupportedOperationException("Este jogo já está favoritado.");
        }
        favoritos.add(jogo);
        return clienteRepository.save(cliente);
    }

    public Cliente desfavoritarJogo(String cpf, Long jogoId) {
        Cliente cliente = buscarClientePorCpf(cpf);
        Jogo jogo = jogoService.buscarJogoPorId(jogoId);
        List<Jogo> favoritos = cliente.getJogosFavoritos();
        if (!favoritos.contains(jogo)) {
            throw new UnsupportedOperationException("Este jogo não é um favorito.");
        }
        favoritos.remove(jogo);
        return clienteRepository.save(cliente);
    }

    public List<Jogo> listarJogosFavoritos(String cpf) {
        return buscarClientePorCpf(cpf).getJogosFavoritos();
    }

}