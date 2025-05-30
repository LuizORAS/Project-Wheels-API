package org.wheels.service;

import org.springframework.stereotype.Service;
import org.wheels.model.User;
import org.wheels.model.Plan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Service
public class ReceiptService {
    private static final String RECEIPT_DIR = "src/main/resources/receipts";

    public ReceiptService() {
        File dir = new File(RECEIPT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void generateReceipt(User user, Plan novoPlano) {
        String fileName = RECEIPT_DIR + "/receipt_" + user.getUserID() + "_" + System.currentTimeMillis() + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Nome do usuário: " + user.getFirstName() + " " + user.getLastName());
            writer.println("Plano comprado: " + novoPlano);
            writer.println("Data da compra: " + LocalDateTime.now());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar recibo: " + e.getMessage(), e);
        }
    }
}