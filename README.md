# BeeFree

<!--toc:start-->
- [BeeFree](#beefree)
  - [✨ Autor](#-autor)
  - [💻 Projeto](#-projeto)
    - [BeeFree](#beefree)
    - [Principais funcionalidades](#principais-funcionalidades)
  - [🏛️ Arquitetura](#%EF%B8%8F-arquitetura)
  - [🧪 Testes](#-testes)
  - [🧰 Tecnologias](#-tecnologias)
  - [🚀 Como executar](#-como-executar)
<!--toc:end-->

## ✨ Autor

| Nome               | Redes Sociais                                               |
| ------------------ | ----------------------------------------------------------- |
| Wesley Alves Rolim | [LinkedIn](https://www.linkedin.com/in/wesley-alves-rolim/) |

## 💻 Projeto

### BeeFree

O **BeeFree** é um aplicativo móvel (mHealth) desenvolvido como Trabalho de Conclusão de Curso, com o objetivo de apoiar indivíduos no enfrentamento do uso problemático de pornografia (PPU). O aplicativo integra intervenções digitais baseadas em atividades e mensagens psicoeducativas fundamentadas em evidências científicas.

Diante de barreiras financeiras, geográficas e do estigma que limitam o acesso ao acompanhamento profissional, o aplicativo oferece uma alternativa acessível: uma triagem clínica estruturada com instrumentos validados (PPCS-6 e avaliação de incongruência moral) que define um perfil psicológico personalizado. Isso direciona as intervenções segundo princípios da TCC (Terapia Cognitivo-Comportamental), da ACT (Terapia de Aceitação e Compromisso) ou de perfis híbridos. O diferencial em relação às soluções existentes, focadas em bloqueio de conteúdo, é a atuação nos momentos críticos de vulnerabilidade por meio de intervenções em momento ecológico (EMA/EMI).

### Principais funcionalidades

- **Onboarding com triagem clínica**: aplicação da PPCS-6 e avaliação de incongruência moral, com cálculo de perfil de tratamento personalizado.
- **Check-in diário**: acompanhamento contínuo adaptado ao perfil do usuário.
- **Intervenção em momento ecológico (EMI)**: assistente passo a passo acionado em situações de vulnerabilidade, com surfe de impulso, respiração guiada, meditação e reflexão, adaptado ao perfil clínico.
- **Registro emocional**: mapeamento de gatilhos, intensidade e sensações corporais.
- **Psicoeducação**: conteúdos educativos.
- **Exportação de dados**: acompanhamento por profissionais de saúde e uso em pesquisa.
- **Notificações inteligentes**: lembretes de check-in e alertas de risco.

## 🏛️ Arquitetura

O sistema segue a **Arquitetura Hexagonal (Ports and Adapters)** combinada com **comunicação interna orientada a eventos**, priorizando baixo acoplamento, alta testabilidade e separação clara de responsabilidades.

- **Domain**: regras de negócio puras, sem dependências externas (perfis clínicos, fluxos de intervenção, escalas).
- **UI**: Jetpack Compose, navegação, temas e ViewModels.
- **Storage**: repositórios e abstrações de persistência (Room).
- **Infrastructure**: adaptadores externos.

## 🧪 Testes

O projeto adota **TDD** como prática obrigatória: toda funcionalidade nasce de um teste que falha, e a camada de domínio busca cobertura próxima de 100%.

```bash
./gradlew testDebugUnitTest       # Testes unitários (JUnit + Mockito + Robolectric)
./gradlew connectedAndroidTest    # Testes instrumentados de UI (Compose Test + UIAutomator)
./gradlew assembleDebug           # Build do APK de debug
```

Os testes instrumentados (`app/src/androidTest/`) cobrem as principais telas do aplicativo (onboarding, home, check-in diário, intervenção guiada, registro emocional, configurações) e os componentes do design system. Eles exigem um emulador ou dispositivo físico conectado.

A integração contínua roda no GitHub Actions com dois jobs: análise estática + testes unitários e build do APK.

## 🧰 Tecnologias

Este projeto foi desenvolvido utilizando:

|  |  |
|------|------------|
| <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/kotlin/kotlin-original.svg" width="24"> | [Kotlin](https://kotlinlang.org/) |
| <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/jetpackcompose/jetpackcompose-original.svg" width="24"> | [Jetpack Compose](https://developer.android.com/jetpack/compose) |
| <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/android/android-original.svg" width="24"> | [Android](https://developer.android.com/) |
| <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/sqlite/sqlite-original.svg" width="24"> | [Room](https://developer.android.com/training/data-storage/room) |
| 📊 | [Vico](https://github.com/patrykandpatrick/vico) |
| <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/junit/junit-original.svg" width="24"> | [JUnit](https://junit.org/) + [Mockito](https://site.mockito.org/) + [Robolectric](https://robolectric.org/) |
| <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/githubactions/githubactions-original.svg" width="24"> | [GitHub Actions](https://github.com/features/actions) |

## 🚀 Como executar

```bash
git clone https://github.com/Wesley-AlvesRolim/Beefree.git
cd Beefree
./gradlew assembleDebug
```

Ou abra o projeto no Android Studio e execute em um emulador ou dispositivo físico (Android 7.0+).

---

Made with ❤️ by Wesley Alves Rolim 👋 [Get in touch](https://www.linkedin.com/in/wesley-alves-rolim/)
