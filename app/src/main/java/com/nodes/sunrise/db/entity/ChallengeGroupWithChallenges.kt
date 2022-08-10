package com.nodes.sunrise.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ChallengeGroupWithChallenges(
    @Embedded
    val group: ChallengeGroup,
    @Relation(
        parentColumn = "challengeGroupId",
        entityColumn = "challengeId",
        associateBy = Junction(ChallengeAndGroupCrossRef::class)
    )
    val challenges: List<Challenge>
)