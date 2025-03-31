`컨테이너 Orchestration`

- 컨테이너를 자동으로 관리할 수 있는 소프트웨어 기술
- 대표적으로 Docker swarm 및 Kubernetes가 대표적인 오픈 소스 오케스트레이션 도구

`Kubernetes orchestrations`

- 컨테이너화된 애플리케이션을 위한 오케스트레이션 플랫폼
- 현재 상태를 계속 추적하며 워크로드 관리 및 구성을 자동화 할 수 있다

`Kubernetes orchestrations 내장기술`

- Service discovery
    - DNS를 통해 각 Pod에 주소를 할당
    - 트래픽이 높으면 여러 Pod를 포함하는 서비스 전체에 자동으로 로드밸런싱 수행
        - 라벨로 연결 (로드밸런싱 기술)
- Automated rollouts and rollbacks
    - 새 Pod를 출시하고 기존 Pod와 교환 (rollouts)
    - 실패된 배포는 변경 사항을 되돌릴 수 있음 (rollbacks)
- Self-healing
    - Pod의 상태 확인을 수행하고 실패한 컨테이너를 다시 시작
    - 상태 확인이 될 때까지 해당 Pod에 대한 연결 허용을 안한다
- Automatic Bin-packing
    - 구성된 CPU 및 RAM 요구사항에 따라 컨테이너를 효율적으로 할당하여 리소스 활용도 최적화
- Storage orchestration
    - 로컬 및 네트워크 스토리지, 퍼블릭 클라우드 제공업체(CSP) 등 다양한 스토리지 시스템을 K8s를 사용하여 Mount
- Secret and configuration management
    - 컨테이너 이미지를 재구성하지 않고도 내부 구성 및 기밀 정보를 안전하게 저장하고 업데이트 가능하도록 지원

`Kubernetes Series`

Kubernetes Architecture

- cluster → controller plane + worker node

Kubernetes Components

- master - worker model 구조
- master - 워커 노드들을 관리

Control Plane (컨테이너의 오케스트레이션을 담당하는 클러스터의 상태 유지)

kube-apiserver - Kubernetes API를 노출하는 컴포넌트 kubectl로 부터 리소스를 조작하라는 지시 받음

etcd - 고가용성을 갖춘 분산 key-value 스토어, 백킹 스토어로 사용됨

kubel-scheduler - 노드를 모니터링하고 컨테이너를 배치할 적절한 노드를 선택

kubel-controller-manager - 리소스를 제어하는 컨트롤러를 실행

→ Kubernetes 클러스터의 상태가 미리 정의된 원하는 상태desired state 와 일치하는지 확인합니다.

---

Kubernetes Cluster Init

- 초기화 작업이라고해서 쿠버네티스 클러스터를 엮는 작업
- 컨트롤 플랜 역할을 하는 마스터 노드에 쿠버네티스 핵심인 친구들을 배포해주고
- 컨트롤 플랜이 만들어지면 제공되는 조인키로 node1, node2 를 가져간다.

Pod Network CIDR ( /12 → 100만개 )

- 클러스터가 설정이되면 POD가 할당되고, 그 때 각 노드의 할당되는 아이피 대역을
  전체 켈리코라는 CNI 통하여 IP를 잡는다
- private ip 를 잡아 . 각 노드들이 연결되는 역할
- sudo kubeadm init —pod-network-cidr=10.96.0.0/12 —apiserver-advertise-address=192.168.56.100


Service CIDR

- 대역을 달리 할당 될 때

Kubectl 자동완성 기능 셋팅

- sudo apt install bash-completion -y
- source <(kubectl completion bash)
- echo “source <(kubectl completion bash)” >> ~/.bashrc
- complete -F __start_kubectl k
- vi .bashrc

kubectl get node

kebectl get po -A

CNI (Container Network Interface)

- CNI는 컨테이너 간의 네트워킹을 제어할 수 있는 Plugin을 만들기 위한 표준
- 다양한 형태의 컨테이너 런타임과 오케스트레이터 사이의 네트워크 계층을 구현하는 방식이
  다양하게 분리되어 각자만의 방식으로 발전하게 되는 것을 방지하고
  공통된 인터페이스를 제공
