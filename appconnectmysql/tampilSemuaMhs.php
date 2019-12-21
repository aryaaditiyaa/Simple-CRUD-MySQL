<?php

	//Import File Koneksi Database
	require_once('koneksi.php');
	
	//Membuat SQL Query
	$sql = "SELECT * FROM mhs";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan NIM kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
			"nim"=>$row['nim'],
			"nama"=>$row['nama']
		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>