package service;

import java.util.*;
import model.Aluno;

public class CalculadoraResultados {

    public static void ordenarAlfabetica(List<Aluno> alunos) {
        alunos.sort(Comparator.comparing(Aluno::getNome));
    }

    public static void ordenarPorNota(List<Aluno> alunos) {
        alunos.sort(Comparator.comparingInt(Aluno::getAcertos).reversed());
    }

    public static double calcularMediaTurma(List<Aluno> alunos) {
        if (alunos.isEmpty()) return 0;
        double soma = 0;
        for (Aluno a : alunos) soma += a.getAcertos();
        return soma / alunos.size();
    }
}