# 🚗 FrotaCerta

Aplicativo Android nativo para **gestão de locação de veículos**, desenvolvido em Kotlin com **Jetpack Compose**, arquitetura **MVVM**, persistência local com **Room**, integração com a agenda do dispositivo e consumo de API REST com **Retrofit 2**.

O projeto foi criado para atender aos requisitos de uma atividade acadêmica de desenvolvimento Android, priorizando organização em camadas, persistência local, navegação declarativa e integração com recursos do sistema operacional.

---

## ✨ Funcionalidades

### 📊 Dashboard
- Exibe somente locações com status **ATIVA**.
- Mostra veículo, placa, cliente, telefone, data de saída e data prevista de entrega.
- Calcula automaticamente os dias restantes.
- Destaca locações em atraso.
- Possui acesso rápido à frota e à criação de uma nova locação.
- Permite testar a integração REST através do botão **Sincronizar API**.

### 🚙 Gestão de veículos
- Cadastro de veículos com:
  - marca;
  - modelo;
  - placa;
  - ano;
  - valor da diária.
- Validação de placas nos formatos:
  - antigo: `AAA-1234`;
  - Mercosul: `AAA1A23`.
- Controle de status:
  - `DISPONIVEL`;
  - `ALUGADO`;
  - `MANUTENCAO`.
- Listagem da frota com atualização reativa via Flow/StateFlow.

### 👤 Seleção de clientes pela agenda
- Utiliza `ContactsContract` e `ContentResolver`.
- Solicita a permissão `READ_CONTACTS` em tempo de execução.
- Exibe explicação quando a permissão é negada.
- Permite tentar novamente.
- Busca contatos em tempo real pelo nome.
- Retorna ID, nome e telefone do contato selecionado para o fluxo de locação.

> O cliente não precisa ser cadastrado manualmente no FrotaCerta. Ele é selecionado diretamente da agenda do Android e armazenado no banco local quando uma locação é confirmada.

### 📝 Nova locação
- Seleção de cliente pela agenda do dispositivo.
- Exibe apenas veículos com status **DISPONÍVEL**.
- Seleção da data de saída e da data prevista de devolução com Material 3 `DatePicker`.
- Cálculo automático da quantidade de dias.
- Cálculo automático do valor estimado:

```text
quantidade de dias × valor da diária
```

- Ao confirmar a locação:
  - o cliente é salvo ou reutilizado no banco local;
  - a locação é criada com status `ATIVA`;
  - o veículo passa automaticamente para `ALUGADO`;
  - o Dashboard é atualizado através do Flow do Room.

A criação da locação utiliza uma **transação atômica**, garantindo que as alterações relacionadas sejam executadas como uma única operação.

---

## 🧱 Arquitetura

O projeto utiliza arquitetura **MVVM**, separando as responsabilidades entre interface, regras de apresentação e acesso a dados.

```text
┌──────────────────────────────┐
│            UI                │
│ Jetpack Compose + Material 3 │
│ Screens + Navigation         │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│          ViewModel           │
│ StateFlow + Coroutines       │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│         Repository           │
│ Coordena fontes de dados     │
└──────────┬─────────┬─────────┘
           │         │
           ▼         ▼
     ┌──────────┐ ┌───────────┐
     │   Room   │ │ Retrofit  │
     │ Database │ │ REST API  │
     └──────────┘ └───────────┘
```

### Estrutura de pacotes

```text
com.marcus.frotacerta
│
├── data
│   ├── contacts
│   ├── di
│   ├── local
│   │   ├── dao
│   │   ├── entity
│   │   └── relation
│   ├── remote
│   │   └── dto
│   └── repository
│
├── domain
│   ├── model
│   └── util
│
├── ui
│   ├── contacts
│   ├── dashboard
│   ├── navigation
│   ├── rental
│   ├── theme
│   └── vehicle
│
├── FrotaCertaApplication.kt
└── MainActivity.kt
```

---

## 🗄️ Banco de dados local

O aplicativo utiliza **Room 3** com três tabelas principais:

### `vehicles`
Armazena os veículos da frota.

### `clients`
Armazena localmente os contatos utilizados em locações.

### `rentals`
Armazena as locações realizadas.

### Relacionamentos

```text
VehicleEntity
     ▲
     │ vehicleId
     │
RentalEntity
     │
     │ clientId
     ▼
ClientEntity
```

`RentalEntity` possui chaves estrangeiras para veículo e cliente, e o projeto utiliza `@Relation` através de `RentalDetails` para recuperar os dados relacionados.

Um detalhe importante da modelagem é que a locação armazena uma **cópia do valor da diária e do valor total estimado**. Assim, se o preço do veículo for alterado futuramente, uma locação já registrada mantém os valores utilizados no momento em que foi criada.

---

## 🌐 Retrofit e API REST

A camada remota utiliza **Retrofit 2** com **Gson**.

A interface `FrotaApiService` contém a estrutura dos endpoints de veículos e locações:

```text
GET  /vehicles
POST /vehicles
GET  /rentals
POST /rentals
```

Para demonstrar e validar uma chamada HTTP real durante o desenvolvimento, o aplicativo também utiliza:

```text
GET /posts/1
```

