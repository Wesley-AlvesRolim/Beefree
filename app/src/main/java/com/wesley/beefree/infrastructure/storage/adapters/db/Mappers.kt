package com.wesley.beefree.infrastructure.storage.adapters.db

import com.wesley.beefree.domain.entities.*
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.*

fun AddictionCategoryEntity.toDomain() =
    AddictionCategory(
        id = id,
        name = name,
        isMonitoringEnabled = isMonitoringEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun AddictionCategory.toEntity() =
    AddictionCategoryEntity(
        id = id,
        name = name,
        isMonitoringEnabled = isMonitoringEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun UserAddictionEntity.toDomain() =
    UserAddiction(
        userProfileId = userProfileId,
        addictionCategoryId = addictionCategoryId,
        createdAt = createdAt,
    )

fun UserAddiction.toEntity() =
    UserAddictionEntity(
        userProfileId = userProfileId,
        addictionCategoryId = addictionCategoryId,
        createdAt = createdAt,
    )

fun RelapseRecordEntity.toDomain() =
    RelapseRecord(
        id = id,
        addictionCategoryId = addictionCategoryId,
        keywordDetected = keywordDetected,
        detectedText = detectedText,
        appUsageSessionId = appUsageSessionId,
        createdAt = createdAt,
    )

fun RelapseRecord.toEntity() =
    RelapseRecordEntity(
        id = id,
        addictionCategoryId = addictionCategoryId,
        keywordDetected = keywordDetected,
        detectedText = detectedText,
        appUsageSessionId = appUsageSessionId,
        createdAt = createdAt,
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

fun AppUsageSessionEntity.toDomain() =
    AppUsageSession(
        id = id,
        packageName = packageName,
        enterTime = enterTime,
        exitTime = exitTime,
    )

fun AppUsageSession.toEntity() =
    AppUsageSessionEntity(
        id = id,
        packageName = packageName,
        enterTime = enterTime,
        exitTime = exitTime,
    )

fun UserSupportContactEntity.toDomain() =
    UserSupportContact(
        id = id,
        userProfileId = userProfileId,
        name = name,
        phoneNumber = phoneNumber,
        isActive = isActive,
        createdAt = createdAt,
    )

fun UserSupportContact.toEntity() =
    UserSupportContactEntity(
        id = id,
        userProfileId = userProfileId,
        name = name,
        phoneNumber = phoneNumber,
        isActive = isActive,
        createdAt = createdAt,
    )

fun UserOnboardingSessionEntity.toDomain() =
    UserOnboardingSession(
        id = id,
        userProfileId = userProfileId,
        clinicalApproach = clinicalApproach,
        ppcsScore = ppcsScore,
        pgsiScore = pgsiScore,
        moralIncongruenceScore = moralIncongruenceScore,
        frequencyScore = frequencyScore,
        moralDisapprovalScore = moralDisapprovalScore,
        hasNeurodivergence = hasNeurodivergence,
        createdAt = createdAt,
    )

fun UserOnboardingSession.toEntity() =
    UserOnboardingSessionEntity(
        id = id,
        userProfileId = userProfileId,
        clinicalApproach = clinicalApproach,
        ppcsScore = ppcsScore,
        pgsiScore = pgsiScore,
        moralIncongruenceScore = moralIncongruenceScore,
        frequencyScore = frequencyScore,
        moralDisapprovalScore = moralDisapprovalScore,
        hasNeurodivergence = hasNeurodivergence,
        createdAt = createdAt,
    )

fun UserCoreValueEntity.toDomain() =
    UserCoreValue(
        id = id,
        userProfileId = userProfileId,
        value = CoreValueType.valueOf(valueName),
        createdAt = createdAt,
    )

fun UserCoreValue.toEntity() =
    UserCoreValueEntity(
        id = id,
        userProfileId = userProfileId,
        valueName = value.name,
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
        treatmentProfile = TreatmentProfile.valueOf(treatmentProfile),
        answers = DailyCheckInCodec.decode(answersJson),
        checkedInAt = checkedInAt,
    )

fun DailyCheckIn.toEntity() =
    DailyCheckInEntity(
        id = id,
        userProfileId = userProfileId,
        treatmentProfile = treatmentProfile.name,
        answersJson = DailyCheckInCodec.encode(answers),
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

fun InterventionRecordEntity.toDomain() =
    InterventionRecord(
        id = id,
        userProfileId = userProfileId,
        interventionType = interventionType,
        triggerType = triggerType,
        wasCompleted = wasCompleted,
        createdAt = createdAt,
    )

fun InterventionRecord.toEntity() =
    InterventionRecordEntity(
        id = id,
        userProfileId = userProfileId,
        interventionType = interventionType,
        triggerType = triggerType,
        wasCompleted = wasCompleted,
        createdAt = createdAt,
    )

fun CognitiveThoughtRecordEntity.toDomain() =
    CognitiveThoughtRecord(
        id = id,
        userProfileId = userProfileId,
        situation = situation,
        automaticThought = automaticThought,
        feeling = feeling,
        consequence = consequence,
        alternativeThought = alternativeThought,
        cognitiveDistortions =
            runCatching {
                Json.decodeFromString(distortionsSerializer, cognitiveDistortions)
            }.getOrDefault(emptyList()),
        createdAt = createdAt,
    )

fun CognitiveThoughtRecord.toEntity() =
    CognitiveThoughtRecordEntity(
        id = id,
        userProfileId = userProfileId,
        situation = situation,
        automaticThought = automaticThought,
        feeling = feeling,
        consequence = consequence,
        alternativeThought = alternativeThought,
        cognitiveDistortions = Json.encodeToString(distortionsSerializer, cognitiveDistortions),
        createdAt = createdAt,
    )

fun InterventionValueLinkEntity.toDomain() =
    InterventionValueLink(
        interventionId = interventionId,
        userCoreValueId = userCoreValueId,
    )

fun InterventionValueLink.toEntity() =
    InterventionValueLinkEntity(
        interventionId = interventionId,
        userCoreValueId = userCoreValueId,
    )

fun PsychoeducationContentEntity.toDomain() =
    PsychoeducationContent(
        id = id,
        addictionCategoryId = addictionCategoryId,
        contentText = contentText,
        isActive = isActive,
    )

fun PsychoeducationContent.toEntity() =
    PsychoeducationContentEntity(
        id = id,
        addictionCategoryId = addictionCategoryId,
        contentText = contentText,
        isActive = isActive,
    )

fun EmotionRecordEntity.toDomain() =
    EmotionRecord(
        id = id,
        userProfileId = userProfileId,
        feelingType = FeelingType.valueOf(feelingType),
        intensity = intensity,
        createdAt = createdAt,
    )

fun EmotionRecord.toEntity() =
    EmotionRecordEntity(
        id = id,
        userProfileId = userProfileId,
        feelingType = feelingType.name,
        intensity = intensity,
        createdAt = createdAt,
    )

fun RiskFeatureSnapshotEntity.toDomain() =
    RiskFeatureSnapshot(
        id = id,
        userProfileId = userProfileId,
        previousSnapshotId = previousSnapshotId,
        humor = humor,
        stress = stress,
        anxiety = anxiety,
        urge = urge,
        hoursSinceLastRelapse = hoursSinceLastRelapse,
        hourOfDay = hourOfDay,
        dayOfWeek = dayOfWeek,
        timeSinceLastAppOpen = timeSinceLastAppOpen,
        missingCheckins = missingCheckins,
        recentIntenseUsage = recentIntenseUsage,
        createdAt = createdAt,
    )

fun RiskFeatureSnapshot.toEntity() =
    RiskFeatureSnapshotEntity(
        id = id,
        userProfileId = userProfileId,
        previousSnapshotId = previousSnapshotId,
        humor = humor,
        stress = stress,
        anxiety = anxiety,
        urge = urge,
        hoursSinceLastRelapse = hoursSinceLastRelapse,
        hourOfDay = hourOfDay,
        dayOfWeek = dayOfWeek,
        timeSinceLastAppOpen = timeSinceLastAppOpen,
        missingCheckins = missingCheckins,
        recentIntenseUsage = recentIntenseUsage,
        createdAt = createdAt,
    )

fun RiskAssessmentEntity.toDomain() =
    RiskAssessment(
        id = id,
        userProfileId = userProfileId,
        riskScore = riskScore,
        timeWindow = timeWindow,
        createdAt = createdAt,
    )

fun RiskAssessment.toEntity() =
    RiskAssessmentEntity(
        id = id,
        userProfileId = userProfileId,
        riskScore = riskScore,
        timeWindow = timeWindow,
        createdAt = createdAt,
    )
