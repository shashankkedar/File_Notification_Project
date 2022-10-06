package com.tcs.telecom.tib.cfl.repository.daoImpl;
/********************************************************************************/
/*Copyright@ 2022 TCS All rights reserved
 * 
 * ******************************************************************************/
/* Module Name   : File Creation
 * Description   : File generation for HSBC
 * Author        : Shashank Kedar(2259420)
 * ******************************************************************************/
/* Version Control Block
 * ~~~~~~~ ~~~~~~~ ~~~~~                                                        */
/* Date            Version      Author                        Description       */
/* =============================================================================*/
/* 22-09-2022      Initial     Shashank Kedar(2259420)       Procedure call for sequence number.

/********************************************************************************/
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.telecom.tib.cfl.util.DB_Utility;

@Service
public class DbOperImpl {
	private static final Logger log = LoggerFactory.getLogger(DbOperImpl.class);

	@Autowired
	DB_Utility util;
	
	public Integer getSequenceNumber() {
		log.info("getSequenceNumber() started ....");
		Connection connection1 = util.getConnection1();
		CallableStatement statement;
		Integer count = null;
		try {
			statement = connection1.prepareCall("{call RecordCount(?)}");
			statement.registerOutParameter(1, Types.INTEGER);
			statement.execute();

			count = (Integer) statement.getInt(1);
			log.info("Max Sequence...." + count);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("getSequenceNumber() ended ....");
		return count;
     
	}
}
