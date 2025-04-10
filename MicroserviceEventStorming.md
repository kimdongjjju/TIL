# Microservice 내부 아키텍처 와 EventStorming 설계

### 모놀리스
- 스케일 아웃의 대상은 모놀리스 전체
- 그것만으로 충분히 확장성, 탄력성이 보장이 가능하나 비용 효율적이지 않음


### 마이크로 서비스
- 애플리케이션이 여러개의 조각으로 구성된 서비스
- 서비스는 각기 독립적인 기능을 제공
- 서비스가 사용하는 저장소는 다른 서비스와 완벽히 격리됨
- 따라서 독립적으로 수정 가능하며 별도 배포, 확장 가능
- 하나의 서비스 실패는 전체 실패가 아닌 부분적인 실패를 의미

- Public Interface , 데이터의 캡슐화 
  - 여러 개의 작은 서비스 집합으로 개발하는 접근 방법
  - 각 서비스는 개별 프로세스에서 실행
  - Http 자원 API 같은 가벼운 수단을 사용하여 통신
  - 서비스는 비즈니스 기능 단위로 구성
    - 중앙 집중적인 관리 최소화
  - 각 서비스는 서로 다른 언어, 데이터, 저장 기술 사용 (Polyglot 이라고 함)

### SOA
- Service를 기반으로 구성한거
- MSA는 SOA안에 있다고 봐야한다.
- 다른점은 DB 격리점
  - SOA는 저장소를 하나로 쓰는걸 꺼리지 않았다 이유는, 서비스만 분리함
  - 서비스의 재사용성을 강조하고 저장소는 크게 신경쓰지 않았다
  - 테이블들의 조인을 허용함
  - 테이블을 변경했을 때 각각의 서비스의 영향이 가게된다. (문제점)


### Event Storming : 마이크로서비스 설계를 위한 워크샵 / 이해관계자 모두 참여

#### Domain Event  (Orange)
- 시스템에서 발생한 중요한 이벤트
- 데이터가 아닌 비즈니스 프로세스에 집중
- 과거 형 동사로 표현한다.
- e.g 회원등록 됨 / 로그인 됨

#### Hot Spot (Purple)
- 질문 가정 경고 의견 수렴이 필요한 내용
- 병목 구간, 자동화 필요한 수작업, 도메인 지식이 없는 경우
- 완전히 정의되지 않는 여역, 해결해야 하는 문제

#### Command
- 이벤트를 트리거 하는 명령
- 현재 동사로 표현
- e.g 회원등록이라는 커멘드

#### Actor
- Command를 동작하게 하는 사용자/역할 (명사) 표현
- e.g 회원이 회원등록이라는 커맨드로 회원 등록을 발생시켰다

#### Policy
- 이벤트 발생 시 정책 시나리오

#### External System
- 외부 시스템 또는 프로세스


#### Aggregate
- Domain Event와 Command에 의해 관리되는 데이터
- Domain Event와 Command를 표현하는 키워드
- 명사로 표현
- 커맨드와 이벤트 사이


```
구매 서비스를 이벤트 스토밍한다면
API : 주문항목검새 / 주문아이템주문 / 상품주문취소
데이터 : 주문아이템 / 주문 / 결제
도메인 이벤트 : 주문아이템검색됨 / 주문아이템주문됨 / 결제승인됨 / 결제거부됨 / 구입완료됨 / 재고변경됨 / 배송요청됨 / 상품주문취소됨 / 결제취소됨
핫스팟 : 취소가능시점 확인
외부 서비스연계 : 회원 서비스 / 상품 서비스 / 재고변경 / 배송생성
외부 시스템 : 결제 시스템 / 이메일 시스템
```

---