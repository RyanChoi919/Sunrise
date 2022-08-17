package com.nodes.sunrise.model

import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.enums.EntryViewType
import java.time.YearMonth

data class EntryAndYearMonth(
    val viewType: EntryViewType,
    val yearMonth: YearMonth? = null,
    val entry: Entry? = null,
)