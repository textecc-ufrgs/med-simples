# Med-Simples

Sistema para ajudar redatores técnicos, comunicadores ou profissionais de saúde a simplificar textos sobre temas de saúde. Atualmente contempla o tema Doença de Parkinson

## Pré-requisitos
- Java 11 (Para Java 8 usar branch feature/java8) 
- [Maven](https://maven.apache.org/)

## Execução
```bash
mvn spring-boot:run
```

## Uso
Deve ser executada uma requisição HTTP POST

URL:
- http://hostname:port/med-simples/simplify

Headers:
- Accept:\*/\*
- Content-Type:text/plain

Body (texto a ser analisado, por exemplo):
- 'Parkinson é por definição uma doença progressiva do sistema neurológico que afeta principalmente o cérebro.'

Por exemplo:

```bash
curl -X POST http://localhost:9000/med-simples/simplify \
-H "Content-Type:text/plain" -H "Accept:*/*" \
-d "Parkinson é por definição uma doença progressiva do sistema neurológico que afeta principalmente o cérebro."
```

A configuração da porta esta no arquivo application.yml (src/main/resources/application.yml)

## Autores e agradecimentos:
- Maria José B. Finatto
- Leonardo Zilio
- Luis A. L. Hercules

## Status do projeto:
Em desenvolvimento

## License
...