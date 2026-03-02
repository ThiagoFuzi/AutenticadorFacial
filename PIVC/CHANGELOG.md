# Histórico de Mudanças

## Versão 2.0 - Integração com Webcam Real (02/03/2026)

### ✨ Novas Funcionalidades

- **Captura via Webcam Real**: Sistema agora suporta captura de imagens reais usando webcam
- **Detecção Automática**: Detecta automaticamente se há webcam disponível
- **Alternância de Modos**: Botão para alternar entre captura real e simulada
- **Salvamento de Imagens**: Imagens capturadas são salvas em `captures/` com timestamp
- **Extração de Template Real**: Templates baseados em características reais da imagem

### 📦 Novas Bibliotecas

- `webcam-capture-0.3.12.jar` - Captura de imagens via webcam
- `slf4j-api-1.7.36.jar` - API de logging
- `slf4j-simple-1.7.36.jar` - Implementação simples do SLF4J

### 📝 Novos Arquivos

- `WebcamFacialScanner.java` - Scanner que usa webcam real
- `BiometricAuthenticationAppWithWebcam.java` - UI com suporte a webcam
- `WEBCAM_INFO.md` - Documentação sobre uso da webcam
- `clean_captures.bat` - Script para limpar imagens capturadas
- `CHANGELOG.md` - Este arquivo

### 🔧 Arquivos Modificados

- `compile.bat` - Atualizado para incluir `lib/*.jar` no classpath
- `run.bat` - Atualizado para usar nova UI com webcam e incluir bibliotecas
- `README.md` - Documentação atualizada com informações sobre webcam
- `COMO_USAR.md` - Instruções atualizadas para captura real

### 📁 Nova Estrutura de Diretórios

```
projeto/
├── lib/                    # Bibliotecas externas (NOVO)
│   ├── webcam-capture-0.3.12.jar
│   ├── slf4j-api-1.7.36.jar
│   └── slf4j-simple-1.7.36.jar
├── captures/               # Imagens capturadas (NOVO)
│   └── capture_*.png
└── ...
```

### 🎯 Melhorias

- Interface mais informativa com status da webcam
- Feedback visual durante captura
- Qualidade de template baseada em características reais da imagem
- Logs mais detalhados sobre o processo de captura

### 🔄 Compatibilidade

- Sistema mantém compatibilidade com modo simulado
- Funciona sem webcam (fallback automático)
- Todos os usuários de exemplo continuam funcionando

---

## Versão 1.0 - Sistema Base (28/02/2026)

### ✨ Funcionalidades Iniciais

- Autenticação biométrica facial simulada
- Três níveis hierárquicos de acesso (Público, Restrito, Confidencial)
- Criptografia AES-256-GCM para templates
- Auditoria completa de operações
- Interface gráfica Swing com 3 abas
- Cadastro de novos usuários
- 3 usuários de exemplo pré-cadastrados

### 📝 Arquivos Criados

- 17 classes Java (models, interfaces, implementações)
- Interface gráfica completa
- Scripts de compilação e execução
- Documentação completa (README.md, COMO_USAR.md)
- Configuração para GitHub

### 🛠️ Tecnologias

- Java 11
- Swing (GUI)
- AES-256-GCM (Criptografia)
- In-Memory Database (ConcurrentHashMap)
