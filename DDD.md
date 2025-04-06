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


---

도메인 모델의 엔티티와 DB 관계형 모델의 엔티티는 같은 것이 아니다.

- 도메인 모델의 엔티티는 단순히 데이터를 담고 있는 데이터 구조라기보다는 데이터와 함께 기능을 제공하는 객체이다.
- 도메인 관점에서 기능을 구현하고 기능 구현을 캡슐화해서 데이터가 임의로 변경되는 것을 막는다.
- 두 개 이상의 데이터가 개념적으로 하나의 경우 밸류 타입을 이용해서 표혀할 수 있다.
- 엔티티의 밸류 타입 데이터를 변경할 떄는 객체 자체를 완전히 교체한다는 것을 의미

```java
  private void setShippingInfo(ShippingInfo newShippingInfo) {
  	if (newShippingInfo == null) throw new IllegalArgumentException();
  	this.shippingInfo = newShippingInfo;
  }

```

**애그리거트**

애그리거트는 관련 객체를 하나로 묶은 군집이다.

애그리거트는 군집에 속한 객체를 관리하는 루트 엔티티를 갖는다.

- 애그리거트는 도메인 규칙을 하나의 단위로 관리
- 트랜잭션 일관성을 보장하는 경계이다.
- 애그리거트 간 참조는 ID를 통해 이루어져야 한다.
- 한 트랜잭션에서는 하나의 애그리거트만 수정하는 것이 좋다.

루트 엔티티는 애그리거트에 속해 있는 엔티티와 밸류 객체를 이용해서 애그리거트가 구현해야 할 기능을 제공한다.

application 영역은 필요한 도메인 객체를 구하거나 저장할 때 repository를 사용한다.

application 영역은 트랜잭션을 관리하는데, 트랜잭션 처리는 repository 구현 기술의 영향을 받는다.

---

표현영역은 데이터 형식이 올바른지 검사하고, 문제 없다면 응용 서비스에 실행을 위임

응용 서비스가 요구하는 형식으로 변환해서 전달

응용 서비스는 도메인 모델을 이용해서 기능을 구현하고, @Transactional 이용하여 트랜잭션을 처리

모듈 구조는 얼마나 세분화 등 정해진 규칙이 없다.  한 패키지에 너무 몰리는 경우와,

한패키지에 가능하면 10~ 15개 미만의 타입 개수를 유지하자.

---

**애그리거트**

복잡한 모델을 관리하는 기준을 제공한다.

한 애그리거트에 속한 객체는 유사하거나 동일한 라이프 사이클을 갖는다.

애그리거트는 독립된 객체 군이며 각 애그리거트는 자기 자신을 관리할 뿐 다른 애그리거트를 관리하지 않는다.

→ 이런 경계를 설정할 때 기본은 도메인 규칙과 요구사항이다

애그리거트에 속한 구성요소는 대부분 함께 생성하고 함께 제거한다. ( 상품 !== 리뷰 )

애그리거트 루트는 단순히 애그리거트에 속한 객체를 포함하는 것으로 끝나는게 아니라.

애그리거트의 일관성이 깨지지 않도록 하는 것이 핵심이다.

→ 애그리거트 외부에서 애그리거트에 속한 객체를 직접 변경하면 안 된다.

→ 밸로 타입의 내부 상태를 변경하려면 애그리거트 루트를 통해서만 가능하다.

→ 애그리거트에 속하는 모델은 한 패키지에 속하기 때문에 패키지나 protected 범위를 사용하면 애그리거트 외부에서 상태 변경 기능을 실행하는 것을 방지할 수 있다.

---

애그리거트에서 다른 애그리거트를 직접 참조할 때 발생할 수 있는 가장 큰 문제

- 편리함의 오용이다.

```java
public class Order {
	private Orderer orderer;

	public void changeShippingInfo(ShippingInfo newShippingInfo, boolean useNewShippingAddrAsMemberAddr) {
		...
		if (useNewShippingAddrAsMemberAddr). {
			// 한 애그리거트 내부에서 다른 애그리거트에 접근할 수 있으면,
			// 구현이 쉬워진다는 것 때문에 다른 애그리거트의 상태를 변경하는
			// 유혹에 빠지기 쉽다
			orderer.getMember().changeAddress(newShippingInfo.getAddress());
		}
	}
}

```

