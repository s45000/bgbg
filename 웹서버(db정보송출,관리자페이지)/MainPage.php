<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>메인페이지</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="MainPage.css">
    </head>
    <?php
        $key = "my_place";
        $value = $_COOKIE[$key];
        setcookie($key, $value);
    ?>
    <body>
        <h1>
            <?php 
                echo $_COOKIE[$key]; 
            ?>

        </h1>
        </script>
        <div class="container">
            <a href="product.php" title="이 시스템에 관한 설명 페이지 입니다."><button class="btn btn1">사이트 이용법</button></a><!-- target="_blank" -->
<?php
	$key = "my_place";

	$con = mysqli_connect("localhost","remoteUser","Password123;","BGdb");
	$res = mysqli_query($con, "SELECT * FROM BGtable WHERE place = '$_COOKIE[$key]'");
	
	if($board = mysqli_fetch_array($res))
        {
            $videoLink = $board['videoLink'];
        }
        mysqli_close($con);
?>
            <a href="<?php echo $videoLink ?>"title="CCTV영상을 실시간으로 확인 할 수 있는 페이지 입니다."><button class="btn btn2">CCTV</button></a>
            <a href="setting.php" title="관리자 전용 페이지이며 현재 출입자 수 및 기본 관리 등을 할 수 있습니다."><button class="btn btn3">관리</button></a>
        </div>
    </body>
</html>
