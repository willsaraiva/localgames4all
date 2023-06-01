package ufc.pds.locagames4all.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import ufc.pds.locagames4all.model.Cliente;
import ufc.pds.locagames4all.model.Jogo;
import ufc.pds.locagames4all.model.Locacao;

import java.time.LocalDate;

@Data
public class LocacaoDTO extends RepresentationModel<ClienteDTO> {
    private long id;
    private String clienteCpf;
    private Long jogoId;
    private LocalDate dataDaLocacao;
    private LocalDate dataPrevistaDevolucao;
    private Double valorDaDiariaNaLocacao;
    private LocalDate dataDaDevolucao = null;
    private Long qtdDiasLocados = null;
    private Double saldo = null;

    public Locacao toModel(Cliente cliente, Jogo jogo) {
        Locacao model = new Locacao();
        model.setCliente(cliente);
        model.setJogo(jogo);
        model.setValorDaDiariaNaLocacao(jogo.getValorDiaria());
        model.setDataDaLocacao(this.dataDaLocacao);
        model.setDataPrevistaDevolucao(this.dataPrevistaDevolucao);
        return model;
    }
}
