<?php
define('HOTE', 'localhost:8889/testLogin');
define('UTIL', 'root');
define('MDP', 'root');
define('BDD', 'login');

$conn = mysqli_connect(HOTE,UTIL,MDP,BDD);

$email = $_POST['email'];
$mdp = $_POST['mdp'];

$email = "adoualle@gmail.com";
$mdp = "123";

$sql = "select * from Utilisateurs where `email_util`='$email' and `mdp_util`='$mdp'";

$res = mysqli_query($conn,$sql);

$check = mysqli_fetch_array($res);

if(isset($check)){
  echo 'login réussi';
}else{
  echo 'login echoué';
}


mysqli_close($conn);
?>
