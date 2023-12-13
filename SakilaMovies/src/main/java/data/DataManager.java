package data;

import java.sql.Date;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
public interface DataManager {
    Date getPetBirthday(int petId);
    void setPetBirthday(int petId, Date birthday );
    String getPetFullName(int petId);
}
