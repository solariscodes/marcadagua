```
# Watermark App

## Descrição
Watermark App é uma aplicação Java que adiciona uma marca d'água com o nome de usuário do sistema em toda a tela. Essa marca d'água é sutil e permite a interação normal com outros aplicativos enquanto está visível. É útil para demonstrações ou para proteger telas com informações sensíveis contra capturas não autorizadas.

## Características
- Marca d'água com nome de usuário do sistema exibido em toda a tela.
- Configuração da fonte, cor, transparência e tamanho da marca d'água através de um arquivo de configuração.
- Implementação de um ícone de bandeja do sistema, sem opções adicionais para fechar o aplicativo.
- A janela da marca d'água não aparece na barra de tarefas e é configurada para ser sempre a janela superior.

## Requisitos
- Java Development Kit (JDK) 8 ou superior.
- Java Native Access (JNA) library.

## Configuração
As configurações de aparência da marca d'água podem ser modificadas no arquivo `marca.cfg` que deve estar localizado na mesma pasta do executável do projeto. As seguintes propriedades podem ser configuradas:

- `fonte`: Nome da fonte (ex: Arial).
- `tamanhoFonte`: Tamanho da fonte (ex: 40).
- `corR`: Componente vermelho da cor RGB (0-255).
- `corG`: Componente verde da cor RGB (0-255).
- `corB`: Componente azul da cor RGB (0-255).
- `transparencia`: Nível de transparência (0-255, onde 255 é totalmente opaco).

## Executando o Projeto
1. Compile o código usando seu ambiente de desenvolvimento Java ou uma linha de comando. Por exemplo:
   ```
javac WatermarkApp.java
   ```
2. Execute o aplicativo:
   ```
java WatermarkApp
   ```

## Contribuições
Contribuições são bem-vindas! Para contribuir, por favor, envie um pull request através do GitHub.

## Licença
Este projeto é distribuído sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## Autor
Rodrigo Prestes Menezes
```
