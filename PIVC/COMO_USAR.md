# 🚀 Como Usar o Sistema de Autenticação Biométrica

## Início Rápido

### 1️⃣ Compilar

Abra o terminal no diretório do projeto e execute:

```bash
compile.bat
```

Você verá a mensagem: "Compilacao concluida com sucesso!"

### 2️⃣ Executar

```bash
run.bat
```

A interface gráfica será aberta automaticamente.

## 📱 Usando a Interface

### Aba "🔐 Autenticação"

1. Selecione o **Nível de Acesso** desejado no dropdown:
   - PÚBLICO (Nível 1)
   - RESTRITO (Nível 2)
   - CONFIDENCIAL (Nível 3)

2. Clique em **"🔍 Capturar e Autenticar"**

3. O sistema irá:
   - Simular a captura biométrica facial
   - Verificar a qualidade da captura
   - Buscar o usuário no banco de dados
   - Validar o nível de acesso
   - Exibir o resultado

### Aba "👤 Cadastro"

1. Preencha os campos:
   - **ID do Usuário**: Identificador único (ex: USER-002)
   - **Nome Completo**: Nome do usuário
   - **Cargo**: Função do usuário
   - **Nível de Acesso**: Selecione o nível apropriado

2. Clique em **"📸 Capturar Biometria e Cadastrar"**

3. O sistema irá:
   - Capturar a biometria facial
   - Validar a qualidade (mínimo 0.8)
   - Criptografar o template
   - Salvar no banco de dados

### Aba "📊 Informações"

Contém documentação completa sobre:
- Níveis de acesso
- Tecnologias utilizadas
- Usuários de exemplo

## 👥 Testando com Usuários de Exemplo

O sistema vem com 3 usuários pré-cadastrados:

### Usuário 1: Funcionário Público
- **ID**: USER-001
- **Nome**: João Silva
- **Nível**: PÚBLICO (1)
- **Pode acessar**: Apenas nível PÚBLICO

### Usuário 2: Diretora
- **ID**: DIR-001
- **Nome**: Maria Santos
- **Nível**: RESTRITO (2)
- **Pode acessar**: Níveis PÚBLICO e RESTRITO

### Usuário 3: Ministro
- **ID**: MIN-001
- **Nome**: Carlos Oliveira
- **Nível**: CONFIDENCIAL (3)
- **Pode acessar**: Todos os níveis (PÚBLICO, RESTRITO e CONFIDENCIAL)

## 🧪 Cenários de Teste

### Teste 1: Autenticação Bem-Sucedida
1. Vá para aba "Autenticação"
2. Selecione "PUBLIC" no dropdown
3. Clique em "Capturar e Autenticar"
4. ✅ Resultado: Autenticação bem-sucedida (qualquer usuário pode acessar nível público)

### Teste 2: Acesso Negado por Nível Insuficiente
1. Vá para aba "Autenticação"
2. Selecione "CONFIDENTIAL" no dropdown
3. Clique em "Capturar e Autenticar"
4. ❌ Resultado: Acesso negado (apenas o Ministro tem nível 3)

### Teste 3: Cadastro de Novo Usuário
1. Vá para aba "Cadastro"
2. Preencha:
   - ID: USER-002
   - Nome: Ana Costa
   - Cargo: Analista
   - Nível: PUBLIC
3. Clique em "Capturar Biometria e Cadastrar"
4. ✅ Resultado: Usuário cadastrado com sucesso

### Teste 4: Tentativa de Cadastro Duplicado
1. Tente cadastrar novamente com ID "USER-001"
2. ❌ Resultado: Falha (usuário já existe)

## 📝 Logs e Auditoria

### Log em Tempo Real
Na parte inferior da janela, você verá um log em tempo real de todas as operações:
```
[14:30:15] Iniciando captura biométrica facial...
[14:30:15] Biometria capturada. Qualidade: 0.92
[14:30:15] Autenticação bem-sucedida: João Silva
```

### Arquivo de Auditoria
Todas as operações são registradas permanentemente em `audit.log`:
```
[2024-02-28 14:30:15.123] SUCCESSFUL_AUTHENTICATION | UserId: USER-001 | UserName: João Silva | GrantedLevel: PUBLIC (1)
```

## 🔒 Segurança

- **Criptografia**: Templates biométricos são criptografados com AES-256-GCM
- **Qualidade**: Autenticação requer qualidade mínima de 0.7
- **Enrollment**: Cadastro requer qualidade mínima de 0.8
- **Threshold**: Reconhecimento facial usa threshold de 0.88
- **Auditoria**: Todas as tentativas são registradas

## ⚠️ Notas Importantes

1. **Simulação**: A captura biométrica é simulada. Em produção, seria necessário hardware real.

2. **Armazenamento**: Os dados são armazenados em memória. Ao fechar a aplicação, apenas os 3 usuários de exemplo permanecerão.

3. **Qualidade Aleatória**: A qualidade da captura simulada varia aleatoriamente entre 0.5 e 1.0.

4. **Templates Únicos**: Cada captura gera um template único, então você não conseguirá autenticar com o mesmo usuário duas vezes seguidas (isso é intencional para demonstração).

## 🐛 Solução de Problemas

### Erro: "javac não é reconhecido"
- Instale o JDK 11 ou superior
- Adicione o Java ao PATH do sistema

### Erro: "Projeto não compilado"
- Execute `compile.bat` antes de `run.bat`

### Interface não abre
- Verifique se o Java está instalado: `java -version`
- Verifique se há erros no console

## 📞 Suporte

Para dúvidas ou problemas, consulte:
- README.md - Documentação completa
- audit.log - Logs de auditoria
- Console da aplicação - Mensagens de erro em tempo real
