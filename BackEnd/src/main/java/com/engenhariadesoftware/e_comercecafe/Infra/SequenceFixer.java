package com.engenhariadesoftware.e_comercecafe.Infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SequenceFixer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SequenceFixer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            String seqName = jdbcTemplate.queryForObject(
                    "select pg_get_serial_sequence('produtos','id_produto')",
                    String.class);

            if (seqName == null) {
                logger.info("No sequence found for produtos.id_produto, skipping sequence sync.");
                return;
            }

            Long maxId = jdbcTemplate.queryForObject(
                    "select coalesce(max(id_produto),0) from produtos",
                    Long.class);

            if (maxId == null) maxId = 0L;

            String sql = String.format("select setval('%s', %d, true)", seqName, maxId);
            jdbcTemplate.execute(sql);
            logger.info("Synchronized sequence {} to value {}", seqName, maxId);
        } catch (Exception e) {
            logger.warn("Could not synchronize produtos sequence on startup: {}", e.getMessage());
        }
    }
}
