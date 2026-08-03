package com.weverton.financas_api.service;

import com.weverton.financas_api.model.Usuario;
import com.weverton.financas_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    // 1. Validação de regra de negócio
    public Usuario registrarUsuario(Usuario usuario){

        if (usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new RuntimeException("Este e-mail já existe!");
        }

        /* 2.
         * NOTA DE ESTUDO:
         * A lógica de criptografia (BCryptPasswordEncoder) foi desenvolvida com auxílio do Assistente.
         * Ela é responsável por pegar a senha em texto limpo do usuário e transformá-la
         * em uma Hash segura antes de gravar no banco de dados.
         * Tema para estudo futuro: Spring Security & Hashing de Senhas.
         */
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // 3. Persistência
        return usuarioRepository.save(usuario);
    }

    public Usuario logarUsuario(String emailDigitado, String senhaDigitada) {

            // PASSO 1: Busca o usuário no banco pelo e-mail. Se não existir, lança exceção.
        Usuario usuarioDB = usuarioRepository.findByEmail(emailDigitado)
                .orElseThrow(()-> new RuntimeException("E-mail ou senha invalidos!"));

            // PASSO 2: Instancia o encoder e valida se as senhas coincidem
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean senhaValida = encoder.matches(senhaDigitada, usuarioDB.getSenha());

            // Se a senha NÃO for válida (!senhaValida), lança a exceção
        if (!senhaValida){
             throw new RuntimeException("E-mail ou senha invalidos!");
        }
        return usuarioDB ;
    }

    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuário não encontrado!"));
    }

    public Usuario atualizarUsuario(Long id, Usuario dadosAtualizado){
        Usuario usuarioExistente = buscarPorId(id);

        if (!usuarioExistente.getEmail().equals(dadosAtualizado.getEmail())
                && usuarioRepository.existsByEmail(dadosAtualizado.getEmail())){
            throw new RuntimeException("Este novo e-mail já está em uso por outro usuário!");
        }

        usuarioExistente.setNome(dadosAtualizado.getNome());
        usuarioExistente.setSenha(dadosAtualizado.getSenha());

        return usuarioRepository.save(usuarioExistente);
    }

    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        // 1. Reutiliza a busca por ID
        Usuario usuarioBD = buscarPorId(id);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 2. Valida se a senha atual digitada está correta
        if (!encoder.matches(senhaAtual, usuarioBD.getSenha())) {
            throw new RuntimeException("A senha atual está incorreta!");
        }

        // 3. Criptografa a nova senha
        String novaSenhaCriptografada = encoder.encode(novaSenha);
        usuarioBD.setSenha(novaSenhaCriptografada);

        // 4. Salva a atualização no banco
        usuarioRepository.save(usuarioBD);
    }
}
