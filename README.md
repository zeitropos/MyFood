##  Descrição

**MyFood** é um sistema para gerenciamento de uma aplicação de delivery.  
Este repositório contém a implementação do **Milestone 1**, que cobrem os testes **US1 a US4**, incluindo:

- **US1 – Criação de contas** (clientes e donos de restaurante)
- **US2 – Criação de restaurantes** (apenas donos)
- **US3 – Criação de produtos** (vinculados a restaurantes)
- **US4 – Criação de pedidos** (clientes podem montar carrinho, adicionar/remover produtos e fechar pedido)

---

##  Arquitetura do Projeto

O projeto segue tanto o padrão Facade como a arquitetura em camadas, melhorando a organização, facilitando testes e escalabilidade.
### Visão geral do sistema:
| Caminho | Descrição |
| :--- | :--- |
| `src/br/ufal/ic/myfood/Facade.java` | Interface de comunicação para o EasyAccept |
| `src/br/ufal/ic/myfood/Gerenciador.java` | Orquestrador central (coordenador dos managers) |
| `src/br/ufal/ic/myfood/models/Pessoa.java` | Classe abstrata base para usuários |
| `src/br/ufal/ic/myfood/models/Usuario.java` | Cliente (herda de `Pessoa`) |
| `src/br/ufal/ic/myfood/models/Proprietario.java` | Dono de restaurante (herda de `Pessoa`) |
| `src/br/ufal/ic/myfood/models/Empresa.java` | Empresa (restaurante) |
| `src/br/ufal/ic/myfood/models/Produto.java` | Produto vinculado a uma empresa |
| `src/br/ufal/ic/myfood/models/Pedido.java` | Pedido com lista de produtos |
| `src/br/ufal/ic/myfood/managers/UsuarioManager.java` | Lógica de criação, login, consulta de usuários |
| `src/br/ufal/ic/myfood/managers/EmpresaManager.java` | Lógica de criação, listagem, consulta de empresas |
| `src/br/ufal/ic/myfood/managers/ProdutoManager.java` | Lógica de criação, edição, listagem de produtos |
| `src/br/ufal/ic/myfood/managers/PedidoManager.java` | Lógica de pedidos, adição/remoção de produtos |
| `src/br/ufal/ic/myfood/exceptions/` | Exceções personalizadas (ex: `UsuarioJaExisteException`) |
| `src/br/ufal/ic/myfood/persistence/PersistenceManager.java` | Persistência em XML usando `XMLEncoder`/`XMLDecoder` |
| `lib/` | Contém `easyaccept.jar` |
| `tests/` | Scripts de teste `us1_1.txt` até `us4_2.txt` |
| `myfood_data.xml` | Arquivo gerado automaticamente com os dados persistidos |


### Principais decisões técnicas

- **Herança** – `Usuario` (cliente) e `Proprietario` herdam de `Pessoa`, facilitando a adição futura de novos stakeholders como `Entregador` (milestone 2).
- **Managers especializados** – Cada entidade possui um manager com sua lógica de negócio, mantendo o código coeso e testável.
- **Gerenciador central** – A classe `Gerenciador` orquestra os managers e centraliza o carregamento/salvamento de dados.
- **Persistência nativa** – Uso de `XMLEncoder`/`XMLDecoder` para salvar os dados em `myfood_data.xml`.  
- **IDs sequenciais** – Gerados automaticamente e restaurados após carregar os dados, conforme exigido pelos testes.
- **Tratamento de exceções** – Exceções específicas (`CampoInvalidoException`, `UsuarioJaExisteException`, etc.) com mensagens exatas que os testes esperam.

---

##  Como executar

### Pré‑requisitos

- **Java 21** (ou superior)

### Passos

1. **Clone o repositório**
2. **Execute o arquivo `Main.java`**

Ao executar o arquivo main os testes serão executados sequencialmente.
