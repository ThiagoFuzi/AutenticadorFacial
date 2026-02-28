package br.gov.mma.biometric;

import br.gov.mma.biometric.model.BiometricType;
import br.gov.mma.biometric.model.User;
import java.util.Optional;

/**
 * Interface do banco de dados de usuários.
 * Responsável por operações de persistência e busca de usuários.
 * 
 * Valida: Requisitos 1.1, 4.1, 6.1
 */
public interface UserDatabase {
    
    /**
     * Busca um usuário por seu template biométrico.
     * 
     * @param template template biométrico a ser buscado
     * @param type tipo de biometria do template
     * @return Optional contendo o usuário se encontrado, vazio caso contrário
     */
    Optional<User> findUserByBiometric(byte[] template, BiometricType type);
    
    /**
     * Busca um usuário por seu identificador único.
     * 
     * @param userId identificador do usuário
     * @return Optional contendo o usuário se encontrado, vazio caso contrário
     */
    Optional<User> findUserById(String userId);
    
    /**
     * Salva um novo usuário no banco de dados.
     * 
     * @param user usuário a ser salvo
     * @return true se o usuário foi salvo com sucesso, false caso contrário
     */
    boolean saveUser(User user);
    
    /**
     * Atualiza os dados de um usuário existente.
     * 
     * @param user usuário com dados atualizados
     * @return true se o usuário foi atualizado com sucesso, false caso contrário
     */
    boolean updateUser(User user);
}
