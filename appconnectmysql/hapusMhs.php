<?php

 //Mendapatkan Nilai ID
 $nim = $_GET['nim'];
 
 //Import File Koneksi Database
 require_once('koneksi.php');
 
 //Membuat SQL Query
 $sql = "DELETE FROM mhs WHERE nim=$nim;";

 
 //Menghapus Nilai pada Database 
 if(mysqli_query($con,$sql)){
 echo 'Berhasil Menghapus Mahasiswa';
 }else{
 echo 'Gagal Menghapus Mahasiswa';
 }
 
 mysqli_close($con);
 ?>