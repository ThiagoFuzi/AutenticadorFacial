package br.gov.mma.biometric;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.AuthenticationResult;
import br.gov.mma.biometric.model.BiometricData;
import br.gov.mma.biometric.model.User;

/**
 * Interface principal do autenticador biométrico.
 * Responsável por autenticar usuários, cadastrar novos usuários e revogar acessos.
 * 
 * Valida: Requisitos 1.1, 4.1, 6.1, 7.1
 */
public interface BiometricAuthenticator {
    
    /**
     * Autentica um usuário usando dados biométricos e verifica permissão para o nível solicitado.
     * 
     * @param biometricData dados biométricos capturados do usuário
     * @param requestedLevel nível de acesso solicitado
     * @return resultado da autenticação contendo sucesso, usuário, nível concedido e token de sessão
     */
    AuthenticationResult authenticate(BiometricData biometricData, AccessLevel requestedLevel);
    
    /**
     * Cadastra um novo usuário no sistema com seus dados biométricos.
     * 
     * @param user dados do usuário a ser cadastrado
     * @param biometricData dados biométricos do usuário
     * @return true se o cadastro foi bem-sucedido, false caso contrário
     */
    boolean enrollUser(User user, BiometricData biometricData);
    
    /**
     * Revoga o acesso de um usuário, marcando-o como inativo.
     * 
     * @param userId identificador do usuário
     * @return true se a revogação foi bem-sucedida, false caso contrário
     */
    boolean revokeAccess(String userId);
}
