# 🚀 Como Criar o Repositório no GitHub

## Passo 1: Criar Repositório no GitHub (Web)

1. Acesse https://github.com
2. Faça login na sua conta
3. Clique no botão **"+"** no canto superior direito
4. Selecione **"New repository"**
5. Preencha:
   - **Repository name**: `biometric-authentication-system`
   - **Description**: `Sistema de Autenticação Biométrica para Ministério do Meio Ambiente`
   - **Visibility**: Public ou Private (sua escolha)
   - **NÃO** marque "Initialize this repository with a README"
6. Clique em **"Create repository"**

## Passo 2: Inicializar Git Local

Abra o terminal no diretório do projeto e execute:

```bash
git init
```

## Passo 3: Adicionar Arquivos

```bash
git add .
```

## Passo 4: Fazer o Primeiro Commit

```bash
git commit -m "Initial commit: Sistema de Autenticação Biométrica completo"
```

## Passo 5: Adicionar Repositório Remoto

Substitua `SEU_USUARIO` pelo seu nome de usuário do GitHub:

```bash
git remote add origin https://github.com/SEU_USUARIO/biometric-authentication-system.git
```

## Passo 6: Enviar para o GitHub

```bash
git branch -M main
git push -u origin main
```

## 🔐 Autenticação

Se for solicitado usuário e senha:
- **Usuário**: seu nome de usuário do GitHub
- **Senha**: use um **Personal Access Token** (não a senha da conta)

### Como criar um Personal Access Token:
1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate new token
3. Marque: `repo` (Full control of private repositories)
4. Copie o token gerado e use como senha

## ✅ Verificar

Após o push, acesse:
```
https://github.com/SEU_USUARIO/biometric-authentication-system
```

Você verá todo o projeto no GitHub! 🎉

## 📝 Comandos Resumidos

```bash
# 1. Inicializar
git init

# 2. Adicionar arquivos
git add .

# 3. Commit
git commit -m "Initial commit: Sistema de Autenticação Biométrica completo"

# 4. Adicionar remote (substitua SEU_USUARIO)
git remote add origin https://github.com/SEU_USUARIO/biometric-authentication-system.git

# 5. Push
git branch -M main
git push -u origin main
```

## 🔄 Atualizações Futuras

Quando fizer alterações no código:

```bash
git add .
git commit -m "Descrição das alterações"
git push
```

## ⚠️ Problemas Comuns

### Erro: "remote origin already exists"
```bash
git remote remove origin
git remote add origin https://github.com/SEU_USUARIO/biometric-authentication-system.git
```

### Erro: "failed to push some refs"
```bash
git pull origin main --allow-unrelated-histories
git push -u origin main
```

### Erro de autenticação
Use um Personal Access Token em vez da senha da conta.
