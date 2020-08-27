import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class MySQL {

	static class InsertDb {
		Connection conn = null;
		private int persize = 200000;

		public void initConn() throws ClassNotFoundException, SQLException {
			String url = "jdbc:mysql://192.168.20.208:61306/cloud?user=leke&password=leke@@@&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false";

			try {
				// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
				// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
				Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
				System.out.println("成功加载MySQL驱动程序");
				// 一个Connection代表一个数据库连接
				conn = DriverManager.getConnection(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void insert() {
			// 开时时间
			Long begin = new Date().getTime();
			// sql前缀
			String prefix = "INSERT INTO question.test_res_tag2  VALUES ";

			try {
				// 保存sql后缀
				StringBuffer suffix = new StringBuffer();
				// 设置事务为非自动提交
				conn.setAutoCommit(false);
				//      Statement pst = conn.createStatement();
				// 比起st，pst会更好些
				PreparedStatement pst = conn.prepareStatement(" ");

				String sql = "CREATE TABLE cld_school_share_count  (\n" +
						"  school_share_count_id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '老师分享主键ID',\n" +
						"  user_id bigint(20) NOT NULL COMMENT '用户ID',\n" +
						"  user_name varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',\n" +
						"  school_id bigint(20) NOT NULL COMMENT '学校ID',\n" +
						"  school_stage_id bigint(20) NOT NULL DEFAULT '0' COMMENT '学段ID',\n" +
						"  subject_id bigint(20) NOT NULL DEFAULT '0' COMMENT '学科ID',\n" +
						"  school_stage_name varchar(32) NOT NULL DEFAULT '' COMMENT '学段名称',\n" +
						"  subject_name varchar(32) NOT NULL DEFAULT '' COMMENT '学科名称',\n" +
						"  courseware_count int(11) NOT NULL DEFAULT '0' COMMENT '课件数量',\n" +
						"  microcourse_count int(11) NOT NULL DEFAULT '0' COMMENT '微课数量',\n" +
						"  beikepkg_count int(11) NOT NULL DEFAULT '0' COMMENT '备课包数量',\n" +
						"  paper_count int(11) NOT NULL DEFAULT '0' COMMENT '试卷数量',\n" +
						"  question_count int(11) NOT NULL DEFAULT '0' COMMENT '习题数量',\n" +
						"  workbook_count int(11) NOT NULL DEFAULT '0' COMMENT '习题册数量',\n" +
						"  total_count int(11) NOT NULL DEFAULT '0' COMMENT '总数量',\n" +
						"  is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',\n" +
						"  share_date int(11) NOT NULL DEFAULT '0' COMMENT '日期（例如20190719）',\n" +
						"  PRIMARY KEY (school_share_count_id),\n" +
						"  KEY idx_cld_school_share_count_schoolId (school_id,user_id)\n" +
						") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT \"校本库分享统计表\";";
				pst.execute(sql);
				// 提交事务
				conn.commit();

				// 关闭连接
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 结束时间
			Long end = new Date().getTime();
			// 耗时
			System.out.println("cast : " + (end - begin) / 1000f + " ms");
		}
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
		InsertDb insertDb = new InsertDb();
		insertDb.initConn();
		insertDb.insert();
	}

}
