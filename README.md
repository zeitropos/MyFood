##  Descrição

**MyFood** é um sistema para gerenciamento de uma aplicação de delivery.  

Este repositório contém a implementação do **Milestone 1 e 2**, que cobrem os testes **US1 a US8**, incluindo:

- **US1 – Criação de contas** (Permite a um usuário criar uma conta no MyFood, O usuário poderá ser de dois tipos, Cliente e Dono de Empresa)
- **US2 – Criação de restaurantes** (Permite que um usuário do tipo dono de empresa crie seu restaurante)
- **US3 – Criação de produtos** (Permite que um usuário do tipo dono de restaurante crie produtos para sua empresa)
- **US4 – Criação de pedidos** (Permite que clientes adicionem produtos de uma empresa a seu carrinho e realizem a compra)
- **US5 – Criação de mercados** (Permite que um usuário do tipo dono de empresa crie seu mercado)
- **US6 – Criação de farmácias** (Permite que um usuário do tipo dono de empresa crie sua farmácia)
- **US7 – Criação de entregadores** (Permite a um usuário criar uma conta no MyFood; O usuário poderá ser de três tipos: Cliente, Dono de Empresa ou Entregador)
- **US8 – Sistema de entregas** (Permite a um usuário receber seus produtos, e que os entregadores possam saber os pedidos existentes e iniciar o processo de entrega)

---

##  Arquitetura do Projeto

O projeto segue tanto o padrão Facade quanto a arquitetura em camadas, garantindo organização, facilitando testes e escalabilidade.

### Visão geral do sistema:
| Caminho | Descrição |
| :--- | :--- |
| `src/br/ufal/ic/myfood/Facade.java` | Interface de comunicação para o EasyAccept |
| `src/br/ufal/ic/myfood/Gerenciador.java` | Orquestrador central (coordenador dos managers) |
| `src/br/ufal/ic/myfood/models/Pessoa.java` | Classe abstrata base para usuários |
| `src/br/ufal/ic/myfood/models/Usuario.java` | Cliente (herda de `Pessoa`) |
| `src/br/ufal/ic/myfood/models/Proprietario.java` | Dono de restaurante (herda de `Pessoa`) |
| `src/br/ufal/ic/myfood/models/Entregador.java` | Entregador (herda de `Pessoa`) |
| `src/br/ufal/ic/myfood/models/Empresa.java` | Classe abstrata base para empresas |
| `src/br/ufal/ic/myfood/models/Restaurante.java` | Restaurante (herda de `Empresa`) |
| `src/br/ufal/ic/myfood/models/Mercado.java` | Mercado (herda de `Empresa`) |
| `src/br/ufal/ic/myfood/models/Farmacia.java` | Farmácia (herda de `Empresa`) |
| `src/br/ufal/ic/myfood/models/Produto.java` | Produto vinculado a uma empresa |
| `src/br/ufal/ic/myfood/models/Pedido.java` | Pedido com lista de produtos e seu estado (ex: aberto, preparando, etc) |
| `src/br/ufal/ic/myfood/models/Entrega.java` | Classe que representa uma entrega |
| `src/br/ufal/ic/myfood/managers/UsuarioManager.java` | Lógica de criação, login, consulta de usuários |
| `src/br/ufal/ic/myfood/managers/EmpresaManager.java` | Lógica de criação, listagem, consulta de empresas, cadastro de entregadores |
| `src/br/ufal/ic/myfood/managers/ProdutoManager.java` | Lógica de criação, edição, listagem de produtos |
| `src/br/ufal/ic/myfood/managers/PedidoManager.java` | Lógica de pedidos, adição/remoção de produtos, fechamento, liberação, entregas |
| `src/br/ufal/ic/myfood/persistence/PersistenceManager.java` | Persistência em XML usando `XMLEncoder`/`XMLDecoder` |
| `src/br/ufal/ic/myfood/exceptions/` | Exceções personalizadas (ex: `UsuarioJaExisteException`, `CampoInvalidoException`, `PedidoNaoEncontradoException`, etc) |
| `lib/` | Contém `easyaccept.jar` |
| `tests/` | Scripts de teste `us1_1.txt` até `us8_2.txt` |
| `myfood_data.xml` | Arquivo gerado automaticamente com os dados persistidos |


### Principais decisões técnicas

- **Herança** – `Usuario`, `Proprietario` e `Entregador` herdam de `Pessoa`; `Restaurante`, `Mercado` e `Farmacia` herdam de `Empresa`.
- **Managers especializados** – Cada entidade possui um manager com sua lógica de negócio, mantendo o código coeso e facilitando testes.
- **Gerenciador central** – A classe `Gerenciador` orquestra os managers e centraliza o carregamento/salvamento de dados.
- **Persistência nativa** – Uso de `XMLEncoder`/`XMLDecoder` para salvar os dados em `myfood_data.xml`.  
- **IDs sequenciais** – Gerados automaticamente e restaurados após carregar os dados, conforme exigido pelos testes.
- **Tratamento de exceções** – Exceções específicas com as mensagens exatas que os testes esperam.

---

##  Como executar

### Pré‑requisitos

- **Java 21** (ou superior)

### Passos

1. **Clone o repositório**

2. **Execute o arquivo `Main.java`**

Ao executar o arquivo Main os testes serão executados sequencialmente.
