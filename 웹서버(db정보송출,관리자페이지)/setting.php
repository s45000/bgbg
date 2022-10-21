<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리</title>
    <link rel="stylesheet" href="setting.css">
</head>
<body>
    <form method="post" class="settingForm">
        <h2>관리자 설정</h2>
        <div class="information">
    <?php
        $key = "my_place";

	$con = mysqli_connect("localhost","remoteUser","Password123;","BGdb");
	$res = mysqli_query($con, "SELECT * FROM BGtable WHERE place = '$_COOKIE[$key]'");
	
	if($board = mysqli_fetch_array($res))
        {
            echo "내 장소\n:\n".$board['place'];
            echo "<br>";
            echo "현재 인원\n:\n".$board['nowN'];
            echo "<br>";
            echo "제한 인원\n:\n".$board['ruleN']; 
            echo "<br>";
            echo "현재 안내 문구\n:\n".$board['notice'];
        }
        mysqli_close($con);
    ?>
    </div>
    <div class="ruleNForm">
    제한 인원 설정
        <input type="text" class="ruleNtext" name="data1">  
        <input type="submit" class="ruleN" name="ruleN" id="ruleN" value="확인"/><br/> 
    </div>
    <div class="noticeForm">
    관리자 안내 문구 설정  
        <input type="text" class="noticetext" name="data2">
        <input type="submit" class="notice" name="notice" id="notice" value="확인"/><br/> 
    </div>
    <div class="print">
        <input type="submit" class="print" name="print" id="print" value="날짜별 인구밀집도 확인"/><br/> 
    </div>
    <?php 
	function info() 
	{ 
	    echo 
	    "<script>
		location.href='readfile.php'
	    </script>"; 
	} 
	if(array_key_exists('print',$_POST))
	{
	info(); 
	} 
    ?>
    <div class="back">
        <input type="submit" class="back" name="back" id="back" value="돌아가기"/><br/> 
    </div>
    <?php 
	function back() 
	{ 
	    echo 
	    "<script>
		location.href='MainPage.php'
	    </script>"; 
	} 
	if(array_key_exists('back',$_POST))
	{
	back(); 
	} 
    ?>
    </form>
</body>
</html>
<?php 
function setRuleN() 
{ 
    $ruleN = $_POST['data1'];
    $key = "my_place";
    $mysqli = mysqli_connect("localhost","remoteUser","Password123;","BGdb");

    $update = "UPDATE BGtable SET ruleN = '$ruleN' WHERE place='$_COOKIE[$key]'";
    mysqli_query($mysqli, $update); 
    mysqli_close($mysqli);

    echo("
    <script>
        location.href='setting.php'
    </script>"
    );
} 
if(array_key_exists('ruleN',$_POST))
{ 
    setRuleN(); 
} 

function setNotice() 
{ 
    $notice = $_POST['data2'];
    $key = "my_place";
    $mysqli = mysqli_connect("localhost","remoteUser","Password123;","BGdb");

    $update = "UPDATE BGtable SET notice = '$notice' WHERE place='$_COOKIE[$key]'";
    mysqli_query($mysqli, $update); 
    mysqli_close($mysqli);
    echo("
    <script>
        location.href='setting.php'
    </script>"
    ); 
} 
if(array_key_exists('notice',$_POST))
{ 
    setNotice(); 
} 
?>
