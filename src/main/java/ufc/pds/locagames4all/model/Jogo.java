package ufc.pds.locagames4all.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufc.pds.locagames4all.enums.StatusJogo;
import ufc.pds.locagames4all.enums.TipoJogo;

import javax.persistence.*;

@Entity
@Table(name = "JOGO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Jogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private StatusJogo status;

    public Jogo(String nome, TipoJogo tipo, String descricao, Integer qtdMinJogadores, Integer qtdMaxJogadores, Double valorDiaria) {
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.qtdMaxJogadores = qtdMaxJogadores;
        this.qtdMinJogadores = qtdMinJogadores;
        this.valorDiaria = valorDiaria;
        this.excluido = false;
        this.status = StatusJogo.DISPONIVEL;
    }
}
