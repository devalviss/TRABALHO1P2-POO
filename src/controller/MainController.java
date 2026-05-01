package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.Aluno;
import model.Disciplina;
import service.GerenciadorArquivos;
import service.CalculadoraResultados;
import java.io.File;
import java.util.List;

public class MainController {
    @FXML private TextField txtNomeDisciplina, txtNomeAluno, txtRespostas;
    @FXML private TextArea txtSaida;
    private Disciplina disciplinaAtual;

    @FXML
    public void criarDisciplina() {
        String nome = txtNomeDisciplina.getText().trim();
        if (nome.isEmpty()) {
            exibirAlerta("Erro", "Digite o nome da disciplina.");
            return;
        }
        disciplinaAtual = new Disciplina(nome);
        txtSaida.setText("Disciplina '" + nome + "' iniciada.");
    }

    @FXML
    public void adicionarAluno() {
        if (disciplinaAtual == null) {
            exibirAlerta("Erro", "Crie a disciplina primeiro.");
            return;
        }
        String nome = txtNomeAluno.getText().trim();
        String resp = txtRespostas.getText().toUpperCase().trim();

        if (nome.isEmpty() || !resp.matches("[VF]{10}")) {
            exibirAlerta("Erro", "Nome obrigatório e respostas devem ser 10 caracteres (V ou F).");
            return;
        }

        disciplinaAtual.adicionarAluno(new Aluno(nome, resp));
        txtSaida.appendText("\nAluno " + nome + " adicionado.");
        txtNomeAluno.clear();
        txtRespostas.clear();
    }

    @FXML
    public void finalizarESalvar() {
        if (disciplinaAtual == null || disciplinaAtual.getAlunos().isEmpty()) {
            exibirAlerta("Erro", "Não há dados para processar.");
            return;
        }
        
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecione o Gabarito Oficial");
        File fileGabarito = fc.showOpenDialog(null);

        if (fileGabarito != null) {
            try {
                String gabarito = GerenciadorArquivos.lerGabarito(fileGabarito);
                List<Aluno> lista = disciplinaAtual.getAlunos();
                
                for (Aluno a : lista) a.calcularNota(gabarito);

                // Gera os arquivos solicitados no trabalho
                CalculadoraResultados.ordenarAlfabetica(lista);
                GerenciadorArquivos.salvarDadosDisciplina(disciplinaAtual.getNome() + "_ALFABETICA", lista);
                
                CalculadoraResultados.ordenarPorNota(lista);
                GerenciadorArquivos.salvarDadosDisciplina(disciplinaAtual.getNome() + "_NOTAS", lista);

                // Mostra na tela (TextArea)
                txtSaida.setText("--- RESULTADOS FINAIS ---");
                for (Aluno a : lista) txtSaida.appendText("\n" + a.getAcertos() + " pts - " + a.getNome());
                
                double media = CalculadoraResultados.calcularMediaTurma(lista);
                txtSaida.appendText("\n\nMÉDIA DA TURMA: " + String.format("%.2f", media));
                
            } catch (Exception e) {
                exibirAlerta("Erro de Processamento", e.getMessage());
            }
        }
    }

    private void exibirAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}