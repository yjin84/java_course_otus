package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @Nonnull
    private Long id;

    @Nonnull
    private Long clientId;

    @Nonnull
    private String street;

    public Address(Long clientId, String street) {
        this(null, clientId, street);
    }

    @PersistenceCreator
    public Address(Long id, Long clientId, String street) {
        this.id = id;
        this.clientId = clientId;
        this.street = street;
    }

}
