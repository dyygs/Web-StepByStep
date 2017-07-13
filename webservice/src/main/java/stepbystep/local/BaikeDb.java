package stepbystep.local;


import stepbystep.spider.BaiKeCell;
import utils.DatabaseUtil;
import utils.StringUtil;

import java.sql.*;
import java.util.List;

/**
 * Created by dy on 2017/7/6.
 */
public class BaikeDb {

    private static BaikeDb instance;
    private final String DB_NAME = "baike";
    private final String TABLE_NAME = "table_bai_ke";
    private final String COLUMN_ID = "id";
    private final String COLUMN_URL = "url";
    private final String COLUMN_CONTENT = "content";
    private final String COLUMN_CATEGORY = "category";
    String create_sql = StringUtil.connectStrings("CREATE TABLE if not exists ",
            TABLE_NAME, " (",
            COLUMN_ID, " INTEGER primary key, ",
            COLUMN_CATEGORY, " text, ",
            COLUMN_CONTENT, " text) character set = utf8");
    public static BaikeDb getInstance() {
        if (instance == null) {
            instance = new BaikeDb();
        }
        return instance;
    }

    public void createTable() {


        Connection conn = DatabaseUtil.getConnectionInstance();
        Statement stmt = DatabaseUtil.getStatement(conn);

        try {
            stmt.execute(create_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DatabaseUtil.close(conn);
            DatabaseUtil.close(stmt);
        }
    }

    public void insertBaikeList(List<BaiKeCell> list) {
            for (BaiKeCell cell : list) {
                insertBaikeCell(cell);
            }
    }

    public void insertBaikeCell(BaiKeCell cell) {
        Connection conn = DatabaseUtil.getConnectionInstance();
        String insertSql = StringUtil.connectStrings("update ",
                TABLE_NAME,
                " set ",
                COLUMN_CONTENT,
                "= ? where ",
                COLUMN_ID,
                "= ?");
        PreparedStatement pstmt = DatabaseUtil.getPreparedStatement(conn, insertSql);

        try {
            String s = cell.getUrl().replace("http://baike.baidu.com/view/", "");
            s = s.replace(".html", "");
            int itemId = Integer.parseInt(s);
            pstmt.setString(1, cell.getContent());
            pstmt.setInt(2, itemId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DatabaseUtil.close(conn);
            DatabaseUtil.close(pstmt);
        }
    }

    public void insertGategoryAndIds(List<Integer> list, String category) {
        Connection conn = DatabaseUtil.getConnectionInstance();
        String insertSql = StringUtil.connectStrings("insert into ",
                TABLE_NAME,
                " (",
                COLUMN_ID,
                ", ",
                COLUMN_CATEGORY,
                ") values (?,?)");
        PreparedStatement pstmt = DatabaseUtil.getPreparedStatement(conn, insertSql);

        try {
            for (Integer i : list) {
                if (isItemIdExists(i, conn)) {
                    continue;
                }
                pstmt.setInt(1, i);
                pstmt.setString(2, category);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DatabaseUtil.close(conn);
            DatabaseUtil.close(pstmt);
        }
    }

    private boolean isItemIdExists(int itemId, Connection conn) {
        boolean result = false;
        String insertSql = StringUtil.connectStrings("select *",
                " from ",
                TABLE_NAME,
                " where ",
                COLUMN_ID,
                " = ?");
        PreparedStatement pstmt = DatabaseUtil.getPreparedStatement(conn, insertSql);

        try {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DatabaseUtil.close(pstmt);
        }
        return result;
    }

    public BaiKeCell getRandomBaiKeCell() {
        BaiKeCell baiKeCell = null;
        Connection conn = DatabaseUtil.getConnectionInstance();
        String insertSql = StringUtil.connectStrings("select *",
                " from ",
                TABLE_NAME,
                " where ",
                COLUMN_ID,
                " >= (((select max(" + COLUMN_ID, ") from "
                , TABLE_NAME, ") - (select min(" , COLUMN_ID, ") from "
                , TABLE_NAME, "))*RAND() + (select min(" + COLUMN_ID, ") from "
                , TABLE_NAME, ")) limit 1");
        PreparedStatement pstmt = DatabaseUtil.getPreparedStatement(conn, insertSql);

        try {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                baiKeCell = new BaiKeCell();
                baiKeCell.setId(rs.getInt(COLUMN_ID));
                baiKeCell.setContent(rs.getString(COLUMN_CONTENT));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DatabaseUtil.close(pstmt);
        }
        return baiKeCell;
    }
}
