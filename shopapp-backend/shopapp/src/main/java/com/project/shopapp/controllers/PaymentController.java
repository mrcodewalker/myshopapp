package com.project.shopapp.controllers;

import com.project.shopapp.configurations.VnPayConfig;
import com.project.shopapp.dtos.PaymentDTO;
import com.project.shopapp.dtos.TransactionStatusDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.services.IOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/payments")
@CrossOrigin(origins = "http://localhost:4300")
@RequiredArgsConstructor
public class PaymentController {
    private final OrderRepository orderRepository;

    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment(
            HttpServletRequest request,
            @RequestParam(value = "total_money") Long amount,
            @RequestParam(value = "order_id") Long orderId
            ) throws UnsupportedEncodingException {
            String orderType = "other";
            amount = amount*100;
//            String bankCode = req.getParameter("bankCode");;

            String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
//            String vnp_IpAddr = VnPayConfig.getIpAddress(req);

            String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", orderId.toString());
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", VnPayConfig.getIpAddress(request));

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;
            PaymentDTO paymentDTO = PaymentDTO.builder()
                    .status("OK")
                    .message("Successfully")
                    .url(paymentUrl)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }
    @GetMapping("/check_payment")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_Amount") Long amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "vnp_OrderInfo") String orderInfor,
            @RequestParam(value = "order_id") Long orderId
    ) throws DataNotFoundException {
        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
        if (responseCode.equals("00")){
           transactionStatusDTO = TransactionStatusDTO.builder()
                    .status("Accepted")
                    .message("Your payment order successfully")
                    .data("")
                    .build();
           Order order = this.orderRepository.findById(orderId)
                   .orElseThrow(() -> new DataNotFoundException(
                           "Can not find order with id: "+orderId
                   ));
           order.setActive(true);
           this.orderRepository.save(order);
           return ResponseEntity.ok(transactionStatusDTO);
        }
        return ResponseEntity.badRequest().body(TransactionStatusDTO.builder()
                .status("Failed")
                .message("Your payment order failed, please try again")
                .data("")
                .build());
    }
}
