package com.wesley.beefree.infrastructure.storage.adapters.db.seeds

import android.os.Build
import com.wesley.beefree.data.keywords.getBetsKeyWords
import com.wesley.beefree.data.keywords.getPornKeywords
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AddictionKeywordEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AddictionTypeEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AppRestrictionEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.BlockScreenConfigEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.DailyLessonEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.HolisticMetricsEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.MicroActivityEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.MotivationalMessageEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.TriggerMappingEntity

private const val ADDICTION_TYPE_ADULT_CONTENT = 1
private const val ADDICTION_TYPE_BETS = 2
private const val USER_ID = 1
private val NOW = System.currentTimeMillis()
private val ONE_DAY_IN_MILLIS = 1000 * 60 * 60 * 24

suspend fun seed(database: AppDatabase) {
    val isProbablyAnEmulator =
        Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("sdk_gphone") ||
            (Build.BRAND == "google" && Build.DEVICE.startsWith("emu64")) ||
            Build.PRODUCT == "sdk_gphone64_x86_64"
    if (!isProbablyAnEmulator) return

    seedAddictionTypes(database)
    seedAddictionKeywords(database)
    seedAppRestrictions(database)
    seedBlockScreenConfigs(database)
    seedMotivationalMessages(database)
    seedMicroActivities(database)
    seedDailyLessons(database)
    seedHolisticMetrics(database)
    seedTriggerMapping(database)
}

private suspend fun seedAddictionTypes(database: AppDatabase) {
    val dao = database.addictionTypeDao()

    dao.insert(
        AddictionTypeEntity(
            id = ADDICTION_TYPE_ADULT_CONTENT,
            name = "ADULT_CONTENT",
            isMonitoringEnabled = true,
            createdAt = NOW,
            updatedAt = NOW,
        ),
    )
    dao.insert(
        AddictionTypeEntity(
            id = ADDICTION_TYPE_BETS,
            name = "BETS",
            isMonitoringEnabled = true,
            createdAt = NOW,
            updatedAt = NOW,
        ),
    )
}

private suspend fun seedAddictionKeywords(database: AppDatabase) {
    val dao = database.addictionKeywordDao()

    getPornKeywords().forEach { keyword ->
        dao.insert(
            AddictionKeywordEntity(
                addictionTypeId = ADDICTION_TYPE_ADULT_CONTENT,
                keyword = keyword,
            ),
        )
    }

    getBetsKeyWords().forEach { keyword ->
        dao.insert(
            AddictionKeywordEntity(
                addictionTypeId = ADDICTION_TYPE_BETS,
                keyword = keyword,
            ),
        )
    }
}

private suspend fun seedAppRestrictions(database: AppDatabase) {
    val dao = database.appRestrictionDao()

    val pornApps =
        listOf(
            "com.discord",
        )

    val betsApps =
        listOf(
            "br.com.betano.app",
        )

    pornApps.forEach { pkg ->
        dao.insert(
            AppRestrictionEntity(
                addictionTypeId = ADDICTION_TYPE_ADULT_CONTENT,
                appPackage = pkg,
                screenTimeLimitMillis = 0,
            ),
        )
    }

    betsApps.forEach { pkg ->
        dao.insert(
            AppRestrictionEntity(
                addictionTypeId = ADDICTION_TYPE_BETS,
                appPackage = pkg,
                screenTimeLimitMillis = 0,
            ),
        )
    }
}

private suspend fun seedBlockScreenConfigs(database: AppDatabase) {
    val dao = database.blockScreenConfigDao()

    dao.insert(
        BlockScreenConfigEntity(
            addictionTypeId = ADDICTION_TYPE_ADULT_CONTENT,
            titleText = "Você é mais forte que esse impulso",
            subtitleText = "Respire fundo. Lembre-se dos seus valores e do motivo que te trouxe até aqui.",
            updatedAt = NOW,
        ),
    )
    dao.insert(
        BlockScreenConfigEntity(
            addictionTypeId = ADDICTION_TYPE_BETS,
            titleText = "Sua paz vale mais que qualquer aposta",
            subtitleText = "Cada vez que você resiste, está construindo uma vida com mais controle e liberdade.",
            updatedAt = NOW,
        ),
    )
}

private suspend fun seedMotivationalMessages(database: AppDatabase) {
    val dao = database.motivationalMessageDao()

    val genericMessages =
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
        )

    val pornMessages =
        listOf(
            "Conexão real é mais poderosa que qualquer tela.",
            "Seu cérebro está se recalibrando. Confie no processo.",
            "Intimidade verdadeira começa com presença, não com pixels.",
            "Você está recuperando o controle sobre sua atenção.",
            "A dopamina artificial nunca preencheu o vazio — seus valores sim.",
        )

    val betsMessages =
        listOf(
            "A maior aposta é em você mesmo — e essa sempre vale.",
            "Cada real não apostado é um investimento na sua liberdade.",
            "O jogo promete emoção, mas entrega ansiedade. Você merece paz.",
            "Sua família e seu futuro valem mais que qualquer odd.",
            "Ganhar é parar. E você já está ganhando.",
        )

    genericMessages.forEach { msg ->
        dao.insert(MotivationalMessageEntity(messageText = msg))
    }

    pornMessages.forEach { msg ->
        dao.insert(
            MotivationalMessageEntity(
                addictionTypeId = ADDICTION_TYPE_ADULT_CONTENT,
                messageText = msg,
            ),
        )
    }

    betsMessages.forEach { msg ->
        dao.insert(
            MotivationalMessageEntity(
                addictionTypeId = ADDICTION_TYPE_BETS,
                messageText = msg,
            ),
        )
    }
}

