package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import model.Aluno;
import model.Disciplina;
import service.GerenciadorArquivos;
import service.CalculadoraResultados;
import java.io.File;
import java.util.List;

public class MainController {

    // Seção 1 – Disciplina
    @FXML private TextField txtNomeDisciplina;
    @FXML private Label lblStatusDisciplina;
    @FXML private Circle circleStatus;

    // Seção 2 – Aluno
    @FXML private VBox painelAluno;
    @FXML private TextField txtNomeAluno, txtRespostas;
    @FXML private Label lblContadorAlunos, lblValidacaoRespostas;

    // Seção 3 – Lista de alunos
    @FXML private VBox painelListaAlunos;
    @FXML private ListView<String> listaAlunos;

    // Seção 4 – Finalizar
    @FXML private VBox painelFinalizar;

    // Seção 5 – Resultados
    @FXML private VBox painelResultados;
    @FXML private TextArea txtSaida;

    // Status bar
    @FXML private Label lblStatusBar;

    private Disciplina disciplinaAtual;

    @FXML
    public void initialize() {
        // Validação em tempo real das respostas
        txtRespostas.textProperty().addListener((obs, oldVal, newVal) -> {
            validarRespostasAoDigitar(newVal);
        });

        // Limite de 10 caracteres nas respostas
        txtRespostas.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 10) txtRespostas.setText(oldVal);
        });
    }

    @FXML
    public void criarDisciplina() {
        String nome = txtNomeDisciplina.getText().trim();
        if (nome.isEmpty()) {
            setStatus("⚠  Digite o nome da disciplina.", true);
            txtNomeDisciplina.requestFocus();
            return;
        }

        disciplinaAtual = new Disciplina(nome);

        // Atualiza badge no header
        lblStatusDisciplina.setText(nome);
        lblStatusDisciplina.setStyle("-fx-text-fill: #10b981; -fx-font-size: 11px;");
        circleStatus.setFill(Color.web("#10b981"));

        // Habilita painel de alunos e finalizar com efeito visual
        setEnabled(painelAluno, true);
        setEnabled(painelFinalizar, true);

        txtNomeDisciplina.setEditable(false);
        txtNomeDisciplina.setStyle(txtNomeDisciplina.getStyle()
                + " -fx-border-color: #10b981; -fx-border-width: 1;");

        listaAlunos.getItems().clear();
        lblContadorAlunos.setText("0 alunos");

        setStatus("Disciplina '" + nome + "' criada. Adicione os alunos.", false);
        txtNomeAluno.requestFocus();
    }

    @FXML
    public void adicionarAluno() {
        if (disciplinaAtual == null) {
            setStatus("⚠  Crie uma disciplina primeiro.", true);
            return;
        }

        String nome = txtNomeAluno.getText().trim();
        String resp = txtRespostas.getText().toUpperCase().trim();

        if (nome.isEmpty()) {
            setStatus("⚠  O nome do aluno é obrigatório.", true);
            txtNomeAluno.requestFocus();
            return;
        }
        if (!resp.matches("[VF]{10}")) {
            setStatus("⚠  As respostas devem ter exatamente 10 caracteres V ou F.", true);
            txtRespostas.requestFocus();
            return;
        }

        // Checa duplicata
        boolean duplicado = disciplinaAtual.getAlunos().stream()
                .anyMatch(a -> a.getNome().equalsIgnoreCase(nome));
        if (duplicado) {
            setStatus("⚠  Já existe um aluno com esse nome.", true);
            return;
        }

        Aluno aluno = new Aluno(nome, resp);
        disciplinaAtual.adicionarAluno(aluno);

        // Atualiza ListView
        listaAlunos.getItems().add(0, formatarItemLista(aluno));

        // Exibe painel da lista
        if (!painelListaAlunos.isVisible()) {
            painelListaAlunos.setVisible(true);
            painelListaAlunos.setManaged(true);
        }

        int total = disciplinaAtual.getAlunos().size();
        lblContadorAlunos.setText(total + (total == 1 ? " aluno" : " alunos"));

        setStatus("Aluno '" + nome + "' adicionado com sucesso.", false);

        txtNomeAluno.clear();
        txtRespostas.clear();
        lblValidacaoRespostas.setText("");
        txtNomeAluno.requestFocus();
    }

    @FXML
    public void finalizarESalvar() {
        if (disciplinaAtual == null || disciplinaAtual.getAlunos().isEmpty()) {
            setStatus("⚠  Adicione ao menos um aluno antes de processar.", true);
            return;
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Selecionar Arquivo de Gabarito");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File fileGabarito = fc.showOpenDialog(null);

        if (fileGabarito == null) return; // usuário cancelou

        try {
            String gabarito = GerenciadorArquivos.lerGabarito(fileGabarito);

            if (!gabarito.matches("[VFvf]{10}")) {
                exibirAlerta("Gabarito Inválido",
                        "O arquivo deve conter exatamente 10 caracteres V ou F na primeira linha.\n" +
                                "Conteúdo lido: \"" + gabarito + "\"");
                return;
            }
            gabarito = gabarito.toUpperCase();

            List<Aluno> lista = disciplinaAtual.getAlunos();
            for (Aluno a : lista) a.calcularNota(gabarito);

            // Salva arquivo ordenado alfabeticamente
            CalculadoraResultados.ordenarAlfabetica(lista);
            String arqAlfabetico = disciplinaAtual.getNome() + "_ALFABETICA";
            GerenciadorArquivos.salvarDadosDisciplina(arqAlfabetico, lista);

            // Salva arquivo ordenado por nota
            CalculadoraResultados.ordenarPorNota(lista);
            String arqNotas = disciplinaAtual.getNome() + "_NOTAS";
            GerenciadorArquivos.salvarDadosDisciplina(arqNotas, lista);

            double media = CalculadoraResultados.calcularMediaTurma(lista);
            int maior = lista.get(0).getAcertos();
            int menor = lista.get(lista.size() - 1).getAcertos();

            // Monta saída formatada
            StringBuilder sb = new StringBuilder();
            sb.append("GABARITO: ").append(gabarito).append("\n");
            sb.append("─".repeat(38)).append("\n");
            sb.append(String.format("%-3s %-25s %s%n", "Pos", "Nome", "Acertos"));
            sb.append("─".repeat(38)).append("\n");

            for (int i = 0; i < lista.size(); i++) {
                Aluno a = lista.get(i);
                sb.append(String.format("%-3d %-25s %d/10%n",
                        i + 1, a.getNome(), a.getAcertos()));
            }

            sb.append("─".repeat(38)).append("\n");
            sb.append(String.format("Média: %.2f | Maior: %d | Menor: %d%n", media, maior, menor));
            sb.append("\n✓ Arquivos gerados:\n");
            sb.append("  · ").append(arqAlfabetico).append(".txt\n");
            sb.append("  · ").append(arqNotas).append(".txt");

            txtSaida.setText(sb.toString());
            painelResultados.setVisible(true);
            painelResultados.setManaged(true);

            setStatus("Processamento concluído. Arquivos exportados com sucesso.", false);

        } catch (Exception e) {
            exibirAlerta("Erro ao processar", e.getMessage());
            setStatus("⚠  Erro: " + e.getMessage(), true);
        }
    }

    @FXML
    public void limparTudo() {
        disciplinaAtual = null;
        txtNomeDisciplina.clear();
        txtNomeDisciplina.setEditable(true);
        txtNomeDisciplina.setStyle("");
        txtNomeAluno.clear();
        txtRespostas.clear();
        txtSaida.clear();
        listaAlunos.getItems().clear();
        lblContadorAlunos.setText("0 alunos");
        lblValidacaoRespostas.setText("");

        lblStatusDisciplina.setText("Sem disciplina");
        lblStatusDisciplina.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11px;");
        circleStatus.setFill(Color.web("#374151"));

        painelListaAlunos.setVisible(false);
        painelListaAlunos.setManaged(false);
        painelResultados.setVisible(false);
        painelResultados.setManaged(false);

        setEnabled(painelAluno, false);
        setEnabled(painelFinalizar, false);

        setStatus("Pronto. Crie uma disciplina para começar.", false);
    }

    /** Valida respostas enquanto o usuário digita, com feedback visual em tempo real. */
    private void validarRespostasAoDigitar(String val) {
        String upper = val.toUpperCase();
        int len = upper.length();
        if (len == 0) {
            lblValidacaoRespostas.setText("");
            return;
        }
        if (!upper.matches("[VF]*")) {
            lblValidacaoRespostas.setText("✗  Use apenas V ou F");
            lblValidacaoRespostas.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 11px;");
        } else if (len < 10) {
            lblValidacaoRespostas.setText(len + "/10 — faltam " + (10 - len));
            lblValidacaoRespostas.setStyle("-fx-text-fill: #f59e0b; -fx-font-size: 11px;");
        } else {
            boolean tudo = upper.equals("VVVVVVVVVV") || upper.equals("FFFFFFFFFF");
            if (tudo) {
                lblValidacaoRespostas.setText("⚠  Todas iguais → nota será 0");
                lblValidacaoRespostas.setStyle("-fx-text-fill: #f59e0b; -fx-font-size: 11px;");
            } else {
                lblValidacaoRespostas.setText("✓  Respostas válidas");
                lblValidacaoRespostas.setStyle("-fx-text-fill: #10b981; -fx-font-size: 11px;");
            }
        }
    }

    /** Habilita/desabilita painéis com opacidade. */
    private void setEnabled(VBox painel, boolean enabled) {
        painel.setOpacity(enabled ? 1.0 : 0.45);
        painel.setDisable(!enabled);
    }

    /** Atualiza status bar. */
    private void setStatus(String msg, boolean erro) {
        lblStatusBar.setText(msg);
        lblStatusBar.setStyle(erro
                ? "-fx-text-fill: #ef4444; -fx-font-size: 11px;"
                : "-fx-text-fill: #6b7280; -fx-font-size: 11px;");
    }

    /** Formata item para a ListView. */
    private String formatarItemLista(Aluno a) {
        return a.getNome() + "  ·  " + a.getRespostas();
    }

    private void exibirAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}