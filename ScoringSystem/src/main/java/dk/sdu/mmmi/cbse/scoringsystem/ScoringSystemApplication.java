package dk.sdu.mmmi.cbse.scoringsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class ScoringSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoringSystemApplication.class, args);
    }


    @RestController
    @RequestMapping("/score")
    public class ScoreController {
        private int score = 0;

        @GetMapping
        public int getScore() {
            return score;
        }

        @PostMapping("/add")
        public void addScore(@RequestParam int points) {
            score += points;
        }

        @PostMapping("/reset")
        public void resetScore() {
            score = 0;
        }
    }
}
