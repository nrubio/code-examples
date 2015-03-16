<?php
require_once "mercadopago.php";

$mp = new MP("CLIENT_ID", "CLIENT_SECRET");

if($_GET["topic"] == 'payment'){
	$payment_info = $mp->get_payment_info($_GET["id"]);
	$merchant_order_info = $mp->get_merchant_order_info($payment_info["response"]["collection"]["merchant_order_id"]);
}else if($_GET["topic"] == 'merchant_order'){
	$merchant_order_info = $mp->get_merchant_order_info($_GET["id"]);
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
	}
echo "dont release your items";

?>