package com.carlos.produtosapi.produtos_api.mapper;

import com.carlos.produtosapi.produtos_api.dto.ProdutoResponseDTO;
import com.carlos.produtosapi.produtos_api.dto.ProdutoRequestDTO;
import com.carlos.produtosapi.produtos_api.entity.Produto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    Produto toEntity(ProdutoResponseDTO dto);

    Produto fromRequestoEntity(ProdutoRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto produto);


    ProdutoResponseDTO toDto(Produto entity);

}
