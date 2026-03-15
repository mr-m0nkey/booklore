package org.booklore.model.entity;

import org.booklore.model.enums.BookFileType;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookEntityTest {

    @Test
    void getPrimaryBookFile_NullBookFilesFound_returnsNull() {
        BookEntity bookEntity = BookEntity
                .builder()
                .bookFiles(null)
                .build();

        BookFileEntity bookFileEntity = bookEntity.getPrimaryBookFile();
        assertNull(bookFileEntity);
    }

    @Test
    void getPrimaryBookFile_EmptyBookFilesFound_returnsNull() {
        BookEntity bookEntity = BookEntity
                .builder()
                .bookFiles(new ArrayList<>())
                .build();

        BookFileEntity bookFileEntity = bookEntity.getPrimaryBookFile();
        assertNull(bookFileEntity);
    }

    @Test
    void getPrimaryBookFile_NullLibrary_returnsFirstBookFile() {
        List<BookFileEntity> bookFiles = getBookFileEntities();


        BookEntity bookEntity = BookEntity
                .builder()
                .bookFiles(bookFiles)
                .build();

        BookFileEntity bookFileEntity = bookEntity.getPrimaryBookFile();
        assertEquals(bookFiles.getFirst(), bookFileEntity);
    }

    @Test
    void getPrimaryBookFile_libraryPriorityExists_useLibraryPriority() {
        List<BookFileEntity> bookFiles = getBookFileEntities();
        List<BookFileType> formatPriority = List.of(BookFileType.AUDIOBOOK, BookFileType.EPUB);
        LibraryEntity libraryEntity = LibraryEntity.builder()
                .formatPriority(formatPriority)
                .build();


        BookEntity bookEntity = BookEntity
                .builder()
                .bookFiles(bookFiles)
                .library(libraryEntity)
                .build();

        Map<BookFileType, BookFileEntity> bookFileTypeBookFileEntityMap = bookFiles.stream().collect(Collectors.toMap(BookFileEntity::getBookType, Function.identity()));

        BookFileEntity bookFileEntity = bookEntity.getPrimaryBookFile();
        assertEquals(bookFileTypeBookFileEntityMap.get(BookFileType.AUDIOBOOK), bookFileEntity);
    }

    @Test
    void getPrimaryBookFile_BookHasPrimaryFileType_useBooksPrimaryFileType() {
        List<BookFileEntity> bookFiles = getBookFileEntities();
        List<BookFileType> formatPriority = List.of(BookFileType.AUDIOBOOK, BookFileType.EPUB);
        LibraryEntity libraryEntity = LibraryEntity.builder()
                .formatPriority(formatPriority)
                .build();


        BookEntity bookEntity = BookEntity
                .builder()
                .bookFiles(bookFiles)
                .library(libraryEntity)
                .primaryBookFileType(BookFileType.MOBI)
                .build();

        Map<BookFileType, BookFileEntity> bookFileTypeBookFileEntityMap = bookFiles.stream().collect(Collectors.toMap(BookFileEntity::getBookType, Function.identity()));

        BookFileEntity bookFileEntity = bookEntity.getPrimaryBookFile();
        assertEquals(bookFileTypeBookFileEntityMap.get(bookEntity.getPrimaryBookFileType()), bookFileEntity);
    }

    private static @NonNull List<BookFileEntity> getBookFileEntities() {
        BookFileEntity mobiBookFile = BookFileEntity
                .builder()
                .isBookFormat(true)
                .bookType(BookFileType.MOBI)
                .build();

        BookFileEntity epubBookFile = BookFileEntity
                .builder()
                .isBookFormat(true)
                .bookType(BookFileType.EPUB)
                .build();

        BookFileEntity audioBookBookFile = BookFileEntity
                .builder()
                .isBookFormat(true)
                .bookType(BookFileType.AUDIOBOOK)
                .build();

        return List.of(mobiBookFile, epubBookFile, audioBookBookFile);
    }
}