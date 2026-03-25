package com.carlos.produtosapi.produtos_api.service;

import com.carlos.produtosapi.produtos_api.dto.request.ProdutoRequestDTO;
import com.carlos.produtosapi.produtos_api.dto.response.ProdutoResponseDTO;
import com.carlos.produtosapi.produtos_api.entity.Produto;
import com.carlos.produtosapi.produtos_api.exceptions.ProdutoNotFoundException;
import com.carlos.produtosapi.produtos_api.mapper.ProdutoMapper;
import com.carlos.produtosapi.produtos_api.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoRequestDTO produtoRequestDTO;
    private ProdutoResponseDTO produtoResponseDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Notebook");
        produto.setPreco(3500.00);
        produto.setDescricao("Notebook i7 16GB RAM");
        produto.setCategoria("Eletrônicos");

        produtoRequestDTO = new ProdutoRequestDTO(
                "Notebook",
                3500.00,
                "Notebook i7 16GB RAM",
                "Eletrônicos"
        );

        produtoResponseDTO = new ProdutoResponseDTO(
                1L,
                "Notebook",
                3500.00,
                "Notebook i7 16GB RAM",
                "Eletrônicos"
        );

        pageable = PageRequest.of(0, 10);
    }


    @Nested
    @DisplayName("cadastrarProduto()")
    class CadastrarProduto {

        @Test
        @DisplayName("Deve cadastrar produto com sucesso")
        void deveCadastrarProdutoComSucesso() {
            when(produtoMapper.fromRequestoEntity(produtoRequestDTO)).thenReturn(produto);

            produtoService.cadastrarProduto(produtoRequestDTO);

            verify(produtoMapper, times(1)).fromRequestoEntity(produtoRequestDTO);
            verify(produtoRepository, times(1)).save(produto);
        }

        @Test
        @DisplayName("Deve chamar o mapper antes de salvar")
        void deveChamarMapperAntesDeSalvar() {
            when(produtoMapper.fromRequestoEntity(produtoRequestDTO)).thenReturn(produto);

            produtoService.cadastrarProduto(produtoRequestDTO);

            InOrder inOrder = inOrder(produtoMapper, produtoRepository);
            inOrder.verify(produtoMapper).fromRequestoEntity(produtoRequestDTO);
            inOrder.verify(produtoRepository).save(produto);
        }
    }


    @Nested
    @DisplayName("listarTodosOsProdutos()")
    class ListarTodosOsProdutos {

        @Test
        @DisplayName("Deve retornar página de produtos com sucesso")
        void deveRetornarPaginaDeProdutosComSucesso() {
            Page<Produto> paginaProdutos = new PageImpl<>(List.of(produto));
            when(produtoRepository.findAll(pageable)).thenReturn(paginaProdutos);
            when(produtoMapper.toDto(produto)).thenReturn(produtoResponseDTO);

            Page<ProdutoResponseDTO> resultado = produtoService.listarTodosOsProdutos(pageable);

            assertThat(resultado).isNotEmpty();
            assertThat(resultado.getTotalElements()).isEqualTo(1);
            assertThat(resultado.getContent().get(0).nome()).isEqualTo("Notebook");
            verify(produtoRepository, times(1)).findAll(pageable);
            verify(produtoMapper, times(1)).toDto(produto);
        }

        @Test
        @DisplayName("Deve retornar página vazia quando não houver produtos")
        void deveRetornarPaginaVaziaQuandoNaoHouverProdutos() {
            Page<Produto> paginaVazia = new PageImpl<>(List.of());
            when(produtoRepository.findAll(pageable)).thenReturn(paginaVazia);

            Page<ProdutoResponseDTO> resultado = produtoService.listarTodosOsProdutos(pageable);

            assertThat(resultado).isEmpty();
            assertThat(resultado.getTotalElements()).isZero();
            verify(produtoRepository, times(1)).findAll(pageable);
            verifyNoInteractions(produtoMapper);
        }
    }


    @Nested
    @DisplayName("listarProdutosPorId()")
    class ListarProdutosPorId {

        @Test
        @DisplayName("Deve retornar produto por ID com sucesso")
        void deveRetornarProdutoPorIdComSucesso() {
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(produtoMapper.toDto(produto)).thenReturn(produtoResponseDTO);

            ProdutoResponseDTO resultado = produtoService.listarProdutosPorId(1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.id()).isEqualTo(1L);
            assertThat(resultado.nome()).isEqualTo("Notebook");
            verify(produtoRepository, times(1)).findById(1L);
            verify(produtoMapper, times(1)).toDto(produto);
        }

        @Test
        @DisplayName("Deve lançar ProdutoNotFoundException quando ID não existir")
        void deveLancarExcecaoQuandoIdNaoExistir() {
            when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> produtoService.listarProdutosPorId(99L))
                    .isInstanceOf(ProdutoNotFoundException.class)
                    .hasMessage("Produto com o Id: 99 não encontrado");

            verify(produtoRepository, times(1)).findById(99L);
            verifyNoInteractions(produtoMapper);
        }
    }


    @Nested
    @DisplayName("atualizarProduto()")
    class AtualizarProduto {

        @Test
        @DisplayName("Deve atualizar produto com sucesso")
        void deveAtualizarProdutoComSucesso() {
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            produtoService.atualizarProduto(1L, produtoRequestDTO);

            verify(produtoRepository, times(1)).findById(1L);
            verify(produtoMapper, times(1)).updateEntityFromDto(produtoRequestDTO, produto);
            verify(produtoRepository, times(1)).save(produto);
        }

        @Test
        @DisplayName("Deve chamar mapper antes de salvar ao atualizar")
        void deveChamarMapperAntesDeSalvarAoAtualizar() {
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            produtoService.atualizarProduto(1L, produtoRequestDTO);

            InOrder inOrder = inOrder(produtoMapper, produtoRepository);
            inOrder.verify(produtoMapper).updateEntityFromDto(produtoRequestDTO, produto);
            inOrder.verify(produtoRepository).save(produto);
        }

        @Test
        @DisplayName("Deve lançar ProdutoNotFoundException ao atualizar ID inexistente")
        void deveLancarExcecaoAoAtualizarIdInexistente() {
            when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> produtoService.atualizarProduto(99L, produtoRequestDTO))
                    .isInstanceOf(ProdutoNotFoundException.class)
                    .hasMessage("Produto com o Id: 99 não encontrado");

            verify(produtoRepository, times(1)).findById(99L);
            verifyNoInteractions(produtoMapper);
            verify(produtoRepository, never()).save(any());
        }
    }


    @Nested
    @DisplayName("deletarProduto()")
    class DeletarProduto {

        @Test
        @DisplayName("Deve deletar produto com sucesso")
        void deveDeletarProdutoComSucesso() {
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            produtoService.deletarProduto(1L);

            verify(produtoRepository, times(1)).findById(1L);
            verify(produtoRepository, times(1)).delete(produto);
        }

        @Test
        @DisplayName("Deve lançar ProdutoNotFoundException ao deletar ID inexistente")
        void deveLancarExcecaoAoDeletarIdInexistente() {
            when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> produtoService.deletarProduto(99L))
                    .isInstanceOf(ProdutoNotFoundException.class)
                    .hasMessage("Produto com o Id: 99 não encontrado");

            verify(produtoRepository, times(1)).findById(99L);
            verify(produtoRepository, never()).delete(any());
        }
    }
}