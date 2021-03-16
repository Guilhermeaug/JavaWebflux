package com.digitalinnovationone.heroesapi.controller;

import com.digitalinnovationone.heroesapi.constants.HeroesConstant;
import com.digitalinnovationone.heroesapi.document.Heroes;
import com.digitalinnovationone.heroesapi.repository.HeroesRepository;
import com.digitalinnovationone.heroesapi.service.HeroesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class HeroesController {
    HeroesService heroesService;
    HeroesRepository heroesRepository;

    private static final Logger log = LoggerFactory.getLogger(HeroesController.class);

    public HeroesController(HeroesService heroesService, HeroesRepository heroesRepository){
        this.heroesRepository = heroesRepository;
        this.heroesService = heroesService;
    }

    @GetMapping(HeroesConstant.HEROES_ENDPOINT_LOCAL)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Heroes> getAllItems(){
        log.info("requesting the list of all heroes");
        return heroesService.findAll();
    }

    @GetMapping(HeroesConstant.HEROES_ENDPOINT_LOCAL+"/{id}")
    public Mono<ResponseEntity<Heroes>> findByIdHero(@PathVariable String id){
        log.info("requesting the hero with id {}", id);
        return heroesService.findByIdHero(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(HeroesConstant.HEROES_ENDPOINT_LOCAL)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Heroes> createHero(@RequestBody Heroes heroes ){
        log.info("a new hero was created");
        return heroesService.save(heroes);
    }

    @DeleteMapping(HeroesConstant.HEROES_ENDPOINT_LOCAL+"/{id}")
    @ResponseStatus(code = HttpStatus.CONTINUE)
    public Mono<HttpStatus> deleteByIdHero(@PathVariable String id){
        heroesService.deleteByIdHero(id);
        log.info("deleting a hero with id {}", id);
        return Mono.just(HttpStatus.CONTINUE);
    }

}
