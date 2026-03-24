package com.carlos.produtosapi.produtos_api.repository;

import com.carlos.produtosapi.produtos_api.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}