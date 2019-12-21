<?php

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Mendapatkan Nilai Variable
		$nama = $_POST['nama'];
		$alamat = $_POST['alamat'];
		
		//Pembuatan Syntax SQL
		$sql = "INSERT INTO mhs (nama,alamat) VALUES ('$nama','$alamat')";
		
		//Import File Koneksi database
		require_once('koneksi.php');
		
		//Eksekusi Query database
		if(mysqli_query($con,$sql)){
			echo 'Berhasil Menambahkan Mahasiswa';
		}else{
			echo 'Gagal Menambahkan Mahasiswa';
		}
		
		mysqli_close($con);
	}
?>