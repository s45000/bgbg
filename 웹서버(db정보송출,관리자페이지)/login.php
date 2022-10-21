<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title> - 로그인 - </title>
    <link rel="stylesheet" href="login.css">
</head>
<body width="100%" height = "100%">

    <div class="container">
    <form action="login_result.php" method="post" class="loginForm">
        <h2>로그인<h2>
        <div class="idForm">
            <input type="text" class="id" name="id" placeholder="아이디">
        </div>
        <div class="passForm">
            <input type="password" class="pw" name="pw" placeholder="비밀번호">
        </div>
        <div class="memoryId">
            <input type="checkbox" class="mid">아이디저장 </input>
        </div>
        <div class="btn">
            <input type="submit" class="btn" value="로그인">
        </div>
        <div class="bottomText">
        회원가입 및 서비스 관련 문의
	<br>
	e-mail : *****@naver.com
	<br>
	phone : 010-****-****
        </div>
    </form>
</div>
</body>
</html>