- K8S는 POD간의 통신을 위해서 CNI를 사용한다.
- 기본 CNI Plugin은 기능이 제한적이라, 3rd-party Plugin을 제공한다

- 각 노드에 존재하는 Container Network의 IP 대역이 동일하여 Pod들이 같은 IP를 할당받을 가능성이 높다.
- Pod의 Ip가 다르게 할당되었다 하더라도 해당 Pod가 어느 노드에 존재하는지 확인 불가
  (자신의 노드에 있는 Pod IP만 식별 가능)
- 따라서 중복되지 않는 IP를 부여해줄 역할을 CNI Plugin 수행
  모든 Worker Node에게 중복되지 않는 Subnet을 부여
- Worker Node에서 실행되는 Pod는 해당 Subnet에 포함된 IP를 제공 받음.

kubectl cluster-info

kubeadm config print init-defaults

---

Kubernetes Node

- Node는 동작 중인 Pod를 유지시키고 Kubernetes Container Runtime 환경을 제공하며, 모든 Node 상에서 동작한다.
- Container를 실행하는 모든 Worker Node에는 Kubelet, Kube-proxy, Container runtime이 실행 됨
    - kubelet은 container 실행 요청을 수신하고 필요한 리소스를 관리하고 로컬 노드에서 이를 감시 역할
    - kube-proxy는 네트워크에 container를 노출하기 위한 네트워킹 연결 관리 규칙을 생성 및 관리

kubectl get node -o wide

- 노드의 다양한 정보를 확장해서 보여준다.

kubectl describe nodes k8s-node1

- 노드의 상세 정보

Node 확장

cat /etc/hosts

sudo systemctl status containered.service

free

sudo swapoff -a

ssh student@k8s-master

exit

cat /proc/sys/net/ipv4/ip_forward == 1

**node join을 위한 token 확인 및 재생성**

[master]

1. kubeadm token list
2. kubeadm token create —ttl 0 —print-join-command
3. kubeadm token create —print-join-command

[node-3]

sudo kubeadm join {host} —token y2ocme.k38xr8a8v8a……

kubectl get no

kubectl get po -A

---

**`Amazon EKS 환경 구성`**

`Amazon EKS`

- AWS 클라우드에서 Kubernetes를 실행하는 데 사용되는 관리형 Kubernetes 서비스
- 컨테이너 스케줄링, 애플리케이션 가용성 관리, 클러스터 데이터 저장 및 주요 작업을 담당하는
  Kubernetes 컨트롤 플레인 노드의 가용성과 확장성을 관리
    - Control Plane → AWS에서 관리
- AWS 네트워킹 . 및보안 서비스와의 통합 뿐만 아니라 AWS 인프라의 모든 성능, 규모, 신뢰성 및 가용성을 활용할 수 있다.

`기본 구성`

`EKS Base EC2`

`kubectl 설치`

- curl -L0 https://d1.k8s.io/release/v1.27.0/bin/linux/amd64/kubectl → kubectl 다운로드
- sudo mv kubectl /usr/local/bin/kubectl
- sudo chmod +x /usr/local/bin/kubectl
- kubectl version -o yaml → 버전확인 (gitVersion)

→ EKS 버전과 kubectl 버전이 같아야한다.

`eksctl cLI 설치`

eksctl은 Amazon EKS에서 Kuberntes cluster를 만들고 관리하는 한 가지. ㅏㅇ법.

- curl —location “httpS://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz” | tar xz -C /tmp
- sudo mv -v /tmp/eksctl /usr/local/bin
- eksctl version
- sudo yum -y install jq

`aws configure 인증`

aws cli 도구 설치

`aws cluster 생성`

`CloudFormation`

현재 진행되고 있는 이벤트 정보를 확인 할 수 있다.

`구동 설치 확인`

kubectl run myweb —image=nginx:1.25.1-alpine

kubectl get po -o wide

---

**`Kubenetes 관리도구`**

Observability

