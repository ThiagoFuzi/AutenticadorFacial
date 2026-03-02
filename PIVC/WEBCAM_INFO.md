# 📷 Informações sobre Captura via Webcam

## Como Funciona

O sistema agora suporta captura biométrica real usando sua webcam! A aplicação detecta automaticamente se há uma webcam disponível no seu computador.

## Detecção Automática

Ao iniciar a aplicação:
- ✅ **Webcam detectada**: O sistema usa captura real por padrão
- ❌ **Sem webcam**: O sistema usa captura simulada automaticamente

## Alternando entre Modos

Na aba de Autenticação, você verá um botão que permite alternar entre:
- **Modo Real**: Captura imagens da sua webcam
- **Modo Simulado**: Gera templates aleatórios para demonstração

## Captura Real

Quando você usa a webcam real:

1. **Captura de Imagem**: O sistema tira uma foto usando sua webcam
2. **Salvamento**: A imagem é salva em `captures/` com timestamp
3. **Extração de Template**: Características da imagem são extraídas para criar o template biométrico
4. **Qualidade**: Calculada baseada nas características da imagem (brilho, contraste, etc.)

## Extração de Template

O sistema extrai características da imagem capturada:
- Análise de pixels em diferentes regiões da imagem
- Cálculo de histogramas de cor
- Detecção de bordas e contraste
- Análise de brilho médio

Essas características são combinadas para criar um template único de 128 valores.

## Diretório de Capturas

Todas as imagens capturadas são salvas em:
```
captures/
├── capture_20240228_143015.png
├── capture_20240228_143127.png
└── ...
```

O formato do nome é: `capture_YYYYMMDD_HHMMSS.png`

## Requisitos da Webcam

- Webcam USB ou integrada
- Drivers instalados corretamente
- Permissão de acesso à câmera (Windows pode solicitar)

## Solução de Problemas

### Webcam não detectada
1. Verifique se a webcam está conectada
2. Teste a webcam em outro aplicativo (ex: Camera do Windows)
3. Reinstale os drivers da webcam
4. Reinicie a aplicação

### Erro ao capturar
1. Feche outros aplicativos que possam estar usando a webcam
2. Verifique as permissões de acesso à câmera
3. Use o modo simulado como alternativa

### Qualidade baixa
1. Melhore a iluminação do ambiente
2. Posicione-se de frente para a câmera
3. Limpe a lente da webcam
4. Ajuste a distância da câmera

## Bibliotecas Utilizadas

- **Webcam Capture 0.3.12**: Biblioteca Java para captura de imagens via webcam
- **SLF4J 1.7.36**: Framework de logging

## Limitações

Este é um sistema educacional. A extração de template é simplificada e não usa algoritmos avançados de reconhecimento facial como:
- Deep Learning (CNNs)
- Detecção de landmarks faciais
- Análise de características faciais específicas

Para um sistema de produção, recomenda-se integrar com:
- OpenCV
- AWS Rekognition
- Azure Face API
- Google Cloud Vision

## Privacidade

- As imagens são salvas localmente em `captures/`
- Nenhuma imagem é enviada para servidores externos
- Você pode deletar as imagens a qualquer momento
- Os templates biométricos são criptografados com AES-256-GCM

## Dicas de Uso

1. **Iluminação**: Use boa iluminação frontal
2. **Posição**: Fique de frente para a câmera
3. **Distância**: Mantenha distância adequada (30-50cm)
4. **Fundo**: Prefira fundos neutros
5. **Expressão**: Mantenha expressão neutra

## Modo Simulado

Se você não tem webcam ou prefere testar sem ela:
1. Clique no botão "Alternar para Modo Simulado"
2. O sistema gerará templates aleatórios
3. Útil para demonstrações e testes

## Segurança

- Templates extraídos são criptografados antes de armazenar
- Imagens originais não são criptografadas (apenas salvas localmente)
- Recomenda-se deletar imagens antigas periodicamente
- Em produção, considere não salvar as imagens ou criptografá-las
