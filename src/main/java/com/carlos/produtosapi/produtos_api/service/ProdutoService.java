package com.carlos.produtosapi.produtos_api.service;

import com.carlos.produtosapi.produtos_api.dto.ProdutoDTO;
import com.carlos.produtosapi.produtos_api.mapper.ProdutoMapper;
import com.carlos.produtosapi.produtos_api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public void cadastrarProduto(ProdutoDTO produtoDTO){
        produtoRepository.save(produtoMapper.toEntity(produtoDTO));
    }
}
