# Sistema de Autenticação Biométrica

Sistema de identificação e autenticação biométrica para controle de acesso ao banco de dados estratégico do Ministério do Meio Ambiente brasileiro.

## 🎯 Funcionalidades

- ✅ Autenticação biométrica facial com threshold de 0.88
- ✅ **Captura via webcam real** (com fallback para simulação)
- ✅ Detecção automática de webcam disponível
- ✅ Três níveis hierárquicos de acesso (Público, Restrito, Confidencial)
- ✅ Criptografia AES-256-GCM para templates biométricos
- ✅ Auditoria completa de todas as operações
- ✅ Interface gráfica moderna e intuitiva
- ✅ Cadastro de novos usuários com captura biométrica
- ✅ Controle de acesso baseado em hierarquia

## 📋 Requisitos

- Java 11 ou superior
- Windows (scripts .bat fornecidos)
- Webcam (opcional - sistema funciona com captura simulada se não houver webcam)

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
- Captura biométrica facial via webcam (ou simulada se não disponível)
- Botão para alternar entre modo real e simulado
- Seleção de nível de acesso desejado
- Exibição de resultado detalhado
- Imagens capturadas salvas em `captures/`

### 👤 Cadastro
- Formulário para novo usuário
- Captura de biometria facial via webcam
- Validação de qualidade (mínimo 0.8)
- Preview da imagem capturada

### 📊 Informações
- Documentação do sistema
- Descrição dos níveis de acesso
- Informações técnicas
- Status da webcam

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
- **Biometria**: Reconhecimento Facial (webcam real ou simulado)
- **Captura de Imagem**: Webcam Capture Library 0.3.12
- **Logging**: SLF4J 1.7.36
- **Armazenamento**: In-Memory (ConcurrentHashMap)

## 📁 Estrutura do Projeto

```
biometric-authentication-system/
├── lib/                          # Bibliotecas externas
│   ├── webcam-capture-0.3.12.jar
│   ├── slf4j-api-1.7.36.jar
│   └── slf4j-simple-1.7.36.jar
├── src/main/java/br/gov/mma/biometric/
│   ├── model/                    # Modelos de dados
│   │   ├── AccessLevel.java
│   │   ├── BiometricType.java
│   │   ├── BiometricData.java
│   │   ├── User.java
│   │   └── AuthenticationResult.java
│   ├── ui/                       # Interface gráfica
│   │   ├── BiometricAuthenticationApp.java
│   │   └── BiometricAuthenticationAppWithWebcam.java
│   ├── BiometricAuthenticator.java
│   ├── BiometricAuthenticatorImpl.java
│   ├── BiometricScanner.java
│   ├── FacialRecognitionScanner.java
│   ├── WebcamFacialScanner.java
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
├── captures/                     # Imagens capturadas da webcam
├── compile.bat                   # Script de compilação
├── run.bat                       # Script de execução
└── README.md
```

## 📝 Logs

Todas as operações são registradas em:
- **Console da aplicação**: Log em tempo real
- **audit.log**: Arquivo permanente com todas as tentativas de autenticação

## ⚠️ Nota Importante

Este sistema suporta captura biométrica real via webcam usando a biblioteca Webcam Capture. O sistema detecta automaticamente se há uma webcam disponível:

- **Com webcam**: Captura imagens reais e extrai templates baseados em características da imagem
- **Sem webcam**: Usa captura simulada com templates aleatórios para demonstração

As imagens capturadas são salvas no diretório `captures/` para referência. Em um ambiente de produção, seria recomendado integrar com bibliotecas especializadas de reconhecimento facial (como OpenCV ou AWS Rekognition) para maior precisão.
