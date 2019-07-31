# databuter

Databuter는 분산 인 메모리 키 - 값 스토어의 서버 구현체이다. Databuter는 하나 이상의 Databuter 노드가 클러스터를 형성하여 클러스터에 참여한 노드들에게 데이터를 분산시켜 저장한다.

## Entry

Databuter는 키 - 값 데이터를 엔트리 객체로 저장한다. 엔트리는 엔트리 정보와 멀티 스레드 구조의 동시성 문제 해결을 위한 락 및 엔트리 연산의 직렬화를 위한 엔트리 연산 큐로 구성되어 있다. 엔트리 정보는 엔트리의 키, 값, 생성 시간, 마지막 변경 시간을 의미한다.

Databuter는 숫자 (Integer, Long), 문자열 (String), 리스트 (List), 셋 (Set) 및 딕셔너리 (Map) 타입에 대한 엔트리를 지원한다.

## Bucket

Databuter 시스템은 엔트리를 키에 따라 분산시켜 저장한다. 버킷은 엔트리를 분산하기 위한 논리적 엔트리 블록으로, Databuter 시스템에는 다수의 논리적 버킷이 존재하며 엔트리는 키를 총 버킷 수에 대해 일관적 해싱을 취한 값 factor와 같은 factor를 가지는 버킷에 엔트리를 저장한다.

Databuter 시스템의 각 노드는 실행 중인 JVM 인스턴스의 가용 메모리에서 자체적으로 사용 할 메모리 크기를 의미하는 변수 `guardMemorySize` 만큼을 뺀 나머지 메모리를 데이터 저장용 메모리로 사용한다. 데이터 저장용 메모리를 버킷 메모리 크기를 의미하는 변수 `bucketMemorySize` 로 나눠 그 수 만큼 버킷을 생성한다.

만약 버킷으로 나누지 않았다면, 서로 다른 사양을 가지는 Databuter 노드들이 공존할 수 없다. 예를 들어, `bucketMemorySize` 값으로 1GB를 가지는 4GB의 데이터 저장용 메모리 크기를 가지는 노드가 실행 중인 환경에서, 8GB의 데이터 저장용 메모리 크기를 가지는 노드가 새로 참여하더라도 고르게 분포되는 데이터 특성 상 8GB를 다 사용하기 전에 4GB의 노드의 메모리가 꽉 차 나머지 4GB를 사용할 수 없게 된다. 그러나 버킷을 적용시킴으로써, 8GB의 메모리를 8개의 버킷으로 나눠 총 12개의 버킷에 데이터가 고르게 분산되므로 메모리 낭비가 발생하지 않는다.

## Cluster

Databuter 클러스터는 클러스터를 관리하기 위한 분산 코디네이터 ZooKeeper와, 본 프로젝트에서 제안하는 Databuter 노드로 구성되어 있다. 클러스터의 정보는 ZooKeeper에 저장되어 있으며, Databuter 노드는 ZooKeeper의 정보를 모니터링하여 클러스터의 변화에 즉시 대응할 수 있도록 하였다. 클러스터의 노드들은 메쉬 네트워크 형태로 구성되도록 설계하여 클러스터에 속한 모든 노드는 다른 노드들에게 요청을 보내기 위한 아웃바운드 세션과, 다른 노드들의 요청을 받기 위한 인바운드 세션을 유지한다.

Databutee는 Databuter 클러스터의 모든 노드와 세션을 유지한다. Databutee는 Databuter 노드들 중 하나에 클러스터 정보를 구독한다. Databuter는 ZooKeeper에서 클러스터 변화를 감지할 때 마다 구독 중인 Databutee 세션에 클러스터 변화를 전달해줘 Databutee 또한 클러스터의 변화에 즉시 대응할 수 있도록 하였다. 이를 통해, 별도의 네임 서버 없이 모든 Databuter 노드가 데이터 서버 뿐만 아니라 네임 서버의 역할도 수행할 수 있도록 설계하였다. Databutee는 데이터가 적재된 Databuter 노드를 직접 계산하여 해당 노드에 데이터를 요청하여 네트워크 홉 수를 줄임으로써 빠른 지연시간을 기대할 수 있다.

클러스터 참여를 원하는 Databuter 노드는, 우선 ZooKeeper에 연결하여 클러스터에 참여한 노드들의 정보를 얻어와 그 정보를 바탕으로 해당 노드에 아웃바운드 세션을 연결한다. 노드와 연결이 수립되면, 원격 노드의 id를 포함하는 handshake 메시지를 원격 노드에 전송한다. Handshake 메시지를 수신한 원격 노드는, 메시지에 포함된 id가 자기 자신의 것과 같은지 검사한다. 만약 id가 다를 경우, 세션을 끊어 ZooKeeper에 등록된 클러스터와 다른 경로를 유지할 수 없도록 한다. 클러스터의 모든 노드에 연결이 완료되면, 자신의 정보를 ZooKeeper에 등록한다. ZooKeeper의 정보를 모니터링하는 다른 노드들도 새로 참여한 노드에 아웃바운드 세션을 연결하여 메쉬 네트워크를 구축한다.

## Usage

```
gradlew jar
java -jar build/libs/databuter-{version}.jar
```

## License

```
MIT License

Copyright (c) 2019 databute

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
