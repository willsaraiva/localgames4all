package ufc.pds.locagames4all.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import ufc.pds.locagames4all.enums.StatusJogo;
import ufc.pds.locagames4all.enums.TipoJogo;
import ufc.pds.locagames4all.model.Jogo;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class JogoDTO extends RepresentationModel<JogoDTO> {
    private Long id;
    private String nome;
    @Enumerated(EnumType.STRING)
    private TipoJogo tipo;
    private String descricao;
    private Integer qtdMinJogadores;
    private Integer qtdMaxJogadores;
    private Double valorDiaria;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean excluido;
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusJogo status;

    public Jogo toModel() {
        Jogo jogo = new Jogo();
        jogo.setNome(this.getNome());
        jogo.setTipo(this.getTipo());
        jogo.setDescricao(this.getDescricao());
        jogo.setQtdMinJogadores(this.getQtdMinJogadores());
        jogo.setQtdMaxJogadores(this.getQtdMaxJogadores());
        jogo.setValorDiaria(this.getValorDiaria());
        return jogo;
    }
}