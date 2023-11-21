package com.knot.feature.note

import io.ktor.resources.*

@Resource("/notes")
class NoteResource {

    @Resource("{id}")
    data class Id(
        val parent: NoteResource = NoteResource(),
        val id: Long,
    )
}
