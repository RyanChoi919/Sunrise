package com.nodes.sunrise.model

import androidx.room.Embedded
import androidx.room.Relation
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.Entry

data class EntryWithChallenge(
    @Embedded
    val entry: Entry,
    @Relation(
        parentColumn = "entryChallengeId",
        entityColumn = "challengeId"
    )
    val challenge: Challenge
)
