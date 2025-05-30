package org.wheels.service;

import org.springframework.stereotype.Service;
import org.wheels.model.Bike;
import org.wheels.model.BikeType;
import org.wheels.model.Plan;
import org.wheels.model.User;
import org.wheels.repository.BikeRepository;
import org.wheels.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {
    private final BikeRepository bikeRepository;
    private final UserRepository userRepository;

    public BikeService(BikeRepository bikeRepository, UserRepository userRepository) {
        this.bikeRepository = bikeRepository;
        this.userRepository = userRepository;
    }

    public List<Bike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public Bike getBikeById(int id) {
        return bikeRepository.findById(id);
    }

    public List<Bike> getAvailableBikesByType(BikeType type) {
        return bikeRepository.findAll().stream()
                .filter(b -> b.getType() == type && b.isAvailable())
                .collect(Collectors.toList());
    }

    public void saveBike(Bike bike) {
        bikeRepository.save(bike);
    }

    // Aluga uma bike para o usuário
    public void rentBike(int bikeId, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Bike bike = bikeRepository.findById(bikeId);

        if (user == null || bike == null || !bike.isAvailable()) {
            throw new IllegalArgumentException("Usuário ou bicicleta não encontrada/disponível!");
        }

        // Checa se usuário já tem bike alugada
        if (user.getBikeAlugada() != null && !user.getBikeAlugada().isEmpty()) {
            throw new IllegalStateException("Usuário já possui bicicleta alugada!");
        }

        // Atualiza Bike
        bike.setAvailable(false);
        bikeRepository.save(bike);

        // Atualiza User
        user.setBikeAlugada(String.valueOf(bike.getId()));
        user.setHoraAluguel(LocalDateTime.now().toString());
        user.setViagensHoje(user.getViagensHoje() + 1);
        userRepository.save(user);
    }

    // Devolve uma bike alugada, atualiza multa se necessário
    public void returnBike(int bikeId, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Bike bike = bikeRepository.findById(bikeId);

        if (user == null || bike == null) {
            throw new IllegalArgumentException("Usuário ou bicicleta não encontrada!");
        }
        if (user.getBikeAlugada() == null || !user.getBikeAlugada().equals(String.valueOf(bikeId))) {
            throw new IllegalStateException("Esta bicicleta não está alugada por este usuário!");
        }

        // Calcula tempo de uso e aplica multa se ultrapassar limite (igual CLI)
        LocalDateTime horaAluguel = LocalDateTime.parse(user.getHoraAluguel());
        LocalDateTime horaAgora = LocalDateTime.now();
        long minutosUso = Duration.between(horaAluguel, horaAgora).toMinutes();

        int limiteMin = getLimiteMinutos(user.getPlano(), bike.getType());
        double multa = 0;
        if (minutosUso > limiteMin && limiteMin > 0) {
            int minutosExcedidos = (int) (minutosUso - limiteMin);
            multa = minutosExcedidos * 0.50; // Exemplo: 0,50 por minuto
            user.setMultaAtual(user.getMultaAtual() + multa);
        }

        // Atualiza Bike
        bike.setAvailable(true);
        bikeRepository.save(bike);

        // Limpa campos do usuário
        user.setBikeAlugada("");
        user.setHoraAluguel("");
        userRepository.save(user);
    }

    private int getLimiteMinutos(Plan plano, BikeType tipo) {
        // Igual ao seu CLI!
        switch (plano) {
            case FREE:    return 30;
            case BASIC:   return 60;
            case GOLD:    return 120;
            case DIAMOND: return tipo == BikeType.ELECTRIC ? 60 : 0; // 0 = ilimitado
            default:      return 30;
        }
    }
}