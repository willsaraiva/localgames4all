package ufc.pds.locagames4all.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import ufc.pds.locagames4all.model.Cliente;

import java.util.List;

@Data
public class ClienteDTO extends RepresentationModel<ClienteDTO> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String nome;
    private String cpf;
    private String endereco;
    private String telefone;
    private String email;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean excluido;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<JogoDTO> jogosFavoritos;

    public Cliente toModel() {
        Cliente cliente = new Cliente();
        cliente.setNome(this.getNome());
        cliente.setCpf(this.getCpf());
        cliente.setEndereco(this.getEndereco());
        cliente.setTelefone(this.getTelefone());
        cliente.setEmail(this.getEmail());
        return cliente;
    }
}
