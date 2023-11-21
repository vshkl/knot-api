package com.knot.feature.note

import io.ktor.resources.*

@Resource("/notes")
class NoteResource(
    val limit: Int = 20,
    val before: Long? = null,
    val after: Long? = null,
    val including: Boolean = false,
) {

    @Resource("{id}")
    data class Id(
        val parent: NoteResource = NoteResource(),
        val id: Long,
    )
}
