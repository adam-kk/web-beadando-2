package com.web.beadando.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.web.beadando.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.beadando.repository.ItemRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ItemController {

	@Autowired
	ItemRepository itemRepository;

	@GetMapping("/items")
	public ResponseEntity<List<Item>> getAllItems(@RequestParam(required = false) String title)
	{
		try
		{
			List<Item> items = new ArrayList<Item>();

			if (title == null)
			{
				itemRepository.findAll().forEach(items::add);
			}
			else
			{
				itemRepository.findByTitleContaining(title).forEach(items::add);
			}

			if (items.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(items, HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/items/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable("id") long id)
	{
		Optional<Item> itemData = itemRepository.findById(id);

		if (itemData.isPresent())
		{
			return new ResponseEntity<>(itemData.get(), HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/items")
	public ResponseEntity<Item> createItem(@RequestBody Item item)
	{
		try
		{
			Item _item = itemRepository.save(new Item(item.getTitle(), item.getDescription(), false));
			return new ResponseEntity<>(_item, HttpStatus.CREATED);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/items/{id}")
	public ResponseEntity<Item> updateItem(@PathVariable("id") long id, @RequestBody Item item)
	{
		Optional<Item> itemData = itemRepository.findById(id);

		if (itemData.isPresent())
		{
			Item _item = itemData.get();
			_item.setTitle(item.getTitle());
			_item.setDescription(item.getDescription());
			_item.setPublished(item.isPublished());
			return new ResponseEntity<>(itemRepository.save(_item), HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/items/{id}")
	public ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") long id)
	{
		try
		{
			itemRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/items")
	public ResponseEntity<HttpStatus> deleteAllItems()
	{
		try
		{
			itemRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/items/published")
	public ResponseEntity<List<Item>> findByPublished()
	{
		try
		{
			List<Item> items = itemRepository.findByPublished(true);

			if (items.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(items, HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
}