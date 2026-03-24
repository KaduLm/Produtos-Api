package com.carlos.produtosapi.produtos_api.service;

import com.carlos.produtosapi.produtos_api.dto.ProdutoResponseDTO;
import com.carlos.produtosapi.produtos_api.dto.ProdutoRequestDTO;
import com.carlos.produtosapi.produtos_api.entity.Produto;
import com.carlos.produtosapi.produtos_api.exceptions.ProdutoNotFoundException;
import com.carlos.produtosapi.produtos_api.mapper.ProdutoMapper;
import com.carlos.produtosapi.produtos_api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public void cadastrarProduto(ProdutoRequestDTO produtoDTO){
        produtoRepository.save(produtoMapper.fromRequestoEntity(produtoDTO));
    }


    public Page<ProdutoResponseDTO> listarTodosOsProdutos(Pageable pageable){
        Page<Produto> paginaProdutos = produtoRepository.findAll(pageable);

        if (paginaProdutos.isEmpty()) {
            log.error("Produtos não encontrados");
            throw new ProdutoNotFoundException("Produtos não encontrados");
        }

        return paginaProdutos.map(produtoMapper::toDto);
    }

    public ProdutoResponseDTO listarProdutosPorId(Long id){
        return produtoMapper.toDto(buscarProdutoPorId(id));
    }

    public void atualizarProduto(Long id, ProdutoRequestDTO produtoDTO){
        Produto produtoExistente = buscarProdutoPorId(id);

        produtoMapper.updateEntityFromDto(produtoDTO, produtoExistente);
        produtoRepository.save(produtoExistente);
    }

    public void deletarProduto(Long id){
        produtoRepository.delete( buscarProdutoPorId(id));
    }


    private Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> {
            log.error("Produto com o Id: {} não encontrado", id);
            return new ProdutoNotFoundException("Produto com o Id: " + id + " não encontrado");
        });
    }

}
