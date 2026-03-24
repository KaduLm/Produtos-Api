package com.carlos.produtosapi.produtos_api.mapper;

import com.carlos.produtosapi.produtos_api.dto.ProdutoDTO;
import com.carlos.produtosapi.produtos_api.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    Produto toEntity(ProdutoDTO dto);

    ProdutoDTO toDto(Produto entity);
}
