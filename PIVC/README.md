# Sistema de Autenticação Biométrica

Sistema de identificação e autenticação biométrica para controle de acesso ao banco de dados estratégico do Ministério do Meio Ambiente brasileiro.

## 🎯 Funcionalidades

- ✅ Autenticação biométrica facial com threshold de 0.88
- ✅ Três níveis hierárquicos de acesso (Público, Restrito, Confidencial)
- ✅ Criptografia AES-256-GCM para templates biométricos
- ✅ Auditoria completa de todas as operações
- ✅ Interface gráfica moderna e intuitiva
- ✅ Cadastro de novos usuários com captura biométrica
- ✅ Controle de acesso baseado em hierarquia

## 📋 Requisitos

- Java 11 ou superior
- Windows (scripts .bat fornecidos)

## 🚀 Como Executar

### 1. Compilar o projeto

```bash
compile.bat
```

### 2. Executar a aplicação

```bash
run.bat
```

## 👥 Usuários de Exemplo

O sistema vem com 3 usuários pré-cadastrados:

| ID | Nome | Cargo | Nível de Acesso |
|----|------|-------|-----------------|
| USER-001 | João Silva | Funcionário Público | PÚBLICO (1) |
| DIR-001 | Maria Santos | Diretora de Divisão | RESTRITO (2) |
| MIN-001 | Carlos Oliveira | Ministro do Meio Ambiente | CONFIDENCIAL (3) |

## 🎨 Interface

A aplicação possui 3 abas principais:

### 🔐 Autenticação
- Captura biométrica facial simulada
- Seleção de nível de acesso desejado
- Exibição de resultado detalhado

### 👤 Cadastro
- Formulário para novo usuário
- Captura de biometria facial
- Validação de qualidade (mínimo 0.8)

### 📊 Informações
- Documentação do sistema
- Descrição dos níveis de acesso
- Informações técnicas

## 🔒 Níveis de Acesso

### PÚBLICO (Nível 1)
Informações gerais acessíveis a todos os funcionários

### RESTRITO (Nível 2)
Informações restritas aos diretores de divisões

### CONFIDENCIAL (Nível 3)
Informações confidenciais acessíveis apenas ao Ministro

## 🛠️ Tecnologias

- **Linguagem**: Java 11
- **Interface**: Swing
- **Criptografia**: AES-256-GCM
- **Biometria**: Reconhecimento Facial (simulado)
- **Armazenamento**: In-Memory (ConcurrentHashMap)

## 📁 Estrutura do Projeto

```
biometric-authentication-system/
├── src/main/java/br/gov/mma/biometric/
│   ├── model/                    # Modelos de dados
│   │   ├── AccessLevel.java
│   │   ├── BiometricType.java
│   │   ├── BiometricData.java
│   │   ├── User.java
│   │   └── AuthenticationResult.java
│   ├── ui/                       # Interface gráfica
│   │   └── BiometricAuthenticationApp.java
│   ├── BiometricAuthenticator.java
│   ├── BiometricAuthenticatorImpl.java
│   ├── BiometricScanner.java
│   ├── FacialRecognitionScanner.java
│   ├── BiometricMatcher.java
│   ├── FacialRecognitionMatcher.java
│   ├── BiometricMatcherFactory.java
│   ├── UserDatabase.java
│   ├── InMemoryUserDatabase.java
│   ├── SessionManager.java
│   ├── SessionManagerImpl.java
│   ├── AuditLog.java
│   ├── AuditLogImpl.java
│   ├── CryptoService.java
│   ├── CryptoServiceImpl.java
│   ├── CryptoException.java
│   └── BiometricCaptureException.java
├── compile.bat                   # Script de compilação
├── run.bat                       # Script de execução
└── README.md
```

## 📝 Logs

Todas as operações são registradas em:
- **Console da aplicação**: Log em tempo real
- **audit.log**: Arquivo permanente com todas as tentativas de autenticação

## ⚠️ Nota Importante

Este é um sistema de demonstração educacional. A captura biométrica é simulada através de templates aleatórios. Em um ambiente de produção, seria necessário integrar com hardware real de captura facial e bibliotecas especializadas de reconhecimento facial.
