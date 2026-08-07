package com.weverton.financas_api.service;

import com.weverton.financas_api.dto.MovimentacaoRequestDTO;
import com.weverton.financas_api.dto.MovimentacaoResponseDTO;
import com.weverton.financas_api.model.Categoria;
import com.weverton.financas_api.model.Movimentacao;
import com.weverton.financas_api.model.TipoMovimentacao;
import com.weverton.financas_api.model.Usuario;
import com.weverton.financas_api.repository.CategoriaRepository;
import com.weverton.financas_api.repository.MovimentacaoRepository;
import com.weverton.financas_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimentacaoService {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public MovimentacaoResponseDTO criarMovimentacao(MovimentacaoRequestDTO dados){

        Usuario usuarioDB = usuarioRepository.findById(dados.getIdUsuario())
                .orElseThrow(()-> new RuntimeException("Id não existe!"));

        Categoria categoriaDB = categoriaRepository.findById(dados.getIdCategoria())
                .orElseThrow(()-> new RuntimeException("Id não existe!"));

        if (dados.getValor().compareTo(BigDecimal.ZERO) <= 0 ){
            throw new RuntimeException("Valor digitado é invalido! Tente novamente.");
        }

        Movimentacao novaMovimentacao = new Movimentacao();
        novaMovimentacao.setUsuario(usuarioDB);
        novaMovimentacao.setCategoria(categoriaDB);
        novaMovimentacao.setDescricao(dados.getDescricao());
        novaMovimentacao.setValor(dados.getValor());
        novaMovimentacao.setData(dados.getData());

        Movimentacao salva = movimentacaoRepository.save(novaMovimentacao);

        MovimentacaoResponseDTO resposta = new MovimentacaoResponseDTO();
        resposta.setId(salva.getId());
        resposta.setDescricao(salva.getDescricao());
        resposta.setValor(salva.getValor());
        resposta.setData(salva.getData());
        resposta.setNomeCategoria(salva.getCategoria().getNome());

        return resposta;
    }

    public List<MovimentacaoResponseDTO> listarMovimentacao(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Id digitado não existe!"));

        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUsuario(usuario);

        List<MovimentacaoResponseDTO> resposta = new ArrayList<>();

        for (Movimentacao movimentacao : movimentacoes) {
            MovimentacaoResponseDTO dto = new MovimentacaoResponseDTO();
            dto.setId(movimentacao.getId());
            dto.setDescricao(movimentacao.getDescricao());
            dto.setValor(movimentacao.getValor());
            dto.setData(movimentacao.getData());
            dto.setNomeCategoria(movimentacao.getCategoria().getNome());
            resposta.add(dto);
        }

        return resposta;
    }

    public List<MovimentacaoResponseDTO> listarPorPeriodo(Long idUsuario, LocalDate dataInicio, LocalDate dataFim){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado!"));

        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUsuarioAndDataBetween(usuario, dataInicio, dataFim);

        List<MovimentacaoResponseDTO> resposta  = new ArrayList<>();

        for (Movimentacao movimentacao : movimentacoes){
            MovimentacaoResponseDTO dto = new MovimentacaoResponseDTO();
            dto.setId(movimentacao.getId());
            dto.setDescricao(movimentacao.getDescricao());
            dto.setValor(movimentacao.getValor());
            dto.setData(movimentacao.getData());
            dto.setNomeCategoria(movimentacao.getCategoria().getNome());
            resposta.add(dto);
        }
        return resposta;
    }

    public List<MovimentacaoResponseDTO> listarPorTipo(Long idUsuario, TipoMovimentacao tipo){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado!"));

        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUsuarioAndCategoriaTipo(usuario, tipo);

        List<MovimentacaoResponseDTO> resposta = new ArrayList<>();

        for (Movimentacao movimentacao : movimentacoes){
            MovimentacaoResponseDTO dto = new MovimentacaoResponseDTO();
            dto.setId(movimentacao.getId());
            dto.setDescricao(movimentacao.getDescricao());
            dto.setValor(movimentacao.getValor());
            dto.setData(movimentacao.getData());
            dto.setNomeCategoria(movimentacao.getCategoria().getNome());
            resposta.add(dto);
        }
        return resposta;
    }

    public MovimentacaoResponseDTO atualizarMovimentacao(Long idMovimentacao, Long idUsuario, MovimentacaoRequestDTO dados){
        Movimentacao movimentacao = movimentacaoRepository.findById(idMovimentacao)
                .orElseThrow(()-> new RuntimeException("id de Movimentação não encontarado!"));

        if (!movimentacao.getUsuario().getId().equals(idUsuario)){
            throw new  RuntimeException("Essa ação não pode ser comcluida! Id não pertence a esse usuario.");
        }

        Categoria categoriaDB = categoriaRepository.findById(dados.getIdCategoria())
                .orElseThrow(()-> new RuntimeException("Id não existe!"));

        if (dados.getValor().compareTo(BigDecimal.ZERO) <= 0 ){
            throw new RuntimeException("Valor digitado é invalido! Tente novamente.");
        }

        movimentacao.setDescricao(dados.getDescricao());
        movimentacao.setValor(dados.getValor());
        movimentacao.setData(dados.getData());
        movimentacao.setCategoria(categoriaDB);

        Movimentacao salva = movimentacaoRepository.save(movimentacao);

        MovimentacaoResponseDTO resposta = new MovimentacaoResponseDTO();
        resposta.setId(salva.getId());
        resposta.setDescricao(salva.getDescricao());
        resposta.setValor(salva.getValor());
        resposta.setData(salva.getData());
        resposta.setNomeCategoria(salva.getCategoria().getNome());

        return resposta;

    }


    public void deletarMovimentacao(Long idMovimentacao, Long idUsuario){
        Movimentacao movimentacao = movimentacaoRepository.findById(idMovimentacao)
                .orElseThrow(()-> new RuntimeException("id de Movimentação não encontarado!"));

        if (!movimentacao.getUsuario().getId().equals(idUsuario)){
            throw new  RuntimeException("Essa ação não pode ser comcluida! Id não pertence a esse usuario.");
        }

        movimentacaoRepository.deleteById(idMovimentacao);
    }
}
