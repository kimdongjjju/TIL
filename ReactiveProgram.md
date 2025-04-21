**CHAPTER 1 : 리액티브 시스템과 리액티브 프로그래밍**

리액티브 시스템이란? 반응을 잘하는 시스템
반응을 잘한다는건, 클라이언트 요청에 즉각적으로 응답함으로써 지연 시간을 최소화 한다.

리액티브 선언문

MEANS(수단)

    통신 수단으로, 비동기 메시지 기반의 통신을 통하여 구성요소들 간의 느슨한 결합, 격리성, 위치 투명성을 보장

FORM(형성)

    탄력성→  작업량이 변화하더라도 일정한 응답을 유지하는 것.
    회복성→ 장애가 발생하더라도 응답성을 유지하는 것.

VALUE(가치)

    확장 알고리즘을 통해 탄력성을 확보함으로 즉각적으로 응답 가능한 시스템을 구축
    즉, 빠른 응답성을 바탕으로 유지보수와 확장이 용이한 시스템을 구축

리액티브 프로그램이란?

    리액티브 시스템을 구축하는 데 필요한 프로그래밍 모델

Non-Blocking I/O 

    스레드가 차단되지 않는다.

즉, 리액티브 시스템의 설계 원칙에 잘 부합하는 비동기 Non-Blocking 통신을 위한 프로그래밍 모델

선언형 프로그래밍
    이러이러한 동작을 하겠다는 목표만 선언, 동작을 구체적으로 명시하지 않고 목표만 선언한다.

````
Publisher , Subscriber, Data Source, Operator 등으로 코드를 구성

Publisher→ 입력으로 들어오는 데이터를 Subscriber에 제공하는 역할을 한다.

Subscriber→ Publisher로부터 전달받은 데이터를 사용하는 역할을 한다.

Data Source→ Publisher의 입력으로 전달되는 데이터를 의미한다.

Operator→ Publisher와 Subscriber 중간에서 데이터를 가공하는 역할을 한다.
````


---
**CHAPTER 2 : 리액티브 스트림즈**

리액티브 스트림즈 정의 → 데이터 스트림을 Non-Blocking이면서 비동기적인 방식으로 처리하기 위한 리액티브 라이브러리의 표준 사양

| 컴포넌트 | 설명 |
| --- | --- |
| Publisher | 데이터를 생성하고 방출하는 역할 |
| Subscriber | 구독한 Publisher로부터 방출된 데이터를 전달받아서 처리하는 역할 |
| Subscription | Publisher에 요청할 데이터의 개수를 지정하고, 데이터의 구독을 취소하는 역할. |
| Processor |  |

`Subscribe(전달받은 데이터 구독) -`

`onSubscribe(방출준비상태알림) -`

`Subscription.request(원하는 데이터의 개수 요청) -`

`onNext(데이터 방출) -`

`onComplete(데이터 방출 완료 알림) -`

`onError(처리과정에러)`

→ Subscription.request 는 각기다른 스레드에서 비동기로 일어나기 때문에 부하 방지 차원에서 개수를 제어

```java
[Publisher Interface]

public interface Publisher<T> {
	public void subscribe(Subscriber<? super T> s);
}
```

Reactive Streams에서는 Publisher가 subscribe 메서드의 파라미터인 Subscriber를 등록하는 형태로 구독이 이뤄진다.

```java
[Subscriber Interface]

public interface Subscriber<T> {
	public void onSubscribe(Subscription s);
	public void onNext(T t);
	public void onError(Throwable t);
	public void onComplete();
}
```

`onSubscirbe` : 구독 시작 시점에 어떤 처리를 하는 역할

`onNext` : Publisher가 방출한 데이터를 처리

`onError` : Publisher가 데이터 방출를 위한 처리 과정에서 에러가 발생 시 에러 처리

`onComplete` : 데이터 방출을 완료했음을 알릴 때 호출

```java
[Subscription Interface]

public interface Subscription {
	public void request(long n);
	public void cancel();
}
```

onComplete, onError, onNext, onSubscribe 메서드는
Subscriber 인터페이스에 정이되지만, 이 메서드를 실제 호출해서 사용하는 주체는 Publisher이기 때문에 Publisher가 Subscriber에게 보내는 Signal 이다.

