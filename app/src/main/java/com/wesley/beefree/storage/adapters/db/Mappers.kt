package com.wesley.beefree.storage.adapters.db

import com.wesley.beefree.domain.entities.*
import com.wesley.beefree.storage.adapters.db.entities.*

fun AddictionTypeEntity.toDomain() =
    AddictionType(
        id = id,
        name = name,
        isMonitoringEnabled = isMonitoringEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun AddictionType.toEntity() =
    AddictionTypeEntity(
        id = id,
        name = name,
        isMonitoringEnabled = isMonitoringEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun AddictionKeywordEntity.toDomain() =
    AddictionKeyword(
        id = id,
        addictionTypeId = addictionTypeId,
        keyword = keyword,
    )

fun AddictionKeyword.toEntity() =
    AddictionKeywordEntity(
        id = id,
        addictionTypeId = addictionTypeId,
        keyword = keyword,
    )

fun RelapseHistoryEntity.toDomain() =
    RelapseHistory(
        id = id,
        addictionTypeId = addictionTypeId,
        keywordDetected = keywordDetected,
        detectedText = detectedText,
        appPackage = appPackage,
        externalApp = externalApp,
        wasSentToExternal = wasSentToExternal,
        relapseAt = relapseAt,
        updatedAt = updatedAt,
    )

fun RelapseHistory.toEntity() =
    RelapseHistoryEntity(
        id = id,
        addictionTypeId = addictionTypeId,
        keywordDetected = keywordDetected,
        detectedText = detectedText,
        appPackage = appPackage,
        externalApp = externalApp,
        wasSentToExternal = wasSentToExternal,
        relapseAt = relapseAt,
        updatedAt = updatedAt,
    )

fun UserProfileEntity.toDomain() =
    UserProfile(
        id = id,
        profileName = profileName,
        gender = gender,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun UserProfile.toEntity() =
    UserProfileEntity(
        id = id,
        profileName = profileName,
        gender = gender,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun UserProfileAddictionEntity.toDomain() =
    UserProfileAddiction(
        userProfileId = userProfileId,
        addictionTypeId = addictionTypeId,
        createdAt = createdAt,
    )

fun UserProfileAddiction.toEntity() =
    UserProfileAddictionEntity(
        userProfileId = userProfileId,
        addictionTypeId = addictionTypeId,
        createdAt = createdAt,
    )

fun AppRestrictionEntity.toDomain() =
    AppRestriction(
        id = id,
        addictionTypeId = addictionTypeId,
        appPackage = appPackage,
        screenTimeLimitMillis = screenTimeLimitMillis,
    )

fun AppRestriction.toEntity() =
    AppRestrictionEntity(
        id = id,
        addictionTypeId = addictionTypeId,
        appPackage = appPackage,
        screenTimeLimitMillis = screenTimeLimitMillis,
    )

fun AppUseEntity.toDomain() =
    AppUse(
        id = id,
        packageName = packageName,
        enterTime = enterTime,
        exitTime = exitTime,
    )

fun AppUse.toEntity() =
    AppUseEntity(
        id = id,
        packageName = packageName,
        enterTime = enterTime,
        exitTime = exitTime,
    )

fun SupportContactEntity.toDomain() =
    SupportContact(
        id = id,
        phoneNumber = phoneNumber,
        isActive = isActive,
        createdAt = createdAt,
    )

fun SupportContact.toEntity() =
    SupportContactEntity(
        id = id,
        phoneNumber = phoneNumber,
        isActive = isActive,
        createdAt = createdAt,
    )

fun BlockScreenConfigEntity.toDomain() =
    BlockScreenConfig(
        id = id,
        addictionTypeId = addictionTypeId,
        titleText = titleText,
        subtitleText = subtitleText,
        imageUri = imageUri,
        backgroundColor = backgroundColor,
        textColor = textColor,
        updatedAt = updatedAt,
    )

fun BlockScreenConfig.toEntity() =
    BlockScreenConfigEntity(
        id = id,
        addictionTypeId = addictionTypeId,
        titleText = titleText,
        subtitleText = subtitleText,
        imageUri = imageUri,
        backgroundColor = backgroundColor,
        textColor = textColor,
        updatedAt = updatedAt,
    )

fun MotivationalMessageEntity.toDomain() =
    MotivationalMessage(
        id = id,
        addictionTypeId = addictionTypeId,
        messageText = messageText,
        isActive = isActive,
    )

fun MotivationalMessage.toEntity() =
    MotivationalMessageEntity(
        id = id,
        addictionTypeId = addictionTypeId,
        messageText = messageText,
        isActive = isActive,
    )

fun OnboardingScaleAnswerEntity.toDomain() =
    OnboardingScaleAnswer(
        id = id,
        userProfileId = userProfileId,
        scaleType = scaleType,
        questionIndex = questionIndex,
        answerValue = answerValue,
        createdAt = createdAt,
    )

fun OnboardingScaleAnswer.toEntity() =
    OnboardingScaleAnswerEntity(
        id = id,
        userProfileId = userProfileId,
        scaleType = scaleType,
        questionIndex = questionIndex,
        answerValue = answerValue,
        createdAt = createdAt,
    )

fun UserProfileOnboardingResultEntity.toDomain() =
    UserProfileOnboardingResult(
        id = id,
        userProfileId = userProfileId,
        addictionTypeId = addictionTypeId,
        ppcsScore = ppcsScore,
        pgsiScore = pgsiScore,
        moralIncongruenceScore = moralIncongruenceScore,
        frequencyScore = frequencyScore,
        moralDisapprovalScore = moralDisapprovalScore,
        hasNeurodivergence = hasNeurodivergence,
        clinicalProfile = clinicalProfile,
        moralIncongruenceBand = moralIncongruenceBand,
        createdAt = createdAt,
    )

fun UserProfileOnboardingResult.toEntity() =
    UserProfileOnboardingResultEntity(
        id = id,
        userProfileId = userProfileId,
        addictionTypeId = addictionTypeId,
        ppcsScore = ppcsScore,
        pgsiScore = pgsiScore,
        moralIncongruenceScore = moralIncongruenceScore,
        frequencyScore = frequencyScore,
        moralDisapprovalScore = moralDisapprovalScore,
        hasNeurodivergence = hasNeurodivergence,
        clinicalProfile = clinicalProfile,
        moralIncongruenceBand = moralIncongruenceBand,
        createdAt = createdAt,
    )

fun UserCoreValueEntity.toDomain() =
    UserCoreValue(
        id = id,
        userProfileId = userProfileId,
        valueName = valueName,
        createdAt = createdAt,
    )

fun UserCoreValue.toEntity() =
    UserCoreValueEntity(
        id = id,
        userProfileId = userProfileId,
        valueName = valueName,
        createdAt = createdAt,
    )

fun UserHobbyEntity.toDomain() =
    UserHobby(
        id = id,
        userProfileId = userProfileId,
        hobbyName = hobbyName,
        createdAt = createdAt,
    )

fun UserHobby.toEntity() =
    UserHobbyEntity(
        id = id,
        userProfileId = userProfileId,
        hobbyName = hobbyName,
        createdAt = createdAt,
    )

fun UserObjectiveEntity.toDomain() =
    UserObjective(
        id = id,
        userProfileId = userProfileId,
        objectiveText = objectiveText,
        createdAt = createdAt,
    )

fun UserObjective.toEntity() =
    UserObjectiveEntity(
        id = id,
        userProfileId = userProfileId,
        objectiveText = objectiveText,
        createdAt = createdAt,
    )

fun UserSymptomEntity.toDomain() =
    UserSymptom(
        id = id,
        userProfileId = userProfileId,
        symptomCode = symptomCode,
        createdAt = createdAt,
    )

fun UserSymptom.toEntity() =
    UserSymptomEntity(
        id = id,
        userProfileId = userProfileId,
        symptomCode = symptomCode,
        createdAt = createdAt,
    )

fun DailyCheckInEntity.toDomain() =
    DailyCheckIn(
        id = id,
        userProfileId = userProfileId,
        dopamineLevel = dopamineLevel,
        mood = mood,
        anxietyLevel = anxietyLevel,
        checkedInAt = checkedInAt,
    )

fun DailyCheckIn.toEntity() =
    DailyCheckInEntity(
        id = id,
        userProfileId = userProfileId,
        dopamineLevel = dopamineLevel,
        mood = mood,
        anxietyLevel = anxietyLevel,
        checkedInAt = checkedInAt,
    )

fun WeeklyCheckInEntity.toDomain() =
    WeeklyCheckIn(
        id = id,
        userProfileId = userProfileId,
        weekStartDate = weekStartDate,
        valuesAlignmentText = valuesAlignmentText,
        realConnectionEnergy = realConnectionEnergy,
        createdAt = createdAt,
    )

fun WeeklyCheckIn.toEntity() =
    WeeklyCheckInEntity(
        id = id,
        userProfileId = userProfileId,
        weekStartDate = weekStartDate,
        valuesAlignmentText = valuesAlignmentText,
        realConnectionEnergy = realConnectionEnergy,
        createdAt = createdAt,
    )

fun TriggerMappingEntity.toDomain() =
    TriggerMapping(
        id = id,
        userProfileId = userProfileId,
        appPackage = appPackage,
        triggerContext = triggerContext,
        cravingIntensity = cravingIntensity,
        didRelapse = didRelapse,
        loggedAt = loggedAt,
    )

fun TriggerMapping.toEntity() =
    TriggerMappingEntity(
        id = id,
        userProfileId = userProfileId,
        appPackage = appPackage,
        triggerContext = triggerContext,
        cravingIntensity = cravingIntensity,
        didRelapse = didRelapse,
        loggedAt = loggedAt,
    )

fun InterventionLogEntity.toDomain() =
    InterventionLog(
        id = id,
        userProfileId = userProfileId,
        interventionType = interventionType,
        triggeredBy = triggeredBy,
        wasCompleted = wasCompleted,
        createdAt = createdAt,
    )

fun InterventionLog.toEntity() =
    InterventionLogEntity(
        id = id,
        userProfileId = userProfileId,
        interventionType = interventionType,
        triggeredBy = triggeredBy,
        wasCompleted = wasCompleted,
        createdAt = createdAt,
    )

fun ThoughtRecordEntity.toDomain() =
    ThoughtRecord(
        id = id,
        userProfileId = userProfileId,
        triggerId = triggerId,
        automaticThought = automaticThought,
        rationalResponse = rationalResponse,
        createdAt = createdAt,
    )

fun ThoughtRecord.toEntity() =
    ThoughtRecordEntity(
        id = id,
        userProfileId = userProfileId,
        triggerId = triggerId,
        automaticThought = automaticThought,
        rationalResponse = rationalResponse,
        createdAt = createdAt,
    )

fun UrgeSurfingSessionEntity.toDomain() =
    UrgeSurfingSession(
        id = id,
        userProfileId = userProfileId,
        interventionId = interventionId,
        peakIntensity = peakIntensity,
        durationSeconds = durationSeconds,
        completed = completed,
        loggedAt = loggedAt,
    )

fun UrgeSurfingSession.toEntity() =
    UrgeSurfingSessionEntity(
        id = id,
        userProfileId = userProfileId,
        interventionId = interventionId,
        peakIntensity = peakIntensity,
        durationSeconds = durationSeconds,
        completed = completed,
        loggedAt = loggedAt,
    )

fun MicroActivityEntity.toDomain() =
    MicroActivity(
        id = id,
        addictionTypeId = addictionTypeId,
        activityType = activityType,
        activityName = activityName,
        createdAt = createdAt,
    )

fun MicroActivity.toEntity() =
    MicroActivityEntity(
        id = id,
        addictionTypeId = addictionTypeId,
        activityType = activityType,
        activityName = activityName,
        createdAt = createdAt,
    )

fun DailyMicroActivityLogEntity.toDomain() =
    DailyMicroActivityLog(
        id = id,
        userProfileId = userProfileId,
        activityId = activityId,
        dayDate = dayDate,
        completedAt = completedAt,
    )

fun DailyMicroActivityLog.toEntity() =
    DailyMicroActivityLogEntity(
        id = id,
        userProfileId = userProfileId,
        activityId = activityId,
        dayDate = dayDate,
        completedAt = completedAt,
    )

fun DailyLessonEntity.toDomain() =
    DailyLesson(
        id = id,
        title = title,
        contentBody = contentBody,
        targetProfile = targetProfile,
        createdAt = createdAt,
    )

fun DailyLesson.toEntity() =
    DailyLessonEntity(
        id = id,
        title = title,
        contentBody = contentBody,
        targetProfile = targetProfile,
        createdAt = createdAt,
    )

fun UserLessonProgressEntity.toDomain() =
    UserLessonProgress(
        id = id,
        userProfileId = userProfileId,
        lessonId = lessonId,
        completedAt = completedAt,
    )

fun UserLessonProgress.toEntity() =
    UserLessonProgressEntity(
        id = id,
        userProfileId = userProfileId,
        lessonId = lessonId,
        completedAt = completedAt,
    )

fun HolisticMetricsEntity.toDomain() =
    HolisticMetrics(
        id = id,
        userProfileId = userProfileId,
        anxietyLevel = anxietyLevel,
        emotionalSatisfaction = emotionalSatisfaction,
        mood = mood,
        loggedAt = loggedAt,
    )

fun HolisticMetrics.toEntity() =
    HolisticMetricsEntity(
        id = id,
        userProfileId = userProfileId,
        anxietyLevel = anxietyLevel,
        emotionalSatisfaction = emotionalSatisfaction,
        mood = mood,
        loggedAt = loggedAt,
    )

fun RiskPredictionEntity.toDomain() =
    RiskPrediction(
        id = id,
        userProfileId = userProfileId,
        riskScore = riskScore,
        riskFactors = riskFactors,
        notificationSent = notificationSent,
        predictedAt = predictedAt,
    )

fun RiskPrediction.toEntity() =
    RiskPredictionEntity(
        id = id,
        userProfileId = userProfileId,
        riskScore = riskScore,
        riskFactors = riskFactors,
        notificationSent = notificationSent,
        predictedAt = predictedAt,
    )

fun NotificationLogEntity.toDomain() =
    NotificationLog(
        id = id,
        userProfileId = userProfileId,
        notificationType = notificationType,
        contentRefId = contentRefId,
        sentAt = sentAt,
        openedAt = openedAt,
    )

fun NotificationLog.toEntity() =
    NotificationLogEntity(
        id = id,
        userProfileId = userProfileId,
        notificationType = notificationType,
        contentRefId = contentRefId,
        sentAt = sentAt,
        openedAt = openedAt,
    )
