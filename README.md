# Quem Está Em Casa?
Projeto em Java que escaneia a rede local e exibe informações dos dispositivos conectados, com interface moderna feita em JavaFX. Utiliza múltiplas threads, comandos ARP e análise de portas para identificar tipos de dispositivos. Desenvolvido como projeto de estudo e portfólio para demonstrar conhecimentos em redes e interfaces gráficas.

👀 Quem Está em Casa?
"Quem Está em Casa?" é um aplicativo de scanner de rede local com interface gráfica desenvolvido em JavaFX. Ele permite detectar todos os dispositivos conectados à rede, identificar possíveis tipos de dispositivos com base em portas abertas e endereços MAC, e exibir as informações de forma visual, intuitiva e agradável. É ideal para quem quer entender quem está conectado ao Wi-Fi — seja em casa, em uma empresa ou laboratório.

![Imagem principal do programa](https://github.com/luccc777/QuemEst-/blob/main/programa.jpg)

---
✨ Destaques
⚡ Escaneia toda a sub-rede local rapidamente.

🔍 Exibe IP, MAC, nome e uma descrição inteligente do tipo de dispositivo.

🖥️ Interface gráfica moderna feita com JavaFX, com foco em usabilidade e visual clean.

🧠 Identificação heurística baseada em:

Portas TCP abertas (ex: RDP, SSH, RTSP, HTTP...)

Prefixo do endereço MAC (ex: Apple, Android, Xiaomi, HP...)

👨‍💻 Ótimo para portfólio: mistura conhecimentos em rede, programação concorrente, JavaFX e estilo visual (CSS).

---
## 🧠 Motivação e Justificativas Técnicas

Este projeto surgiu da ideia de criar uma ferramenta simples, mas poderosa, que ajude a entender rapidamente quais dispositivos estão conectados a uma rede. A ideia é unir conhecimentos práticos de:

- Redes de computadores (ARP, portas TCP, IPs locais);
- Threads e paralelismo com `ExecutorService`;
- Interfaces ricas com JavaFX;
- Estilização avançada com CSS.

As tecnologias escolhidas foram:

| Tecnologia | Justificativa |
|------------|---------------|
| **Java** | Linguagem robusta e multiplataforma, muito utilizada em aplicações corporativas. |
| **JavaFX** | Permite criar interfaces modernas e altamente personalizáveis com facilidade. |
| **CSS personalizado** | Aplicação com visual escuro moderno e cores diferenciadas para tipos de informação. |
| **Multithreading** | Escaneamento rápido da rede utilizando múltiplas threads. |
| **Processo ARP** | Uso do `arp -a` para mapear IPs e MACs ativos na rede. |


---

## 🖼️ Interface

| Coluna       | Descrição |
|--------------|-----------|
| **Nome**     | Editável pelo usuário. Pode atribuir apelidos a cada dispositivo. |
| **IP**       | IP detectado na rede. |
| **MAC**      | Endereço MAC obtido via ARP. |
| **Descrição**| Resultado da identificação heurística. |
| **Identificar** | Botão que ativa o scanner de portas + heurísticas. |

---

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos

- Java JDK 11 ou superior instalado
- JavaFX SDK compatível com sua versão do Java

---

### 🧰 Passo a passo para compilar e rodar

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/quem-esta-em-casa.git
cd quem-esta-em-casa
 ```

2. Compile os arquivos Java:

``` bash
javac --module-path /CAMINHO/para/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml application/*.java
```

3. Execute a aplicação:

```bash
java --module-path /CAMINHO/para/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml application.Main
```

Exemplo no Windows
``` bash
javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls,javafx.fxml application/*.java
java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls,javafx.fxml application.Main
```

---

💡 Possíveis Melhorias Futuras
Exportar os resultados para CSV ou JSON.

Rodar escaneamento contínuo em background.

Permitir bloqueio de MACs não reconhecidos (via integração com roteador).

Integração com APIs para identificação de fabricantes via MAC.

Modo CLI para servidores headless.

---

👨‍💻 Autor
Lucas Souza
  <a href="https://github.com/luccc777">
    <img height="180em" src="https://github-readme-stats-eight-theta.vercel.app/api/top-langs/?username=luccc777&layout=compact&langs_count=8&theme=algolia"/>
  </a>
Projeto de portfólio para demonstrar habilidades em redes, interfaces e JavaFX.
