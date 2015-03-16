import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.mercadopago.MP;

import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

public class ReceiveFeed extends HttpServlet {
 
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, JSONException, Exception {
		MP mp = new MP ("CLIENT_ID", "CLIENT_SECRET");

		JSONObject merchantOrderInfo=null;
		JSONObject paymentInfo = null;
		
		if(request.getParameter("topic").equals("merchant_order")){
			merchantOrderInfo = mp.getMerchantOrderInfo(request.getParameter("id"));
		}else if(request.getParameter("topic").equals("payment")){
			paymentInfo = mp.getPaymentInfo(request.getParameter("id"));
			merchantOrderInfo = mp.getMerchantOrderInfo(paymentInfo.getJSONObject("response").getJSONObject("collection").getString("merchant_order_id"));
		}
		
		if (merchantOrderInfo.getInt("status") == 200) {
			Double transactionAmountPayments = 0D;
			Double transactionAmountOrder = merchantOrderInfo.getJSONObject("response").getDouble("total_amount");
			JSONArray payments=merchantOrderInfo.getJSONObject("response").getJSONArray("payments");
		    for (int i=0; i < payments.length(); i++) {
		    	JSONObject payment = payments.getJSONObject(i);
		    	if(payment.getString("status").equals("approved")){
		    		transactionAmountPayments += payment.getDouble("transaction_amount");
			    }	
		    }
		    if(transactionAmountPayments >= transactionAmountOrder){
		    	System.out.print("release your items");
		    }
		}
		
		System.out.print("dont release your items");
	}
}