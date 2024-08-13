package com.example.produtoapi.controllers;

import com.example.produtoapi.models.Produto;
import com.example.produtoapi.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;
    private final Validator validador;

    @Autowired
    public ProdutoController(ProdutoService produtoService, Validator validador){
        this.produtoService = produtoService;
        this.validador = validador;
    }
    //Selecionar
    @GetMapping("/selecionar")

    public List<Produto> listarProdutos() {
        return produtoService.listarProduto();
    }


    @GetMapping("/buscarPorNome")
    @Operation(summary = "Lista todos os produtos",
            description = "Retorna uma lista de todos os produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Requisition inválida",
            content = @Content()),

    })
    public ResponseEntity<List<Produto>> findByNomeLikeIgnoreCaseAndPrecoLessThan(String nome, double preco){
        List<Produto> busca = produtoService.findByNomeLikeIgnoreCaseAndPrecoLessThan(nome, preco);
        return ResponseEntity.ok(busca);
    }

    //Inserir
    @PostMapping("/inserir")
    @Schema(description = "Insere um novo produto")
    public ResponseEntity<String> inserirProduto(@Valid @RequestBody Produto produto, BindingResult result) {
//        return ResponseEntity.ok("Produto inserido com sucesso");

        produtoService.salvarProduto(produto);
        if(produto.getNome() == null){
            return ResponseEntity.status(400).body("Insira um nome de produto válido..");
        }
        if(produto.getPreco() <= 0){
            return ResponseEntity.status(400).body("Insira um preço válido.");
        }
        if(produto.getQtdEstoque() <= 0){
            return ResponseEntity.status(400).body("Quantidade em estoque não pode ser menor que 1.");
        }
        else{
            return ResponseEntity.ok("Produto inserido com sucesso");
        }

//        if(result.hasErrors()) {
//            if(result.)
//        }
    }
    //Excluir
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirProduto(@PathVariable Long id){
        Produto produtoExistente = produtoService.excluirProduto(id);
        if(produtoExistente != null){
            return ResponseEntity.ok("Produto removido com sucesso");
        }
        return ResponseEntity.status(404).body("Produto não encontrado");

        // Mayla solution
//        if(produtoRepository.existsById(id)){
//            produtoRepository.deleteById(id);
//            return ResponseEntity.ok("Produto removido com sucesso");
//        }
//        return ResponseEntity.status(404).body("Produto não encontrado");

        // JV solution
//        Optional<Produto> produtoExistente = produtoRepository.findById(id);
//        if(produtoExistente.isPresent()){
//            produtoRepository.delete(produtoExistente.get());
//            return ResponseEntity.ok("Produto removido com sucesso");
//        }
//        return ResponseEntity.status(404).body("Produto não encontrado");

        // My solution (chatgpt)
//        return produtoRepository.findById(id)
//                .map(produto -> {
//                    produtoRepository.deleteById(id);
//                    return ResponseEntity.ok("Produto removido com sucesso");
//                })
//                .orElse(ResponseEntity.notFound().build());

    }
    //Alterar
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado){
        Produto alterar = produtoService.atualizarProduto(id, produtoAtualizado);
        System.out.println(produtoAtualizado.getNome());
        System.out.println(alterar.getNome());
        System.out.println(produtoAtualizado);
        System.out.println(alterar);
        if(alterar != null){
            return ResponseEntity.ok("Deu bom");
        }
        return ResponseEntity.status(404).body("Produto não encontrado");

//        Optional<Produto> produtoExistente = produtoRepository.findById(id);
//        if(produtoExistente.isPresent()) {
//            Produto produto = produtoExistente.get();
//            produto.setNome(produtoAtualizado.getNome());
//            produto.setDescricao(produtoAtualizado.getDescricao());
//            produto.setPreco(produtoAtualizado.getPreco());
//            produto.setQtdEstoque(produtoAtualizado.getQtdEstoque());
//            produtoRepository.save(produto);
//            return ResponseEntity.ok("Produto atualizado com sucesso");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }
    //Alterar Parcial
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<?> atualizarProdutoParcial(@Valid @PathVariable Long id, @RequestBody Map<String, Object> updates){

        try {
            Produto produto = produtoService.buscarProdutoPorId(id);
            if (updates.containsKey("nome")) {
                produto.setNome((String) updates.get("nome"));
            }
            if (updates.containsKey("descricao")) {
                produto.setDescricao((String) updates.get("descricao"));
            }
            if (updates.containsKey("preco")) {
                produto.setPreco((double) updates.get("preco"));
            }
            if (updates.containsKey("quantidadeEstoque")) {
                produto.setQtdEstoque((Integer) updates.get("nome"));
            }

            // Validar
            DataBinder binder = new DataBinder(produto);
            binder.setValidator(validador);
            binder.validate();
            BindingResult resultado = binder.getBindingResult();
            if (resultado.hasErrors()) {
                Map erros = validarProduto(resultado);
                return ResponseEntity.badRequest().body(erros);
            }
            produtoService.salvarProduto(produto);
            return ResponseEntity.ok("Produto atualizado com sucesso");
        } catch (ClassCastException ce) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preço deve ter o formato 0.00");
        } catch (RuntimeException re) {
            re.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }

    }

    // Exceção
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(MethodArgumentNotValidException manve) {
        return manve.getBindingResult().getFieldError().getDefaultMessage();
    }

    public Map<String, String> validarProduto(BindingResult resultado) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        return erros;
    }
}
