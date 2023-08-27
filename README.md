# resilient-microservices-example

## Subindo os três serviços

### Serviço A
Para subir o serviço A basta executar o seguinte comando na pasta raiz do projeto
```shell
mvn spring-boot:run -f ./service-a/pom.xml
```
Para testar o serviço A vamos fazer duas requisições:
- uma para [http://localhost:8001/service-a/greetings/a](http://localhost:8001/service-a/greetings/a) que deve retornar
```text
Greetings from service A
```
- uma para [http://localhost:8001/service-a/congratulations/a](http://localhost:8001/service-a/congratulations/a) que deve retornar
```text
Congratulations from service A
```

### Serviço B
Para subir o serviço B basta executar o seguinte comando na pasta raiz do projeto
```shell
mvn spring-boot:run -f ./service-b/pom.xml
```
Para testar o serviço B vamos fazer duas requisições:
- uma para [http://localhost:8002/service-b/greetings/b](http://localhost:8002/service-b/greetings/b) que deve retornar
```text
Greetings from service B
```
- uma para [http://localhost:8002/service-b/congratulations/b](http://localhost:8002/service-b/congratulations/b) que deve retornar
```text
Congratulations from service B
```

### Serviço C
Para subir o serviço C basta executar o seguinte comando na pasta raiz do projeto
```shell
mvn spring-boot:run -f ./service-c/pom.xml
```
Para testar o serviço C vamos fazer duas requisições:
- uma para [http://localhost:8003/service-c/greetings/c](http://localhost:8003/service-c/greetings/c) que deve retornar
```text
Greetings from service C
```
- uma para [http://localhost:8003/service-c/congratulations/c](http://localhost:8003/service-c/congratulations/c) que deve retornar
```text
Congratulations from service C
```

## Testando a integração entre os três serviços
Para testar a integração entre os serviços vamos fazer quatro requisições:
- uma para [http://localhost:8001/service-a/greetings/ab](http://localhost:8001/service-a/greetings/ab) que deve retornar
```text
Greetings from service A and Greetings from service B 
```
- uma para [http://localhost:8001/service-a/greetings/ac](http://localhost:8001/service-a/greetings/ac) que deve retornar
```text
Greetings from service A and Greetings from service C 
```
- uma para [http://localhost:8001/service-a/congratulations/ab](http://localhost:8001/service-a/congratulations/ab) que deve retornar
```text
Congratulations from service A and Congratulations from service B 
```
- uma para [http://localhost:8001/service-a/congratulations/ac](http://localhost:8001/service-a/congratulations/ac) que deve retornar
```text
Congratulations from service A and Congratulations from service C 
```
