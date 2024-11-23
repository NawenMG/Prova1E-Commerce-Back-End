package com.prova.e_commerce.Config;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageKafka {
    private String id;
    private String content;
    private LocalDateTime timestamp;
}