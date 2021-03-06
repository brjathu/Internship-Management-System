package com.msd.pool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.msd.poolinterfaces.ApplicantDAO;
import com.msd.users.Applicant;

public class PoolApplicants implements ApplicantDAO {

	// Create a database handler
	JdbcTemplate dbHandler;

	// Setter for handler
	public void setdbHandler(JdbcTemplate dbHandler) {
		this.dbHandler = dbHandler;
	}

	@Override
	public int addApplicant(Applicant applicant) {
		PoolCriteria criteria = applicant.convertListToPref();
		String sql = "INSERT INTO " + ApplicantDAO.TABLE
				+ " (name, surname, gender, indexNumber, emailAddress, telephone, gradedPoint, aboutMe, ARDUINO, "
				+ "FPGA, ROBOTICS, WIFI, ANTENNAS, NETWORKING, PROCESSORDESIGN, IMAGEPROCESSING, PROGRAMMING, AUTOMATION, "
				+ "BIOMEDICAL, BIOMECHANICS, TELECOM, SEMICONDUCTORS, CIRCUITS, IOT, AI, SIGNALPROCESSING) "
				+ "VALUES ('" + applicant.getName() + "', '" + applicant.getSurname() + "', '" + applicant.getGender()
				+ "', '" + applicant.getIndexNumber() + "', '" + applicant.getEmailAddress() + "', '"
				+ applicant.getTelephone() + "'," + applicant.getGradedPoint() + ", '" + applicant.getAboutMe() + "',"
				+ criteria.isARDUINO() + "," + criteria.isFPGA() + "," + criteria.isROBOTICS() + "," + criteria.isWIFI()
				+ "," + criteria.isANTENNAS() + "," + criteria.isNETWORKING() + "," + criteria.isPROCESSORDESIGN() + ","
				+ criteria.isIMAGEPROCESSING() + "," + criteria.isPROGRAMMING() + "," + criteria.isAUTOMATION() + ","
				+ criteria.isBIOMEDICAL() + "," + criteria.isBIOMECHANICS() + "," + criteria.isTELECOM() + ","
				+ criteria.isSEMICONDUCTORS() + "," + criteria.isCIRCUITS() + "," + criteria.isIOT() + ","
				+ criteria.isAI() + "," + criteria.isSIGNALPROCESSING() + ")";

		return dbHandler.update(sql);
	}

	@Override
	public Applicant fetchApplicant(String indexNumber) {

		String sql = "SELECT * FROM " + ApplicantDAO.TABLE + " WHERE indexNumber = '" + indexNumber + "'";
		return dbHandler.query(sql, new ResultSetExtractor<Applicant>() {
			@Override
			public Applicant extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					// Create a new applicant and a criteria
					Applicant info = new Applicant(rs.getString("indexNumber"), rs.getString("surname"),
							rs.getString("emailAddress"), rs.getString("telephone"), rs.getDouble("gradedPoint"),
							rs.getString("aboutMe"));
					PoolCriteria criteria = new PoolCriteria(rs.getBoolean("ARDUINO"), rs.getBoolean("FPGA"),
							rs.getBoolean("ROBOTICS"), rs.getBoolean("WIFI"), rs.getBoolean("ANTENNAS"),
							rs.getBoolean("NETWORKING"), rs.getBoolean("PROCESSORDESIGN"),
							rs.getBoolean("IMAGEPROCESSING"), rs.getBoolean("PROGRAMMING"), rs.getBoolean("AUTOMATION"),
							rs.getBoolean("BIOMEDICAL"), rs.getBoolean("BIOMECHANICS"), rs.getBoolean("TELECOM"),
							rs.getBoolean("SEMICONDUCTORS"), rs.getBoolean("CIRCUITS"), rs.getBoolean("IOT"),
							rs.getBoolean("AI"), rs.getBoolean("SIGNALPROCESSING"));
					// Fetch
					info.setName(rs.getString("name"));
					info.setGender(rs.getString("gender"));
					info.convertPrefToList(criteria);
					return info;
				}
				return null;
			}
		});
	}

	@Override
	public int deleteApplicant(String indexNumber) {
		String sql = "DELETE FROM " + ApplicantDAO.TABLE + " WHERE indexNumber = ?";
		return dbHandler.update(sql, indexNumber);

	}

	@Override
	public void updateApplicant(Applicant newApplicant) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Applicant> getAllApplicants() {
		List<Applicant> list = dbHandler.query("SELECT * FROM " + ApplicantDAO.TABLE, new RowMapper<Applicant>() {
			public Applicant mapRow(ResultSet rs, int row) throws SQLException {
				// Create a new applicant and a criteria
				Applicant info = new Applicant(rs.getString("indexNumber"), rs.getString("surname"),
						rs.getString("emailAddress"), rs.getString("telephone"), rs.getDouble("gradedPoint"),
						rs.getString("aboutMe"));
				PoolCriteria criteria = new PoolCriteria(rs.getBoolean("ARDUINO"), rs.getBoolean("FPGA"),
						rs.getBoolean("ROBOTICS"), rs.getBoolean("WIFI"), rs.getBoolean("ANTENNAS"),
						rs.getBoolean("NETWORKING"), rs.getBoolean("PROCESSORDESIGN"), rs.getBoolean("IMAGEPROCESSING"),
						rs.getBoolean("PROGRAMMING"), rs.getBoolean("AUTOMATION"), rs.getBoolean("BIOMEDICAL"),
						rs.getBoolean("BIOMECHANICS"), rs.getBoolean("TELECOM"), rs.getBoolean("SEMICONDUCTORS"),
						rs.getBoolean("CIRCUITS"), rs.getBoolean("IOT"), rs.getBoolean("AI"),
						rs.getBoolean("SIGNALPROCESSING"));
				// Fetch
				info.setName(rs.getString("name"));
				info.setGender(rs.getString("gender"));
				info.convertPrefToList(criteria);
				return info;
			}
		});
		return list;
	}

	@Override
	public List<Applicant> getTypeOfApplicants(PoolCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
