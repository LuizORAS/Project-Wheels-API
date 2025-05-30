package org.wheels.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;            // login
    private String password;         // senha

    private Plan plano;
    private String dataCriacao;
    private int viagensHoje;
    private double multaAtual;
    private String proximaCobranca;
    private String bikeAlugada;      // id da bike como string
    private String horaAluguel;
}