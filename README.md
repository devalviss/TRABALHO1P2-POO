# ` TRABALHO1P2-POO `

Este projeto consiste em uma ferramenta desenvolvida para automatizar a correção de provas objetivas do tipo Verdadeiro (V) ou Falso (F) e a gestão de notas de múltiplas disciplinas, contando com uma **Interface Gráfica (GUI)** moderna em JavaFX.

---

## 📝 Sistema de Correção de Provas (V ou F)

Este projeto é uma ferramenta desenvolvida em **Java** para automatizar a gestão, correção e análise de desempenho de provas objetivas de 10 questões do tipo Verdadeiro ou Falso.

---

## 🛠️ Como Configurar e Rodar (Instruções Importantes)

Como este projeto utiliza **JavaFX**, ele possui dependências externas que não vêm no JDK padrão. Siga estes passos para rodar em qualquer IDE ou sistema operacional:

### 1. Pré-requisitos

* **JDK 21 ou superior** (testado no Java 25)
* **JavaFX SDK**: Baixe a versão correspondente ao seu sistema operacional (Windows ou Linux) no site da Gluon
* **Scene Builder (Opcional)**: Para visualizar ou editar o layout em `src/view/MainScene.fxml`

---

### 2. Configuração na IDE (Eclipse, IntelliJ, VS Code, NetBeans)

Para que o projeto funcione corretamente:

1. Adicione todos os arquivos `.jar` da pasta `lib` do JavaFX SDK ao **Build Path** (ou Libraries/Project Structure).
2. Configure os **VM Arguments** na execução do projeto:

```bash
--module-path "/CAMINHO/PARA/SEU/JAVAFX-SDK/lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
```

> ⚠️ Substitua `/CAMINHO/PARA/SEU/...` pelo caminho real onde você extraiu o JavaFX.

---

## 📋 Descrição da Task

O sistema processa ficheiros de texto onde cada linha representa a tentativa de um aluno, seguindo o padrão:

```
[10 Respostas V/F] + [TAB] + [Nome do Aluno]
```

---

## 🚀 Funcionalidades Principais

* **Interface Gráfica**

  * Cadastro de alunos
  * Visualização de resultados em tempo real

* **Gestão de Disciplinas**

  * Suporte a múltiplos ficheiros (um por disciplina)

* **Processamento de Resultados**

  * Comparação automática com um ficheiro de **gabarito oficial**
  * Regra de validação:

    * Se o aluno responder apenas `V` ou apenas `F` → nota **0**

* **Relatórios Automáticos**

  * Arquivo ordenado **alfabeticamente** (Nome + Nota)
  * Arquivo ordenado por **nota decrescente**
  * Cálculo da **média geral da turma**
  * Visualização imediata na interface

---

## 📂 Estrutura do Projeto (MVC)

O projeto segue o padrão **Model-View-Controller**:

```
src/
├── main/
│   └── MainApp.java
├── controller/
│   └── MainController.java
├── model/
│   ├── Aluno.java
│   └── Disciplina.java
├── service/
│   └── (Regras de negócio e manipulação de arquivos)
└── view/
    └── MainScene.fxml
```

---

## 📁 Estrutura de Ficheiros

### Entrada

* Arquivo `.txt` com respostas dos alunos (nome da disciplina)

### Gabarito

* Arquivo `.txt` com uma linha contendo as 10 respostas corretas

### Saída

* Arquivo ordenado por **nome**
* Arquivo ordenado por **nota**
* Média da turma incluída

---

## 🛑 Dica para Colaboradores

Se sua IDE não reconhecer corretamente o projeto:

1. Crie um novo projeto JavaFX localmente
2. Copie a pasta `src` deste repositório para o novo projeto

Isso garante que as configurações de módulos funcionem corretamente no seu sistema.

---

