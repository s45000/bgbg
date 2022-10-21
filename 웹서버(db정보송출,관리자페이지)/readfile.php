<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title> - 정보 - </title>
    <link rel="stylesheet" href="readfile.css">
</head>
<body>
    <form method="post" class="settingForm">
    <div style="overflow:scroll; height:700px; overflow-x: hidden;">
    <?php
        $key = "my_place";
    ?>
    <h2>
        <?php
            echo $_COOKIE[$key];
        ?>
    </h2>
        <?php
            echo "<br>";
            $fp = fopen($_COOKIE[$key].".txt","r") or die("파일을 열 수 없습니다.");
            while(!feof($fp)) {
                echo fgets($fp);
                echo "<br>";
            }
            fclose($fp);
        ?>
    </div>
</form>
</body>
</html>
