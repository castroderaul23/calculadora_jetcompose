# 📱 Calculadora Android (Jetpack Compose)

Este projeto consiste no desenvolvimento de uma **calculadora funcional para Android**, criada utilizando **Kotlin** e **Jetpack Compose**, com foco em praticar conceitos de **desenvolvimento mobile moderno**, gerenciamento de estado e construção de interfaces declarativas.

A aplicação permite realizar operações matemáticas básicas através de uma interface simples e intuitiva.

---

## 📸 Interface da Aplicação

![Calculadora](img/calculadora.png)

---

## 🚀 Tecnologias Utilizadas

* **Kotlin**
* **Android Studio**
* **Jetpack Compose**
* **Material Design 3**

Essas tecnologias permitem a criação de interfaces modernas utilizando o paradigma **declarativo**, facilitando a manutenção e evolução da aplicação.

---

## 🧠 Funcionalidades Implementadas

A calculadora possui as seguintes funcionalidades:

* Operações matemáticas básicas

  * Adição (+)
  * Subtração (-)
  * Multiplicação (×)
  * Divisão (÷)

* Entrada de números inteiros e decimais

* Botão **AC** para limpar completamente a operação

* Botão **⌫ (Backspace)** para apagar o último dígito digitado

* Tratamento de erros (ex: divisão por zero)

* Atualização automática do display utilizando **state management do Compose**

---

## 🏗️ Estrutura do Projeto

A aplicação é composta principalmente pelos seguintes componentes:

### `MainActivity.kt`

Responsável por iniciar a aplicação Android e carregar a interface principal.

### `CalculatorScreen`

Composable responsável por:

* Gerenciar os **estados da calculadora**
* Controlar a lógica das operações matemáticas
* Construir o layout principal da tela

### `CalcButton`

Composable reutilizável utilizado para criar todos os botões da calculadora, permitindo:

* Definir o texto do botão
* Definir a cor
* Definir a ação ao clicar

---

## ⚙️ Gerenciamento de Estado

O projeto utiliza:

```
rememberSaveable
mutableStateOf
```

para armazenar estados importantes da calculadora, como:

* Valor exibido no display
* Primeiro operando
* Operador selecionado
* Controle da entrada do segundo número

Isso garante que a interface seja **reativa**, atualizando automaticamente quando o estado muda.

---

## 📐 Layout da Interface

A interface foi construída utilizando os componentes do **Jetpack Compose**:

* `Column` → organização vertical da tela
* `Row` → organização das linhas de botões
* `Box` → alinhamento do display
* `Modifier.weight()` → distribuição uniforme dos botões

Estrutura visual:

```
DISPLAY

AC   ⌫

7  8  9  ÷
4  5  6  ×
1  2  3  -
0  .  =  +
```

---

## 📚 Objetivo do Projeto

Este projeto foi desenvolvido com o objetivo de praticar:

* Desenvolvimento Android com **Jetpack Compose**
* Gerenciamento de **estado em interfaces declarativas**
* Criação de **componentes reutilizáveis**
* Organização de layout responsivo

---

## 👨‍💻 Autor

Projeto desenvolvido por **Raul Castro**.

Estudante de **Engenharia de Software** e interessado em áreas como **dados, desenvolvimento e tecnologia**.
