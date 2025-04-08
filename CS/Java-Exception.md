## 에러와 예외

### 프로그램의 오류 종리
- Compile Error: 컴파일 시 발생하는 에러
  - 보통 문법 구문 오류가 대표적인 원인
  - 심각하게 볼 에러는 아니다
- Runtime Error: 실행시에 발생하는 에러
  - 개발 시 설계 미숙, 외부적인 요인으로 발생함
- Logical Error: 논리적 에러
  - 버그
  - 서비스 이용에 지장


### Error
> 시스템에 비정상적인 상황이 발생했을 경우 발생.\
> 메모리 부족, 스택오버플로우 같이 발생하면 복구할 수 없는 오류이고 예측 불가능한 오류 \
> JVM 실행에 문제가 생긴 것.


### 예외
> 프로그램 실행 중에 개발자의 실수로 예기치 않은 상황이 발생했을 때 \
> ArrayIndexOutOfBoundsException, NullPointException 등 복구 가능한 수준의 오류

### Checked Exception
> Exception의 하위지만 RuntimeException의 하위가 아닌 오류들 \
> try / catch or throw 처리를 해야하는 특징이 있음\
> FileNotFoundException, IOException, SQLException, ClassNotFoundException 등

### Unchecked Exception
> RuntimeException의 하위 클래스들, 에러 처리를 강제하지 않음 \
> 실행 중에 발생할 수 있는 예외를 의미. \
> IllegalArgumentException, NullPointerException 등 