내부 시스템에 대한 이해를 근거로 발생 가능한 이벤트를 예측한다.

시스템에서 외부로 출력되는 값 만을 사용한다.

- 문재 해결 속도 향상
- 전체 시스템 이해도 증가
- 대규모 시스템 관리 가능
- 문제 예방 및 최적화

```java
1. Kubernetes Dashboard
2. Prometheus & Grafana
3. ELK stack
4. Kubewatch
5. Jaeger
6. OpenTelemetry
. . .
```

— Pod 속에는 컨테이너가 포함되어 있다

`Kubernetes Dashboard`

[github.com/kubernetes/dashboard/releases](http://github.com/kubernetes/dashboard/releases)

1. kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml

1. kubectl get po -A
2. 대시보드 접속 방법
    1. Proxy port를 이용한 접속
    2. Node port를 이용한 접속
    3. API Server를 이용한 접속

**API Server를 이용한 보안 접속하기**

kubectl get clusterrole cluster-admin



kubectl apply -f dashboard-admin-user.yaml

kubectl apply -f ClusterRoleBinding-admin-user.yml

kubectl -n kubernetes-dashboard get sa

kubectl -n kubernetes-dashboard create token admin-user → 대시보드의 Token 생성

alias dtoken=’kubectl create token -n kubernetes-dashboard admin-user’ → vi ~/.bashrc

인증서 생성

grep ‘client-certificate-date’ ~/.kube/config | head -n 1 | awk ‘{print $2}’ | base64 -d >> kubecfg.crt

grep ‘client-key-data’ ~/.kube/config | head -n 1 | awk ‘{print $2}’ | base64 -d >> kubecfg.key

생성한 키 기반으로 p12 인증서 파일 생성

openssl pkcs12 -export -clcerts -inkey kubecfg.key -in kubecfg.crt -out kubecfg.p12 -name “kubernetes-admin”

Enter Export Password:

Verifying - Enter Export Password:

sudo cp /etc/kubernetes/pki/ca.crt ./

→ winSCP를 이용하여 dashboard_rbac 디렉터리를 윈도우로 이동

------
------

`Prometheus & Grafana`

**Prometheus**

**PUSH 방식으로써, 각각의 노드에에 데몬셋 구조(pod)로 exporter를 구성하고 메트릭을 전달**

- key-value 형태로 식별되는 TSDB 사용
- PromQL 쿼리 언어를 통해 조회 및 분석 가능
- Alert 기능을 통해 알림 기능 제공
- 자체 시각화 기능도 있지만, Grafana에 Metric을 연동시켜 시각화

**Grafana (Visualization)**

- 시계열 데이터베이스
- 클라우드 모니터링 지원

연동

git clone https://github.com/brayanlee/k8s-prometheus.git
cd k8s-prometheus
tree

![image.png](attachment:cdc9b8e9-34c8-4c78-9390-6b9642d6c139:image.png)

- 프로메테우스 접속 시 Warning 발생 시 시간을 맞춰줘야한다.
- Metrics 종류는 3000개 정도되고, 매트릭을 Execute 하여 확인할 수 있다.

Grafana

- DATA SOURCE → Prometheus Clone → Connection & Auth 설정 → Save
- 메뉴 → Dashboards → Import로 Dashboard 가져올 수도 있다
- Dashboard ID 가져오기 → grafana 오픈소스에서 확인이 가능하다.

---

`Kubeshark`

- Wireshark의 Kubernetes 버전
- Kubernetes의 내부 네트워크에 대한 실시간 프로토콜 수준 가시성 제공
- 컨테이너, Pod, Node 및 클러스터에 들어오고 나가는 모든 트래픽과 페이로드를 캡처하고 모니터링하는
  Kubernetes용 API 트래픽 분석기

- eBPF를 통해 구현, 커널 공간과 유저 공간의 모니터링을 통해 인사이트 제공
- kprobe를 사용해 TCP 커넥션에 대한 Source, Destination의 IP Port를 저장

Kubeshark Install

- sh <(curl -Ls https://kubeshark.co/install)
- ks tap → 현재 클러스터의 내용물 확인 후 설치가 진행
  - Added: pod=kubeshark-hub
  - Added: pod=kubeshark-front

Kubeshark Service Map

- 연동되는 서비스를 확인할 수 있다.

Kubershark 구성

- Kubeshark Worker
  - Daemonset으로 생성되고 네트워크 및 시스템 관련 권한 등 노드에서 발생하는 모든 패킷 모니터링을 위해 높은 수준의 권한이 위임
  - 데이터를 수집, 가공하여 Kubeshark Hub로 보내는 역할
- Kubeshark Hub
  - Kubeshark Worker로부터 전달받은 여러 패킷 정보를 Websocket으로 전달해 브라우저에서 유저가 데이터를 확인 가능
- Kubeshark Fronted
  - 웹 브라우저를 통해 Kubeshark Hub와 통신하며 데이터를 검색, 탐색 가능
- Kubeshark CLI
  - Kubeshark를 설치, 삭제하거나 `kubeshark tap이라는` 명령어를 통해 모니터링을 시작

ks tap -n default “(calico*|myweb*)” 처럼 자원을 많이 사용하기때문에 대상을 정확히 지정하여 Trace 하는 것을 권장

---

`Portainer`

웹 기반의 컨테이너 관리 도구

pv 를 우선 설치해야한다.

```java
vi portainer-pv.yaml → pv 먼저 만들고

kubectl apply -f portainer-pv.yaml

kubectl apply -n portainer -f https://raw.gihubusercontent.com/portainer/k8s/master/deploy/manifests/portainer/portainer.yaml

kubectl -n portainer get pv,pvc
kubectl -n portainer get po,svc -o wide

# kubectl -n portainer rollout restart deployment portainer -> 로그인창이 안뜬다면
```

---

`k9s`

- Kubernetes 클러스터를 터미널에서 사용하기 위한 오픈소스 CLI 도구
- 터미널 기반의 UI를 통해 kubectl 명령어를 입력하지 않아도 직관적으로 작업 가능
- UI를 통해 리소스를 제어한다.

`k9s install`

```java
wget https://github.com/derailed/k9s/releases/download/v0.26.7/k9s_Linux_x86_64.tar.gz
tar zxvf k9s_Linux_...tar.gz
sudo mv k9s /usr/local/bin/k9s
k9s info 
k9s version
```

---

**`Kubernetes 구조 이해`**

`Kubernetes architecture`

- Kubernetes architecture는 클러스터 전반의 서비스 검색을 위해 제공하기 위한 오픈 소스 플랫폼
  - Kubernetes 클러스터에는 하나 이상의 control plane과 하나 이상의 컴퓨팅 Node가 있다.
  - control plane은 전체 클러스터를 중앙 관리하고, API 트래픽을 통해 각 Node와 통신한다
    사용자가 원하는 상태 제공을 위해 적합한 컴퓨팅 Node에 object 스케줄링 (`배포,할당`)
  - worker node는 controle plane과 통신하는 kubelet daemon 과 함께 Docker, Containerd, CRI-O와 같은 컨테이너 런타임을 실행
  - 각 Node는 온프레미스 서버, 클라우드 기반 가상 머신 등으로 구성

![image.png](attachment:bbee5b30-4cc1-4e8f-985f-23ada2511024:image.png)

→ pod apply 요청 시 kubernetes architecture의 구성 과 흐름 확인하기. (4-01)

```
kubectl run <pod-name> --image=<imgae-name> ..
1. 이 요청은 API 서버에 의해 검증
2. API 서버는 이 요청을 control plane의 Scheduler 로 전달.
3. API 서버는 etcd 저장소와 상호 작용할 수 있는 유일한 구성 요소이므로 Scheduler는 API 서버에 클러스터 관련 정보를 요청
4. API 서버는 Scheduler 가 요청한 정보를 etcd 저장소에서 데이터를 읽어 제공
5. 정보를 받은 Scheduler 는 해당 정보를 기반으로 지정된 노드에 Pod를 할당하고 이 메시지를 API 서버에 전달.
5-1. 계산은 Filter 와 Score 를 통해서 점수가 가장 높은 노드를 전달
6. API 서버로부터 요청을 받은 노드의 kubelet은 CRI 를 통해 컨테이너 런타임과 상호 작용하여 노드에서 Pod가 생성, 실행됨.
7. Pod가 실행되는 동안 Controller Manager 는 원하는 클러스터 상태(Desired Sate)가 Kubernetes 클러스터의 실제 상태와 일치하는지 지속적으로 확인
```

- pstree → containerd , containerd-shim 이 연결되어 컨테이너 작업들이 진행.


`Kubernetes Cluster`

- 컨테이너화된 애플리케이션을 실행하는 . 데사용되는 물리적 및 가상 Node 그룹
- k8s object의 성능과 안전성을 보장할 수 있는 시스템을 구축하기 위해 모든 Node를 그룹화하는 것을 클러스터라 한다.

`Kubernetes Node`

- Node는 Kubernetes 클러스터에 소속된 **`단일 서버 → 노드라는것도 하나의** Object**이다**`
  - Kubernetes 직접적으로 서버의 영역을 관리할 수 있도록 추상화 시켜놓은게 Node이고 노드 안에서 Pod를 호스팅.
  - 실제 workload 인 Pod가 실행되는 기본 단위, 즉 Pod 호스팅 → 하나의 Pod는 여러개의 컨테이너를 보유할 수 있다 (설계 패턴이 있다)
  - Pod 내부에서는 우리가 필요로 하는 애플리케이션 컨테이너를 실행,
  - 컨테이너 내에서 독립적인 애플리케이션을 실행
- Control Plane 은 Worker node 와 클러스터 내 Pod를 관리하고, Worker node는
  애플리케이션의 구성요소인 Pod를 호스트한다. (Data Plane: 작업이 실행되는 영역)
- 운영 환경에서는 일반적으로 Controle Plane 이 여러 서버에 걸쳐 관리를 수행하고,
  클러스터는 일반적으로 여러 Node를 실행하므로 `내결함성과 고가용성`이 제공된다.

- Node는 “kube-controller-manager” 에 의해 5초 간격으로 노드 상태를 체크하여 API 서버에 보고
  (node controller가 따로있기 때문에)
  - Node가 등록될 때 Node에 CIDR Block 할당
  - 내부 Node 정보를 최신 상태로 유지, Cloud provider와 연동 시 사용
  - Node 상태 모니터링 (—node-monitor-period=5)
- Node가 사용 중인 리소스 용량에 대한 정보 추적하여 API 서버에 보고 됨 (by kubelet 역할)
  이 정보는 Scheduler가 모든 Node의 모든 Pod에 충분한 지원이 있는지 확인할 때 사용

`Kubernetes Node 가용성 유지`

- kubelet
- controller-manager

![image.png](attachment:c6a2f919-ef91-4cac-af4e-85cce4c38459:image.png)

`Kubernetes worker node 구성 요소 (Pod를 예약하고 관리하기 위한 도구)`

- kubelet
  - Control plane 및 worker node 모두에 존재
  - Pod 실행(CRUD)을 위해 control plane의 API 서버와 통신 수행
  - probe 기능을 이용하여 (liveliness, readiness ..) 처리
  - **kubelet architecture**

    ![image.png](attachment:26971de4-bf94-4a04-b81d-089662944593:image.png)

- kube-proxy
  - pod를 외부에 노출 시키기 위함 → service object를 이용
  - 모든 노드에 Daemon Set으로 지원
  - **API 서버와 통신, 서비스와 해당 PodIP, Port에 대한 세부 정보를 가져옴**
  - IPTables 모드를 사용 → NAT를 따로 안해도됨 → 로드밸런싱 처리도 가능함
- container runtime
  - OCI는 컨테이너 형식 및 런타임에 대한 표준 집함
  - **container runtime architecture**

    ![image.png](attachment:74088150-61ec-40ef-b5d7-a8a5a8633dbd:image.png)

- CNI, Metrics Server

`Kubernetes 주요 구성요소`

- kube-apiserver
- etcd
  - kubectl exec -it -n kube-system etcd-k8s-msater -sh
  - snapsho {save/restore} /var/lib/etcd/snapshot.db → 장애
- kube-scheduler
  - kubenetes의 파드를 예약하는,
  - API서버로부터 Pod 생성 이벤트를 수신하여 배포 시 우선순위를 정해서 할당
    - Scheduling context
      - Scheduling cycle worker node 선택
      - Binding cycle → 변경 사항 적용
  - 생성에 가장 적합한 노드를 선택한다.
- kube-controller-manager
  - 무한 제어 루프를 실행하는 프로그램으로 모든 Kubernetes 컨트롤러를 관리하는 구성요소
  - 지속적으로 실행하면서 관리 및 감시
- kube-cloud-controller-manager

`Kubernetes CNI (calico)`

Container Network Interface

- CNI는 컨테이너(Pod)간의 네트워킹을 제어할 수 있는 플러그인 기반 Network architecture
- kubenet이라는 기본적인 CNI가 있지만 기능이 매우 제한적임
- Kubernetes cluster 환경에서는 필요하다
  - 중복되지 않는 IP를 부여하면서
  - 각 노드에 있는 Pod들과 통신하기 위함
  - 각 노드별로 subnet(CIDR) 부여 → Block이라고 표현함
- 생성 시 IP를 할당하고 삭제 시 회수를 하여 재사용가능하게 된다.
- 컨테이너 네트워크는 Bridge Interface (2계층) 사용한다.

kubectl -n kube-system get daemonset

kubectl get pod -n kube-system -o wide | grep calico

kubectl -n kube-system describe pod {pod}

---
deployment replica 를 2로 구성하면

사용자자가 설정한 2개의 pod 스케일을 항상 맞춰줘야하기 때문에

pod 하나가 죽더라도 pod를 자동으로 2개로 다시 맞춰준다. ( 롤링업데이트 등도)

`Pod`

- 하나 이상의 container를 포함
- pod 단위로 배포
  - runtime container → application 수행
  - init container → 기동 시점에 처리하고 종료
  - sidecar container → 보조 역할 (로그수집 등)
- 내부 매커니즘
  - Pause container를 통해 HostOS의 namespace를 공유함(커널기술)
  - Pod를 만들면 Pause Container가 먼저 들어오고 안쪽의 container에 커널기술을 공유해준다.
    Pod가 컨테이너의 Host가 된다.
  - IPC, Network, PID, File System 등이 namespace를 통해 공유
  - Pod 내 컨테이너들은 [localhost](http://localhost) 처럼 통신하게 된다.

- Pause container
  - pause container의 namespace를 pod내부의 모든 container에게 공유하는 역할
    namespace → 격리기술
  - $ ps -ef | grep pause
  - $ pstree
- 멀티컨테이너는 Pod 속 컨테이너들은 이더넷을 공유받아서 각은 IP로 사용

$ strace kubectl get pod

→ kubectl debug

`create pod`

```yaml
apiVersion: v1
kind: Pod
metadata:
	name: myweb1
	labels:
		app: myweb1
spec:
	containers:
	- name: nginx-container
		image: nginx:1.25.3-alpine
		ports:
		- containerPort: 80
```

$ kubectl apply -f myweb1.yaml

$ kubectl get po -o wide | grep myweb1

`create pod service`

```yaml
apiVersion: v1
kind: service
metadata:
	name: myweb-svc
spec:
	selector:
		app: myweb1
	ports:
		- port: 8001
			targetPort: 80
	externalIPs:
		- 192.168.56.103 (pod 의 노드 ip)
```

$ apply -f myweb1-svc.yaml

$ get po,svc -o wide | grep myweb

$ kubectl logs ${pod-name}

`create mysql pod`

```yaml
apiVersion: v1
kind: Pod
metadata:
	name: mysql57-pod
	labels:
		type: mysql57
spec:
	containers:
	- name: mysql57-container
		image: mysql:5.7
		ports:
		- containerPort: 3306
		env:
		- name: MYSQL_ROOT_PASSWORD
			value: "k8spass#"
```

$ kubectl exec -it mysql57-pod — bash

$ mysql -uroot -p

`create mysql service pod`

```yaml
apiVersion: v1
kind: Service
metadata:
	name: mysql-svc
spec:
	selector:
		type: mysql57
	ports:
	- port: 13306
		targetPort: 3306
	externalIPs:
	- 192.168.56.103
	
```

$ kubectl apply -f mysql-svc.yaml

`Pod to Pod network`

- 모든 Pod는 Ip 주소(eth0) 와 network namespace가 있고, 연결할 . 수있는 가상 이더넷 연결이 있어서 가능

`Node to Node network`

- 클러스터 대부분의 경우엔 Node에 할당된 IP 주소가 포함된 Routing Table 가지고 있음

![image.png](attachment:3c86e9d1-87e1-43f1-9bd7-f1d010c76b2c:image.png)

pod → eh0 → calie → vRouter(calico) → node eth0 → routing → routing → eth0 → pod

`Pod 삭제`

- SIGTERM 보낸 . 후일정 시간동안 graceful shutdown이  되지않는다면 강제 SIGKILL을 보내서 pod를 종료 시킴
  - 이 대기 기간은 termination grace period seconds 으로 설정(default = 30ms)

$ kubectl delete pod myweb —grace-period=0 —force  → 즉시 삭제

Pod가 실행되는 동안 오류가 있다면 kubelet은 오류 처리를 위해 restart 수행

실행 중인 pod는 명세와 실제 상태를 모두 보유

`Pod lifecycle`

![image.png](attachment:e3246251-5833-4d40-b2c1-aafa79a6eaa0:image.png)

`Pod Conditions`

![image.png](attachment:6cf36b62-1730-4e56-864e-e498806b49a4:image.png)

initial container

- Pod restartPolicy : Never 설정하면 재시도 안한다.
- 원격지 소스로 부터 최신 구성 파일을 가져올때
- 데이터 베이스를 초기화 해야할때

sidecar container

---

`Label`

복잡하고, 다양한 Pod를 효율적인 집합으로 다루기 위한 방법으로 Label 사용

- 객체를 식별하고 그룹화
- Label로 선택된 리소스만 Service로 연결하여 부하분산을 가능하게 해준다.
- 특정 리소스만 배포하고 업데이트할 수 있다.

`Annotations` → API를 통해 추가적인 데이터를 저장하는 방법

$ get pod —show-labels | grep {pod}

$ get pod —selector=key=value

`부하분산 서비스 작성`

```yaml
apiVersion: v1
kind: Pod
metadata:
	name: label-pod-a
	namespace: infra-team-ns1
	labels:
		type: infra1
spec:
	containers:
	- name: pod-a-container
		image: dbgurum/k8s-lab:initial
		
---

apiVersion: v1
kind: Pod
metadata:
	name: label-pod-a
	namespace: infra-team-ns1
	labels:
		type: infra1
spec:
	containers:
	- name: pod-a-container
		image: dbgurum/k8s-lab:initial
		
---

apiVersion: v1
kind: Pod
metadata:
	name: label-pod-a
	namespace: infra-team-ns1
	labels:
		type: infra1
spec:
	containers:
	- name: pod-a-container
		image: dbgurum/k8s-lab:initial
		
---

apiVersion: v1
kind: Service
metadata:
	name: infra-svc1
	namespace: infra-team-ns1
spec:
	selector:
		type: infra1
	ports:
	- port: 7777

```

$ kubectl -n infra-team-nsl describe svc infra-svc

---

`Probes`

Pod Probe Service

kubelet이 주기적으로 수행 Container 내 Handler를 호출

- ExecAction
- TCPSocketAction
- HTTPGetAction

`Pod 진단(Probe) 서비스 종류`

→ 모두 Pod에 있는 컨테이너의 안정성과 가용성을 보장하는 데 사용되는 메커니즘

liveness Probe: container가 동작 중인지 여부를 진단한다. (healthcheck)

readiness Probe: container가 요청을 처리할 준비가 되었는지 여부를 진단한다. (traffic을 반을준비)

```yaml
spec:
	conatiners:
	- name: 
		image:
		prots:
		- containerPort: 8080
		readinessProbe:
			httpGet:
				path: /healthcheck
				port: 8080
			initialDelaySeconds: 15
			periodSeconds: 10
		...
		readinessProbe:
			tcpSocket:
				port: 8080
			initialDelaySeconds: 15
			periodSeconds: 10
		...
		readinessProbe:
			exec:
				command:
				- /bin/sh
				- -c
				- test-shell.sh
			initialDelaySeconds: 15
			periodSeconds: 10		
```

startup Probe: container 내의 애플리케이션이 시작되었는지를 진단한다.

![image.png](attachment:a0968fc5-a32f-4767-be0d-1a4d947aed55:image.png)

---

`Pod Networking을 위한 Service 생성과 관리`

`Kubernetes Service`

- Service는 네트워크 추상화 object로 생성된 pod에 동적 접근이 가능하고, 이를통해 애플리케이션을 클러스터 내의 네트워크 서비스로 노출할 수 있다.
- Service는 Ip 또는 DNS 이름을 통해 특정 포트에 직접 액세스할 수 있도록 Pod를 논리적으로 그룹화할 수 있다.
- Service를 등록하면 자체적인 DNS가 부여된다.

`Kubernetes Service 필요 이유`

→ 특정 Pod로 직접 들어가는게 아니고 단일 진입점 역할을 한다.

→ Service selector에 app: backend로 설정하면 , Pod에 labels app: backend 라면 트래픽을 전달한다.

![image.png](attachment:72b87109-17c7-4b4e-b0f0-af2d59297e85:image.png)

$ kubectl describe svc app-svc

`Service는 어떻게 연결되나?`

- service는 가상IP와 port를 가지고 생성
- kube-proxy는 이 Service의 가상IP를 구현하고 port와 함께 관리하는 역할
- Service object는 Pod를 노출하면 kube-proxy는 Service object와 그룹화된 Pod로 트래픽을 보내는 네트워크 규칙(Rules) 생성
- Iptables mode
- IPVS
  - RR
  - LC
  - DH
  - SH
  - SED

$ kubectl logs -f -n kube-system kube-proxy-h16s

`kube-proxy는 apiserver를 감시하여 iptables 정보를 업데이트 함`

Service type

- Cluster Ip  → Service를 클러스터 내부 가상 IP를 구현하여 노출
- Node Port → 고정 포트로 각 Node의 Ip에 Service를 외부에 노출
- Load Balancer → 클라우드 공급자의 로드 밸런서를 사용하여 Service를 외부에 노출
- External Name → 일반적인 selector 연결이 아닌 외부 서비스에 대한 DNS name을 제공해 내부 파드가 외부의 특정 도메인에 접근하게 하기 위한 리소스

- Ingress → Ingress를 사용하여 Service를 외부에 노출, Ingress는 서비스 유형은 아니지만, 클러스터의 진입점 역할 수행

---

`Ingress`

- L7 HTTP 및 HTTPS 경로를 서비스에 노출하고 트래픽 규칙을 정의
- 수신 규칙 및 요청을 이행하는 Ingress Controller를 사용 (AWS Load Balancer Controller)
- Ingress object를 사용하면 사용하는 로드 밸런서의 수를 줄일 수 있다.
  - 기존의 서비스당 로드밸런서 하나에서 여러 Ingress당 로드 밸랜서 하나로 전환하고 여러 서비스로 라우팅할 수 있다.
  - 트래픽은 경로 기반 라우팅을 사용하여 적절한 서비스로 라우팅이 가능

`Ingress 동작과정`

인터넷을 타고 트래픽이 진입 → Ingress Controller를 거치고 → Ingress로 들어오고 → Routing Rule을 통해

→ 연결되어 있는 경로를 통해 서비스를 분리함 → 서비스는 파드에 연결되어있고 → 파드엔 컨테이너 즉 애플리케이션이 있다