package com.crick.apis.Services;

import com.crick.apis.entities.Match;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface MatchService {
    //get all matches

    List<Match> getAllMatches();
    //get live matches

    List<Match> getLiveMatches();
    //get wtc point table

    List<List<String>> getPointTable();
}
