package com.example.contracttesting.provider.api;

import com.example.contracttesting.provider.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spells")
@RequiredArgsConstructor
public class SpellController {

    private final SpellReadService readService;
    private final SpellWriteService writeService;

    @GetMapping
    ResponseEntity<List<Spell>> getSpellDescriptions() {
        return ResponseEntity.ok(readService.getAll());
    }

    @GetMapping("search")
    ResponseEntity<Spell> getSpellDescription(@RequestParam String name) throws SpellNotFoundException {
        return ResponseEntity.ok(readService.getByName(name));
    }

    @PostMapping
    ResponseEntity<Void> addSpell(@RequestBody Spell spell) throws SpellAlreadyExistsException {
        writeService.save(spell);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(SpellNotFoundException.class)
    ResponseEntity<String> handleSpellNotFoundException(SpellNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(SpellAlreadyExistsException.class)
    ResponseEntity<String> handleSpellAlreadyExistsException(SpellAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
