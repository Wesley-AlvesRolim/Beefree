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
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun UserProfile.toEntity() =
    UserProfileEntity(
        id = id,
        profileName = profileName,
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
