package com.wesley.beefree.infrastructure.storage.adapters.db.seeds

import android.os.Build
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AddictionCategoryEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.PsychoeducationContentEntity
import kotlinx.coroutines.flow.first

private const val CATEGORY_ADULT_CONTENT = 1
private const val CATEGORY_BETS = 2
private val NOW = System.currentTimeMillis()

suspend fun seed(database: AppDatabase) {
    val isProbablyAnEmulator =
        Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("sdk_gphone") ||
            (Build.BRAND == "google" && Build.DEVICE.startsWith("emu64")) ||
            Build.PRODUCT == "sdk_gphone64_x86_64"
    if (!isProbablyAnEmulator) return

    val isNotCreatedTheUser =
        database
            .userProfileDao()
            .getAll()
            .first()
            .isEmpty()
    if (isNotCreatedTheUser) return

    seedAddictionCategories(database)
    seedPsychoeducationContent(database)
}

private suspend fun seedAddictionCategories(database: AppDatabase) {
    val dao = database.addictionCategoryDao()

    dao.insert(
        AddictionCategoryEntity(
            id = CATEGORY_ADULT_CONTENT,
            name = "ADULT_CONTENT",
            isMonitoringEnabled = true,
            createdAt = NOW,
            updatedAt = NOW,
        ),
    )
    dao.insert(
        AddictionCategoryEntity(
            id = CATEGORY_BETS,
            name = "BETS",
            isMonitoringEnabled = true,
            createdAt = NOW,
            updatedAt = NOW,
        ),
    )
}

private suspend fun seedPsychoeducationContent(database: AppDatabase) {
    val dao = database.psychoeducationContentDao()

    val genericContent =
        listOf(
            "Cada dia sem recaída é uma vitória silenciosa.",
            "Você não precisa ser perfeito, só precisa continuar.",
            "A mudança começa quando você decide que merece mais.",
            "O impulso é temporário. Seus valores são permanentes.",
            "Respirar fundo já é um ato de coragem.",
            "Você está reescrevendo sua história agora.",
            "Progresso não é linear — e tudo bem.",
            "Sua mente pode duvidar, mas seu coração sabe o caminho.",
            "Cada escolha consciente fortalece seu novo eu.",
            "Você já provou que consegue. Continue.",
            "Como a dopamina sequestra seu cérebro: a dopamina não é o hormônio do prazer — é o hormônio da antecipação. Quando você consome conteúdo compulsivamente, seu cérebro cria um ciclo de busca que nunca satisfaz de verdade.",
            "O mito da força de vontade: força de vontade é um recurso limitado. Estratégias ambientais — como remover gatilhos e criar barreiras — são muito mais eficazes a longo prazo.",
            "Neuroplasticidade: cada vez que você resiste a um impulso e escolhe uma alternativa saudável, seu cérebro forma novas conexões neurais. Em semanas, esses novos caminhos ficam mais fortes.",
            "O ciclo da compulsão: Gatilho → Fissura → Uso → Culpa → Gatilho. Reconhecer em qual etapa você está é essencial para quebrar o ciclo.",
            "Solidão e vulnerabilidade: momentos de solidão são os maiores gatilhos para recaídas. Construir uma rede de apoio real é uma das ferramentas mais poderosas de recuperação.",
            "O efeito da abstinência no humor: nas primeiras semanas, é normal sentir irritabilidade, ansiedade ou tristeza. Esses sintomas são temporários e são sinais de que seu cérebro está se curando.",
            "Sono e recuperação: dormir bem é fundamental para a regulação emocional e o controle de impulsos.",
            "Exercício físico e dopamina natural: 30 minutos de exercício moderado liberam dopamina, serotonina e endorfinas de forma saudável.",
            "A recaída não é o fim: se acontecer uma recaída, não significa que você fracassou. Use isso como informação para aprender.",
        )

    val adultContentItems =
        listOf(
            "Conexão real é mais poderosa que qualquer tela.",
            "Seu cérebro está se recalibrando. Confie no processo.",
            "Intimidade verdadeira começa com presença, não com pixels.",
            "Você está recuperando o controle sobre sua atenção.",
            "Desfusão cognitiva: você não é seus pensamentos. Na ACT, aprendemos que pensamentos são apenas eventos mentais. Você pode observar o pensamento sem obedecer a ele.",
            "Valores como bússola: seus valores fundamentais são o que dão direção à sua vida. Quando um impulso surge, pergunte-se: 'Essa ação me aproxima ou me afasta de quem eu quero ser?'",
            "Aceitação radical: aceitar não significa concordar ou desistir. Significa parar de lutar contra a realidade do momento presente.",
        )

    val betsItems =
        listOf(
            "A maior aposta é em você mesmo — e essa sempre vale.",
            "Cada real não apostado é um investimento na sua liberdade.",
            "O jogo promete emoção, mas entrega ansiedade. Você merece paz.",
            "Sua família e seu futuro valem mais que qualquer odd.",
            "Ganhar é parar. E você já está ganhando.",
            "Registro de pensamentos automáticos: pensamentos como 'eu mereço', 'só dessa vez' são distorções cognitivas. Registrá-los e encontrar respostas racionais enfraquece seu poder.",
            "Prevenção de recaída: identifique seus 3 principais gatilhos e planeje uma resposta para cada um. Ter um plano pronto é mais eficaz do que decidir no momento.",
        )

    genericContent.forEach { text ->
        dao.insert(PsychoeducationContentEntity(contentText = text))
    }

    adultContentItems.forEach { text ->
        dao.insert(PsychoeducationContentEntity(addictionCategoryId = CATEGORY_ADULT_CONTENT, contentText = text))
    }

    betsItems.forEach { text ->
        dao.insert(PsychoeducationContentEntity(addictionCategoryId = CATEGORY_BETS, contentText = text))
    }
}
