<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		//MEndapatkan Nilai Dari Variable 
		$nim = $_POST['nim'];
		$nama = $_POST['nama'];
		$alamat = $_POST['alamat'];
		
		//import file koneksi database 
		require_once('koneksi.php');
		
		//Membuat SQL Query
		$sql = "UPDATE mhs SET nama = '$nama', alamat = '$alamat' WHERE nim = $nim;";
		
		//Meng-update Database 
		if(mysqli_query($con,$sql)){
			echo 'Berhasil Update Data Mahasiswa';
		}else{
			echo 'Gagal Update Data Mahasiswa';
		}
		
		mysqli_close($con);
	}
?>