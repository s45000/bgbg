<?php
	$id=$_POST['id'];
	$pw=$_POST['pw'];

	$con = mysqli_connect("localhost","remoteUser","Password123;","BGdb");
	$res = mysqli_query($con, "SELECT * FROM BGtable WHERE id = '$id' AND pw = '$pw'");
	
	if($board = mysqli_fetch_array($res))
	{
	  $key = "my_place";
	  $value = $board['place'];
	  setcookie($key, $value);
	  echo (
	    "<script>
		location.href='MainPage.php';
	    </script>"
	  );
	}
	else{
	  echo (
	    "<script>
	      window.alert('아이디나 비밀번호가 틀렸습니다.');
	      location.href='login.php';
	    </script>"
	  );
	}
    	mysqli_close($con);
?>
