# WatermarkApp

## Visão Geral
WatermarkApp é uma aplicação Java que aplica uma marca d'água personalizável e transparente em seu ambiente de desktop. Esta aplicação utiliza JNA (Java Native Access) para integrar-se com sistemas Windows e exibir marcas d'água sobre todas as outras janelas. Inclui opções de configuração para texto da marca d'água, fonte, cor e transparência, baseadas em informações do sistema como nome do usuário, endereço IP, data e hora.

## Funcionalidades
- Exibe uma marca d'água personalizável em toda a tela.
- Configuração através do arquivo `marca.cfg` para configurações personalizadas, incluindo fonte, cor, tamanho e transparência.
- Opções para incluir o endereço IP do sistema, hora e data na marca d'água.
- Integração perfeita com o sistema operacional Windows usando JNA para manipulação de janelas.

## Instalação
Para executar o WatermarkApp, certifique-se de ter o Java instalado em sua máquina. Baixe os arquivos da aplicação para o seu sistema local.

### Pré-requisitos
- Java JDK 11 ou superior
- Biblioteca JNA (incluída no diretório lib)

## Configuração
Edite o arquivo `marca.cfg` para personalizar as configurações da aplicação:
- `font.name`: Nome da fonte (ex.: Arial, Times New Roman)
- `font.style`: Estilo da fonte (ex.: Bold, Plain)
- `font.size`: Tamanho da fonte em pixels
- `color.red`, `color.green`, `color.blue`: Configurações de cor RGB
- `color.alpha`: Transparência da marca d'água (0-100)
- `address`: Defina como "1" para exibir o endereço IP
- `time`: Defina como "1" para exibir a hora atual
- `date`: Defina como "1" para exibir a data atual

## Uso
Para iniciar a aplicação, execute:
```bash
java -jar WatermarkApp.jar
```

## Contribuições
Contribuições para o WatermarkApp são bem-vindas. Por favor, assegure-se de seguir os padrões de codificação e enviar solicitações de pull para quaisquer novas funcionalidades ou correções de bugs.

## Licença
Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE para detalhes.
