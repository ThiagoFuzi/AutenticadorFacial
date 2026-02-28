@echo off
echo ========================================
echo Compilando Sistema de Autenticacao Biometrica
echo ========================================
echo.

REM Criar diretorio de saida se nao existir
if not exist "bin" mkdir bin

REM Compilar todos os arquivos Java
echo Compilando arquivos Java...
javac -d bin -encoding UTF-8 -sourcepath src/main/java src/main/java/br/gov/mma/biometric/model/*.java src/main/java/br/gov/mma/biometric/*.java src/main/java/br/gov/mma/biometric/ui/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilacao concluida com sucesso!
    echo ========================================
    echo.
    echo Para executar a aplicacao, use: run.bat
) else (
    echo.
    echo ========================================
    echo ERRO na compilacao!
    echo ========================================
    pause
)
