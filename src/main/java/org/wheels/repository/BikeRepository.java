package org.wheels.repository;

import org.wheels.model.Bike;
import org.wheels.model.BikeType;
import java.util.*;
import java.io.*;

import org.springframework.stereotype.Repository;

@Repository
public class BikeRepository {
    private static final String DATA_DIR = "data/csv";
    private static final String BIKE_CSV = DATA_DIR + "/bikes.csv";
    private static final String CSV_HEADER = "id,type,available";

    public BikeRepository() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public List<Bike> findAll() {
        File file = new File(BIKE_CSV);
        // Garante que o arquivo existe e tem cabeçalho
        if (!file.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(BIKE_CSV))) {
                pw.println(CSV_HEADER);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao criar bikes.csv", e);
            }
        }

        List<Bike> bikes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BIKE_CSV))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",", -1);
                if (tokens.length >= 3) {
                    int id = Integer.parseInt(tokens[0]);
                    BikeType type = BikeType.valueOf(tokens[1]);
                    boolean available = Boolean.parseBoolean(tokens[2]);
                    bikes.add(new Bike(id, type, available));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler bikes.csv", e);
        }
        return bikes;
    }

    public Bike findById(int id) {
        return findAll().stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void saveAll(List<Bike> bikes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BIKE_CSV))) {
            pw.println(CSV_HEADER);
            for (Bike b : bikes) {
                pw.printf("%d,%s,%b%n", b.getId(), b.getType().name(), b.isAvailable());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar bikes.csv", e);
        }
    }

    public void save(Bike bike) {
        List<Bike> bikes = findAll();
        Optional<Bike> existingBike = bikes.stream().filter(b -> b.getId() == bike.getId()).findFirst();
        if (existingBike.isPresent()) {
            bikes.remove(existingBike.get());
        }
        bikes.add(bike);
        saveAll(bikes);
    }
}