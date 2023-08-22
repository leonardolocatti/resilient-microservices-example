## Subindo a stack de observabilidade que será utilizada

```bash
docker compose up -d
```

Esse comando criará e iniciará 3 containers
- [Prometheus](https://prometheus.io/), que pode ser acessado em [http://localhost:9090](http://localhost:9090)
- [Zipkin](https://zipkin.io/), que pode ser acessado em [http://localhost:9411](http://localhost:9411)
- [Grafana](https://grafana.com/oss/grafana/), que pode ser acessado em [http://localhost:3000](http://localhost:3000)

## Executando os serviços

### Executando o serviço A

```bash
mvn spring-boot:run -f ./service-a/pom.xml
```

Esse comando iniciará o serviço na porta 8001. Para verificar o funcionamento basta acessar [http://localhost:8001/greetings/a](http://localhost:8001/greetings/a) e 
deverá ser o retornado o seguinte json:

```json
{
  "a": "Greetings from service A"
}
```

### Executando o serviço B

```bash
mvn spring-boot:run -f ./service-b/pom.xml
```

Esse comando iniciará o serviço na porta 8002. Para verificar o funcionamento basta acessar [http://localhost:8002/greetings/b](http://localhost:8002/greetings/b) e
deverá ser o retornado o seguinte json:

```json
{
  "b": "Greetings from service B"
}
```

### Executando o serviço C

```bash
mvn spring-boot:run -f ./service-c/pom.xml
```

Esse comando iniciará o serviço na porta 8003. Para verificar o funcionamento basta acessar [http://localhost:8003/greetings/c](http://localhost:8003/greetings/c) e
deverá ser o retornado o seguinte json:

```json
{
  "c": "Greetings from service C"
}
```

### Verificando a integração entre os 3 serviços

- Acessar [http://localhost:8002/bc](http://localhost:8002/bc) testará a integração B -> C e deverá retornar o json
```json
{
  "b": "Greetings from service B",
  "c": "Greetings from service C"
}
```

- Acessar [http://localhost:8001/ab](http://localhost:8001/ab) testará a integração A -> B e deverá retornar o json
 ```json
{
  "a": "Greetings from service A",
  "b": "Greetings from service B"
}
```

- Acessar [http://localhost:8001/ac](http://localhost:8001/ac) testará a integração A -> C e deverá retornar o json
 ```json
{
  "a": "Greetings from service A",
  "c": "Greetings from service C"
}
```
- Acessar [http://localhost:8001/abc](http://localhost:8001/abc) testará a integração A -> B e B -> C e deverá retornar o json
```json
{
  "a": "Greetings from service A",
  "b": "Greetings from service B",
  "c": "Greetings from service C"
}
```

## Gerando carga nos serviços

```bash
docker run --rm --add-host=host.docker.internal:host-gateway -i grafana/k6 run - <load-script.js
```

Esse comando criará um container que será removido ao final da exeução, rodando o [K6](https://k6.io/open-source/) com um script que chamará
cada um dos enpoints das aplicações por 5 minutos

## Simulando um gargalo na integração entre B e C

```bash
docker run --rm --add-host=host.docker.internal:host-gateway -i grafana/k6 run - <timeout-script.js
```

Esse comando executará um teste chamando o endpoint /greetings/abc do Serviço A simulando um esgotamento de recursos em C, fazendo com que o serviço B comece a tomar timeout,
ocasionando problemas em A também.