package com.anhub.prize_draw_system.admin;

import com.anhub.prize_draw_system.draw.PrizeDrawOrchestrator;
import com.anhub.prize_draw_system.draw.enumerated.DrawAlgorithmType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/draw")
@RequiredArgsConstructor
public class AdminDrawController {

    private final PrizeDrawOrchestrator orchestrator;

    @GetMapping("/algorithm")
    public ResponseEntity<Map<String, Object>> getCurrentAlgorithm() {

        return ResponseEntity.ok(Map.of(
                "activeAlgorithm", orchestrator.getCurrentAlgorithm(),
                "availableAlgorithms", Arrays.asList(DrawAlgorithmType.values())
        ));
    }

    @PutMapping("/algorithm")
    public ResponseEntity<Map<String, Object>> updateAlgorithm(
            @RequestParam DrawAlgorithmType algorithm
    ) {

        DrawAlgorithmType updated = orchestrator.switchAlgorithm(algorithm);
        return ResponseEntity.ok(Map.of(
                "message", "Draw algorithm updated successfully.",
                "activeAlgorithm", updated
        ));
    }
}
