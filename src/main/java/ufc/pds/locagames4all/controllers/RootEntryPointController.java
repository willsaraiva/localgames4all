package ufc.pds.locagames4all.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ufc.pds.locagames4all.model.RootEntryPoint;

@RestController
public class RootEntryPointController {
    @GetMapping
    @Operation(summary = "Rotas iniciais da API.",
            description = "Retorna as principais rotas da aplicação, incluindo links para o projeto no github e " +
                    "documentação via interface gráfica do Swagger.")
    public RootEntryPoint root() {
        var model = new RootEntryPoint();
        String root = linkTo(RootEntryPointController.class).toUri().toString();

        model.add(Link.of(root + "/swagger-ui/index.html").withRel("documentacao_swagger"));
        model.add(linkTo(methodOn(ClienteController.class)
                .buscarClientes()).withRel("clientes"));
        model.add(linkTo(methodOn(JogoController.class)
                .buscarJogos()).withRel("jogos"));
        model.add(linkTo(methodOn(LocacaoController.class)
                .buscarLocacoes()).withRel("locacoes"));
        model.add(Link.of("https://github.com/DiFeitoza/locagames4all").withRel("github_project"));
        return model;
    }
}
