<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용방법</title>
    <link rel="stylesheet" href="product.css">
</head>
<body>
<form method="post" class="settingForm">
    <div style="overflow:scroll; height:700px; overflow-x: hidden;">
    <div class="content">
        <div class="main">
            <a>웹사이트 사용법</a><br>
        </div>
        <div class="text1">
        <br>
            바글바글 - CCTV를 통한 영상감시 및 인구 밀집도 확인 서비스
        </div>
	<?php
		$key = "my_place";
		$con = mysqli_connect("localhost","remoteUser","Password123;","BGdb");
		$res = mysqli_query($con, "SELECT * FROM BGtable WHERE place = '$_COOKIE[$key]'");
		
		$place = "공간";
		if($board = mysqli_fetch_array($res)){
			$place = $board['place'];
		}
	?>
        <div class="text2">
        <br>
            <li>서비스를 신청하신 <?php echo $place ?> 관리자님께선 바글바글 웹페이지를 통해 인원 출입 영상 확인 및 공간 정보를 관리 하실 수 있습니다.</li>
            <br> 
            <li>메인 페이지로 돌아가 중앙의 cctv 버튼을 누르시면 출입구에 설치된 cctv 영상을 확인하실 수 있습니다.</li>
            <br>
            <li>가장 오른쪽의 관리페이지를 누르시면 <?php echo $place ?>의 정보를 관리하실수 있습니다.</li>
            <br>
            <li>관리페이지 에서 출입구의 cctv를 지나 현재 공간에 들어온 사람 수를 확인하실수 있으며, 사회적 거리두기 단계 혹은 <?php echo $place ?>의 관리 방침에 따른 제한인원을 설정 하실수 있습니다.</li>
            <br>
            <li>일반 사용자들이 어플로 제한인원과 현재인원의 비율을 통해 각 장소의 바글바글한 정도를 확인 할 수 있습니다.</li>
            <br>
            <li>또한 어플에서 확인할 수 있는 공간 사용에 대한 안내 문구를 입력하실 수 있습니다.</li>
            <br>
            <li>마지막으로 날짜별 인구밀집도 확인 버튼을 누르시면 시스템이 설치된 날짜부터 현재까지의 시간당 인구밀집 정보를 확인하실 수 있습니다.</li>
        </div>
    </div>
</form>
</body>
</html>
