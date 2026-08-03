package com.weverton.financas_api.repository;

import com.weverton.financas_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByemail(String email);

    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
}