request, cancel은 Subscription 인터페이스 코드지만, 실제로 사용하는 주체는 Subscriber이므로 Subscriber가 Publisher에게 보내는 Signal 이다.

UpStream은 현재 호출한 메서드에서 반환된 Flux의 위치에서 자신보다 더 상위에 있는 Flux
DownStream은 하위

`[Publisher 구현을 위한 주요 기본 규칙]`

| 번호 | 규칙 |
| --- | --- |
| 1 | Publisher가 Subscriber에게 보내는 onNext signal의 총 개수는 항상 해당 Subscriber의 구독을 통해 요청된 데이터의 총 개수보다 더 작거나 같아야함. |
| 2 | signal을 보내고 onComplete 또는 onError를 호출하여 구독을 종료할 수 있다. |
| 3 | Publisher의 데이터 처리가 실패하면 onError signal을 보내야 한다. |
| 4 | Publisher의 데이터 처리가 성공적으로 종료되면 onComplete signal을 보내야한다. |
| 5 | Publisher가 Subscriber에게 onError 또는 onComplete signal을 보내는 경우 해당 Subscriber의 구독은 취소된 것으로 간주되어야 한다. |
| 6 | 일단 종료된 상태 signal을 받으면(onError, onComplete) 더 이상 signal이 발생되지 않아야 한다. |
| 7 | 구독이 취소되면 Subscriber는 결국 signal을 받는 것을 중지해야 한다. |

**CHAPTER 15 : Spring WebFlux 개요**

WebFlux의 기본 서버 엔진은 Netty 기반이지만, Jetty나 Undertow 같은 서버 엔진에서 지원하는 Reactive Streams Adapter를 통해 Reactive Streams를 지원해준다.
WebFlux는 Non-Blocking I/O를 지원할 수 있도록 Non-Blocking I/O를 지원해주는 모듈들을 사용해야 한다 (R2DBC 등)

[WebFlux Flow]
1. 클라이언트에서 요청을 전송
2. 서버 엔진을 거쳐, **HttpHandler** 가 들어오는 요청을 전달 받는다.
3. ServerWebExchange (ServerHttpRequest, ServerHttpResponse 포함) 생성한 후 
4. WebFliter Chain으로 전달
5. WebFliter Chain에서 전처리 과정 진행
6. WebHandler Interface의 구현체인 DispatchHandler 에게 전달.
7. HandlerMapping List를 원본 Flux의 소스로 전달 받는다.
8. ServletWebExchange를 처리를 핸들러를 조회합니다.
9. 핸들러를 HandlerAdapter에 위임한하고 , HandlerAdapter -> ServerWebExchange 처리할 핸들러를 호출
10. 처리 후 응답 데이터 리턴
11. 응답 데이터를 처리할 HandlerResultHandler를 조회
12. 응답 데이터를 적절하게 처리후 response로 리턴

WebFilter 와 HandlerFilterFunction의 차이점은 
WebFilter는 함수형 기반의 요청 핸들러에서 모두 동작하지만, HandlerFilterFunction은 함수형 기반의 핸들러에서만 동작하기 때문에
함수형 기반의 핸들러에서만 제한적으로 필터링 작업을 수행한다.

```java
@Component
public class LogFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPaht();
        return chain.filter(exchange).doAfterTerminate(() -> {
           if (path.contains("필터대상")) {
               log.info("path : {}, status : {}", exchange.getResponse(), exchange.getStatusCode());
           }
        });
    }
}
```

[WebFlux Process]
- Non-Blocking의 Event Loop 방식으로 요청을 처리한다.
1. 클라이언트가 요청 시 요청 핸들러가 전달 받는다.
2. 요청 핸들러가 이벤트 루프에 요청을 푸시한다.
3. 이벤트 루프는 네트워크, 데이터베이스 연결 작업 등 비용이 드는 작업에 대한 콜백을 등록합니다.
4. 작업이 완료되면 완료 이벤트를 이벤트 루프에 푸시합니다.
5. 등록한 콜백을 호출해 처리 결과를 전달 합니다.
-> 단일 스레드로 계속 실행되며, 모든 작업들이 이벤트로 처리된다 -> 이벤트 발생 시 해당 이벤트에 대한 콜백을 등록함과 동시에 다음 이벤트 처리로 넘어간다.
-> 일반적으로 CPU 코어 개수만큼의 스레드 생성 