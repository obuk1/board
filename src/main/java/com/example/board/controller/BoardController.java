package com.example.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


import com.example.board.model.Board;
import com.example.board.model.User;
import com.example.board.repository.BoardRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	HttpSession session;

	@GetMapping("/board/delete/{id}")
	public String boardDelete(@PathVariable("id") long id) {
		boardRepository.deleteById(id);
		return "redirect:/board/list";
	}

	@GetMapping("/board/update/{id}")
	public String boardUpdate(Model model, @PathVariable("id") long id) {
		Optional<Board> data = boardRepository.findById(id);
		Board board = data.get();
		model.addAttribute("board", board);
		return "board/update";
	}
		
	@PostMapping("/board/update/{id}")
	public String boardUpdate(
			@ModelAttribute Board board, @PathVariable("id") long id) {
		User user = (User) session.getAttribute("user_info");
		String userId = user.getEmail();
		board.setUserId(userId);
		board.setId(id);
		boardRepository.save(board);
		return "redirect:/board/" + id;
	}

	@GetMapping("/board/{id}")
	public String boardView(Model model, @PathVariable("id") long id) {
		Optional<Board> data = boardRepository.findById(id);
		Board board = data.get();
		model.addAttribute("board", board);
		return "board/view";
	}
	
	@GetMapping("/board/list")
	public String boardList(Model model) {
		// Sort sort = Sort.by(Sort.Direction.DESC, "id");
		// List<Board> list = boardRepository.findAll(sort);

		Sort sort = Sort.by(Order.desc("id"));
		List<Board> list = boardRepository.findAll(sort);
		
		model.addAttribute("list", list);
		return "board/list";
	}

	@GetMapping("/board/write")
	public String boardWrite() {
		return "board/write";
	}
	
	@PostMapping("/board/write")
	public String boardWrite(@ModelAttribute Board board) {
		User user = (User) session.getAttribute("user_info");
		String userId = user.getEmail();
		board.setUserId(userId);
		boardRepository.save(board);

		return "board/write";
	}
}