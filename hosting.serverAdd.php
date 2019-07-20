<?php
function servers_add($data) {
	$ch = curl_init();
	// Получить ключ можно через техническую поддержку: http://ip-games.ru/contact.html
	curl_setopt($ch, CURLOPT_URL, 'https://api.ip-games.ru/method/hosting.serverAdd?key=[hosting_secret_key]');
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($ch, CURLOPT_POST, 1);
	curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));
	$output = curl_exec($ch);
	curl_close($ch);
	return json_decode($output, true);
}

#Список поддерживаемых игр для параметра server_game - https://api.ip-games.ru/files/game_list.txt

$data = array(
	array("server_ip" => "127.0.0.1:27019","qport" => 0,"server_game" => "css"),
	array("server_ip" => "127.0.0.1:7777","qport" => 0,"server_game" => "samp"),
	array("server_ip" => "127.0.0.1:27018","qport" => 0,"server_game" => "csgo"),
	array("server_ip" => "127.0.0.1:27015","qport" => 0,"server_game" => "tf2")
);

$output = servers_add($data);

/*
	echo '<pre>';
	print_r($output);
	echo '</pre>';
*/

foreach($output as $ou) {
	echo $ou['server_ip'].' - (success: '.$ou['success'].') (comment: '.$ou['comment'].')<br/>';
}
?>