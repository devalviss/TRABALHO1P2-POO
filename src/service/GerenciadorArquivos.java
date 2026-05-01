package service;

import java.io.*;
import java.util.*;
import model.Aluno;

public class GerenciadorArquivos {

    public static void salvarDadosDisciplina(String nomeArquivo, List<Aluno> alunos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo + ".txt"))) {
            for (Aluno a : alunos) {
                // Salva no formato: RESPOSTAS [TAB] NOME
                bw.write(a.getRespostas() + "\t" + a.getNome());
                bw.newLine();
            }
        }
    }

    public static List<Aluno> lerDadosDisciplina(String nomeArquivo) throws IOException {
        List<Aluno> alunos = new ArrayList<>();
        File file = new File(nomeArquivo + ".txt");
        
        if (!file.exists()) throw new FileNotFoundException("Arquivo da disciplina não encontrado.");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split("\t");
                if (partes.length >= 2) {
                    alunos.add(new Aluno(partes[1], partes[0]));
                }
            }
        }
        return alunos;
    }

    public static String lerGabarito(String caminho) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine();
            return (linha != null) ? linha.trim() : "";
        }
    }
}