private suspend fun seedMicroActivities(database: AppDatabase) {
    val dao = database.microActivityDao()

    data class Activity(
        val type: String,
        val name: String,
        val addictionTypeId: Int?,
    )

    val activities =
        listOf(
            Activity("PHYSICAL", "Faça 10 flexões ou polichinelos", null),
            Activity("PHYSICAL", "Caminhe por 5 minutos ao ar livre", null),
            Activity("PHYSICAL", "Alongue-se por 3 minutos", null),
            Activity("BREATHING", "Respiração 4-7-8 (inspire 4s, segure 7s, expire 8s)", null),
            Activity(
                "BREATHING",
                "Box breathing: 4s inspire, 4s segure, 4s expire, 4s segure",
                null,
            ),
            Activity("MINDFULNESS", "Observe 5 coisas ao seu redor com atenção plena", null),
            Activity("MINDFULNESS", "Feche os olhos e escute os sons por 2 minutos", null),
            Activity("MINDFULNESS", "Segure um cubo de gelo e observe a sensação", null),
            Activity("SOCIAL", "Envie uma mensagem para alguém que você gosta", null),
            Activity("SOCIAL", "Ligue para um amigo por 5 minutos", null),
            Activity("CREATIVE", "Escreva 3 coisas pelas quais é grato hoje", null),
            Activity("CREATIVE", "Desenhe algo por 5 minutos", null),
            Activity("COGNITIVE", "Leia uma página de um livro", null),
            Activity("COGNITIVE", "Resolva um quebra-cabeça ou sudoku", null),
            Activity("PHYSICAL", "Tome um banho gelado rápido", ADDICTION_TYPE_ADULT_CONTENT),
            Activity(
                "MINDFULNESS",
                "Pratique urge surfing por 3 minutos",
                ADDICTION_TYPE_ADULT_CONTENT,
            ),
            Activity(
                "COGNITIVE",
                "Escreva como quer se sentir depois de resistir",
                ADDICTION_TYPE_ADULT_CONTENT,
            ),
            Activity("PHYSICAL", "Saia de casa e caminhe por 10 minutos", ADDICTION_TYPE_BETS),
            Activity(
                "COGNITIVE",
                "Calcule quanto economizou esta semana sem apostar",
                ADDICTION_TYPE_BETS,
            ),
            Activity(
                "CREATIVE",
                "Planeje algo legal para fazer com o dinheiro economizado",
                ADDICTION_TYPE_BETS,
            ),
        )

    activities.forEach { activity ->
        dao.insert(
            MicroActivityEntity(
                addictionTypeId = activity.addictionTypeId,
                activityType = activity.type,
                activityName = activity.name,
                createdAt = NOW,
            ),
        )
    }
}

