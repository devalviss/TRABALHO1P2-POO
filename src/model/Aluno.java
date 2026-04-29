package model;

public class Aluno {
    private String nome;
    private String respostas;
    private int acertos;

    public Aluno(String nome, String respostas) {
        this.nome = nome;
        this.respostas = respostas;
    }

    public void calcularNota(String gabarito) {
        // Regra: VVVVVVVVVV ou FFFFFFFFFF resulta em 0
        if (respostas.equals("VVVVVVVVVV") || respostas.equals("FFFFFFFFFF")) {
            this.acertos = 0;
        } else {
            int soma = 0;
            for (int i = 0; i < 10; i++) {
                if (respostas.charAt(i) == gabarito.charAt(i)) soma++;
            }
            this.acertos = soma;
        }
    }

    public String getNome() { return nome; }
    public String getRespostas() { return respostas; }
    public int getAcertos() { return acertos; }

    @Override
    public String toString() {
        return acertos + "\t" + nome;
    }
}