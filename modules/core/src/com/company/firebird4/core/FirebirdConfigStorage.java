package com.company.firebird4.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.app.ConfigStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FirebirdConfigStorage extends ConfigStorage {
    private Logger log = LoggerFactory.getLogger(ConfigStorage.class);

    @Override
    protected void loadCache() {
        if (cache == null) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (cache == null) {
                    log.info("Loading DB-stored app properties cache");
                    // Don't use transactions here because of loop possibility from EntityLog
                    QueryRunner queryRunner = new QueryRunner(persistence.getDataSource());
                    try {
                        cache = queryRunner.query("select NAME, VALUE_ from SYS_CONFIG",
                                new ResultSetHandler<Map<String, String>>() {
                                    @Override
                                    public Map<String, String> handle(ResultSet rs) throws SQLException {
                                        HashMap<String, String> map = new HashMap<>();
                                        while (rs.next()) {
                                            map.put(rs.getString(1), rs.getString(2));
                                        }
                                        return map;
                                    }
                                });
                    } catch (SQLException e) {
                        throw new RuntimeException("Error loading DB-stored app properties cache", e);
                    }
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }
}
