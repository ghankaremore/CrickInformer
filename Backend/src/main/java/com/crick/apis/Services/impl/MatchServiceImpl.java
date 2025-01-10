package com.crick.apis.Services.impl;

import com.crick.apis.Services.MatchService;
import com.crick.apis.entities.Match;
import com.crick.apis.repositories.MatchRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.util.*;

import java.io.IOException;
import java.util.*;

@Service
public class MatchServiceImpl implements MatchService {
    private MatchRepo matchRepo;

    public MatchServiceImpl(MatchRepo matchRepo) {
        this.matchRepo = matchRepo;
    }

    @Override
    public List<Match> getAllMatches() {
        return this.matchRepo.findAll();
    }

    @Override
    public List<Match> getLiveMatches() {
        List<Match> matches = new ArrayList<>();
        try {
            String url = "https://www.cricbuzz.com/cricket-match/live-scores";
            Document document = Jsoup.connect(url).get();
            Elements liveScoreElements = document.select("div.cb-mtch-lst.cb-tms-itm");
            for (Element match : liveScoreElements) {
                HashMap<String, String> liveMatchInfo = new LinkedHashMap<>();
                String teamsHeading = match.select("h3.cb-lv-scr-mtch-hdr").select("a").text();
                String matchNumberVenue = match.select("span").text();
                Elements matchBatTeamInfo = match.select("div.cb-hmscg-bat-txt");
                String battingTeam = matchBatTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String score = matchBatTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                Elements bowlTeamInfo = match.select("div.cb-hmscg-bwl-txt");
                String bowlTeam = bowlTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String bowlTeamScore = bowlTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                String textLive = match.select("div.cb-text-live").text();
                String textComplete = match.select("div.cb-text-complete").text();
                //getting match link
                String matchLink = match.select("a.cb-lv-scrs-well.cb-lv-scrs-well-live").attr("href").toString();

                Match match1 = new Match();
                match1.setTeamHeading(teamsHeading);
                match1.setMatchNumberVenue(matchNumberVenue);
                match1.setBattingTeam(battingTeam);
                match1.setBattingTeamScore(score);
                match1.setBowlTeam(bowlTeam);
                match1.setBowlTeamScore(bowlTeamScore);
                match1.setLiveText(textLive);
                match1.setMatchLink(matchLink);
                match1.setTextComplete(textComplete);
                match1.setMatchStatus();


                matches.add(match1);

//                update the match in database

               updateMatchinDb(match1);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }


    private void updateMatchinDb(Match match1){
      Match match =   this.matchRepo.findByTeamHeading(match1.getTeamHeading()).orElse(null);
        if(match==null){
            matchRepo.save(match1);
        }else{
            match1.setMatchId(match.getMatchId());
            matchRepo.save(match1);


        }

    }

    @Override
    public List<List<String>> getPointTable() {
//        List<List<String>> pointTable = new ArrayList<>();
//        String tableURL = "https://www.cricbuzz.com/cricket-stats/points-table/test/icc-world-test-championship";
//        try {
//            Document document = Jsoup.connect(tableURL).get();
//            Elements table = document.select("table cb-srs-pnts");
//            Elements tableHeads = table.select("thead>tr>*");
//            List<String> headers = new ArrayList<>();
//            tableHeads.forEach(element -> {
//                headers.add(element.text());
//            });
//            pointTable.add(headers);
//            Elements bodyTrs = table.select("tbody>*");
//            bodyTrs.forEach(tr -> {
//                List<String> points = new ArrayList<>();
//                if (tr.hasAttr("class")) {
//                    Elements tds = tr.select("td");
////                    String team = tds.get(0).text();
//                    String team = tds.get(0).select("div.cb-col-84").text();
//                    points.add(team);
//                    tds.forEach(td -> {
//                        if (!td.hasClass("cb-srs-pnts-name")) {
//                            points.add(td.text());
//                        }
//                    });
////                    System.out.println(points);
//                    pointTable.add(points);
//                }
//
//
//            });
//
//            System.out.println(pointTable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return pointTable;



//        List<List<String>> pointTable = new ArrayList<>();
//
//        try {
//            // Fetch the HTML content using Jsoup
//            Document doc = Jsoup.connect("https://www.cricbuzz.com/cricket-stats/points-table/test/icc-world-test-championship").get();
//
//            // Select the table element (ensure the CSS selector matches your table)
//            Element table = doc.selectFirst("table.table.cb-srs-pnts");
//
//            if (table != null) {
//                // Select all rows of the table body
//                Elements rows = table.select("tbody > tr");
//
//                for (Element row : rows) {
//                    List<String> rowData = new ArrayList<>();
//                    // Select all cells within the row
//                    Elements cells = row.select("td");
//
//                    for (Element cell : cells) {
//                        rowData.add(cell.text()); // Add the text of each cell to the row
//                    }
//
//                    pointTable.add(rowData); // Add the row to the table
//                }
//
//            } else {
//
//                System.out.println("Table not found on the page.");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return pointTable;

        List<List<String>> pointTable = new ArrayList<>();

        try {
            // Fetch the HTML content using Jsoup
            Document doc = Jsoup.connect("https://www.cricbuzz.com/cricket-stats/points-table/test/icc-world-test-championship").get();

            // Select the table element (ensure the CSS selector matches your table)
            Element table = doc.selectFirst("table.table.cb-srs-pnts");

            if (table != null) {
                // Step 1: Fetch table headings (thead)
                List<String> tableHeadings = new ArrayList<>();
                Elements headings = table.select("thead > tr");
                for (Element headingRow : headings) {
                    String headingText = headingRow.text(); // Extract the text from the row
                    String[] arr = headingText.split(" ");  // Split the text into words
                    for (String word : arr) {
                        tableHeadings.add(word);  // Print each word in the array
                    }
                }

                pointTable.add(tableHeadings); // Add headings to the pointTable list


                // Step 2: Fetch table body rows (tbody)
                Elements rows = table.select("tbody > tr");

                for (Element row : rows) {
                    List<String> rowData = new ArrayList<>();
                    // Select all cells within the row
                    Elements cells = row.select("td");

                    for (Element cell : cells) {
                        if(!cell.text().trim().isEmpty()) {
                            rowData.add(cell.text());
                        // Add the text of each cell to the row
                        }
                    }

                    pointTable.add(rowData); // Add the row to the table
                }

            } else {
                System.out.println("Table not found on the page.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

// Print the table (optional)
//        for (List<String> row : pointTable) {
//            System.out.println(row);
//        }

        return pointTable;

    }
    }

