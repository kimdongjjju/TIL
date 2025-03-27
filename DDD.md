도메인 주도 개발 시작하기

- 도메인은 여러 하위 도메인으로 구성된다.
- 소프트웨어가 도메인의 모든 기능을 제공하진 않는다
- 결제도 결제 시스템은 외부 PG사를 사용하는 것처럼
- 도메인마다 고정된 하위 도메인이 무조건 존재하는건 아니다
- 구성여부와 상황에 따라 달라진다.
- 기본적으로 도메인 모델은 특정 도메인을 개념적으로 표현한 것이다.
- 도메인 모델링 중 객체 모델은 기능과 데이터를 함께 보여주기에 적합핟.
- 상태 다이어그램을 이용할 수 있고.
- 클래스 다이어그램이나 상태 다이어그램 같은 UML 표기법만 사용해야 하는건 아니다.
- 도메인에 따라 용어나 의미가 결정되므로, 여러 하위 도메인을 하나의 다이어그램에 모델링하면 안된다.

---

Presentation - 사용자 인터페이스 (UI) 또는 표현 계층

- 사용자의 요청을 처리하고 사용자에게 정보를 보여준다. 여기서 사용자는 소프트웨어를 사용하는 사람뿐만 아니라 외부 시스템일 수도 있다.

Application - 응용 계층

- 사용자가 요청한 기능을 실행한다. 업무 로직을 직접 구현하지 않으며 도메인 계층을 조합해서 기능을 실행한다.

Domain

- 시스템이 제공할 도메인 규칙을 구현한다.

Infrastructure

- 데이터베이스나 메시징 시스템 같은 외부 시스템과의 연동을 처리한다.

---

개념모델

순수하게 문제를 분석한 결과물 → 기술을 고려하지 않은 모델

도메인을 모델링할 때

- 핵심 구성요소, 규칙, 기능등을 먼저 찾아보기 → 요구사항에서 출발한다.
- 요구사항을 파악하면 관련 기능들을 메서드로 추가할 수 있다.
- 요구사항에 따르는 조건들을 구현한다. e.g verifyAtLeastOneOrMoreOrderLines()

```java
public class Order {
	private List<OrderLine> orderLines;
	private ShippingInfo shippingInfo;

	public Order(List<OrderLine> orderLines, ShippingInfo shippingInfo) 	{
		setOrderLines(orderLines);
		setShippingInfo(shippingInfo);
	}

	private void setShippingInfo(ShippingInfo shippingInfo) {
		if (shippingInfo == null)
			throw new IllegalArgumentException("no ShippingInfo");
			this.shippingInfo = shippingInfo;
	}
}

```

→ 배송지 정보필수라는 도메인 규칙을 구현

---

Entity

- 식별자를 가진다  e.g 주문의 식별자는 각 주문마다 다른 “주무번호”이다.
- 식별자 생성 방식
    - 특정 규칙에 따라 생성
    - UUID or Nano ID같은 고유 식별자 생성기 사용
    - UUID uuid = java.util.UUID.randomUUID();
    - https://zelark.github.io/nano-id-cc/
    - 값을 직접 입력
    - 일련번호 사용 (시퀀스나 DB의 자동 증가 컬럼 사용)

Value

- 개념적으로 완전한 하나를 표현할 때 사용
    - e.g → reveiverName , receiverPhoneNumber 로 나누지말고

```java
public class Receiver {
private String name;
private String phoneNumber;
	public Receiver(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
    }
    public String getName() {
		return name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
}

```

→ 이렇게 하나의 Value 타입으로 받자

- 불변 객체로 생성하는게 좋다.
    - 불변 객체는 참조 투명성과 스레드에 안전한 특징을 가지고 있다.
    - [https://ko.wikipedia.org/wiki/불변객체](https://ko.wikipedia.org/wiki/%EB%B6%88%EB%B3%80%EA%B0%9D%EC%B2%B4)
- 두 밸류 객체를 비교할 때는 모든 속성이 같은지 비교한다

```java
public clas Receiver {
	private String name;
	private String phoneNumber;

	public boolean equals(Object other) {
		if (other == null) return false;
		if (this == other) return true;
		if (! (other instanceof Receiver) ) return false;
		Receiver that = (Receiver)other;
		return this.name.equals(that.name) &&
		this.phoneNumber.equals(that.phoneNumber)
	}
}

```

- set method는 도메인의 핵심 개념이나 의도를 코드에서 사라지게 한다.
- 불변 밸류 타입을 사용하면 자연스럽게 밸류 타입에는 set method를 구현하지 않는다

---

도메인 모델은 도메인의 핵심 로직을 구현한다.

계층 구조는 그 특성상 상위 계층에서 하위 계층으로의 의존만 존재한다.

→ application & domain 계층은 복잡도에 따라 분리하기도하고 합치기도 한다

application & domain 둘다 infrastructure 영역에 의존할 수 있다

하지만 infrastructure에 의존하면 ‘테스트 어려움’과 ‘기능 확장의 어려움’이라는 두 가지 문제가 발생한다.

고수준 모듈은 저수준 모듈을 사용해서 구현 저수준 모듈은 하위 기능을 실제로 구현한 것들

DIP → 저수준 모듈이 고수준 모듈에 의존하도록 바꾼다 → 추상화한 인터페이스가 핵심

- DIP는 시스템의 결합도를 낮추고 유연성을 높인다.
- 구현 교체가 용이해져 테스트와 유지보수가 쉬워진다.
- 고수준 모듈이 저수준 모듈의 구현 변경에 영향받지 않게 됩니다.

```java
public class CalculateDiscountService {
	private RuleDiscounter ruleDiscounter;

	public CalculateDiscountService(RuleDiscounter ruleDiscounter) {
		this.ruleDiscounter = ruleDiscounter;
	}

	public Money claculateDiscount(List<OrderLine> orderLines, String customerId) {
		Customer customer = findCustomer(customerId);
		return ruleDiscounter.applyRules(customer, orderLines);
	}
}

```

이렇게 된다면, 고수준 모듈은 더 이상 저수준 모듈에 의존하지 않고 구현을 추상화한 인터페이스에 의존한다.

(RuleDiscounter Interface)

구현 기술을 변경하더라도 CalculateDiscountService를 수정할 필요가 없다.

- infrastructure 영역은 구현 기술을 다루는 저수준 모듈이다.
- application & domain 영역은 고수준 모듈이다.