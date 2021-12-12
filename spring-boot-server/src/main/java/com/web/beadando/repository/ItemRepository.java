package com.web.beadando.repository;

import java.util.List;

import com.web.beadando.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>
{
	List<Item> findByPublished(boolean published);
	List<Item> findByTitleContaining(String title);
}
