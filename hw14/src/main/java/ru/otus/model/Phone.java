package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "phone")
public class Phone {
    @Id
    @Nonnull
    private Long id;

    @Nonnull
    private Long clientId;

    @Nonnull
    private String number;

    public Phone(Long clientId, String number) {
        this(null, clientId, number);
    }

    @PersistenceCreator
    public Phone(Long id, Long clientId, String number) {
        this.id = id;
        this.clientId = clientId;
        this.number = number;
    }
}
