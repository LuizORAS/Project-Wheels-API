package org.wheels.repository;

import org.wheels.model.User;
import org.wheels.model.Plan;
import java.util.*;
import java.io.*;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private static final String USER_CSV = "src/main/resources/csv/users.csv";
    private static final String CSV_HEADER = "userID,firstName,lastName,email,password,plano,dataCriacao,viagensHoje,multaAtual,proximaCobranca,bikeAlugada,horaAluguel";

    public List<User> findAll() {
        File file = new File(USER_CSV);
        // Garante que o arquivo existe e tem cabeçalho
        if (!file.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(USER_CSV))) {
                pw.println(CSV_HEADER);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao criar users.csv", e);
            }
        }

        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USER_CSV))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",", -1); // -1 para pegar campos vazios
                if (tokens.length >= 12) {
                    int id = Integer.parseInt(tokens[0]);
                    String firstName = tokens[1];
                    String lastName = tokens[2];
                    String email = tokens[3];
                    String password = tokens[4];
                    Plan plano = Plan.valueOf(tokens[5]);
                    String dataCriacao = tokens[6];
                    int viagensHoje = Integer.parseInt(tokens[7]);
                    double multaAtual = Double.parseDouble(tokens[8]);
                    String proximaCobranca = tokens[9];
                    String bikeAlugada = tokens[10];
                    String horaAluguel = tokens[11];

                    users.add(new User(id, firstName, lastName, email, password, plano, dataCriacao, viagensHoje, multaAtual, proximaCobranca, bikeAlugada, horaAluguel));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler users.csv", e);
        }
        return users;
    }

    public User findByEmail(String email) {
        return findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public void saveAll(List<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_CSV))) {
            pw.println(CSV_HEADER);
            for (User u : users) {
                pw.printf("%d,%s,%s,%s,%s,%s,%s,%d,%.2f,%s,%s,%s%n",
                        u.getUserID(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getEmail(),
                        u.getPassword(),
                        u.getPlano().name(),
                        u.getDataCriacao(),
                        u.getViagensHoje(),
                        u.getMultaAtual(),
                        u.getProximaCobranca(),
                        u.getBikeAlugada(),
                        u.getHoraAluguel()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar users.csv", e);
        }
    }

    public void save(User user) {
        List<User> users = findAll();
        if (user.getUserID() == 0 || users.stream().anyMatch(u -> u.getUserID() == user.getUserID())) {
            // Gera novo ID
            int maxId = users.stream().mapToInt(User::getUserID).max().orElse(0);
            user.setUserID(maxId + 1);
        }
        // Remove qualquer usuário antigo com mesmo email (para evitar duplicidade)
        users.removeIf(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));
        users.add(user);
        saveAll(users);
    }

    public void deleteByEmail(String email) {
        List<User> users = findAll();
        users.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
        saveAll(users);
    }
}