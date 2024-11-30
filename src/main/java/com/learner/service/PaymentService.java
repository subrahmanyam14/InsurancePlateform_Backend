package com.learner.service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learner.dto.PaymentDto;
import com.learner.entity.DateUtil;
import com.learner.entity.Payment;
import com.learner.exception.InsuranceRelavantException;
import com.learner.exception.ResourceNotFoundException;
import com.learner.repository.PaymentRepository;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private EmailService emailService;

    private PaymentDto toDto(Payment req) {
        PaymentDto dto = new PaymentDto();
        dto.setAmount(req.getAmount());
        dto.setId(req.getId());
        dto.setImageProof(null);
        dto.setPolicyId(req.getPolicyId());
        dto.setPolicyNo(req.getPolicyNo());
        dto.setTransactionId(req.getTransactionId());
        dto.setReferenceId(req.getReferenceId());
        dto.setEmail(req.getEmail());
        return dto;
    }

    public PaymentDto savePayment(String email, String amount, String policyId, String policyNo, String policyName, String transactionId, String referenceId, MultipartFile file) throws IOException, SerialException, SQLException {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPolicyId(policyId);
        payment.setPolicyNo(policyNo);
        payment.setPolicyName(policyName);
        payment.setReferenceId(referenceId);
        payment.setTransactionId(transactionId);
        payment.setDate(getCurrentDateInYYYYMMDDHHMMSS());
        payment.setEmail(email);
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            payment.setImageProof(photoBlob);
        }

        String subject = "Your payment done Successfully";
        String text = "Your payment has been done, Successfully! Wait for the policy Approval.";

        try {
            emailService.sendMail(payment.getEmail(), subject, text);
        } catch (MailSendException e) {
            // logger.error("Failed to send policy confirmation email due to mail server connection issue", e);
        } catch (Exception e) {
            // logger.error("Failed to send policy confirmation email", e);
        }
        return toDto(paymentRepository.save(payment));
    }

    private byte[] getPhotoBytes(Blob photoBlob) throws SQLException {
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    private static String getCurrentDateInYYYYMMDDHHMMSS() {
        return DateUtil.getCurrentDateInYYYYMMDDHHMMSS();
    }

    public List<PaymentDto> getPaymentByEmail(String email) throws SQLException {
        List<Payment> pays = paymentRepository.findAllByEmail(email);
        List<PaymentDto> dtos = new ArrayList<>();
        for (Payment req : pays) {
            PaymentDto dto = new PaymentDto();
            dto.setAmount(req.getAmount());
            dto.setId(req.getId());
            dto.setPolicyId(req.getPolicyId());
            dto.setPolicyNo(req.getPolicyNo());
            dto.setPolicyName(req.getPolicyName());
            dto.setTransactionId(req.getTransactionId());
            dto.setReferenceId(req.getReferenceId());
            dto.setDate(req.getDate());
            dto.setEmail(req.getEmail());
            byte[] photoBytes = getPhotoBytes(req.getImageProof());
            String base64Photo = (photoBytes != null && photoBytes.length > 0) ? Base64.encodeBase64String(photoBytes) : null;
            dto.setImageProof(base64Photo);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<PaymentDto> getAllPayments() throws SQLException {
        List<Payment> pays = paymentRepository.findAll();
        List<PaymentDto> dtos = new ArrayList<>();
        for (Payment req : pays) {
            PaymentDto dto = new PaymentDto();
            dto.setAmount(req.getAmount());
            dto.setId(req.getId());
            dto.setPolicyId(req.getPolicyId());
            dto.setPolicyNo(req.getPolicyNo());
            dto.setPolicyName(req.getPolicyName());
            dto.setTransactionId(req.getTransactionId());
            dto.setReferenceId(req.getReferenceId());
            dto.setDate(req.getDate());
            dto.setEmail(req.getEmail());
            byte[] photoBytes = getPhotoBytes(req.getImageProof());
            if (photoBytes == null) {
                throw new ResourceNotFoundException("Resource not found with id " + dto.getId());
            }
            String base64Photo = Base64.encodeBase64String(photoBytes);
            dto.setImageProof(base64Photo);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<PaymentDto> getSpecificPayment(String policyNo) throws SQLException {
        List<Payment> pays = paymentRepository.findAllByPolicyNo(policyNo);
        List<PaymentDto> dtos = new ArrayList<>();
        for (Payment req : pays) {
            PaymentDto dto = new PaymentDto();
            dto.setAmount(req.getAmount());
            dto.setId(req.getId());
            dto.setPolicyId(req.getPolicyId());
            dto.setPolicyNo(req.getPolicyNo());
            dto.setPolicyName(req.getPolicyName());
            dto.setTransactionId(req.getTransactionId());
            dto.setDate(req.getDate());
            dto.setReferenceId(req.getReferenceId());
            dto.setEmail(req.getEmail());
            byte[] photoBytes = getPhotoBytes(req.getImageProof());
            if (photoBytes == null) {
                throw new ResourceNotFoundException("Resource not found with id " + dto.getId());
            }
            String base64Photo = Base64.encodeBase64String(photoBytes);
            dto.setImageProof(base64Photo);
            dtos.add(dto);
        }
        return dtos;
    }
}
