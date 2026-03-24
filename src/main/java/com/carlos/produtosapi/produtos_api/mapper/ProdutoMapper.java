package com.carlos.produtosapi.produtos_api.mapper;

import com.carlos.produtosapi.produtos_api.dto.ProdutoResponseDTO;
import com.carlos.produtosapi.produtos_api.dto.ProdutoRequestDTO;
import com.carlos.produtosapi.produtos_api.entity.Produto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    Produto toEntity(ProdutoResponseDTO dto);

    Produto fromRequestoEntity(ProdutoRequestDTO dto);


    ProdutoResponseDTO toDto(Produto entity);

}
