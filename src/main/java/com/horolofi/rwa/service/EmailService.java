package com.horolofi.rwa.service;

import com.horolofi.rwa.entity.User;
import com.horolofi.rwa.repository.OrderBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final OrderBookRepository orderBookRepository;
    
    public int sendEmailNotif(String serialNumber, String votingDate) {
        List<User> users = orderBookRepository.findUsersByAssetSerialNumber(serialNumber);
        
        if (users.isEmpty()) {
            log.warn("Ga ada user yang nemegang token dengan serial number: {}", serialNumber);
            return 0;
        }
        
        int emailsSent = 0;
        for (User user : users) {
            try {
                sendEmail(user.getEmail(), user.getUsername(), serialNumber, votingDate);
                emailsSent++;
                log.info("Email berhasil dikirim ke: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Gagal kirim email ke: {}, error: {}", user.getEmail(), e.getMessage());
            }
        }
        
        return emailsSent;
    }
    
    private void sendEmail(String toEmail, String username, String serialNumber, String votingDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Notifikasi Voting Aset " + serialNumber);
        
        String emailBody = String.format(
            "Yth. %s,\n\n" +
            "Dengan hormat, kami informasikan bahwa aset dari token %s yang Anda miliki sedang dalam proses voting " +
            "untuk penjualan yang akan dilaksanakan pada tanggal %s.\n\n" +
            "Mohon untuk mengikuti proses voting tersebut. Apabila Anda tidak mengikuti voting, maka akan dianggap " +
            "sebagai persetujuan terhadap keputusan yang diambil.\n\n" +
            "Terima kasih atas perhatian dan partisipasi Anda.\n\n" +
            "Hormat kami,\n" +
            "HoroloFi Team",
            username,
            serialNumber,
            votingDate
        );
        
        message.setText(emailBody);
        mailSender.send(message);
    }
}