private suspend fun seedDailyLessons(database: AppDatabase) {
    val dao = database.dailyLessonDao()

    data class Lesson(
        val title: String,
        val body: String,
        val profile: String?,
    )

    val lessons =
        listOf(
            Lesson(
                "Como a dopamina sequestra seu cérebro",
                "A dopamina não é o hormônio do prazer — é o hormônio da antecipação. " +
                    "Quando você consome conteúdo compulsivamente, seu cérebro cria um ciclo de busca " +
                    "que nunca satisfaz de verdade. Entender isso é o primeiro passo para retomar o controle.",
                null,
            ),
            Lesson(
                "O mito da força de vontade",
                "Força de vontade é um recurso limitado. Por isso, depender apenas dela para resistir " +
                    "a impulsos é uma estratégia frágil. Estratégias ambientais — como remover gatilhos e " +
                    "criar barreiras — são muito mais eficazes a longo prazo.",
                null,
            ),
            Lesson(
                "Neuroplasticidade: seu cérebro muda",
                "Cada vez que você resiste a um impulso e escolhe uma alternativa saudável, seu cérebro " +
                    "forma novas conexões neurais. Em semanas, esses novos caminhos ficam mais fortes. " +
                    "A recuperação não é só comportamental — é física.",
                null,
            ),
            Lesson(
                "O ciclo da compulsão",
                "Gatilho → Fissura → Uso → Culpa → Gatilho. Reconhecer em qual etapa você está " +
                    "é essencial para quebrar o ciclo. A intervenção mais poderosa acontece entre " +
                    "o gatilho e a fissura.",
                null,
            ),
            Lesson(
                "Solidão e vulnerabilidade",
                "Momentos de solidão são os maiores gatilhos para recaídas. Não porque você é fraco, " +
                    "mas porque o cérebro busca conexão. Construir uma rede de apoio real é uma das " +
                    "ferramentas mais poderosas de recuperação.",
                null,
            ),
            Lesson(
                "Desfusão cognitiva: você não é seus pensamentos",
                "Na Terapia de Aceitação e Compromisso (ACT), aprendemos que pensamentos são apenas " +
                    "eventos mentais. Você pode observar o pensamento 'eu preciso disso' sem obedecer a ele. " +
                    "Tente dizer: 'Estou tendo o pensamento de que preciso disso'.",
                "ACT",
            ),
            Lesson(
                "Valores como bússola",
                "Seus valores fundamentais são o que dão direção à sua vida. Quando um impulso surge, " +
                    "pergunte-se: 'Essa ação me aproxima ou me afasta de quem eu quero ser?' " +
                    "Viver de acordo com seus valores é mais satisfatório que qualquer prazer imediato.",
                "ACT",
            ),
            Lesson(
                "Aceitação radical",
                "Aceitar não significa concordar ou desistir. Significa parar de lutar contra a realidade " +
                    "do momento presente. Quando você aceita que está sentindo uma fissura, paradoxalmente, " +
                    "ela perde parte do seu poder sobre você.",
                "ACT",
            ),
            Lesson(
                "Registro de pensamentos automáticos",
                "Pensamentos como 'eu mereço', 'só dessa vez' ou 'ninguém vai saber' são distorções " +
                    "cognitivas comuns. Na TCC, registrar esses pensamentos e encontrar respostas racionais " +
                    "enfraquece seu poder. Pratique: qual evidência real apoia esse pensamento?",
                "TCC",
            ),
            Lesson(
                "Reestruturação cognitiva",
                "Quando um pensamento disfuncional aparece, questione: 'Isso é um fato ou uma interpretação?' " +
                    "'Eu pensaria isso sobre um amigo na mesma situação?' Substituir pensamentos automáticos " +
                    "por pensamentos mais equilibrados reduz a intensidade do impulso.",
                "TCC",
            ),
            Lesson(
                "Prevenção de recaída",
                "Na TCC, mapeamos situações de alto risco antes que aconteçam. Identifique seus " +
                    "3 principais gatilhos (horário, local, emoção) e planeje uma resposta para cada um. " +
                    "Ter um plano pronto é mais eficaz do que decidir no momento.",
                "TCC",
            ),
            Lesson(
                "O efeito da abstinência no humor",
                "Nas primeiras semanas, é normal sentir irritabilidade, ansiedade ou tristeza. " +
                    "Isso acontece porque seu sistema de recompensa está se recalibrando. " +
                    "Esses sintomas são temporários e são sinais de que seu cérebro está se curando.",
                null,
            ),
            Lesson(
                "Sono e recuperação",
                "Dormir bem é fundamental para a regulação emocional e o controle de impulsos. " +
                    "A privação de sono aumenta a reatividade do sistema límbico e diminui a capacidade " +
                    "do córtex pré-frontal de frear decisões impulsivas.",
                null,
            ),
            Lesson(
                "Exercício físico e dopamina natural",
                "30 minutos de exercício moderado liberam dopamina, serotonina e endorfinas de forma " +
                    "saudável. Essa é a melhor forma de recalibrar seu sistema de recompensa " +
                    "e reduzir a intensidade das fissuras ao longo do tempo.",
                null,
            ),
            Lesson(
                "A recaída não é o fim",
                "Se acontecer uma recaída, não significa que você fracassou. Significa que encontrou " +
                    "um ponto de vulnerabilidade. Use isso como informação: o que aconteceu antes? " +
                    "O que você pode fazer diferente? Cada recaída é uma oportunidade de aprendizado.",
                null,
            ),
        )

    lessons.forEach { lesson ->
        dao.insert(
            DailyLessonEntity(
                title = lesson.title,
                contentBody = lesson.body,
                targetProfile = lesson.profile,
                createdAt = NOW,
            ),
        )
    }
}

private suspend fun seedHolisticMetrics(database: AppDatabase) {
    val dao = database.holisticMetricsDao()
    for (i in 1..7) {
        dao.insert(
            HolisticMetricsEntity(
                userProfileId = USER_ID,
                anxietyLevel = listOf(1, 2, 3, 4, 5).random(),
                emotionalSatisfaction = listOf(1, 2, 3, 4, 5).random(),
                mood = "Eu preciso!",
                loggedAt = NOW - (i * ONE_DAY_IN_MILLIS),
            ),
        )
    }
}

private suspend fun seedTriggerMapping(database: AppDatabase) {
    val dao = database.triggerMappingDao()
    for (i in 1..5) {
        dao.insert(
            TriggerMappingEntity(
                userProfileId = USER_ID,
                appPackage = null,
                triggerContext = "Sozinho",
                cravingIntensity = 5,
                didRelapse = true,
                loggedAt = NOW,
            ),
        )
    }
    for (i in 1..5) {
        dao.insert(
            TriggerMappingEntity(
                userProfileId = USER_ID,
                appPackage = null,
                triggerContext = "Tédio",
                cravingIntensity = 5,
                didRelapse = true,
                loggedAt = NOW,
            ),
        )
    }
}
