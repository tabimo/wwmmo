package au.com.codeka.warworlds.server.ctrl;

import au.com.codeka.common.protobuf.Messages;
import au.com.codeka.warworlds.server.RequestException;
import au.com.codeka.warworlds.server.data.DB;
import au.com.codeka.warworlds.server.data.SqlResult;
import au.com.codeka.warworlds.server.data.SqlStmt;
import au.com.codeka.warworlds.server.data.Transaction;

public class CombatReportController {
    private DataBase db;

    public CombatReportController() {
        db = new DataBase();
    }
    public CombatReportController(Transaction trans) {
        db = new DataBase(trans);
    }

    public Messages.CombatReport fetchCombatReportPb(int combatReportID) throws RequestException {
        try {
            return db.fetchCombatReportPb(combatReportID);
        } catch(Exception e) {
            throw new RequestException(e);
        }
    }

    private static class DataBase extends BaseDataBase {
        public DataBase() {
            super();
        }
        public DataBase(Transaction trans) {
            super(trans);
        }

        public Messages.CombatReport fetchCombatReportPb(int combatReportID) throws Exception {
            String sql = "SELECT rounds FROM combat_reports WHERE id = ?";
            try (SqlStmt stmt = DB.prepare(sql)) {
                stmt.setInt(1, combatReportID);
                SqlResult res = stmt.select();
                while (res.next()) {
                    return Messages.CombatReport.parseFrom(res.getBytes(1));
                }
            }

            return null;
        }

    }
}
