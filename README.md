# Wifi Indoor Position

Wifi 를 통한 실내 위치 추정 어플리케이션

  - 개발 배경
  - 개발 Flow
  - 사용 기술
  - 프로그램 출력 결과


# 개발 배경
 - 4차 산업혁명으로 사물 인터넷 분야가 발전하고 있고 그중에서도 위치 기반 서비스가 급속도로 성장.
  - 흔히 알고 있는 GPS는 위성을 통해 위치 정보를 수신하기 때문에, 실내 환경에서는 사용하기 부적합.

따라서 :
  - 일상생활 속 많이 사용 중인 Wifi를 통해서 사용자 위치 추정 기술 연구를 개발. 


# 개발 Flow

1. MainActivity 
 - 주변 Wifi들을 조회
 - Ssid/Mac/Rssi/Frequency/Distance를 Listview로 표시
 - Listview Item 하나를 누르면 GraphActivity로 이동

2. GraphActivity
 - Wifi의 Rssi와 Distance의 변화를 Graph로 표현

3. InActivity
 - Rssi값이 큰 3개의 Wifi를 스캔
 - 삼변 측량을 통해 구해진 좌표값의 3번의 값을 평균값을 표시
 - 주기적으로 현재 위치 Update



# 사용 기술 - FSPL(Free Space Path Loss)

![fspl](https://user-images.githubusercontent.com/29969821/42431041-e5c1967c-837d-11e8-9d9f-1af2077ead00.png)

FSPL를 통해 RSSI에 따른 거리를 추정

![12](https://user-images.githubusercontent.com/29969821/42431358-04235e46-8380-11e8-90c2-468c647e46e8.JPG)

FSPL로 계산된 거리와 임의의 3개의 Beacon의 AP (Access Point)를 삼변 측량 (Trilateration) 공식에 대입해 현재 위치를 추정

# 프로그램 출력 결과
![screenshot_2018-07-09-14-06-25](https://user-images.githubusercontent.com/29969821/42431604-94ca6cf4-8381-11e8-87a6-f0672511b4a4.png)
 - MainAcitivity에서 주변 Wifi 조회
 - Listview Item 하나를 누르면 GraphActivity로 이동
 - 상단 Next 버튼 누르면 InActivity로 이동
![screenshot_2018-07-09-14-06-47](https://user-images.githubusercontent.com/29969821/42431606-9630192c-8381-11e8-9fe8-e1736d349814.png)
 - GraphAcitivity에서 선택한 Wifi의 Rssi와 추정된 Distance를 Graph로 표시

![screenshot_2018-07-09-14-06-51](https://user-images.githubusercontent.com/29969821/42431610-976ec7e8-8381-11e8-9755-d3e3d2382732.png)
스캔된 Wifi들 중 제일 가까운 3개를 삼변측량 공식에 대입해 현재 위치 표시
