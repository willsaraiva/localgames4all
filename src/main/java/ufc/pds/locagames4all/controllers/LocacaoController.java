package ufc.pds.locagames4all.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufc.pds.locagames4all.dto.LocacaoDTO;
import ufc.pds.locagames4all.model.Locacao;
import ufc.pds.locagames4all.service.LocacaoService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/locacoes")
@Tag(name = "Locacao")
public class LocacaoController {
    @Autowired
    private LocacaoService locacaoService;

    ModelMapper modelMapper = new ModelMapper();

    @PostMapping
    @Operation(summary = "Cadastrar locação.",
            description = "Permite cadastrar uma locação a partir de um CPF e um id válidos.<br>" +
                    "Retorna retorna a locação cadastrada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Locação cadastrada."),
            @ApiResponse(responseCode = "400", description = "Requisição mal formatada."),
            @ApiResponse(responseCode = "422", description = "Regra de Negócio não atendida."),
    })
    public ResponseEntity<LocacaoDTO> cadastrarLocacao(
            @Parameter(description = "Modelo de locação para cadastro.")
            @RequestBody LocacaoDTO locacaoDTO) {
        Locacao locacaoCriada = locacaoService.cadastrarLocacao(locacaoDTO);
        URI locacaoURI = linkTo(methodOn(LocacaoController.class).buscarLocacaoPorId(locacaoCriada.getId())).toUri();
        return ResponseEntity.created(locacaoURI).body(toDTO(locacaoCriada));
    }

    @GetMapping
    @Operation(summary = "Buscar locações.",
            description = "Permite a busca de todos as locações.<br>" +
                    "Retorna a lista com todos as locações cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
    })
    public ResponseEntity<List<LocacaoDTO>> buscarLocacoes() {
        return ResponseEntity.ok().body(toCollectionDTO(locacaoService.buscarTodasLocacoes()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar locação pelo id.",
            description = "Permite a busca de todos as locações.<br>" +
                    "Retorna a lista com todos as locações cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
    })
    public ResponseEntity<LocacaoDTO> buscarLocacaoPorId(
            @Parameter(description = "Id da locação a ser buscada.")
            @PathVariable Long id) {
        return ResponseEntity.ok().body(toDTO(locacaoService.buscarLocacoesPorId(id)));
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar histórico de locações pelo CPF do cliente, com filtro.",
            description = "Permite a busca de todos as locações em aberto, ou encerradas de um cliente pelo seu CPF." +
                    "<br>Retorna uma lista com o histórico de locações do jogo de id informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
    })
    public ResponseEntity<List<LocacaoDTO>> buscarHistoricoDeLocacoesPorCPF(
            @Parameter(description = "CPF do cliente para a busca de locações.")
            @PathVariable String cpf,
            @Parameter(description = "Selecione 'locacaoativa=true' para filtrar locações em aberto ou " +
                    "'locacaoativa=false' para todas as locações.")
            @RequestParam(name = "locacaoativa", required = false, defaultValue = "false") boolean locacaoativa) {
        if (locacaoativa) {
            return ResponseEntity.ok().body(toCollectionDTO(locacaoService.buscarLocacoesAtivasPorCPF(cpf)));
        } else {
            return ResponseEntity.ok().body(toCollectionDTO(locacaoService.buscarHistoricoDeLocacoesPorCPF(cpf)));
        }
    }

    @GetMapping("/jogoid/{jogoId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
    })
    @Operation(summary = "Buscar histórico de locações pelo id do jogo.",
            description = "Permite a busca de todos as locações em aberto, ou encerradas de um jogo pelo seu id.<br>" +
                    "Retorna uma lista com o histórico de locações do jogo de id informado.")
    public ResponseEntity<List<LocacaoDTO>> buscarHistoricoDeLocacoesPorJogoId(
            @Parameter(description = "Id do jogo a ser buscado.")
            @PathVariable Long jogoId) {
        List<Locacao> locacoes = locacaoService.buscarHistoricoDeLocacoesPorJogoId(jogoId);
        return ResponseEntity.ok().body(toCollectionDTO(locacoes));
    }

    @GetMapping("/{cpf}/{jogoId}")
    @Operation(summary = "Buscar histórico de locações pelo par CPF do cliente e id do jogo.",
            description = "Permite a busca de todas as locações em aberto, ou encerradas pelo par de dados CPF do " +
                    "cliente e id do jogo.<br>" +
                    "Retorna uma lista com o histórico de locações para o par CPF do cliente e id do jogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
    })
    public ResponseEntity<List<LocacaoDTO>> buscarLocacoesPorCPFeJogoId(
            @Parameter(description = "CPF do cliente locador.")
            @PathVariable String cpf,
            @Parameter(description = "Id do jogo locado.")
            @PathVariable Long jogoId) {
        return ResponseEntity.ok().body(toCollectionDTO(locacaoService.buscarLocacoesPorCPFeJogoId(cpf, jogoId)));
    }

    @GetMapping("/saldo/{id}")
    @Operation(summary = "Consultar dados de locação para devolução.",
            description = "Permite a consulta de dados de uma locação em aberto, pelo id da locação, para " +
                    "verificar dados como o saldo e a duração da locação, em dias, até o momento.<br>" +
                    "Retorna a locação em aberto com cálculo de saldo, devido ou a receber, e a duração da locação " +
                    "em dias até o momento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
    })
    public ResponseEntity<LocacaoDTO> consultarLocacaoParaDevolucao(
            @Parameter(description = "Id da locação para consulta de devolução.")
            @PathVariable Long id) {
        return ResponseEntity.ok().body(toDTO(locacaoService.consultarLocacaoParaDevolucao(id)));
    }

    @PatchMapping("/devolucao/{id}")
    @Operation(summary = "Devolver locação.",
            description = "Realiza a devolução de uma locação.<br>" +
                    "Retorna a locação com o a data de devolução e o cálculo de saldo, devido ou a receber, e a " +
                    "duração da locação em dias até o momento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem conteúdo."),
            @ApiResponse(responseCode = "400", description = "Requisição mal formatada."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "422", description = "Regra de Negócio não atendida.")
    })
    public ResponseEntity<LocacaoDTO> devolverLocacao(
            @Parameter(description = "Id da locação que será devolvida.")
            @PathVariable Long id) {
        return ResponseEntity.ok().body(toDTO(locacaoService.devolverLocacao(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar locação.",
            description = "Realiza a exclusão de uma locação do sistema.<br>" +
                    "Retorna a mensagem de confirmação. Caso seja uma locação em aberto, " +
                    "o jogo locado se torna disponível para locação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Locação excluída."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "422", description = "Regra de Negócio não atendida.")
    })
    public ResponseEntity<String> excluirLocacao(
            @Parameter(description = "Id da locação que será excluída.")
            @PathVariable Long id) {
        locacaoService.excluirLocacao(id);
        return ResponseEntity.noContent().build();
    }

    public LocacaoDTO toDTO(Locacao locacao) {
        LocacaoDTO locacaoDTO = modelMapper.map(locacao, LocacaoDTO.class);
        locacaoDTO.add(linkTo(methodOn(LocacaoController.class)
                .buscarLocacaoPorId(locacaoDTO.getId())).withSelfRel());
        locacaoDTO.add(linkTo(methodOn(LocacaoController.class)
                .buscarLocacoes()).withRel("buscar-locacoes"));
        locacaoDTO.add(linkTo(methodOn(ClienteController.class)
                .buscarClientePorCpf(locacaoDTO.getClienteCpf())).withRel("cliente-locador"));
        locacaoDTO.add(linkTo(methodOn(JogoController.class)
                .buscarJogoPorId(locacaoDTO.getJogoId())).withRel("jogo-locado"));
        return locacaoDTO;
    }

    public List<LocacaoDTO> toCollectionDTO(List<Locacao> locacoes) {
        return locacoes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