- 직접 참조 시 성능과 관련된 여러 가지 고민이 필요하다 → Lazy & eager
- 확장 → 도메인별로 시스템을 분리하기 시작할 시 DBMS가 달라지고 등등의 문제

→ 해결방안은 ID를 이용해서 다른 애그리거트를 참조하는 것.

```java
public class ChangeOrderService {
	@Transactional
	public void changeShippingInfo(OrderId id, ShippingInfo newShippingInfo, boolean useNewShippingAddrAsMemberAddr) {
	Order order = orderRepository.findbyId(id);
	if (order == null) throw new OrderNotFoundException();
	order.changeShippingInfo(newShippingInfo);
	if (useNewShippingAsMemberAddr) {
		Member member =memberRepository.findById(order.getOrderer().getMemberId());
	member.changeAddress(newShippingInfo.getAddress());
	}
	}
}

```

---

```java
public class RegisterProductService {
	public ProductId registerNewProduct(NewProductRequest req) {
		Store store = storeRepository.findById(req.getStoreId());
		checkNull(store);
		if (store.isBlocked()) {
			throw new StoreBlockedException();
		}
		ProductId id = productRepository.nextId();
		Product product = new Product(id, store.getId(), ...);
		productRepository.save(product);
		return id;
}

```

→ 중요한 도메인 로직 처리가 응용 서비스에 노출되어있다. → 도메인 기능을 응용 서비스에서 구현하고 있는 것이 문제.

---

애그리거트와 JPA 매핑을 위한 기본 규칙

- 애거리거트 루트는 엔티티이므로 @Entity로 매핑 설정한다.

한 테이블에 엔티티와 밸류 데이터가 같이 있다면

- 밸류는 @Embeddable로 매핑 설정한다.
- 밸류 타입 프로퍼티는 @Embedded로 매핑 설정한다.

```java
@Entity
@Table(name= "purchase_order")
public class Order {
	private Orderer orderer;
}

@Embeddable
public class Orderer {
	@Embedded
	@AttributeOverrides(
		@AttributeOverride(name="id", column= @Column(name="orderer_id"))
	)
	private MemberId memberId;
}

```

- AttributeOverrides @Embeddable 타입에 설정한 컬럼이름과 실제 컬럼 이름이 다르므로 사용

@Embeddable 매핑 타입은 함께 저장되고 삭제되므로 cascade 속성을 추가로 설정하지 않아도 된다.

반면에 애그리거트에 속한 @Entity 타입에 대한 매핑은 cascade 속성을 사용해서 저장과 삭제 시 함께 처리되도

록 설정해야 한다

→ @OneToOne, @OneToMany는 cascade 속성의 기본값이 없으므로 다음 코드처럼 cascade 속성값으로

CascadeType.PERSIST, CascadeType.REMOVE를 설정해야 한다.

---

식별자는 크게 세 가지 방식 중 하나로 생성한다.

- 사용자가 직접 생성
- 도메인 로직으로 생성
- DB를 이용한 일련번호 사용
  - @GeneratedValue 사용
  - @Id로 매핑 한 프로퍼티/필드에 할당하므로 저장 이후에 엔티티의 식별자를 사용할 수 있다.

---

**응용서비스**

실제 사용자가 원하는 기능을 제공하는 것은 응용 영역에 위치한 서비스이다.

```java
@PostMapping("/member/join")
public ModelAndView join(HttpServletRequest request) {
	String email = request.getParameter("email");
	String password = request.getParameter("password");
	// 사용자 요청을 응용 서비스에 맞게 변환
	JoinRequest joinReq = new JoinRequest(email, password);
	// 변환한 객체(데이터)를 이용해서 응용 서비스를 실행
	joinService.join(joinReq);
}

```

→ 응용 영역은 사용자가 웹 브라우저를 사용하는지 REST API를 호출하는지, TCP 소켓을 사용하는지 알 필요가

