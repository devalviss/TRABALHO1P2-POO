 # `TRABALHO1P2-POO`
Este projeto consiste em uma ferramenta de linha de comando desenvolvida para automatizar a correção de provas objetivas do tipo Verdadeiro (V) ou Falso (F) e a gestão de notas de múltiplas disciplinas
# 📝 Sistema de Correção de Provas (V ou F)

Este projeto é uma ferramenta desenvolvida em **Java** para automatizar a gestão, correção e análise de desempenho de provas objetivas de 10 questões do tipo Verdadeiro ou Falso.

---

## 📋 Descrição da Task

O sistema processa ficheiros de texto onde cada linha representa a tentativa de um aluno, seguindo o padrão:
`[10 Respostas V/F]` + `[TAB]` + `[Nome do Aluno]`

### 🚀 Funcionalidades Principais

1.  **Gestão de Disciplinas:**
    * Permite criar múltiplos ficheiros (um por disciplina).
    * Inserção dinâmica de alunos e as suas respetivas respostas.
2.  **Processamento de Resultados:**
    * Comparação automática com um ficheiro de **gabarito oficial**.
    * **Regra de Validação:** Alunos que respondam apenas 'V' ou apenas 'F' em todas as questões recebem nota **0** (anulação por suspeita de fraude/erro).
3.  **Relatórios Automáticos:**
    * Gera um ficheiro com ordenação **alfabética** (Nome e Nota).
    * Gera um ficheiro ordenado por **nota decrescente**, incluindo a **média geral da turma** ao final.
    * Visualização imediata dos dados processados no terminal.

---

## 📂 Estrutura de Ficheiros

O programa foi desenhado para organizar os dados da seguinte forma:

* **Entrada:** Ficheiro `.txt` com o nome da disciplina.
* **Gabarito:** Ficheiro `.txt` contendo uma única linha com as 10 respostas corretas.
* **Saída:** Dois novos ficheiros gerados após o processamento dos resultados.
