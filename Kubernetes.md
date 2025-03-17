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