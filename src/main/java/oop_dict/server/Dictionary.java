package oop_dict.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class Dictionary {
	private static final String HOST_NAME = "localhost";
	private static final String DB_NAME = "klmk-oop";
	private static final String USERNAME = "klmk-oop";
	private static final String PASSWORD = "klmk-oop-dictionary";
	private static final String PORT = "3306";
	private static final String MYSQL_URL = String.format("jdbc:mysql://%s:%s/%s", HOST_NAME, PORT, DB_NAME);

	private static Connection connection = null;

	public Dictionary(){
	}

	private static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void close(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void close(ResultSet ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		close(connection);
	}

	private void connectToDB() throws SQLException {
		connection = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD);
	}

	public void init() throws SQLException {
		connectToDB();

		ArrayList<String> targets = getAllWordTargets();

		for (String word : targets) {
			Trie.insert(word);
		}
	}

	public String findWord(String target) {
		String SQL_QUERY = "SELECT definition FROM dictionary WHERE target = ?";

		try {
			PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
			ps.setString(1, target);

			try {
				ResultSet rs = ps.executeQuery();

				try {
					if (rs.next()) {
						return rs.getString("definition");
					} else {
						return "404";
					}
				} finally {
					close(rs);
				}
			} finally {
				close(ps);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "404";
	}

	public boolean addWord(String target, String explain) {
		String SQL_QUERY = "INSERT INTO dictionary (target, definition) VALUES (?, ?)";

		try {
			PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
			ps.setString(1, target);
			ps.setString(2, explain);

			try {
				ps.executeUpdate();
			} catch (SQLIntegrityConstraintViolationException e) {
				return false;
			} finally {
				close(ps);
			}

			Trie.insert(target);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}


	public boolean deleteWord(String target) {
		String SQL_QUERY = "DELETE FROM dictionary WHERE target = ?";

		try {
			PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
			ps.setString(1, target);

			try {
				int deletedRow = ps.executeUpdate();

				if (deletedRow == 0) {
					return false;
				}
			} finally {
				close(ps);
			}

			Trie.delete(target);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}


	public boolean updateWord(String target, String explain) {
		String SQL_QUERY = "UPDATE dictionary SET definition = ? WHERE target = ?";

		try {
			PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
			ps.setString(1, target);
			ps.setString(2, explain);

			try {
				int updatedRow = ps.executeUpdate();

				if (updatedRow == 0) {
					return false;
				}
			} finally {
				close(ps);
			}

			Trie.insert(target);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}

	private ArrayList<Word> getWordsFromResultSet(PreparedStatement ps) throws SQLException {
		try {
			ResultSet rs = ps.executeQuery();

			try {
				ArrayList<Word> res = new ArrayList<>();

				for (; rs.next();) {
					res.add(new Word(rs.getString(2), rs.getString(3)));
				}

				return res;
			} finally {
				close(rs);
			}
		} finally {
			close(ps);
		}
	}


	public ArrayList<Word> getAllWords() {
		String SQL_QUERY = "SELECT * FROM dictionary";

		try {
			PreparedStatement ps = connection.prepareStatement(SQL_QUERY);

			return getWordsFromResultSet(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}


	public ArrayList<String> getAllWordTargets() {
		String SQL_QUERY = "SELECT * FROM dictionary";

		try {
			PreparedStatement ps = connection.prepareStatement(SQL_QUERY);

			try {
				ResultSet rs = ps.executeQuery();

				try {
					ArrayList<String> res = new ArrayList<>();

					for (; rs.next();) {
						res.add(new String(rs.getString(2)));
					}

					return res;
				} finally {
					close(rs);
				}
			} finally {
				close(ps);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}
}