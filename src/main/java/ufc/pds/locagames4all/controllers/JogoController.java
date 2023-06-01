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
import ufc.pds.locagames4all.dto.JogoDTO;
import ufc.pds.locagames4all.enums.StatusJogo;
import ufc.pds.locagames4all.enums.TipoJogo;
import ufc.pds.locagames4all.model.Jogo;
import ufc.pds.locagames4all.service.JogoService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jogos")
@Tag(name = "Jogo")
public class JogoController {
    @Autowired
    private JogoService jogoService;

    ModelMapper modelMapper = new ModelMapper();

    @PostMapping
    @Operation(summary = "Cadastrar jogo.",
            description = "Permite o cadastro de jogos. <br>Retorna a location do jogo cadastrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jogo cadastrado."),
            @ApiResponse(responseCode = "400", description = "Requisição mal formatada."),
            @ApiResponse(responseCode = "422", description = "Regra de Negócio não atendida.")
    })
    public ResponseEntity<?> cadastrarJogo(@RequestBody JogoDTO jogoDTO) {
        Jogo jogoCriado = jogoService.cadastrarJogo(jogoDTO);
        URI jogoURI = linkTo(methodOn(JogoController.class).buscarJogoPorId(jogoCriado.getId())).toUri();
        return ResponseEntity.created(jogoURI).body(toDTO(jogoCriado));
    }

    @GetMapping
    @Operation(summary = "Buscar jogos.",
            description = "Permite a busca de todos os jogos. <br>Retorna a lista com todos os jogos do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    public ResponseEntity<List<JogoDTO>> buscarJogos() {
        return ResponseEntity.ok().body(toCollectionDTO(jogoService.buscarTodosJogos()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jogo pelo id.",
            description = "Permite a busca de um jogo por seu id. <br>" +
                    "Retorna o jogo caso o id informado corresponda a um jogo cadastrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    public ResponseEntity<JogoDTO> buscarJogoPorId(
            @Parameter(description = "Id do jogo que será buscado.")
            @PathVariable Long id) {
        return ResponseEntity.ok().body(toDTO(jogoService.buscarJogoPorId(id)));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar jogo pelo tipo.",
            description = "Permite a busca de jogos pelo tipo. " +
                    "<br>Retorna todos os jogos com o tipo informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    public ResponseEntity<List<JogoDTO>> buscarJogosPorTipo(
            @Parameter(description = "Tipo do jogo que será buscado.")
            @PathVariable TipoJogo tipo) {
        return ResponseEntity.ok().body(toCollectionDTO(jogoService.buscarJogosPorTipo(tipo)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar jogos por pelo status.",
            description = "Permite a busca de jogos pelo status. <br>" +
                    "Retorna todos os jogos com o status informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    public ResponseEntity<List<JogoDTO>> buscarJogosPorStatus(
            @Parameter(description = "Status do jogo que será buscado.")
            @PathVariable StatusJogo status) {
        return ResponseEntity.ok().body(toCollectionDTO(jogoService.buscarJogosPorStatus(status)));
    }

    @GetMapping("/qtdjogadores/{quantidade}")
    @Operation(summary = "Buscar jogos pela quantidade de jogadores.",
            description = "Permite a busca de jogos pela quantidade de jogadores que pretendem jogar. <br>" +
                    "Retorna todos os jogos que permitam a quantidade informada entre a sua quantidade " +
                    "mínima e máxima de jogadores permitidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    public ResponseEntity<List<JogoDTO>> buscarJogosPorQtdJogadores(
            @Parameter(description = "Quantidade de jogadores da busca.")
            @PathVariable Integer quantidade) {
        return ResponseEntity.ok().body(toCollectionDTO(jogoService.buscarJogosPorQtdJogadores(quantidade)));
    }

    @GetMapping("/valordiaria")
    @Operation(summary = "Buscar jogos pelo valor de diária de locação.",
            description = "Permite a busca de jogos pelo valor da diária. <br>" +
                    "Retorna todos os jogos com valor igual ou menor ao informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    public ResponseEntity<List<JogoDTO>> buscarJogosPorValorDiaria(
            @Parameter(description = "Valor de busca.")
            @RequestParam("valor") Double valor) {
        return ResponseEntity.ok().body(toCollectionDTO(jogoService.buscarJogosPorValorDiaria(valor)));
    }

    @GetMapping("/nome")
    @Operation(summary = "Buscar jogos pelo nome.",
            description = "Permite a busca de jogos por parte do nome." +
                    "<br>Retorna todos os jogos que contenham o texto informado como parte do nome do jogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    public ResponseEntity<List<JogoDTO>> buscarJogosPorNome(
            @Parameter(description = "Texto para busca no atributo 'nome' dos jogos.")
            @RequestParam("texto") String texto) {
        return ResponseEntity.ok().body(toCollectionDTO(jogoService.buscarJogosPorNome(texto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar jogos pelo id.",
            description = "Permite a atualização dos dados de um jogo no sistema pelo seu id. <br>" +
                    "Retorna o objeto atualizado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo atualizado."),
            @ApiResponse(responseCode = "400", description = "Requisição mal formatada."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "422", description = "Regra de Negócio não atendida.")
    })
    public ResponseEntity<?> atualizarJogo(
            @Parameter(description = "Id do jogo que será atualizado.")
            @PathVariable Long id, @RequestBody JogoDTO jogoAtualizado) {
        if (id.equals(jogoAtualizado.getId())) {
            return ResponseEntity.ok().body(toDTO(jogoService.atualizarJogo(jogoAtualizado)));
        } else {
            return ResponseEntity.badRequest().body("O id informado no PATH deve ser o mesmo do jogo informado.");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar jogos pelo id.",
            description = "Permite que um jogo seja desativado do sistema pelo seu id. <br>" +
                    "Retorna os dados do jogo que foi desativado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo desativado."),
            @ApiResponse(responseCode = "400", description = "Requisição mal formatada."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "422", description = "Regra de Negócio não atendida.")
    })
    public ResponseEntity<JogoDTO> excluirJogo(
            @Parameter(description = "Id do jogo que será desativado.")
            @PathVariable Long id) {
        return ResponseEntity.ok().body(toDTO(jogoService.excluirJogo(id)));
    }

    public JogoDTO toDTO(Jogo jogo) {
        JogoDTO jogoDTO = modelMapper.map(jogo, JogoDTO.class);
        jogoDTO.add(linkTo(methodOn(JogoController.class)
                .buscarJogoPorId(jogo.getId())).withSelfRel());
        jogoDTO.add(linkTo(methodOn(JogoController.class)
                .buscarJogos()).withRel("buscar-jogos"));
        return jogoDTO;
    }

    public List<JogoDTO> toCollectionDTO(List<Jogo> jogos) {
        return jogos.stream().map(this::toDTO).collect(Collectors.toList());
    }

}