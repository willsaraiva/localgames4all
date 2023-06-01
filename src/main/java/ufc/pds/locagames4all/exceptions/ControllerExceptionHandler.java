package ufc.pds.locagames4all.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ExcecaoPadrao> operacaoNaoSuportada(UnsupportedOperationException e){
        String categoriaErro = "Regra de Negocio não atendida.";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        return ResponseEntity.status(status).body(geraExcecao(e,categoriaErro,status.value()));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExcecaoPadrao> recursoNaoEncontrado(EntityNotFoundException e){
        String categoriaErro = "Recurso não encontrado.";
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(geraExcecao(e, categoriaErro, status.value()));
    }
    private ExcecaoPadrao geraExcecao(Exception e,String erro ,Integer codigo){
        return new ExcecaoPadrao(erro, e.getMessage(), codigo);
    }
}
