@echo off
echo ========================================
echo Limpeza de Capturas da Webcam
echo ========================================
echo.

if not exist "captures" (
    echo Diretorio captures/ nao existe.
    echo Nada para limpar.
    pause
    exit /b 0
)

echo Este script ira deletar todas as imagens em captures/
echo.
set /p confirm="Tem certeza? (S/N): "

if /i "%confirm%"=="S" (
    del /Q captures\*.png 2>nul
    del /Q captures\*.jpg 2>nul
    echo.
    echo ========================================
    echo Capturas deletadas com sucesso!
    echo ========================================
) else (
    echo.
    echo Operacao cancelada.
)

echo.
pause
