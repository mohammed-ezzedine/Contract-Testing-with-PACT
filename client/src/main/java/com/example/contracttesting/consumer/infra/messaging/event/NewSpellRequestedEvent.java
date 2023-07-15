package com.example.contracttesting.consumer.infra.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewSpellRequestedEvent implements Serializable {
    private String name;
    private String description;
}
