<?php
	$con = mysqli_connect("localhost","remoteUser","Password123;","BGdb");
	mysqli_set_charset($con, "utf8");

	$res = mysqli_query($con, "SELECT * FROM BGtable");
	$result = array();

	while($row = mysqli_fetch_array($res)){
		array_push($result,
			array('doh'=>$row['doh'],'si'=>$row['si'],'gun'=>$row['gun'],'gu'=>$row['gu'],'place'=>$row['place'],'nowN'=>$row['nowN'],'ruleN'=>$row['ruleN'],'updateTime'=>$row['updateTime'],'notice'=>$row['notice'],'lat'=>$row['lat'],'lng'=>$row['lng']));
	}

	$leng = mysqli_num_rows($res);
	echo json_encode(array("Info"=>$result,"Leng"=>$leng), JSON_UNESCAPED_UNICODE);
	mysqli_close($con);
?>
