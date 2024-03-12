package com.springbootstudy2024.springbootstudy2024.chapter4.endpoint

import com.springbootstudy2024.springbootstudy2024.chapter4.dto.ReleaseNote
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation
import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.boot.actuate.endpoint.annotation.Selector
import org.springframework.stereotype.Component

@Component
@Endpoint(id = "releaseNotes")
//@JmxEndpoint(id = "releaseNotes")
class ReleaseNotesEndpoint( private val releaseNotes: MutableCollection<ReleaseNote>) {
    @ReadOperation
    fun releaseNotes(): Iterable<ReleaseNote> {
        return releaseNotes
    }

    @ReadOperation
    fun selectCourse(@Selector version: String): Any {
        val releaseNoteOptional = releaseNotes
            .stream()
            .filter { releaseNote -> version == releaseNote.version }
            .findFirst()
        return releaseNoteOptional
    }

    @DeleteOperation
    fun removeReleaseVersion(@Selector
    version: String) {
        val releaseNoteOptional = releaseNotes
            .stream()
            .filter { releaseNote -> version == releaseNote.version }
            .findFirst()
        releaseNoteOptional.ifPresent { releaseNote -> releaseNotes.remove(releaseNote) }
    }
}
