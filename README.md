# 얼굴인식 기반 스마트 도어록
안드로이드스튜디오 코드

### 📢 팀원 소개
|배선영 (팀장) |배가형|양영준|
|------|---|---|
|<div align="center"><img width="200" height="200" src="https://github.com/visAIge/android_studio/assets/87819894/27e2ff02-3bb6-406d-b71f-eecddcfebdd3"/></div>|<div align="center"><img width="200" height="200" src="https://github.com/visAIge/android_studio/assets/87819894/219c2257-46c6-430e-985f-893db864df85"/></div>|<div align="center"><img width="200" height="200" src="https://github.com/visAIge/android_studio/assets/87819894/a110f222-cc14-4cd4-9e94-ad51710c440a"/></div>|
|- 회의록, PPT, 보고서 작성 및 취합<br/>- 안드로이드 애플리케이션 기능 구현<br/>- Jetson Nano QR 코드 수정·보완<br/>- 데이터베이스 설계|- 보고서, PPT 작성<br/>- 얼굴, 신발 인식 AI 구현 및 관련 데이터 수집<br/>- AI 구동 환경 구축 (Jetson Nano, 윈도우)<br/>- 애플리케이션 UI/UX 수정·보완|- 보고서 작성<br/>- 서보모터를 이용한 카메라 각도 조정부 구현<br/>- 디지털 도어록, 아두이노 연동 및 구현|


### 📌 주요 기능
1. 얼굴 인식
- 카메라 각도를 아래를 향하게 하여 발을 먼저 찍은 후 문 앞에 사람이 서 있다는 것이 확인이 되면 각도를 조절하여 얼굴을 인식할 수 있도록 한다.  

2. QR 코드 인식
- 방문 지도 교사, 가족이나 친척 등 외부인 방문 시 QR 코드를 공유하고 해당 QR 코드를 카메라에서 인식하여 올바른 정보인 것이 확인이 되면 도어록 잠금을 해제한다.
- 모든 사용자는 해당 앱에 회원가입을 해야 되고 회원 가입 시 입력한 정보를 이용하여 QR 코드를 생성한다.
- 일정 기간이 지나면 자동으로 삭제되도록 한다.
- QR 코드에 저장되는 데이터는 암호화되며, 카메라에서 인식 후 복호화하여 DB에 저장된 데이터와 비교한다.

3. OTP 코드
- 도어록 사용자가 회원 가입을하면 해당 고유 아이디로 OTP 계정이 생성된다.
- 해당 OTP 계정을 이용하여 OTP 코드를 얻을 수 있으며 키패드 5회 이상 입력에 실패할 경우 도어록을 비활성화하고 앱에 OTP 코드를 입력해야만 도어록을 재활성화할 수 있도록 한다.


### 🔎 결과 
**1. 시스템 흐름**

<img width="600" height="350" src="https://github.com/visAIge/android_studio/assets/87819894/6c9acd74-bdcb-441f-8395-7952c784ba4c"/>
<br/><br/><br/><br/>

**2. 하드웨어**

<img width="500" height="350" src="https://github.com/visAIge/android_studio/assets/87819894/006ed492-763e-45e4-bb0e-e42d0763a27c"/>

|부품|설명|
|------|---|
|Jetson Nano (라즈베리파이에서 변경)|AI 및 QR 인식 구현|
|아두이노 와이파이 ESP8266 WIFI ESP-01|도어록, 파이어베이스 연동|
|소형 서보모터 DM-S0090D 180도|카메라 각도 조절|
|Logitech 스크림캠 스트리밍캠 웹캠 화상캠|얼굴, 신발 및 QR 인식|
|솔리티 웹콤 WRB300 현관문 디지털도어락|외관 잠금 제어 장치|

<br/>

**3. 애플리케이션**

- 로그인
<img width="200" height="400" src="https://github.com/visAIge/android_studio/assets/87819894/a427f19f-b385-4b98-b708-6dda9c4b7646"/>

- 메인화면
<img width="200" height="400" src="https://github.com/visAIge/android_studio/assets/87819894/d6d67945-c7f8-463f-83a6-a949f6950d7f"/>
<img width="200" height="400" src="https://github.com/visAIge/android_studio/assets/87819894/64d5a03a-935d-4037-9d4e-05fc40c3d8a2"/>
<br/><br/>
