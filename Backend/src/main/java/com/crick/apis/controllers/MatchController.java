package com.crick.apis.controllers;

import com.crick.apis.Services.MatchService;
import com.crick.apis.entities.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/match")
@CrossOrigin("http://13.60.99.210:5173")
public class MatchController {



    private MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }




    @GetMapping("/live")
    public ResponseEntity<List<Match>> getLiveMatches() {
        return new ResponseEntity<>(this.matchService.getLiveMatches(), HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<List<Match>> getAllMatches(){
        return new ResponseEntity<>(this.matchService.getAllMatches(),HttpStatus.OK);
    }

    @GetMapping("/point-table")
    public ResponseEntity<?> getPointTable(){
        return new ResponseEntity<>(this.matchService.getPointTable(),HttpStatus.OK);
    }


}

