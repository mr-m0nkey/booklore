package org.booklore.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.booklore.model.enums.BookFileType;

@Data
public class UpdateBookPrimaryFormatRequest {

    @NotNull
    private BookFileType bookFileType;

}