através da API pública **JSONPlaceholder**.

Ao tocar em **Sincronizar API** no Dashboard, o aplicativo executa a chamada com Retrofit e apresenta os estados:

```text
Idle → Loading → Success
              ↘ Error
```

> A chamada `/posts/1` é utilizada como demonstração funcional de consumo REST. Os endpoints `/vehicles` e `/rentals` representam a estrutura prevista para integração com um backend específico do FrotaCerta.

---

## 🔄 Navegação

O aplicativo segue o padrão **Single Activity** e utiliza **Navigation Compose**.

Fluxo principal:

```text
Dashboard
   ├── Frota
   │    └── Novo veículo
   │
   └── Nova locação
        └── Selecionar cliente
             └── Agenda Android
```

A seleção do contato retorna os dados para a tela anterior utilizando `SavedStateHandle`:

```text
contact_id
contact_name
contact_phone
```

---

## 🛠️ Tecnologias utilizadas

- Kotlin
- Android SDK
- Jetpack Compose
- Material 3
- MVVM
- ViewModel
- StateFlow / Flow
- Kotlin Coroutines
- Navigation Compose
- SavedStateHandle
- Room 3
- Foreign Keys e `@Relation`
- Content Providers
- `ContactsContract`
- Retrofit 2
- Gson
- Gradle Kotlin DSL
- KSP

---

## 🔐 Permissões

O aplicativo utiliza:

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.INTERNET" />
```

`READ_CONTACTS` é solicitada em tempo de execução antes da leitura da agenda do dispositivo.

---

## ▶️ Como executar

### Requisitos

- Android Studio compatível com o projeto;
- JDK 11;
- Android SDK instalado;
- dispositivo físico ou emulador Android com **API 26 ou superior**;
- acesso à internet para testar a chamada REST.

### Passos

1. Clone o repositório:

```bash
git clone https://github.com/Mrc0345/FrotaCerta.git
```

2. Abra a pasta `FrotaCerta` no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Selecione um emulador ou dispositivo físico.
5. Execute o módulo `app`.
6. Ao acessar a seleção de cliente, permita o acesso aos contatos.

Para testar a seleção de clientes em um emulador vazio, cadastre primeiro um contato no aplicativo **Contatos** do próprio Android.

---

## 🧪 Fluxo sugerido para teste

1. Abra **Frota**.
2. Cadastre um veículo.
3. Confirme que ele aparece como **DISPONÍVEL**.
4. Cadastre um contato na agenda do Android, caso ainda não exista.
5. Volte ao Dashboard.
6. Toque em **Nova locação**.
7. Selecione o cliente.
8. Selecione um veículo disponível.
9. Escolha as datas.
10. Confira o cálculo de dias e do valor total.
11. Confirme a locação.
12. Verifique se a locação apareceu no Dashboard.
13. Abra a frota e confirme que o veículo agora aparece como **ALUGADO**.
14. Volte ao Dashboard e toque em **Sincronizar API**.
15. Verifique a mensagem de sucesso retornada pela integração REST.

---

## 📸 Screenshots

Adicione aqui as capturas das principais telas antes da entrega.

Sugestão de organização:

```text
docs/screenshots/
├── dashboard.png
├── frota.png
├── novo-veiculo.png
├── contatos.png
└── nova-locacao.png
```

Depois, substitua esta seção pelas imagens:

```md
### Dashboard
![Dashboard](docs/screenshots/dashboard.png)

### Frota
![Frota](docs/screenshots/frota.png)

### Cadastro de veículo
![Cadastro de veículo](docs/screenshots/novo-veiculo.png)

### Seleção de contato
![Seleção de contato](docs/screenshots/contatos.png)

### Nova locação
![Nova locação](docs/screenshots/nova-locacao.png)
```

---

## 📦 Build do APK

Para gerar um APK pelo Android Studio:

```text
Build → Generate App Bundles or APKs → Generate APKs
```

Para uma versão de entrega assinada, utilize:

```text
Build → Generate Signed App Bundle or APK
```

---

## 📌 Status do projeto

- [x] Dashboard com locações ativas
- [x] Cadastro e listagem de veículos
- [x] Validação de placa e diária
- [x] Persistência local com Room
- [x] Relacionamentos entre veículo, cliente e locação
- [x] Seleção de clientes pela agenda Android
- [x] Permissão de contatos em tempo de execução
- [x] Busca de contatos
- [x] Nova locação com seleção de veículo
- [x] DatePicker
- [x] Cálculo automático de diárias
- [x] Transação atômica de criação da locação
- [x] Navigation Compose
- [x] SavedStateHandle
- [x] Retrofit 2
- [x] Chamada REST real testada
- [ ] Adicionar screenshots finais ao README
- [ ] Gerar APK de entrega

---

## 📂 Repositório

**GitHub:** https://github.com/Mrc0345/FrotaCerta

O projeto possui histórico incremental de commits demonstrando a evolução da implementação.

---

## 🎓 Contexto acadêmico

Projeto desenvolvido como atividade de avaliação em desenvolvimento Android, aplicando conceitos de interface declarativa, arquitetura em camadas, persistência local, integração com recursos do sistema operacional, navegação e consumo de serviços REST.
