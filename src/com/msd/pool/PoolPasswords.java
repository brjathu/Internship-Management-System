package com.msd.pool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.msd.poolinterfaces.PasswordDAO;
import com.msd.registers.LoginInfo;

public class PoolPasswords implements PasswordDAO {

	// Create a database handler
	JdbcTemplate dbHandler;

	// Setter for handler
	public void setdbHandler(JdbcTemplate dbHandler) {
		this.dbHandler = dbHandler;
	}

	@Override
	public int addPassword(LoginInfo info) {
		String sql = "INSERT INTO " + PasswordDAO.TABLE + " (username, password, user_type) " + "VALUES (?, ?, ?)";
		return dbHandler.update(sql, info.getUsername(), info.getencodedPassword(), info.isCompany());
	}

	@Override
	public LoginInfo fetchUser(String username) {
		String sql = "SELECT * FROM " + PasswordDAO.TABLE + " WHERE username = ?";
		try {
			LoginInfo info = dbHandler.queryForObject(sql, new Object[] { username },
					new BeanPropertyRowMapper<LoginInfo>(LoginInfo.class));
			return info;
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		} catch (org.springframework.dao.IncorrectResultSizeDataAccessException n) {
			return null;
		}
	}
	
	@Override
	public LoginInfo fetchAdmin(String username) {
		String sql = "SELECT * FROM " + PasswordDAO.ADMIN + " WHERE username = ?";
		try {
			LoginInfo info = dbHandler.queryForObject(sql, new Object[] { username },
					new BeanPropertyRowMapper<LoginInfo>(LoginInfo.class));
			return info;
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public int deletePassword(String username) {
		String sql = "DELETE FROM " + PasswordDAO.TABLE + " WHERE username = ?";
		return dbHandler.update(sql, username);
	}

	@Override
	public int updatePassword(LoginInfo info) {
		String sql = "UPDATE " + PasswordDAO.TABLE + " SET password = ? WHERE username = ?";
		return dbHandler.update(sql, info.getUsername(), info.getencodedPassword());
	}

	@Override
	public List<LoginInfo> listOutPWs() {
		List<LoginInfo> list = dbHandler.query("SELECT * FROM " + PasswordDAO.TABLE, new RowMapper<LoginInfo>() {
			public LoginInfo mapRow(ResultSet rs, int row) throws SQLException {
				LoginInfo info = new LoginInfo();
				info.setUsername(rs.getString(2));
				info.setPassword(rs.getString(3));
				info.setCompany(rs.getBoolean(4));
				return info;
			}
		});
		return list;
	}

	// Checks if the typed password and username match with the one in the table
	public boolean matchThisAndThat(LoginInfo typedData) {
		// Fetch data in the password table
		LoginInfo originalData = fetchUser(typedData.getUsername());
		// If there are no matching password or no account, data will be passed
		// null
		if (originalData == null) {
			return false;
		}
		// Decode both passwords
		String decodedPW = originalData.decodePassword(originalData.getPassword());
		String typedPW = typedData.getPassword();
		// Check if the passwords are correct as well as the user types
		return decodedPW.equals(typedPW) && (originalData.isCompany() == typedData.isCompany());
	}

	// Validate admin passwords
	public boolean matchThisAndAdmin(LoginInfo typedData) {
		// Fetch data from the admin password table
		LoginInfo originalData = fetchAdmin(typedData.getUsername());
		// If there are no matching password or no account, data will be passed
		// null
		if (originalData == null) {
			return false;
		}
		// Decode both passwords
		String adminPW = originalData.decodePassword(originalData.getPassword());
		String typedPW = typedData.getPassword();
		// Check if the passwords are correct as well as the user types
		return adminPW.equals(typedPW);
	}

	@Override
	public List<LoginInfo> listTypeOfPWs(boolean type) {
		List<LoginInfo> list = dbHandler.query("SELECT * FROM " + PasswordDAO.TABLE + " WHERE user_type = " + type,
				new RowMapper<LoginInfo>() {
					public LoginInfo mapRow(ResultSet rs, int row) throws SQLException {
						LoginInfo info = new LoginInfo();
						info.setUsername(rs.getString(2));
						info.setPassword(rs.getString(3));
						info.setCompany(rs.getBoolean(4));
						return info;
					}
				});
		return list;
	}
}
