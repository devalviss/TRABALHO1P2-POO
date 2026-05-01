package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Aluno;
import model.Disciplina;
import service.GerenciadorArquivos;
import service.CalculadoraResultados;
import java.util.List;

public class MainController {
    @FXML private TextField txtNomeDisciplina;
    @FXML private TextField txtNomeAluno;
    @FXML private TextField txtRespostas;
    @FXML private TextArea txtSaida;

    private Disciplina disciplinaAtual;

    @FXML
    public void criarDisciplina() {
        String nome = txtNomeDisciplina.getText();
        if (nome.isEmpty()) {
            exibirAlerta("Erro", "Digite o nome da disciplina.");
            return;
        }
        disciplinaAtual = new Disciplina(nome);
        txtSaida.setText("Disciplina '" + nome + "' iniciada. Adicione os alunos.");
    }

    @FXML
    public void adicionarAluno() {
        if (disciplinaAtual == null) {
            exibirAlerta("Erro", "Crie a disciplina primeiro.");
            return;
        }
        
        String nome = txtNomeAluno.getText();
        String resp = txtRespostas.getText().toUpperCase();

        if (resp.matches("[VF]{10}")) {
            disciplinaAtual.adicionarAluno(new Aluno(nome, resp));
            txtSaida.appendText("\nAluno " + nome + " adicionado.");
            txtNomeAluno.clear();
            txtRespostas.clear();
        } else {
            exibirAlerta("Erro", "Respostas devem ter 10 caracteres (V ou F).");
        }
    }

    @FXML
    public void finalizarESalvar() {
        try {
            GerenciadorArquivos.salvarDadosDisciplina(disciplinaAtual.getNome(), disciplinaAtual.getAlunos());
            txtSaida.appendText("\nArquivo salvo com sucesso!");
        } catch (Exception e) {
            exibirAlerta("Erro", "Falha ao salvar: " + e.getMessage());
        }
    }

    private void exibirAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}