없다. 단지 기능 실행에 필요한 입력 값을 받고 실행 결과만 리턴하면 될 뿐이다.

도메인 로직은 응용 서비스에서 구현하지 않는다.

-> 코드의 응집성이 떨어진다.

-> 중복 코드가 발생할 수 있다.

응용서비스 구현 방법 중

- 한 응용 서비스 클래스에 도메인의 모든 기능 구현하기.
  - 동일한 로직을 위한 코드 중복을 제거하기 쉽다.
  - 코드 줄 수가 커진다.
  - 코드가 모이기 시작하면 분리하는게 좋다.
- 구분되는 기능별로 응용 서비스 클래스 따로 구현하기
  - 클래스 개수는 많아지지만 코드 품질을 일정 수준 유지할 수 있다.
  - 중복 코드를 구현할 가능성이 있다.

```java
public final class MemberServiceHelper {
	public static Member findExistingMember(MemberRepository repo, String memberId) {
		Member member = memberRepository.findById(memberId);
		if (member = null)
			throw new NoMemberException(memberId);
		return member;
	}
}
// 공통 로직을 제공하는 메서드를 서비스에서 사용
import static com.myshop.member.application.MemberServiceHelper.*;

public class ChangePasswordService {
	private MemberRepository memberRepository;
	public void changePassword(String memberId, String curPw, String newPw) {
		Member member = findExistingMember(memberRepository, memberId);
		member.changePassword(curPw, newPw);
	}
}

```

- 구현 클래스가 여러 개인 경우가 아니라면, 인터페이스를 따로 두지 않는다.

응용 서비스의 파라미터 타입을 결정할 떄 주의할 점은 표현 영역과 관련된 타입을 사용하면 안 된다는 점이다.

```java
@Controller
@RequestMapping("/member/changePassword")
public class MemberPasswordController {
	@PostMapping
	public String submit(HttpServletRequest request) {
		try {
			// 응용 서비스가 표현 영역을 의존하면 안 됨!
			changePasswordService.changePassword(request);
		} catch(NoMemberException ex) {
			//
		}
	}
}

```

→ 응용 서비스만 단독 테스트가 어렵다

→ 의존관계가 발생하게 된다.

→ HttpSession 이나 HttpServletRequest에 대한 처리는 응용서비스에서 처리하면 안된다.

원칙적으로 모든 값에 대한 검증은 응용 서비스에서 처리한다.

---

응용 서비스가 사용자 요청 기능을 실행하는 데 별다른 기여를 하지 못한다면 굳이 서비스를 만들지 않아도 된다.

여러 도메인의 로직이 필요할 대는 한 애그리거트에 억지로 넣기보다는 도메인  서비스를 이용해서 도메인 개념을

명시적으로 드러내면 된다.

```java
public class Order {
	public void calculateAmounts(DiscountCalculationService disCalSvc, MemberGrade grade) {
		Money totalAmounts = getTotalAmounts();
		Money discountAmounts = disCalSvc.calculateDiscountAmounts(this.orderLines, thiscoupons, grade);
		this.paymentAmounts = totalAmounts.minus(discountAmounts);
	}
}

```

트랜잭션 처리와 같은 로직은 응용 로직이므로 도메인 서비스가 아닌 응용 서비스에서 처리해야한다.

**Tip**

특정 기능이 응용 서비스인지 도메인 서비스인지 감을 잡기 어려울 때는 해당 로직이 애그리거트의 상태를

변경하거나 애그리거트의 상태 값을 계산하는지 검사해 보면 된다.

e.g 계좌 이체 로직은 계좌 애그리거트의 상태를 변경 , 결제 금액 로직은 주문 애그리거트의 주문 금액을 계산.

→ 각각 애그리거트를 변경하고 애그리거트의 값을 계산하는 도메인 로직.

도메인 서비스의 구현이 특정 기술에 종속되면 인터페이스와 구현 클래스로 분리한다

→ 특정 구현 기술에 의존하거나 외부 시스템의 API를 실행한다면 도메인 영역의 도메인 서비스는 인터페이스로 추상화해야 한다.