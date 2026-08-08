package com.weverton.financas_api.service;

import com.weverton.financas_api.dto.AtualizarPerfilRequestDTO;
import com.weverton.financas_api.dto.LoginRequestDTO;
import com.weverton.financas_api.dto.UsuarioRequestDTO;
import com.weverton.financas_api.dto.UsuarioResponseDTO;
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
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO usuario){

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
        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setNome(usuario.getNome());
        usuarioNovo.setEmail(usuario.getEmail());
        usuarioNovo.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(usuarioNovo);

        UsuarioResponseDTO resposta = new UsuarioResponseDTO();
        resposta.setId(usuarioSalvo.getId());
        resposta.setNome(usuarioSalvo.getNome());
        resposta.setEmail(usuarioSalvo.getEmail());

        return resposta;

    }

    public UsuarioResponseDTO logarUsuario(LoginRequestDTO dados) {

            // PASSO 1: Busca o usuário no banco pelo e-mail. Se não existir, lança exceção.
        Usuario usuarioDB = usuarioRepository.findByEmail(dados.getEmail())
                .orElseThrow(()-> new RuntimeException("E-mail ou senha invalidos!"));

            // PASSO 2: Instancia o encoder e valida se as senhas coincidem
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean senhaValida = encoder.matches(dados.getSenha(), usuarioDB.getSenha());

            // Se a senha NÃO for válida (!senhaValida), lança a exceção
        if (!senhaValida){
             throw new RuntimeException("E-mail ou senha invalidos!");
        }

        UsuarioResponseDTO usuariologin = new UsuarioResponseDTO();
        usuariologin.setId(usuarioDB.getId());
        usuariologin.setEmail(usuarioDB.getEmail());
        usuariologin.setNome(usuarioDB.getNome());

        return usuariologin ;
    }

    public UsuarioResponseDTO buscarPorId(Long id){

        Usuario usuarioEncontrado = usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuário não encontrado!"));

        UsuarioResponseDTO usuarioDto = new UsuarioResponseDTO();
        usuarioDto.setId(usuarioEncontrado.getId());
        usuarioDto.setNome(usuarioEncontrado.getNome());
        usuarioDto.setEmail(usuarioEncontrado.getEmail());

        return usuarioDto;
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, AtualizarPerfilRequestDTO dadosAtualizado){

        Usuario usuarioEncontrado = usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuário não encontrado!"));

        if (!usuarioEncontrado.getEmail().equals(dadosAtualizado.getEmail())
                && usuarioRepository.existsByEmail(dadosAtualizado.getEmail())){
            throw new RuntimeException("Este novo e-mail já está em uso por outro usuário!");
        }

        usuarioEncontrado.setNome(dadosAtualizado.getNome());
        usuarioEncontrado.setEmail(dadosAtualizado.getEmail());

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioEncontrado);


        UsuarioResponseDTO resposta = new UsuarioResponseDTO();
        resposta.setId(usuarioAtualizado.getId());
        resposta.setNome(usuarioAtualizado.getNome());
        resposta.setEmail(usuarioAtualizado.getEmail());

        return resposta;
    }

    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        // 1. Reutiliza a busca por ID
        Usuario usuarioEncontrado = usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuário não encontrado!"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 2. Valida se a senha atual digitada está correta
        if (!encoder.matches(senhaAtual, usuarioEncontrado.getSenha())) {
            throw new RuntimeException("A senha atual está incorreta!");
        }

        // 3. Criptografa a nova senha
        String novaSenhaCriptografada = encoder.encode(novaSenha);
        usuarioEncontrado.setSenha(novaSenhaCriptografada);

        // 4. Salva a atualização no banco
        usuarioRepository.save(usuarioEncontrado);
    }
}
