package com.weverton.financas_api.repository;

import com.weverton.financas_api.model.Movimentacao;
import com.weverton.financas_api.model.TipoMovimentacao;
import com.weverton.financas_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByUsuario(Usuario usuario);
    List<Movimentacao> findByUsuarioAndDataBetween(Usuario usuario, LocalDate dataInicio, LocalDate dataFim);
    List<Movimentacao> findByUsuarioAndCategoriaTipo(Usuario usuario, TipoMovimentacao tipo);
}
