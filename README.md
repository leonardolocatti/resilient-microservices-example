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

## Subindo a stack de observabilidade
Para subir a stack de observabilidade que será utilizada vamos executar o seguinte comando na pasta raiz do projeto
```shell
docker compose up -d
```
Esse comando criará e iniciará 3 containers
- [Zipkin](https://zipkin.io/), que pode ser acessado em [http://localhost:9411](http://localhost:9411)
- [Prometheus](https://prometheus.io/), que pode ser acessado em [http://localhost:9090](http://localhost:9090)
- [Grafana](https://grafana.com/oss/grafana/), que pode ser acessado em [http://localhost:3000](http://localhost:3000)

## Configurando o Zipkin no Serviço C
Para configurar o zipkin no serviço C primeiro vamos adicionar as seguintes dependências no pom do projeto
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>

<dependency>
  <groupId>io.zipkin.reporter2</groupId>
  <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```
Após isso vamos adicionar a seguinte propriedade no application.yml
```yaml
management:
  tracing:
    sampling:
      probability: 1.0
```
Para realizar o teste podemos fazer uma requisição para [http://localhost:8001/service-a/greetings/ac](http://localhost:8001/service-a/greetings/ac), 
depois acessar o Zipkin que subimos anteriormente e buscar os traces. Com isso podemos ver o trace da requisição que acabamos de fazer. Clicando
em "show", são exibidos mais detalhes e podemos ver que o serviço A chamou o serviço C e quanto tempo demorou cada etapa do trace

***ADD imagem do trace (traces_result)***
***ADD imagem dos detalhes do trace (trace_show)***

Clicando em "Dependencies" e depois em "RUN QUERY" podemos ver o um diagrama com as dependências construído a partir dos traces

***ADD imagem do grafico de dependencia (dependency_view)***

## Configurando o Prometheus no Serviço C
Primeiro vamos adicionar a dependencia no pom.xml
```xml
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```
Agora vamos adicionar a configuração no application.yml para expor a porta e o endpoint onde o Prometheus irá coletar as métricas
```yaml
management:
  endpoints:
    web:
      exposure:
        include: prometheus
  server:
    port: 9003
```
Com isso quando acessamos o [http://localhost:9003/actuator/health](http://localhost:9003/actuator/health) vemos as métricas no
formato que o Prometheus espera

***ADD imagem do actuator prometheus (actuator_prometheus)***

Por fim vamos configurar o Prometheus para coletar as métricas nesse endpoint. No arquivo de configuração do prometheus adicionamos
```yaml
- job_name: service-c
  metrics_path: /actuator/prometheus
  static_configs:
    - targets:
        - host.docker.internal:9003
      labels:
        application: service-c
```
Vamos parar o container do Prometheus rodando com o comando e recriá-lo com as configurações atualizadas com o comando
```shell
docker stop prometheus && docker compose up -d --build prometheus
```
Agora podemos acessar o Prometheus em http://localhost:9090 e fazer a consulta pra verificar se deu tudo certo

***ADD imagem up do prometheus (prometheus-service-c-up)***

## Configurando o Grafana
A primeira coisa que faremos é adicionar o Prometheus no Grafana, para isso basta entrarmos no menu Connections -> Datasource e clicar em "Add new data source"
Selecione Prometheus e preencha o campo "Prometheus server URL" com "http://prometheus:9090" e o campo "Scrape interval" com "5s". Depois clique em "Save & Test"

Agora vamos adicionar um dashboard para acompanharmos as métricas das aplicações. Vá até o menu Dashboards e clique em "new" e depois em "import". Preencha com o ID do Dashboard,
clique em "Load", selecione o Datasource que acabamos de criar, clique em e depois clique em "Import"

## Circuit Breaker
Acesse o Dashboard K6 Load Tests Results para visualizarmos os testes que serão executados

Vamos executar o seguinte comando para rodar um script de teste de carga utilizando o [K6](https://k6.io/open-source/)
```shell
docker run --rm --add-host=host.docker.internal:host-gateway -e K6_PROMETHEUS_RW_SERVER_URL=http://host.docker.internal:9090/api/v1/write -e K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true -e K6_PROMETHEUS_RW_PUSH_INTERVAL=5 -i grafana/k6 run -o experimental-prometheus-rw --tag testid=circuit-breaker - <circuit-breaker-script.js
```
***ADD imagem do teste sem o circuit breaker***
Com os resultados dos testes podemos perceber que o o tempo de resposta começa a aumentar até o ponto em que a aplicação começa a responder com erros, obtendo uma taxa de sucesso de apenas 33%
***ADD imagem do trace com timeout
No trace podemos perceber que o serviço B demorou muito tempo para conseguir responder o serviço A tomou um timeout

### Adicionando um circuit breaker em A
Para proteger nossas aplicações desse cenário podemos utilizar um circuit breaker

Primeiro vamos adicionar as dependências no pom.xml
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-spring-boot3</artifactId>
  <version>2.1.0</version>
</dependency>
```
Agora vamos até a classe Client do Serviço A para adicionar a anotação do circuit breaker no método
```java
@CircuitBreaker(name = "service-b")
public String greetingsFromB() {
    return restTemplate.getForObject(SERVICE_B_BASE_URL + "/greetings/b", String.class);
}
```
Por último vamos realizar as configurações das propriedades do circuit breaker no application.yml
```yaml
resilience4j:
  circuitbreaker:
    instances:
      service-b:
        slidingWindowSize: 25
        slowCallDurationThreshold: 2150ms
        slowCallRateThreshold: 20
        permittedNumberOfCallsInHalfOpenState: 5
        waitDurationInOpenState: 300ms
```
Vamos executar o script novamente
```shell
docker run --rm --add-host=host.docker.internal:host-gateway -e K6_PROMETHEUS_RW_SERVER_URL=http://host.docker.internal:9090/api/v1/write -e K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true -e K6_PROMETHEUS_RW_PUSH_INTERVAL=5 -i grafana/k6 run -o experimental-prometheus-rw --tag testid=circuit-breaker - <circuit-breaker-script.js
```
***ADD imagem do teste com o circuit breaker***
Com os resultados dos testes podemos perceber que o o tempo de resposta novamente começa a aumentar, mas agora nossa taxa de erro se mantém mais baixa porque o circuit breaker
identifica que o serviço B não está saudável e não envia requisições pra ele por um período. Nesse cenário obtemos uma taxa de sucesso de 91%
***ADD imagem do trace com o circuit breaker aberto
No trace podemos ver que não foi realizada chamada para o serviço B porque o circuito estava aberto

### Adicionando um Fallback no Circuit Breaker
Somente com o circuit breaker que implementamos anteriormente já obtemos um ganho muito bom, mas caso a exista algum outro 
serviço que possa funcionar como contingência executando a mesma ação do primeiro conseguimos melhorar ainda mais. Supondo
que o serviço C seja uma contingência para essa operação, vamos adicionar ele como fallback no circuit breaker.

Vamos até a classe Client do Serviço A e vamos incluir o fallback
```java
@CircuitBreaker(name = "service-b", fallbackMethod = "greetingsFromBFallback")
public String greetingsFromB() {
    return restTemplate.getForObject(SERVICE_B_BASE_URL + "/greetings/b", String.class);
}

public String greetingsFromBFallback(Exception e) {
    return congratulationsFromC();
}
```

Vamos executar o script novamente
```shell
docker run --rm --add-host=host.docker.internal:host-gateway -e K6_PROMETHEUS_RW_SERVER_URL=http://host.docker.internal:9090/api/v1/write -e K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true -e K6_PROMETHEUS_RW_PUSH_INTERVAL=5 -i grafana/k6 run -o experimental-prometheus-rw --tag testid=circuit-breaker - <circuit-breaker-script.js
```
***ADD imagem do teste com o circuit breaker com fallback***
Com os resultados dos testes vemos que agora obtemos 100% de sucesso. Isso ocorre porque o serviço C está lá para atender as requisições que falharam em B que
foram direto pra ele quando o circuito estava aberto
identifica que o serviço B não está saudável e não envia requisições pra ele por um período. Nesse cenário obtemos uma taxa de sucesso de 91%
***ADD imagem de dependencia quando falha
No diagrama de dependências podemos ver que algumas requisições agora são encaminhadas para o serviço C

## Bulkhead
Vamos executar o seguinte comando para rodar o script de teste de carga que será utilizado para o bulkhead
```shell
docker run --rm --add-host=host.docker.internal:host-gateway -e K6_PROMETHEUS_RW_SERVER_URL=http://host.docker.internal:9090/api/v1/write -e K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true -e K6_PROMETHEUS_RW_PUSH_INTERVAL=5 -i grafana/k6 run -o experimental-prometheus-rw --tag testid=bulkhead - <bulkhead-script.js
```
***ADD imagem do teste sem bulkhead***
Com os resultados podemos observar que com o passar do tempo o endpoint do serviço B que estava com problemas começou a afetar
o serviço A no consumo do serviço C. Isso fez com que aparecessem timeouts no endpoint /congratulations/ac. Desconsiderando
as chamadas para o serviço B que está com problemas, obtemos uma taxa de suceço de apenas 34%

### Adicionando Bulkhead no serviço A
Vamos até a classe Client do serviço A e vamos incluir o bulkhead
```java
@Bulkhead(name = "service-b")
public String congratulationsFromB() {
    return restTemplate.getForObject(SERVICE_B_BASE_URL + "/congratulations/b", String.class);
}
```
Agora vamos adicionar no application.yml a configuração do bulkhead
```yaml
resilience4j:
  bulkhead:
    instances:
      service-b:
        maxWaitDuration: 100ms
        maxConcurrentCalls: 28
```
Vamos executar novamente o script
```shell
docker run --rm --add-host=host.docker.internal:host-gateway -e K6_PROMETHEUS_RW_SERVER_URL=http://host.docker.internal:9090/api/v1/write -e K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true -e K6_PROMETHEUS_RW_PUSH_INTERVAL=5 -i grafana/k6 run -o experimental-prometheus-rw --tag testid=bulkhead - <bulkhead-script.js
```
***ADD imagem do teste com bulkhead***
Com os resultados podemos observar que agora as chamadas do endpoint /congratulations/ac que vão para o serviço C continuam funcionando
sem problema algum. Isso acontece porque o bulkhead está limitando a quantidade de recursos que a aplicação pode utilizar no consumo de B.
***ADD imagem do trace com o bloqueio de recurso***
Podemos comprovar a atuação do bulkhead pelo trace, onde conseguimos ver que a requisição não foi enviada para B porque o 
bulkhead já estava cheio