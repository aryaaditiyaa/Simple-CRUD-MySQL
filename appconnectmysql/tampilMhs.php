<?php
	
	//Mendapatkan Nilai Dari Variable NIM yang ingin ditampilkan
	$nim = $_GET['nim'];
	
	//Importing database
	require_once('koneksi.php');
	
	//Membuat SQL Query dengan pegawai yang ditentukan secara spesifik sesuai ID
	$sql = "SELECT * FROM mhs WHERE nim=$nim";
	
	//Mendapatkan Hasil 
	$r = mysqli_query($con,$sql);
	
	//Memasukkan Hasil Kedalam Array
	$result = array();
	$row = mysqli_fetch_array($r);
	array_push($result,array(
			"nim"=>$row['nim'],
			"nama"=>$row['nama'],
			"alamat"=>$row['alamat']
		));

	//Menampilkan dalam format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>