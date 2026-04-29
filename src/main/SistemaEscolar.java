package main;

import java.util.*;
import model.*;
import service.*;

public class SistemaEscolar {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n1- Criar Disciplina\n2- Gerar Resultados\n0- Sair");
            opcao = sc.nextInt();
            sc.nextLine(); // Limpar buffer

            try {
                if (opcao == 1) criarDisciplina();
                else if (opcao == 2) gerarResultados();
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private static void criarDisciplina() throws Exception {
        System.out.print("Nome da disciplina: ");
        String nome = sc.nextLine();
        Disciplina disc = new Disciplina(nome);

        while (true) {
            System.out.print("Nome do aluno (ou 'fim'): ");
            String nomeA = sc.nextLine();
            if (nomeA.equalsIgnoreCase("fim")) break;
            System.out.print("Respostas (10 caracteres V/F): ");
            String resp = sc.nextLine();
            disc.adicionarAluno(new Aluno(nomeA, resp));
        }
        GerenciadorArquivos.salvarDadosDisciplina(nome, disc.getAlunos());
    }

    private static void gerarResultados() throws Exception {
        System.out.print("Nome da disciplina: ");
        String nome = sc.nextLine();
        System.out.print("Caminho do gabarito: ");
        String pathG = sc.nextLine();

        List<Aluno> alunos = GerenciadorArquivos.lerDadosDisciplina(nome);
        String gabarito = GerenciadorArquivos.lerGabarito(pathG);

        for (Aluno a : alunos) a.calcularNota(gabarito);

        // Gerar arquivos e exibir na tela
        CalculadoraResultados.ordenarAlfabetica(alunos);
        System.out.println("\n--- Ordem Alfabética ---");
        alunos.forEach(System.out::println);

        CalculadoraResultados.ordenarPorNota(alunos);
        System.out.println("\n--- Por Nota ---");
        alunos.forEach(System.out::println);
        System.out.println("Média da Turma: " + CalculadoraResultados.calcularMediaTurma(alunos));
    }
}