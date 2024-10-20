package ru.practicum.shareit.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CollectionsConfig {
    @Bean
    public Map<Long, Item> itemsMap() {
        return new HashMap<>();
    }

    @Bean
    Map<Long, User> usersMap() {
        return new HashMap<>();
    }
}
