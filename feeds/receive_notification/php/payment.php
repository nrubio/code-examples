<?php
require_once "../../lib/mercadopago.php";

$mp = new MP("CLIENT_ID", "CLIENT_SECRET");

$params = ["access_token" => $mp->get_access_token()];
if($_GET["topic"] == 'payment'){
	$payment_info = $mp->get("/collections/notifications/" . $_GET["id"], $params, false);
	$merchant_order_info = $mp->get("/merchant_orders/" . $payment_info["response"]["collection"]["merchant_order_id"], $params, false);
}else if($_GET["topic"] == 'merchant_order'){
	$merchant_order_info = $mp->get("/merchant_orders/" . $_GET["id"], $params, false);
}
if ($merchant_order_info["status"] == 200) {
	$transaction_amount_payments= 0;
	$transaction_amount_order = $merchant_order_info["response"]["total_amount"];
    $payments=$merchant_order_info["response"]["payments"];
    foreach ($payments as  $payment) {
    	if($payment['status'] == 'approved'){
	    	$transaction_amount_payments += $payment['transaction_amount'];
	    }	
    }
    if($transaction_amount_payments >= $transaction_amount_order){
    	echo "release your items";
    }
    else{
		echo "dont release your items";
	}
}
?>