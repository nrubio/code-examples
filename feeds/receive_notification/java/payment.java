package util;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mercadopago.MP;

import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;

public class ReceiveFeed extends HttpServlet{
 
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		MP mp = new MP ("CLIENT_ID", "CLIENT_SECRET");
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("access_token", mp.getAccessToken());
			JSONObject merchantOrderInfo=null;
			 
			if(request.getParameter("topic").equals("merchant_order")){
				merchantOrderInfo = mp.get("/merchant_orders/" + request.getParameter("id"), params, false);
			}else if(request.getParameter("topic").equals("payment")){
				JSONObject paymentInfo = mp.get("/collections/notifications/" + request.getParameter("id"), params, false);
				merchantOrderInfo = mp.get("/merchant_orders/" + paymentInfo.getJSONObject("response").getJSONObject("collection").getString("merchant_order_id"), params, false);
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
			    }else{
					System.out.print("dont release your items");
				}
			}
		}catch(Exception e){
			throw new ServletException(e);
		}
	}
}