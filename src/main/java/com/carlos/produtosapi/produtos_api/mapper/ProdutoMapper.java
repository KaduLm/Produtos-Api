package com.carlos.produtosapi.produtos_api.mapper;

import com.carlos.produtosapi.produtos_api.dto.ProdutoDTO;
import com.carlos.produtosapi.produtos_api.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    Produto toEntity(ProdutoDTO dto);

    ProdutoDTO toDto(Produto entity);

    List<ProdutoDTO> toDtoList(List<Produto> entityList);
}
