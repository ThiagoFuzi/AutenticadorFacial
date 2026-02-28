@echo off
echo ========================================
echo Executando Sistema de Autenticacao Biometrica
echo ========================================
echo.

REM Verificar se foi compilado
if not exist "bin" (
    echo ERRO: Projeto nao compilado!
    echo Execute compile.bat primeiro.
    pause
    exit /b 1
)

REM Executar aplicacao
java -cp bin br.gov.mma.biometric.ui.BiometricAuthenticationApp

pause
