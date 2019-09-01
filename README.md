# Databuter

> Databuter는 제 7회 D2 CAMPUS FEST mini의 최우수상 수상작입니다.

Databuter는 분산 인 메모리 키 - 값 스토어 Databute의 서버 구현체입니다.

Databuter의 목표는 메모리에 키 - 값 엔트리를 저장하여, 기존의 데이터베이스 시스템 보다 더 빨리 
엔트리를 조회할 수 있도록 하는 것입니다. 또한, 하나 이상의 Databuter 노드가 클러스터를 형성한 뒤 
엔트리를 분산시켜 저장하여 용량 및 처리량을 확장하거나, 축소할 수 있도록 하는 것입니다.

사용자는 엔트리를 조회 (GET), 저장 (SET, UPDATE) 및 삭제 (DELETE) 하거나 유효 기간을 설정 
(EXPIRE) 할 수 있습니다. 엔트리는 숫자 (Integer, Long), 문자열 (String), 리스트 (List), 셋 
(Set) 및 딕셔너리 (Map) 타입을 지원합니다.

엔트리들은 버킷이라 하는 논리적 엔트리 블록에 저장됩니다. Databuter에는 다수의 논리적 버킷이 존재하며, 
키를 총 버킷 수에 대해 일관적 해싱을 취한 값과 같은 인자를 가지는 버킷에 엔트리를 저장합니다.

버킷들은 Databuter 클러스터의 노드들이 유지합니다. 한 버킷은 서로 다른 두 노드가 똑같이 유지하고 있어 
한 노드에 장애가 발생하더라도 즉시 극복할 수 있습니다.

## 사용법

```
gradlew jar
java -jar build/libs/databuter-{version}.jar
```

## 라이센스

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