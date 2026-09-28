CREATE INDEX idx_prize_pool_claimable
    ON prize_pool (release_time ASC) WHERE status = 'AVAILABLE';